package proyecto5.tiendavideojugos.services;

import proyecto5.tiendavideojugos.entities.Videojuego;
import proyecto5.tiendavideojugos.repositories.VideojuegoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class VideojuegoService {

    private final VideojuegoRepository repositorio;
    private final FileStorageService almacenamiento;

    public VideojuegoService(VideojuegoRepository repositorio, FileStorageService almacenamiento) {
        this.repositorio = repositorio;
        this.almacenamiento = almacenamiento;
    }

    @Transactional(readOnly = true)
    public List<Videojuego> listar() {
        return repositorio.findAll(Sort.by("titulo"));
    }

    @Transactional(readOnly = true)
    public Optional<Videojuego> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Videojuego> buscarPorTitulo(String titulo) {
        return repositorio.findByTituloIgnoreCase(titulo);
    }

    @Transactional(readOnly = true)
    public Optional<Videojuego> buscarPorFicha(Long fichaId) {
        return repositorio.findByFichaTecnicaId(fichaId);
    }

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

    @Transactional
    public void eliminar(Long id) {
        Optional<Videojuego> existente = repositorio.findById(id);
        if (existente.isPresent()) {
            Videojuego vj = existente.get();
            String imagen = vj.getImagen();
            try {
                repositorio.delete(vj);
                repositorio.flush();
            } catch (DataIntegrityViolationException e) {
                throw new DataIntegrityViolationException("No se puede eliminar el videojuego porque está asociado a otros registros.", e);
            }
            if (imagen != null && !imagen.isBlank()) {
                try {
                    almacenamiento.eliminar(imagen);
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public long contar() {
        return repositorio.count();
    }
}
