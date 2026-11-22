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
import proyecto5.tiendavideojugos.entities.Factura;
import proyecto5.tiendavideojugos.services.FacturaService;
import proyecto5.tiendavideojugos.services.PedidoService;

import java.util.Optional;

/**
 * Controlador para gestionar las operaciones CRUD de {@link Factura}.
 */
@Controller
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    private final FacturaService facturaService;
    private final PedidoService pedidoService;

    private void loadFormData(Model model) {
        model.addAttribute("pedidos", pedidoService.findAllPedidos());
    }

    @GetMapping
    public String listFacturas(Model model, RedirectAttributes redirectAttributes) {
        logger.info("Listando facturas.");
        try {
            model.addAttribute("facturas", facturaService.findAll());
            return "facturas/factura";
        } catch (DataAccessException e) {
            logger.error("Error al listar las facturas: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar el listado de facturas.");
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String showNewForm(Model model) {
        model.addAttribute("factura", facturaService.prepareNewFactura());
        loadFormData(model);
        return "facturas/factura-form";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Factura> facturaOpt = facturaService.findById(id);
            if (facturaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "No se encontró la factura solicitada.");
                return "redirect:/facturas";
            }
            model.addAttribute("factura", facturaOpt.get());
            loadFormData(model);
            return "facturas/factura-form";
        } catch (DataAccessException e) {
            logger.error("Error al obtener la factura con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cargar los datos de la factura.");
            return "redirect:/facturas";
        }
    }

    @PostMapping("/guardar")
    public String saveFactura(@Valid @ModelAttribute("factura") Factura factura,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            if (factura.getPedido() == null || factura.getPedido().getId() == null) {
                result.rejectValue("pedido", "msg.factura.pedido.notNull", "Debes seleccionar un pedido.");
            } else if (facturaService.existsPedidoFacturado(factura.getPedido().getId(), factura.getId())) {
                result.rejectValue("pedido", "error.factura.pedido.duplicado", "Ese pedido ya tiene una factura asignada.");
            }

            if (facturaService.existsNumeroFactura(factura.getNumeroFactura(), factura.getId())) {
                result.rejectValue("numeroFactura", "error.factura.numero.duplicado", "Ya existe una factura con ese número.");
            }

            if (result.hasErrors()) {
                loadFormData(model);
                return "facturas/factura-form";
            }

            Optional<Factura> saved = facturaService.save(factura);
            if (saved.isEmpty()) {
                result.rejectValue("pedido", "error.factura.pedido.invalido", "El pedido seleccionado no existe.");
                loadFormData(model);
                return "facturas/factura-form";
            }

            redirectAttributes.addFlashAttribute("successMessage", "Factura guardada correctamente.");
            return "redirect:/facturas";
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("numeroFactura", "error.factura.integridad", "Número de factura o pedido duplicado.");
            loadFormData(model);
            return "facturas/factura-form";
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al guardar la factura: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la factura.");
            return "redirect:/facturas";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String deleteFactura(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (facturaService.findById(id).isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "La factura no existe.");
                return "redirect:/facturas";
            }
            facturaService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Factura eliminada correctamente.");
            return "redirect:/facturas";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede eliminar la factura por restricciones de integridad.");
            return "redirect:/facturas";
        } catch (DataAccessException e) {
            logger.error("Error al eliminar la factura con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la factura.");
            return "redirect:/facturas";
        }
    }
}
