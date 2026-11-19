package proyecto5.tiendavideojugos.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase `Desarrolladora` representa una entidad que modela una desarrolladora dentro de la base de datos.
 * Contiene cinco campos: `id`, `nombre`, `paisOrigen`, `fechaFundacion` y `paginaWeb`.
 */

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "desarrolladora") // Define el nombre de la tabla asociada a esta entidad.
@Data // Aquí mantenemos Lombok.
@NoArgsConstructor
@AllArgsConstructor
public class Desarrolladora {

    // Campo que almacena el identificador único de la desarrolladora.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el nombre completo de la desarrolladora.
    @NotEmpty(message = "La desarrolladora debe tener nombre")
    @Size(max = 100, message = "El nombre no debe ser superior 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Campo que almacena el país de origen de la desarrolladora.
    @NotEmpty(message = "La desarrolladora debe tener un país de origen")
    @Size(max = 100, message = "El país de origen de la desarrolladora no puede superar los 100 caracteres")
    @Column(name = "pais_origen", nullable = false, length = 100)
    private String paisOrigen;

    // Campo que almacena la fecha de fundación de la desarrolladora.
    @Column(name = "fecha_fundacion", nullable = true)
    private java.time.LocalDate fechaFundacion;


    // Campo que almacena la pagina web oficial de la desarrolladora.
    @Size(max = 200, message = "La página web de la desarrolladora no puede superar los 200 caracteres")
    @Column(name = "pagina_web", nullable = true, length = 200)
    private String paginaWeb;

    /**
     * Constructor que permite crear una instancia de `Desarrolladora` con los campos `nombre`, `paisOrigen`,
     * `fechaFundacion` y `paginaWeb`.
     *
     * @param nombre         El nombre de la desarrolladora.
     * @param paisOrigen     El país de origen de la desarrolladora.
     * @param fechaFundacion La fecha de fundación de la desarrolladora.
     * @param paginaWeb      La página web oficial de la desarrolladora.
     */
    public Desarrolladora(String nombre, String paisOrigen, java.time.LocalDate fechaFundacion, String paginaWeb) {
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
        this.fechaFundacion = fechaFundacion;
        this.paginaWeb = paginaWeb;
    }
}
