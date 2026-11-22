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
import proyecto5.tiendavideojugos.entities.Pedido;
import proyecto5.tiendavideojugos.services.ClienteService;
import proyecto5.tiendavideojugos.services.PedidoService;

import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    private void loadFormData(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("estados", Pedido.Estado.values());
        model.addAttribute("videojuegos", pedidoService.findAllVideojuegos());
    }

    @GetMapping
    public String listPedidos(Model model, RedirectAttributes redirectAttributes) {
        logger.info("Listando pedidos.");
        try {
            model.addAttribute("pedidos", pedidoService.findAll());
            return "pedidos/pedido";
        } catch (DataAccessException e) {
            logger.error("Error al listar los pedidos: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar el listado de pedidos.");
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String showNewForm(Model model) {
        model.addAttribute("pedido", pedidoService.prepareNewPedido());
        loadFormData(model);
        return "pedidos/pedido-form";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Pedido> pedidoOpt = pedidoService.findById(id);
            if (pedidoOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No se encontró el pedido solicitado.");
                return "redirect:/pedidos";
            }
            model.addAttribute("pedido", pedidoOpt.get());
            loadFormData(model);
            return "pedidos/pedido-form";
        } catch (DataAccessException e) {
            logger.error("Error al obtener el pedido con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar los datos del pedido.");
            return "redirect:/pedidos";
        }
    }

    @PostMapping("/guardar")
    public String savePedido(@Valid @ModelAttribute("pedido") Pedido pedido,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
                result.rejectValue("cliente", "msg.pedido.cliente.notNull", "Debes seleccionar un cliente.");
            }

            if (result.hasErrors()) {
                loadFormData(model);
                return "pedidos/pedido-form";
            }

            Optional<Pedido> saved = pedidoService.save(pedido);
            if (saved.isEmpty()) {
                result.rejectValue("cliente", "error.pedido.cliente.invalido", "El cliente seleccionado no existe.");
                loadFormData(model);
                return "pedidos/pedido-form";
            }

            redirectAttributes.addFlashAttribute("successMessage", "Pedido guardado correctamente.");
            return "redirect:/pedidos";
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("cliente", "error.pedido.integridad", "Datos no válidos en el pedido.");
            loadFormData(model);
            return "pedidos/pedido-form";
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al guardar el pedido: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el pedido.");
            return "redirect:/pedidos";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String deletePedido(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (pedidoService.findById(id).isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "El pedido no existe.");
                return "redirect:/pedidos";
            }
            pedidoService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido eliminado correctamente.");
            return "redirect:/pedidos";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede eliminar el pedido porque tiene registros asociados.");
            return "redirect:/pedidos";
        } catch (DataAccessException e) {
            logger.error("Error al eliminar el pedido con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el pedido.");
            return "redirect:/pedidos";
        }
    }
}
