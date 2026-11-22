package proyecto5.tiendavideojugos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto5.tiendavideojugos.entities.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT DISTINCT p FROM Pedido p JOIN FETCH p.cliente LEFT JOIN FETCH p.videojuegos ORDER BY p.fecha DESC, p.id DESC")
    List<Pedido> findAllConRelaciones();

    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente LEFT JOIN FETCH p.videojuegos WHERE p.id = :id")
    Optional<Pedido> findByIdConRelaciones(@Param("id") Long id);
}