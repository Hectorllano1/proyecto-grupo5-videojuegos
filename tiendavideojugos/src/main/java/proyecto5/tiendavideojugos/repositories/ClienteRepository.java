package proyecto5.tiendavideojugos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto5.tiendavideojugos.entities.Cliente;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Cliente} usando Spring Data JPA.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByOrderByApellidosAscNombreAsc();
    boolean existsByDni(String dni);
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE UPPER(c.dni) = UPPER(:dni) AND c.id <> :id")
    boolean existsByDniAndNotId(@Param("dni") String dni, @Param("id") Long id);
    boolean existsByCorreo(String correo);
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE LOWER(c.correo) = LOWER(:correo) AND c.id <> :id")
    boolean existsByCorreoAndNotId(@Param("correo") String correo, @Param("id") Long id);
}
