package proyecto5.tiendavideojugos.controllers;

import es.gamestore.model.Categoria;
import es.gamestore.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador del CRUD de categorías.
 * <p>
 * Rutas: listado, formulario único de alta/edición, guardado y eliminación.
 * </p>
 */
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService servicio;

    /**
     * @param servicio servicio de categorías
     */
    public CategoriaController(CategoriaService servicio) {
        this.servicio = servicio;
    }

    /**
     * Muestra el listado completo de categorías.
     *
     * @param model modelo de la vista
     * @return plantilla del listado
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", servicio.listar());
        return "categorias/lista";
    }

    /**
     * Muestra el formulario vacío para dar de alta una categoría.
     *
     * @param model modelo de la vista
     * @return plantilla del formulario
     */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";
    }

    /**
     * Muestra el mismo formulario con los datos de una categoría existente.
     *
     * @param id    identificador de la categoría
     * @param model modelo de la vista
     * @param ra    atributos flash
     * @return plantilla del formulario, o redirección si no existe
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Categoria> categoria = servicio.buscarPorId(id);
        if (categoria.isEmpty()) {
            ra.addFlashAttribute("error", "La categoría no existe.");
            return "redirect:/categorias";
        }
        model.addAttribute("categoria", categoria.get());
        return "categorias/form";
    }

    /**
     * Valida y guarda una categoría (alta o edición según tenga id o no).
     * Si hay errores, vuelve al formulario conservando lo escrito.
     *
     * @param categoria categoría enviada desde el formulario
     * @param result    resultado de la validación
     * @param ra        atributos flash
     * @return redirección al listado, o el formulario si hay errores
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria,
                          BindingResult result, RedirectAttributes ra) {
        if (categoria.getNombre() != null && !categoria.getNombre().isBlank()) {
            Optional<Categoria> existente = servicio.buscarPorNombre(categoria.getNombre().trim());
            if (existente.isPresent() && !existente.get().getId().equals(categoria.getId())) {
                result.rejectValue("nombre", "duplicado", "Ya existe una categoría con ese nombre");
            }
        }
        if (result.hasErrors()) {
            return "categorias/form";
        }
        categoria.setNombre(categoria.getNombre().trim());
        boolean esNueva = categoria.getId() == null;
        servicio.guardar(categoria);
        ra.addFlashAttribute("exito", esNueva ? "Categoría creada correctamente." : "Categoría actualizada correctamente.");
        return "redirect:/categorias";
    }

    /**
     * Elimina una categoría y vuelve al listado actualizado.
     * No se puede borrar si tiene videojuegos asociados.
     *
     * @param id identificador de la categoría
     * @param ra atributos flash
     * @return redirección al listado
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            servicio.eliminar(id);
            ra.addFlashAttribute("exito", "Categoría eliminada correctamente.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "No se puede eliminar la categoría porque tiene videojuegos asociados.");
        }
        return "redirect:/categorias";
    }
}
