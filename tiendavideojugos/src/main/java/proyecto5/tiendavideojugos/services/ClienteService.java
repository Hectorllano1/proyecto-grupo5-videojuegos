package proyecto5.tiendavideojugos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto5.tiendavideojugos.entities.Cliente;
import proyecto5.tiendavideojugos.repositories.ClienteRepository;
import java.util.List;
import java.util.Optional;

/**
 * Servicio con la lógica de negocio asociada a {@link Cliente}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAllByOrderByApellidosAscNombreAsc();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        if (cliente.getDni() != null) {
            cliente.setDni(cliente.getDni().trim().toUpperCase());
        }
        if (cliente.getCorreo() != null) {
            cliente.setCorreo(cliente.getCorreo().trim().toLowerCase());
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    public boolean existsDni(String dni, Long currentId) {
        if (dni == null || dni.isBlank()) {
            return false;
        }
        String normalized = dni.trim().toUpperCase();
        return (currentId == null)
                ? clienteRepository.existsByDni(normalized)
                : clienteRepository.existsByDniAndNotId(normalized, currentId);
    }

    public boolean existsCorreo(String correo, Long currentId) {
        if (correo == null || correo.isBlank()) {
            return false;
        }
        String normalized = correo.trim().toLowerCase();
        return (currentId == null)
                ? clienteRepository.existsByCorreo(normalized)
                : clienteRepository.existsByCorreoAndNotId(normalized, currentId);
    }
}
