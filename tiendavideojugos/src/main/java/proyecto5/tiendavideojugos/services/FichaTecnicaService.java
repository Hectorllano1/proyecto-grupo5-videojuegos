package proyecto5.tiendavideojugos.services;

import proyecto5.tiendavideojugos.entities.FichaTecnica;
import proyecto5.tiendavideojugos.repositories.FichaTecnicaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FichaTecnicaService {

    private final FichaTecnicaRepository repositorio;

    public FichaTecnicaService(FichaTecnicaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<FichaTecnica> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    @Transactional(readOnly = true)
    public List<FichaTecnica> listarDisponibles(Long videojuegoId) {
        return repositorio.findDisponibles(videojuegoId == null ? -1L : videojuegoId);
    }

    @Transactional(readOnly = true)
    public Optional<FichaTecnica> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    @Transactional
    public FichaTecnica guardar(FichaTecnica ficha) {
        return repositorio.save(ficha);
    }

    @Transactional
    public void eliminar(Long id) {
        Optional<FichaTecnica> existente = repositorio.findById(id);
        if (existente.isPresent()) {
            try {
                repositorio.delete(existente.get());
                repositorio.flush();
            } catch (DataIntegrityViolationException e) {
                throw new DataIntegrityViolationException("No se puede eliminar la ficha porque está asignada a un videojuego.", e);
            }
        }
    }

    @Transactional(readOnly = true)
    public long contar() {
        return repositorio.count();
    }
}