package proyecto5.tiendavideojugos.entities;

import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase `Cliente` representa una entidad que modela un cliente de la tienda de videojuegos.
 * Contiene los datos personales del cliente (nombre, apellidos, DNI, correo, teléfono, dirección),
 * su fecha de registro, si es premium y la lista de pedidos que ha realizado.
 *
 * Un cliente puede tener muchos pedidos (relación OneToMany con `Pedido`).
 * El DNI y el correo son únicos.
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "clientes") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    // Identificador único del cliente. Es autogenerado y clave primaria.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del cliente. No puede estar vacío.
    @NotEmpty(message = "{msg.cliente.nombre.notEmpty}")
    @Size(max = 50, message = "{msg.cliente.nombre.size}")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    // Apellidos del cliente. No pueden estar vacíos.
    @NotEmpty(message = "{msg.cliente.apellidos.notEmpty}")
    @Size(max = 100, message = "{msg.cliente.apellidos.size}")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    // DNI del cliente (8 números y una letra). Debe ser único.
    @NotEmpty(message = "{msg.cliente.dni.notEmpty}")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "{msg.cliente.dni.pattern}")
    @Column(name = "dni", nullable = false, unique = true, length = 9)
    private String dni;

    // Correo electrónico del cliente. Debe ser único.
    @NotEmpty(message = "{msg.cliente.correo.notEmpty}")
    @Email(message = "{msg.cliente.correo.email}")
    @Size(max = 100, message = "{msg.cliente.correo.size}")
    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    // Teléfono de contacto (entre 9 y 15 dígitos, con prefijo internacional opcional).
    @NotEmpty(message = "{msg.cliente.telefono.notEmpty}")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "{msg.cliente.telefono.pattern}")
    @Column(name = "telefono", nullable = false, length = 16)
    private String telefono;

    // Fecha en la que el cliente se registró en la tienda.
    @NotNull(message = "{msg.cliente.fechaRegistro.notNull}")
    @PastOrPresent(message = "{msg.cliente.fechaRegistro.pastOrPresent}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Para que el input type="date" se enlace bien con LocalDate.
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    // Dirección postal del cliente (opcional).
    @Size(max = 200, message = "{msg.cliente.direccion.size}")
    @Column(name = "direccion", length = 200)
    private String direccion;

    // Indica si el cliente es premium.
    @Column(name = "es_premium", nullable = false)
    private boolean esPremium;

    // Relación uno a muchos con la entidad `Pedido`. Un cliente puede realizar muchos pedidos.
    // Se excluye de toString y equals/hashCode para evitar bucles infinitos con Pedido.cliente.
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    /**
     * Constructor que excluye los campos `id` y `pedidos`. Se utiliza para crear instancias de `Cliente`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarlo en la base de datos).
     * @param nombre Nombre del cliente.
     * @param apellidos Apellidos del cliente.
     * @param dni DNI del cliente.
     * @param correo Correo electrónico del cliente.
     * @param telefono Teléfono del cliente.
     * @param fechaRegistro Fecha de registro del cliente.
     * @param direccion Dirección del cliente.
     * @param esPremium Indica si el cliente es premium.
     */
    public Cliente(String nombre, String apellidos, String dni, String correo, String telefono,
                   LocalDate fechaRegistro, String direccion, boolean esPremium) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.direccion = direccion;
        this.esPremium = esPremium;
    }

    /**
     * Devuelve el nombre y los apellidos juntos. Se usa en los listados y en el desplegable
     * de selección de cliente del formulario de pedidos.
     * @return Nombre completo del cliente.
     */
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }
}
