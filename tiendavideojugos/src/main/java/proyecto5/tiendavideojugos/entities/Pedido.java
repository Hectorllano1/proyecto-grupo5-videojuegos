package proyecto5.tiendavideojugos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * La clase `Pedido` representa una entidad que modela un pedido realizado por un cliente.
 * Contiene la fecha, el precio total, el estado, la dirección de envío, el número de seguimiento
 * y el cliente que lo realiza.
 *
 * Muchos pedidos pertenecen a un único cliente (relación ManyToOne con `Cliente`).
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "pedidos") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    /**
     * Estados posibles de un pedido durante su ciclo de vida.
     */
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

        /**
         * @return Texto legible del estado para mostrarlo en las vistas.
         */
        public String getEtiqueta() {
            return etiqueta;
        }
    }

    // Identificador único del pedido. Es autogenerado y clave primaria.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha en la que se realizó el pedido.
    @NotNull(message = "{msg.pedido.fecha.notNull}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Para que el input type="date" se enlace bien con LocalDate.
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // Precio total del pedido. No puede ser nulo ni negativo.
    @NotNull(message = "{msg.pedido.precioTotal.notNull}")
    @DecimalMin(value = "0.00", message = "{msg.pedido.precioTotal.min}")
    @Digits(integer = 8, fraction = 2, message = "{msg.pedido.precioTotal.digits}")
    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    // Estado actual del pedido. Se guarda como texto (por ejemplo "ENVIADO").
    @NotNull(message = "{msg.pedido.estado.notNull}")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private Estado estado;

    // Dirección a la que se enviará el pedido. No puede estar vacía.
    @NotEmpty(message = "{msg.pedido.direccionEnvio.notEmpty}")
    @Size(max = 200, message = "{msg.pedido.direccionEnvio.size}")
    @Column(name = "direccion_envio", nullable = false, length = 200)
    private String direccionEnvio;

    // Número de seguimiento del envío (opcional hasta que el pedido se envía).
    @Size(max = 50, message = "{msg.pedido.numeroSeguimiento.size}")
    @Column(name = "numero_seguimiento", length = 50)
    private String numeroSeguimiento;

    // Relación con el cliente que realiza el pedido. No puede ser nulo.
    @NotNull(message = "{msg.pedido.cliente.notNull}")
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchos pedidos a un cliente.
    @JoinColumn(name = "cliente_id", nullable = false) // Clave foránea en la tabla pedidos que referencia a clientes.
    private Cliente cliente;

    // Relación N:M "Contiene" con Videojuego. Se añadirá cuando exista la entidad Videojuego:
    //
    // @ManyToMany
    // @JoinTable(name = "pedido_videojuego",
    //         joinColumns = @JoinColumn(name = "pedido_id"),
    //         inverseJoinColumns = @JoinColumn(name = "videojuego_id"))
    // private List<Videojuego> videojuegos = new ArrayList<>();

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Pedido`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarlo en la base de datos).
     * @param fecha Fecha del pedido.
     * @param precioTotal Precio total del pedido.
     * @param estado Estado del pedido.
     * @param direccionEnvio Dirección de envío.
     * @param numeroSeguimiento Número de seguimiento del envío.
     * @param cliente Cliente que realiza el pedido.
     */
    public Pedido(LocalDate fecha, BigDecimal precioTotal, Estado estado, String direccionEnvio,
                  String numeroSeguimiento, Cliente cliente) {
        this.fecha = fecha;
        this.precioTotal = precioTotal;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
        this.numeroSeguimiento = numeroSeguimiento;
        this.cliente = cliente;
    }
}
