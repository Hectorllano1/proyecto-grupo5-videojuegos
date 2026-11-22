package proyecto5.tiendavideojugos.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto5.tiendavideojugos.entities.Plataforma;
import proyecto5.tiendavideojugos.repositories.PlataformaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio con la lógica de negocio asociada a {@link Plataforma}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlataformaService {

    private final PlataformaRepository plataformaRepository;

    public List<Plataforma> findAll() {
        return plataformaRepository.findAllByOrderByNombreAsc();
    }

    public Optional<Plataforma> findById(Long id) {
        return plataformaRepository.findById(id);
    }

    @Transactional
    public Plataforma save(Plataforma plataforma) {
        if (plataforma.getNombre() != null) {
            plataforma.setNombre(plataforma.getNombre().trim());
        }
        if (plataforma.getFabricante() != null) {
            plataforma.setFabricante(plataforma.getFabricante().trim());
        }
        if (plataforma.getGeneracion() != null) {
            plataforma.setGeneracion(plataforma.getGeneracion().trim());
        }
        return plataformaRepository.save(plataforma);
    }

    @Transactional
    public void deleteById(Long id) {
        plataformaRepository.deleteById(id);
        plataformaRepository.flush();
    }

    public boolean existsNombre(String nombre, Long currentId) {
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        String normalized = nombre.trim();
        return (currentId == null)
                ? plataformaRepository.existsByNombre(normalized)
                : plataformaRepository.existsByNombreAndNotId(normalized, currentId);
    }
}