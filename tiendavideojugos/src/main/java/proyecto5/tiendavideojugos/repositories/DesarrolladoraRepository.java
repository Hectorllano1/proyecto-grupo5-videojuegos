package proyecto5.tiendavideojugos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto5.tiendavideojugos.entities.Desarrolladora;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Desarrolladora} usando Spring Data JPA.
 */
public interface DesarrolladoraRepository extends JpaRepository<Desarrolladora, Long> {

    List<Desarrolladora> findAllByOrderByNombreAsc();

    @Query("SELECT COUNT(d) > 0 FROM Desarrolladora d WHERE LOWER(d.nombre) = LOWER(:nombre)")
    boolean existsByNombre(@Param("nombre") String nombre);

    @Query("SELECT COUNT(d) > 0 FROM Desarrolladora d WHERE LOWER(d.nombre) = LOWER(:nombre) AND d.id <> :id")
    boolean existsByNombreAndNotId(@Param("nombre") String nombre, @Param("id") Long id);
}