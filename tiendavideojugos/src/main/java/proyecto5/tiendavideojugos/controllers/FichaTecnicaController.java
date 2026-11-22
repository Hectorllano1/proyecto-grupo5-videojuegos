package es.gamestore.controller;

import es.gamestore.model.FichaTecnica;
import es.gamestore.service.FichaTecnicaService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador del CRUD de fichas técnicas (entidad auxiliar de la relación OneToOne con videojuego).
 */
@Controller
@RequestMapping("/fichas")
public class FichaTecnicaController {

    private final FichaTecnicaService servicio;

    /**
     * @param servicio servicio de fichas técnicas
     */
    public FichaTecnicaController(FichaTecnicaService servicio) {
        this.servicio = servicio;
    }

    /**
     * Muestra el listado completo de fichas técnicas.
     *
     * @param model modelo de la vista
     * @return plantilla del listado
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("fichas", servicio.listar());
        return "fichas/lista";
    }

    /**
     * Muestra el formulario vacío de alta.
     *
     * @param model modelo de la vista
     * @return plantilla del formulario
     */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("fichaTecnica", new FichaTecnica());
        return "fichas/form";
    }

    /**
     * Muestra el formulario con los datos de una ficha existente.
     *
     * @param id    identificador de la ficha
     * @param model modelo de la vista
     * @param ra    atributos flash
     * @return plantilla del formulario, o redirección si no existe
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<FichaTecnica> ficha = servicio.buscarPorId(id);
        if (ficha.isEmpty()) {
            ra.addFlashAttribute("error", "La ficha técnica no existe.");
            return "redirect:/fichas";
        }
        model.addAttribute("fichaTecnica", ficha.get());
        return "fichas/form";
    }

    /**
     * Valida y guarda una ficha técnica.
     *
     * @param ficha  ficha enviada desde el formulario
     * @param result resultado de la validación
     * @param ra     atributos flash
     * @return redirección al listado, o el formulario si hay errores
     */
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("fichaTecnica") FichaTecnica ficha,
                          BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "fichas/form";
        }
        boolean esNueva = ficha.getId() == null;
        servicio.guardar(ficha);
        ra.addFlashAttribute("exito", esNueva ? "Ficha técnica creada correctamente." : "Ficha técnica actualizada correctamente.");
        return "redirect:/fichas";
    }

    /**
     * Elimina una ficha técnica. No se puede borrar si está asignada a un videojuego.
     *
     * @param id identificador de la ficha
     * @param ra atributos flash
     * @return redirección al listado
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            servicio.eliminar(id);
            ra.addFlashAttribute("exito", "Ficha técnica eliminada correctamente.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "No se puede eliminar la ficha porque está asignada a un videojuego.");
        }
        return "redirect:/fichas";
    }
}
