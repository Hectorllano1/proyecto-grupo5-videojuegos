package proyecto5.tiendavideojugos.controllers;

import proyecto5.tiendavideojugos.entities.FichaTecnica;
import proyecto5.tiendavideojugos.services.FichaTecnicaService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/fichas")
public class FichaTecnicaController {

    private final FichaTecnicaService servicio;

    public FichaTecnicaController(FichaTecnicaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("fichas", servicio.listar());
        return "fichas/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("fichaTecnica", new FichaTecnica());
        return "fichas/form";
    }

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

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            servicio.eliminar(id);
            ra.addFlashAttribute("exito", "Ficha técnica eliminada correctamente.");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "No se puede eliminar la ficha porque está asignada a un videojuego.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se pudo eliminar la ficha técnica: " + e.getMessage());
        }
        return "redirect:/fichas";
    }
}

