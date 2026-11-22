package proyecto5.tiendavideojugos.controllers;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto5.tiendavideojugos.entities.Categoria;
import proyecto5.tiendavideojugos.entities.Desarrolladora;
import proyecto5.tiendavideojugos.entities.FichaTecnica;
import proyecto5.tiendavideojugos.entities.Plataforma;
import proyecto5.tiendavideojugos.entities.Videojuego;
import proyecto5.tiendavideojugos.services.CategoriaService;
import proyecto5.tiendavideojugos.services.DesarrolladoraService;
import proyecto5.tiendavideojugos.services.FichaTecnicaService;
import proyecto5.tiendavideojugos.services.PlataformaService;
import proyecto5.tiendavideojugos.services.VideojuegoService;

import java.io.UncheckedIOException;
import java.util.Optional;

/**
 * Controlador del CRUD de videojuegos: listado, ficha de detalle, formulario único de
 * alta/edición con subida de imagen y eliminación.
 */
@Controller
@RequestMapping("/videojuegos")
public class VideojuegoController {

    private static final String FORMULARIO = "videojuegos/form";

    private final VideojuegoService videojuegoService;
    private final CategoriaService categoriaService;
    private final FichaTecnicaService fichaService;
    private final PlataformaService plataformaService;
    private final DesarrolladoraService desarrolladoraService;

    /**
     * @param videojuegoService     servicio de videojuegos
     * @param categoriaService      servicio de categorías (para el desplegable)
     * @param fichaService          servicio de fichas técnicas (para el desplegable)
     * @param plataformaService     servicio de plataformas (para el desplegable)
     * @param desarrolladoraService servicio de desarrolladoras (para el desplegable)
     */
    public VideojuegoController(VideojuegoService videojuegoService, CategoriaService categoriaService,
                                FichaTecnicaService fichaService, PlataformaService plataformaService,
                                DesarrolladoraService desarrolladoraService) {
        this.videojuegoService = videojuegoService;
        this.categoriaService = categoriaService;
        this.fichaService = fichaService;
        this.plataformaService = plataformaService;
        this.desarrolladoraService = desarrolladoraService;
    }

