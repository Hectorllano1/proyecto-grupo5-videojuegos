package proyecto5.tiendavideojugos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import proyecto5.tiendavideojugos.entities.Factura;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Factura} usando Spring Data JPA.
 */
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query("SELECT f FROM Factura f JOIN FETCH f.pedido p JOIN FETCH p.cliente ORDER BY f.fechaEmision DESC, f.id DESC")
    List<Factura> findAllConPedido();

    @Query("SELECT f FROM Factura f JOIN FETCH f.pedido p JOIN FETCH p.cliente WHERE f.id = :id")
    Optional<Factura> findByIdConPedido(@Param("id") Long id);

    boolean existsByNumeroFactura(String numeroFactura);

    @Query("SELECT COUNT(f) > 0 FROM Factura f WHERE UPPER(f.numeroFactura) = UPPER(:numero) AND f.id <> :id")
    boolean existsByNumeroFacturaAndNotId(@Param("numero") String numero, @Param("id") Long id);

    boolean existsByPedidoId(Long pedidoId);

    @Query("SELECT COUNT(f) > 0 FROM Factura f WHERE f.pedido.id = :pedidoId AND f.id <> :id")
    boolean existsByPedidoIdAndNotId(@Param("pedidoId") Long pedidoId, @Param("id") Long id);
}