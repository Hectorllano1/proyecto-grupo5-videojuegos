package proyecto5.tiendavideojugos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * La clase `Plataforma` representa una entidad que modela una plataforma dentro de la base de datos.
 * Contiene seis campos: `id`, `nombre`, `fabricante`, `generacion`, `fechaLanzamiento` y `esPortatil`.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "plataforma") // Define el nombre de la tabla asociada a esta entidad.
@Data // Aquí mantenemos Lombok.
@NoArgsConstructor
@AllArgsConstructor
public class Plataforma {

    // Campo que almacena el identificador único de la plataforma.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el nombre completo de la plataforma.
    @NotEmpty(message = "La plataforma debe tener nombre")
    @Size(max = 100, message = "El nombre no debe ser superior 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Campo que almacena el nombre del fabricante de la plataforma.
    @NotEmpty(message = "La plataforma debe tener un fabricante")
    @Size(max = 100, message = "El fabricante de la plataforma no puede superar los 100 caracteres")
    @Column(name = "fabricante", nullable = false, length = 100)
    private String fabricante;

    // Campo que almacena la generación de la plataforma.
    @NotEmpty(message = "La plataforma debe tener una generación")
    @Size(max = 100, message = "La generación de la plataforma no puede superar los 100 caracteres")
    @Column(name = "generacion", nullable = false, length = 100)
    private String generacion;

    // Campo que almacena la fecha de lanzamiento de la plataforma.
    @NotNull(message = "La plataforma debe tener una fecha de lanzamiento")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDate fechaLanzamiento;

    // Campo que indica si la plataforma es portátil o no.
    @Column(name = "es_portatil", nullable = false)
    private boolean esPortatil;

    /**
     * Constructor que permite crear una instancia de `Plataforma` con los campos `nombre`, `fabricante`, `generacion`,
     * `fechaLanzamiento` y `esPortatil`.
     *
     * @param nombre           El nombre de la plataforma.
     * @param fabricante       El fabricante de la plataforma.
     * @param generacion       La generación de la plataforma.
     * @param fechaLanzamiento La fecha de lanzamiento de la plataforma.
     * @param esPortatil       Indica si la plataforma es portátil o no.
     */
    public Plataforma(String nombre, String fabricante, String generacion, LocalDate fechaLanzamiento, boolean esPortatil) {
        this.nombre = nombre;
        this.fabricante = fabricante;
        this.generacion = generacion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.esPortatil = esPortatil;

    }
}