    /**
     * Muestra el listado completo de videojuegos.
     *
     * @param model modelo de la vista
     * @return plantilla del listado
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("videojuegos", videojuegoService.listar());
        return "videojuegos/lista";
    }

    /**
     * Muestra la ficha de detalle de un videojuego (imagen grande y ficha técnica).
     *
     * @param id    identificador del videojuego
     * @param model modelo de la vista
     * @param ra    atributos flash
     * @return plantilla de detalle, o redirección si no existe
     */
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Videojuego> videojuego = videojuegoService.buscarPorId(id);
        if (videojuego.isEmpty()) {
            ra.addFlashAttribute("error", "El videojuego no existe.");
            return "redirect:/videojuegos";
        }
        model.addAttribute("videojuego", videojuego.get());
        return "videojuegos/detalle";
    }

    /**
     * Muestra el formulario vacío para dar de alta un videojuego.
     *
     * @param model modelo de la vista
     * @return plantilla del formulario
     */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        return prepararFormulario(model, new Videojuego());
    }

    /**
     * Muestra el mismo formulario con los datos de un videojuego existente.
     *
     * @param id    identificador del videojuego
     * @param model modelo de la vista
     * @param ra    atributos flash
     * @return plantilla del formulario, o redirección si no existe
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Videojuego> videojuego = videojuegoService.buscarPorId(id);
        if (videojuego.isEmpty()) {
            ra.addFlashAttribute("error", "El videojuego no existe.");
            return "redirect:/videojuegos";
        }
        return prepararFormulario(model, videojuego.get());
    }

    /**
     * Valida y guarda un videojuego (alta o edición según tenga id o no).
     * <p>
     * Además de las anotaciones de validación, comprueba que se haya elegido una categoría
     * existente, que el título no esté repetido y que la ficha técnica elegida no esté
     * ya asignada a otro videojuego. Si algo falla, vuelve al formulario conservando
     * todo lo que el usuario había escrito.
     * </p>
     *
     * @param videojuego    videojuego enviado desde el formulario
     * @param result        resultado de la validación
     * @param archivoImagen imagen subida (opcional)
     * @param model         modelo de la vista
     * @param ra            atributos flash
     * @return redirección al listado, o el formulario si hay errores
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("videojuego") Videojuego videojuego,
                          BindingResult result,
                          @RequestParam(value = "archivoImagen", required = false) MultipartFile archivoImagen,
                          Model model, RedirectAttributes ra) {

        // Categoría: obligatoria y debe existir (se sustituye la referencia por la entidad completa)
        Long categoriaId = videojuego.getCategoria() != null ? videojuego.getCategoria().getId() : null;
        Categoria categoria = categoriaId == null ? null : categoriaService.buscarPorId(categoriaId).orElse(null);
        if (categoria == null) {
            result.rejectValue("categoria", "obligatoria", "Debes seleccionar una categoría");
        }
        videojuego.setCategoria(categoria);

        // Ficha técnica: opcional, pero si se elige no puede estar ya asignada a otro juego
        Long fichaId = videojuego.getFichaTecnica() != null ? videojuego.getFichaTecnica().getId() : null;
        FichaTecnica ficha = fichaId == null ? null : fichaService.buscarPorId(fichaId).orElse(null);
        if (ficha != null) {
            Optional<Videojuego> otro = videojuegoService.buscarPorFicha(fichaId);
            if (otro.isPresent() && !otro.get().getId().equals(videojuego.getId())) {
                result.rejectValue("fichaTecnica", "duplicada", "Esa ficha técnica ya está asignada a otro videojuego");
            }
        }
        videojuego.setFichaTecnica(ficha);

        // Plataforma y desarrolladora (ManyToOne opcionales): se recargan desde la BD por su id.
        Long plataformaId = videojuego.getPlataforma() != null ? videojuego.getPlataforma().getId() : null;
        Plataforma plataforma = plataformaId == null ? null : plataformaService.findById(plataformaId).orElse(null);
        videojuego.setPlataforma(plataforma);

        Long desarrolladoraId = videojuego.getDesarrolladora() != null ? videojuego.getDesarrolladora().getId() : null;
        Desarrolladora desarrolladora = desarrolladoraId == null ? null
                : desarrolladoraService.findById(desarrolladoraId).orElse(null);
        videojuego.setDesarrolladora(desarrolladora);

        // Título único
        if (videojuego.getTitulo() != null && !videojuego.getTitulo().isBlank()) {
            Optional<Videojuego> existente = videojuegoService.buscarPorTitulo(videojuego.getTitulo().trim());
            if (existente.isPresent() && !existente.get().getId().equals(videojuego.getId())) {
                result.rejectValue("titulo", "duplicado", "Ya existe un videojuego con ese título");
            }
        }

        if (result.hasErrors()) {
            return prepararFormulario(model, videojuego);
        }

        videojuego.setTitulo(videojuego.getTitulo().trim());
        boolean esNuevo = videojuego.getId() == null;
        try {
            videojuegoService.guardar(videojuego, archivoImagen);
        } catch (IllegalArgumentException | UncheckedIOException e) {
            result.rejectValue("imagen", "imagenInvalida", e.getMessage());
            return prepararFormulario(model, videojuego);
        }
        ra.addFlashAttribute("exito", esNuevo ? "Videojuego creado correctamente." : "Videojuego actualizado correctamente.");
        return "redirect:/videojuegos";
    }

    /**
     * Elimina un videojuego (y su imagen) y vuelve al listado actualizado.
     *
     * @param id identificador del videojuego
     * @param ra atributos flash
     * @return redirección al listado
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            videojuegoService.eliminar(id);
            ra.addFlashAttribute("exito", "Videojuego eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "No se puede eliminar el videojuego porque está incluido en algún pedido.");
        }
        return "redirect:/videojuegos";
    }

    /**
     * Añade al modelo el videojuego y las listas de opciones necesarias para el formulario.
     *
     * @param model      modelo de la vista
     * @param videojuego videojuego a mostrar en el formulario
     * @return plantilla del formulario
     */
    private String prepararFormulario(Model model, Videojuego videojuego) {
        model.addAttribute("videojuego", videojuego);
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("fichas", fichaService.listarDisponibles(videojuego.getId()));
        model.addAttribute("plataformas", plataformaService.findAll());
        model.addAttribute("desarrolladoras", desarrolladoraService.findAll());
        return FORMULARIO;
    }
}
