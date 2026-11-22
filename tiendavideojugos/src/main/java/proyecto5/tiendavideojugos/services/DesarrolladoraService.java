package proyecto5.tiendavideojugos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto5.tiendavideojugos.entities.Desarrolladora;
import proyecto5.tiendavideojugos.repositories.DesarrolladoraRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio con la lógica de negocio asociada a {@link Desarrolladora}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DesarrolladoraService {

    private final DesarrolladoraRepository desarrolladoraRepository;

    public List<Desarrolladora> findAll() {
        return desarrolladoraRepository.findAllByOrderByNombreAsc();
    }

    public Optional<Desarrolladora> findById(Long id) {
        return desarrolladoraRepository.findById(id);
    }

    @Transactional
    public Desarrolladora save(Desarrolladora desarrolladora) {
        if (desarrolladora.getNombre() != null) {
            desarrolladora.setNombre(desarrolladora.getNombre().trim());
        }
        if (desarrolladora.getPaisOrigen() != null) {
            desarrolladora.setPaisOrigen(desarrolladora.getPaisOrigen().trim());
        }
        if (desarrolladora.getPaginaWeb() != null && desarrolladora.getPaginaWeb().isBlank()) {
            desarrolladora.setPaginaWeb(null);
        }
        return desarrolladoraRepository.save(desarrolladora);
    }

    @Transactional
    public void deleteById(Long id) {
        desarrolladoraRepository.deleteById(id);
    }

    public boolean existsNombre(String nombre, Long currentId) {
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        String normalized = nombre.trim();
        return (currentId == null)
                ? desarrolladoraRepository.existsByNombre(normalized)
                : desarrolladoraRepository.existsByNombreAndNotId(normalized, currentId);
    }
}