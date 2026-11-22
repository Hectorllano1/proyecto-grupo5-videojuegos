package proyecto5.tiendavideojugos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto5.tiendavideojugos.entities.Plataforma;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Plataforma} usando Spring Data JPA.
 */
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {

    List<Plataforma> findAllByOrderByNombreAsc();

    @Query("SELECT COUNT(p) > 0 FROM Plataforma p WHERE LOWER(p.nombre) = LOWER(:nombre)")
    boolean existsByNombre(@Param("nombre") String nombre);

    @Query("SELECT COUNT(p) > 0 FROM Plataforma p WHERE LOWER(p.nombre) = LOWER(:nombre) AND p.id <> :id")
    boolean existsByNombreAndNotId(@Param("nombre") String nombre, @Param("id") Long id);
}