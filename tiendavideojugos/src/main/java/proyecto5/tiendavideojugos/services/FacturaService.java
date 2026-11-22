package proyecto5.tiendavideojugos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto5.tiendavideojugos.entities.Factura;
import proyecto5.tiendavideojugos.entities.Pedido;
import proyecto5.tiendavideojugos.repositories.FacturaRepository;
import proyecto5.tiendavideojugos.repositories.PedidoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio con la lógica de negocio asociada a {@link Factura}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;

    public List<Factura> findAll() {
        return facturaRepository.findAllConPedido();
    }

    public Optional<Factura> findById(Long id) {
        return facturaRepository.findByIdConPedido(id);
    }

    public Factura prepareNewFactura() {
        Factura factura = new Factura();
        factura.setFechaEmision(LocalDate.now());
        factura.setPagada(false);
        return factura;
    }

    public boolean existsNumeroFactura(String numeroFactura, Long currentId) {
        if (numeroFactura == null || numeroFactura.isBlank()) {
            return false;
        }
        String normalized = numeroFactura.trim().toUpperCase();
        return (currentId == null)
                ? facturaRepository.existsByNumeroFactura(normalized)
                : facturaRepository.existsByNumeroFacturaAndNotId(normalized, currentId);
    }

    public boolean existsPedidoFacturado(Long pedidoId, Long currentFacturaId) {
        if (pedidoId == null) {
            return false;
        }
        return (currentFacturaId == null)
                ? facturaRepository.existsByPedidoId(pedidoId)
                : facturaRepository.existsByPedidoIdAndNotId(pedidoId, currentFacturaId);
    }

    @Transactional
    public Optional<Factura> save(Factura factura) {
        if (factura.getPedido() == null || factura.getPedido().getId() == null) {
            return Optional.empty();
        }
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(factura.getPedido().getId());
        if (pedidoOpt.isEmpty()) {
            return Optional.empty();
        }
        factura.setPedido(pedidoOpt.get());

        if (factura.getNumeroFactura() != null) {
            factura.setNumeroFactura(factura.getNumeroFactura().trim().toUpperCase());
        }
        if (factura.getMetodoPago() != null && factura.getMetodoPago().isBlank()) {
            factura.setMetodoPago(null);
        }

        return Optional.of(facturaRepository.save(factura));
    }

    @Transactional
    public void deleteById(Long id) {
        facturaRepository.deleteById(id);
    }
}
