package proyecto5.tiendavideojugos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto5.tiendavideojugos.entities.Pedido;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Pedido} usando Spring Data JPA.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente ORDER BY p.fecha DESC, p.id DESC")
    List<Pedido> findAllConCliente();
    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.id = :id")
    Optional<Pedido> findByIdConCliente(@Param("id") Long id);
}