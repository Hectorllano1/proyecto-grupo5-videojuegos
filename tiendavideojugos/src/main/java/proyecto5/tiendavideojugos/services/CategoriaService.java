package proyecto5.tiendavideojugos.services;

import proyecto5.tiendavideojugos.entities.Categoria;
import proyecto5.tiendavideojugos.repositories.CategoriaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio con la lógica de negocio de {@link Categoria}.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository repositorio;

    /**
     * @param repositorio repositorio de categorías
     */
    public CategoriaService(CategoriaRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * @return todas las categorías ordenadas por nombre
     */
    @Transactional(readOnly = true)
    public List<Categoria> listar() {
        return repositorio.findAll(Sort.by("nombre"));
    }

    /**
     * @param id identificador
     * @return la categoría, si existe
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    /**
     * @param nombre nombre a buscar (sin distinguir mayúsculas)
     * @return la categoría con ese nombre, si existe
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return repositorio.findByNombreIgnoreCase(nombre);
    }

    /**
     * Crea o actualiza una categoría.
     *
     * @param categoria categoría a guardar
     * @return la categoría guardada
     */
    @Transactional
    public Categoria guardar(Categoria categoria) {
        return repositorio.save(categoria);
    }

    /**
     * Elimina una categoría.
     *
     * @param id identificador
     */
    @Transactional
    public void eliminar(Long id) {
        repositorio.deleteById(id);
        repositorio.flush();
    }

    /**
     * @return número total de categorías
     */
    @Transactional(readOnly = true)
    public long contar() {
        return repositorio.count();
    }
}

