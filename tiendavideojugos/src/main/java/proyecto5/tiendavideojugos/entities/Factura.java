package proyecto5.tiendavideojugos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que modela una factura asociada a un pedido mediante una relación OneToOne.
 */
@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.factura.numeroFactura.notEmpty}")
    @Size(max = 30, message = "{msg.factura.numeroFactura.size}")
    @Column(name = "numero_factura", nullable = false, unique = true, length = 30)
    private String numeroFactura;

    @NotNull(message = "{msg.factura.fechaEmision.notNull}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @NotNull(message = "{msg.factura.importeTotal.notNull}")
    @DecimalMin(value = "0.00", message = "{msg.factura.importeTotal.min}")
    @Digits(integer = 8, fraction = 2, message = "{msg.factura.importeTotal.digits}")
    @Column(name = "importe_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeTotal;

    @Column(name = "pagada", nullable = false)
    private boolean pagada;

    @Size(max = 50, message = "{msg.factura.metodoPago.size}")
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @NotNull(message = "{msg.factura.pedido.notNull}")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    public Factura(String numeroFactura, LocalDate fechaEmision, BigDecimal importeTotal,
                   boolean pagada, String metodoPago, Pedido pedido) {
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.importeTotal = importeTotal;
        this.pagada = pagada;
        this.metodoPago = metodoPago;
        this.pedido = pedido;
    }
}
