package proyecto5.tiendavideojugos.services;

import proyecto5.tiendavideojugos.entities.Videojuego;
import proyecto5.tiendavideojugos.repositories.VideojuegoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Servicio con la lógica de negocio de {@link Videojuego}, incluida la gestión de la imagen.
 */
@Service
public class VideojuegoService {

    private final VideojuegoRepository repositorio;
    private final FileStorageService almacenamiento;

    /**
     * @param repositorio    repositorio de videojuegos
     * @param almacenamiento servicio de almacenamiento de imágenes
     */
    public VideojuegoService(VideojuegoRepository repositorio, FileStorageService almacenamiento) {
        this.repositorio = repositorio;
        this.almacenamiento = almacenamiento;
    }

    /**
     * @return todos los videojuegos ordenados por título
     */
    @Transactional(readOnly = true)
    public List<Videojuego> listar() {
        return repositorio.findAll(Sort.by("titulo"));
    }

    /**
     * @param id identificador
     * @return el videojuego, si existe
     */
    @Transactional(readOnly = true)
    public Optional<Videojuego> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    /**
     * @param titulo título a buscar (sin distinguir mayúsculas)
     * @return el videojuego con ese título, si existe
     */
    @Transactional(readOnly = true)
    public Optional<Videojuego> buscarPorTitulo(String titulo) {
        return repositorio.findByTituloIgnoreCase(titulo);
    }

    /**
     * @param fichaId identificador de una ficha técnica
     * @return el videojuego que la tiene asignada, si existe
     */
    @Transactional(readOnly = true)
    public Optional<Videojuego> buscarPorFicha(Long fichaId) {
        return repositorio.findByFichaTecnicaId(fichaId);
    }

    /**
     * Crea o actualiza un videojuego.
     * <p>
     * Si se sube una imagen nueva, se guarda en el servidor y se borra la anterior.
     * Si no se sube ninguna al editar, se conserva la imagen que ya tenía.
     * </p>
     *
     * @param videojuego videojuego a guardar
     * @param imagen     fichero de imagen del formulario (puede ser {@code null} o estar vacío)
     * @return el videojuego guardado
     * @throws IllegalArgumentException si el fichero subido no es una imagen válida
     */
    @Transactional
    public Videojuego guardar(Videojuego videojuego, MultipartFile imagen) {
        String imagenAnterior = null;
        if (videojuego.getId() != null) {
            imagenAnterior = repositorio.findById(videojuego.getId())
                    .map(Videojuego::getImagen).orElse(null);
            videojuego.setImagen(imagenAnterior);
        }
        if (imagen != null && !imagen.isEmpty()) {
            videojuego.setImagen(almacenamiento.guardar(imagen));
            almacenamiento.eliminar(imagenAnterior);
        }
        return repositorio.save(videojuego);
    }

    /**
     * Elimina un videojuego y su imagen del servidor.
     *
     * @param id identificador
     */
    @Transactional
    public void eliminar(Long id) {
        Optional<Videojuego> existente = repositorio.findById(id);
        if (existente.isPresent()) {
            String imagen = existente.get().getImagen();
            repositorio.delete(existente.get());
            repositorio.flush();
            almacenamiento.eliminar(imagen);
        }
    }

    /**
     * @return número total de videojuegos
     */
    @Transactional(readOnly = true)
    public long contar() {
        return repositorio.count();
    }
}
