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
import proyecto5.tiendavideojugos.entities.Cliente;
import proyecto5.tiendavideojugos.services.ClienteService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para gestionar las operaciones CRUD de {@link Cliente}.
 */
@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService clienteService;

    @GetMapping
    public String listClientes(Model model, RedirectAttributes redirectAttributes) {
        logger.info("Listando clientes.");
        try {
            List<Cliente> clientes = clienteService.findAll();
            model.addAttribute("clientes", clientes);
            return "clientes/lista";
        } catch (DataAccessException e) {
            logger.error("Error al listar los clientes: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar el listado de clientes.");
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario de alta de cliente.");
        Cliente cliente = new Cliente();
        cliente.setFechaRegistro(LocalDate.now());
        model.addAttribute("cliente", cliente);
        return "clientes/formulario";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Mostrando formulario de edición para el cliente con ID {}", id);
        try {
            Optional<Cliente> clienteOpt = clienteService.findById(id);
            if (clienteOpt.isEmpty()) {
                logger.warn("No se encontró el cliente con ID {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "No se encontró el cliente solicitado.");
                return "redirect:/clientes";
            }
            model.addAttribute("cliente", clienteOpt.get());
            return "clientes/formulario";
        } catch (DataAccessException e) {
            logger.error("Error al obtener el cliente con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar los datos del cliente.");
            return "redirect:/clientes";
        }
    }

    @PostMapping("/guardar")
    public String saveCliente(@Valid @ModelAttribute("cliente") Cliente cliente,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        logger.info("Guardando cliente con DNI {}", cliente.getDni());
        try {
            if (result.hasErrors()) {
                return "clientes/formulario";
            }

            if (clienteService.existsDni(cliente.getDni(), cliente.getId())) {
                logger.warn("El DNI {} ya existe.", cliente.getDni());
                result.rejectValue("dni", "error.cliente.dni.duplicado", "Ya existe un cliente con ese DNI.");
                return "clientes/formulario";
            }

            if (clienteService.existsCorreo(cliente.getCorreo(), cliente.getId())) {
                logger.warn("El correo {} ya existe.", cliente.getCorreo());
                result.rejectValue("correo", "error.cliente.correo.duplicado", "Ya existe un cliente con ese correo.");
                return "clientes/formulario";
            }

            clienteService.save(cliente);
            logger.info("Cliente con DNI {} guardado con éxito.", cliente.getDni());
            redirectAttributes.addFlashAttribute("successMessage", "Cliente guardado correctamente.");
            return "redirect:/clientes";
        } catch (DataIntegrityViolationException e) {
            logger.warn("Violación de integridad al guardar el cliente: {}", e.getMessage());
            result.rejectValue("dni", "error.cliente.integridad", "DNI o correo duplicado en la base de datos.");
            return "clientes/formulario";
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al guardar el cliente: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el cliente.");
            return "redirect:/clientes";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String deleteCliente(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando cliente con ID {}", id);
        try {
            Optional<Cliente> clienteOpt = clienteService.findById(id);
            if (clienteOpt.isEmpty()) {
                logger.warn("No se encontró el cliente con ID {} para eliminar.", id);
                redirectAttributes.addFlashAttribute("errorMessage", "El cliente no existe.");
                return "redirect:/clientes";
            }

            clienteService.deleteById(id);
            logger.info("Cliente con ID {} eliminado con éxito.", id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente eliminado correctamente.");
            return "redirect:/clientes";
        } catch (DataIntegrityViolationException e) {
            logger.warn("No se puede eliminar el cliente con ID {} porque tiene registros asociados: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede eliminar el cliente porque tiene pedidos asociados.");
            return "redirect:/clientes";
        } catch (DataAccessException e) {
            logger.error("Error al eliminar el cliente con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el cliente.");
            return "redirect:/clientes";
        }
    }
}
