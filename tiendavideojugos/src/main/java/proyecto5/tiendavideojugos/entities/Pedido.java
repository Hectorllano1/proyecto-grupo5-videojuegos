package proyecto5.tiendavideojugos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que modela un pedido realizado por un cliente y los videojuegos incluidos en él.
 */
@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    public enum Estado {
        PENDIENTE("Pendiente"),
        EN_PREPARACION("En preparación"),
        ENVIADO("Enviado"),
        ENTREGADO("Entregado"),
        CANCELADO("Cancelado");

        private final String etiqueta;

        Estado(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        public String getEtiqueta() {
            return etiqueta;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{msg.pedido.fecha.notNull}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "{msg.pedido.precioTotal.notNull}")
    @DecimalMin(value = "0.00", message = "{msg.pedido.precioTotal.min}")
    @Digits(integer = 8, fraction = 2, message = "{msg.pedido.precioTotal.digits}")
    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @NotNull(message = "{msg.pedido.estado.notNull}")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private Estado estado;

    @NotEmpty(message = "{msg.pedido.direccionEnvio.notEmpty}")
    @Size(max = 200, message = "{msg.pedido.direccionEnvio.size}")
    @Column(name = "direccion_envio", nullable = false, length = 200)
    private String direccionEnvio;

    @Size(max = 50, message = "{msg.pedido.numeroSeguimiento.size}")
    @Column(name = "numero_seguimiento", length = 50)
    private String numeroSeguimiento;

    @NotNull(message = "{msg.pedido.cliente.notNull}")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Relación N:M con Videojuego. Pedido es el lado propietario (tabla intermedia pedido_videojuego).
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "pedido_videojuego",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "videojuego_id")
    )
    private List<Videojuego> videojuegos = new ArrayList<>();

    // Relación 1:1 con Factura (lado inverso: la clave foránea está en facturas.pedido_id).
    // Sin cascade: un pedido facturado no se puede borrar hasta eliminar su factura.
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "pedido", fetch = FetchType.LAZY)
    private Factura factura;

    public Pedido(LocalDate fecha, BigDecimal precioTotal, Estado estado, String direccionEnvio,
                  String numeroSeguimiento, Cliente cliente) {
        this.fecha = fecha;
        this.precioTotal = precioTotal;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
        this.numeroSeguimiento = numeroSeguimiento;
        this.cliente = cliente;
    }

    /**
     * Indica si el pedido incluye el videojuego con el id dado. Se usa en el formulario
     * para marcar como seleccionadas las opciones del desplegable múltiple.
     * @param videojuegoId Id del videojuego.
     * @return {@code true} si el pedido lo contiene.
     */
    public boolean contieneVideojuego(Long videojuegoId) {
        if (videojuegoId == null || videojuegos == null) {
            return false;
        }
        return videojuegos.stream().anyMatch(v -> videojuegoId.equals(v.getId()));
    }
}
