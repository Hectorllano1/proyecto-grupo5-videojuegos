package proyecto5.tiendavideojugos.repositories;

import proyecto5.tiendavideojugos.entities.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA de {@link Videojuego}.
 */
@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {

    /**
     * Busca un videojuego por título ignorando mayúsculas/minúsculas.
     * Se usa para comprobar que el título no esté repetido.
     *
     * @param titulo título a buscar
     * @return el videojuego, si existe
     */
    Optional<Videojuego> findByTituloIgnoreCase(String titulo);

    /**
     * Busca el videojuego que tiene asignada una ficha técnica concreta.
     *
     * @param fichaId identificador de la ficha técnica
     * @return el videojuego, si existe
     */
    Optional<Videojuego> findByFichaTecnicaId(Long fichaId);
}
