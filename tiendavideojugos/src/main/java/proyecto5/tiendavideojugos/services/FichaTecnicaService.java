package proyecto5.tiendavideojugos.services;

import es.gamestore.model.FichaTecnica;
import es.gamestore.repository.FichaTecnicaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio con la lógica de negocio de {@link FichaTecnica}.
 */
@Service
public class FichaTecnicaService {

    private final FichaTecnicaRepository repositorio;

    /**
     * @param repositorio repositorio de fichas técnicas
     */
    public FichaTecnicaService(FichaTecnicaRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * @return todas las fichas técnicas
     */
    @Transactional(readOnly = true)
    public List<FichaTecnica> listar() {
        return repositorio.findAll(Sort.by("id"));
    }

    /**
     * Fichas que se pueden elegir en el formulario de un videojuego.
     *
     * @param videojuegoId id del videojuego en edición, o {@code null} si es nuevo
     * @return fichas libres más la ya asignada a ese videojuego
     */
    @Transactional(readOnly = true)
    public List<FichaTecnica> listarDisponibles(Long videojuegoId) {
        return repositorio.findDisponibles(videojuegoId == null ? -1L : videojuegoId);
    }

    /**
     * @param id identificador
     * @return la ficha, si existe
     */
    @Transactional(readOnly = true)
    public Optional<FichaTecnica> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    /**
     * Crea o actualiza una ficha técnica.
     *
     * @param ficha ficha a guardar
     * @return la ficha guardada
     */
    @Transactional
    public FichaTecnica guardar(FichaTecnica ficha) {
        return repositorio.save(ficha);
    }

    /**
     * Elimina una ficha técnica.
     *
     * @param id identificador
     */
    @Transactional
    public void eliminar(Long id) {
        repositorio.deleteById(id);
        repositorio.flush();
    }

    /**
     * @return número total de fichas
     */
    @Transactional(readOnly = true)
    public long contar() {
        return repositorio.count();
    }
}
