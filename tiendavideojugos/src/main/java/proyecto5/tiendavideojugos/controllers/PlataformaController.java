package proyecto5.tiendavideojugos.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proyecto5.tiendavideojugos.entities.Plataforma;
import proyecto5.tiendavideojugos.services.PlataformaService;

import java.util.Optional;

/**
 * Controlador para gestionar las operaciones CRUD de {@link Plataforma}.
 */
@Controller
@RequestMapping("/plataformas")
@RequiredArgsConstructor
public class PlataformaController {

    private static final Logger logger = LoggerFactory.getLogger(PlataformaController.class);

    private final PlataformaService plataformaService;

    @GetMapping
    public String listPlataformas(Model model, RedirectAttributes redirectAttributes) {
        logger.info("Listando plataformas.");
        try {
            model.addAttribute("plataformas", plataformaService.findAll());
            return "plataformas/plataforma";
        } catch (DataAccessException e) {
            logger.error("Error al listar las plataformas: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar el listado de plataformas.");
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String showNewForm(Model model) {
        model.addAttribute("plataforma", new Plataforma());
        return "plataformas/plataforma-form";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Plataforma> plataformaOpt = plataformaService.findById(id);
            if (plataformaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No se encontró la plataforma solicitada.");
                return "redirect:/plataformas";
            }
            model.addAttribute("plataforma", plataformaOpt.get());
            return "plataformas/plataforma-form";
        } catch (DataAccessException e) {
            logger.error("Error al obtener la plataforma con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar los datos de la plataforma.");
            return "redirect:/plataformas";
        }
    }

    @PostMapping("/guardar")
    public String savePlataforma(@Valid @ModelAttribute("plataforma") Plataforma plataforma,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "plataformas/plataforma-form";
            }

            if (plataformaService.existsNombre(plataforma.getNombre(), plataforma.getId())) {
                result.rejectValue("nombre", "error.plataforma.nombre.duplicado",
                        "Ya existe una plataforma con ese nombre.");
                return "plataformas/plataforma-form";
            }

            plataformaService.save(plataforma);
            redirectAttributes.addFlashAttribute("successMessage", "Plataforma guardada correctamente.");
            return "redirect:/plataformas";
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("nombre", "error.plataforma.integridad",
                    "Nombre duplicado o datos no válidos en la base de datos.");
            return "plataformas/plataforma-form";
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al guardar la plataforma: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la plataforma.");
            return "redirect:/plataformas";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String deletePlataforma(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (plataformaService.findById(id).isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "La plataforma no existe.");
                return "redirect:/plataformas";
            }
            plataformaService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Plataforma eliminada correctamente.");
            return "redirect:/plataformas";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "No se puede eliminar la plataforma porque tiene videojuegos asociados.");
            return "redirect:/plataformas";
        } catch (DataAccessException e) {
            logger.error("Error al eliminar la plataforma con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la plataforma.");
            return "redirect:/plataformas";
        }
    }
}