package proyecto5.tiendavideojugos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto5.tiendavideojugos.entities.Cliente;
import proyecto5.tiendavideojugos.entities.Pedido;
import proyecto5.tiendavideojugos.entities.Videojuego;
import proyecto5.tiendavideojugos.repositories.ClienteRepository;
import proyecto5.tiendavideojugos.repositories.PedidoRepository;
import proyecto5.tiendavideojugos.repositories.VideojuegoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio con la lógica de negocio asociada a {@link Pedido}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final VideojuegoRepository videojuegoRepository;

    public List<Pedido> findAll() {
        return pedidoRepository.findAllConRelaciones();
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findByIdConRelaciones(id);
    }

    public List<Pedido> findAllPedidos() {
        return pedidoRepository.findAllConRelaciones();
    }

    public List<Videojuego> findAllVideojuegos() {
        return videojuegoRepository.findAll();
    }

    public Pedido prepareNewPedido() {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setEstado(Pedido.Estado.PENDIENTE);
        return pedido;
    }

    @Transactional
    public Optional<Pedido> save(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            return Optional.empty();
        }
        Optional<Cliente> clienteOpt = clienteRepository.findById(pedido.getCliente().getId());
        if (clienteOpt.isEmpty()) {
            return Optional.empty();
        }
        pedido.setCliente(clienteOpt.get());

        if (pedido.getVideojuegos() != null && !pedido.getVideojuegos().isEmpty()) {
            List<Long> ids = pedido.getVideojuegos().stream()
                    .map(Videojuego::getId)
                    .filter(id -> id != null)
                    .toList();
            pedido.setVideojuegos(new ArrayList<>(videojuegoRepository.findAllById(ids)));
        } else {
            pedido.setVideojuegos(new ArrayList<>());
        }

        if (pedido.getNumeroSeguimiento() != null && pedido.getNumeroSeguimiento().isBlank()) {
            pedido.setNumeroSeguimiento(null);
        }

        return Optional.of(pedidoRepository.save(pedido));
    }

    @Transactional
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }
}