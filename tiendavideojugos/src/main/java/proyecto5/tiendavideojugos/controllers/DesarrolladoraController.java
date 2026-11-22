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
import proyecto5.tiendavideojugos.entities.Desarrolladora;
import proyecto5.tiendavideojugos.services.DesarrolladoraService;

import java.util.Optional;

/**
 * Controlador para gestionar las operaciones CRUD de {@link Desarrolladora}.
 */
@Controller
@RequestMapping("/desarrolladoras")
@RequiredArgsConstructor
public class DesarrolladoraController {

    private static final Logger logger = LoggerFactory.getLogger(DesarrolladoraController.class);

    private final DesarrolladoraService desarrolladoraService;

    @GetMapping
    public String listDesarrolladoras(Model model, RedirectAttributes redirectAttributes) {
        logger.info("Listando desarrolladoras.");
        try {
            model.addAttribute("desarrolladoras", desarrolladoraService.findAll());
            return "desarrolladoras/desarrolladora";
        } catch (DataAccessException e) {
            logger.error("Error al listar las desarrolladoras: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar el listado de desarrolladoras.");
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String showNewForm(Model model) {
        model.addAttribute("desarrolladora", new Desarrolladora());
        return "desarrolladoras/desarrolladora-form";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Desarrolladora> desarrolladoraOpt = desarrolladoraService.findById(id);
            if (desarrolladoraOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No se encontró la desarrolladora solicitada.");
                return "redirect:/desarrolladoras";
            }
            model.addAttribute("desarrolladora", desarrolladoraOpt.get());
            return "desarrolladoras/desarrolladora-form";
        } catch (DataAccessException e) {
            logger.error("Error al obtener la desarrolladora con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar los datos de la desarrolladora.");
            return "redirect:/desarrolladoras";
        }
    }

    @PostMapping("/guardar")
    public String saveDesarrolladora(@Valid @ModelAttribute("desarrolladora") Desarrolladora desarrolladora,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "desarrolladoras/desarrolladora-form";
            }

            if (desarrolladoraService.existsNombre(desarrolladora.getNombre(), desarrolladora.getId())) {
                result.rejectValue("nombre", "error.desarrolladora.nombre.duplicado",
                        "Ya existe una desarrolladora con ese nombre.");
                return "desarrolladoras/desarrolladora-form";
            }

            desarrolladoraService.save(desarrolladora);
            redirectAttributes.addFlashAttribute("successMessage", "Desarrolladora guardada correctamente.");
            return "redirect:/desarrolladoras";
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("nombre", "error.desarrolladora.integridad",
                    "Nombre duplicado o datos no válidos en la base de datos.");
            return "desarrolladoras/desarrolladora-form";
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al guardar la desarrolladora: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la desarrolladora.");
            return "redirect:/desarrolladoras";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String deleteDesarrolladora(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (desarrolladoraService.findById(id).isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "La desarrolladora no existe.");
                return "redirect:/desarrolladoras";
            }
            desarrolladoraService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Desarrolladora eliminada correctamente.");
            return "redirect:/desarrolladoras";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "No se puede eliminar la desarrolladora porque tiene videojuegos asociados.");
            return "redirect:/desarrolladoras";
        } catch (DataAccessException e) {
            logger.error("Error al eliminar la desarrolladora con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la desarrolladora.");
            return "redirect:/desarrolladoras";
        }
    }
}
