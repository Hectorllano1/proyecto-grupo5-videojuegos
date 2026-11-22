package proyecto5.tiendavideojugos.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una categoría (género) de videojuegos.
 * <p>
 * Relación: una categoría tiene muchos videojuegos (OneToMany); el lado propietario
 * de la relación es {@link Videojuego#getCategoria()}.
 * </p>
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    /** Identificador autogenerado. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la categoría. Obligatorio y único. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar los 60 caracteres")
    @Column(nullable = false, unique = true, length = 60)
    private String nombre;

    /** Descripción de la categoría. Obligatoria. */
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")
    @Column(nullable = false, length = 300)
    private String descripcion;

    /** Indica si la categoría está activa. */
    @NotNull
    @Column(nullable = false)
    private Boolean activa = true;

    /** Videojuegos que pertenecen a esta categoría (lado inverso de la relación). */
    @OneToMany(mappedBy = "categoria")
    private List<Videojuego> videojuegos = new ArrayList<>();

    /** Constructor vacío requerido por JPA. */
    public Categoria() {
    }

    /** @return el identificador */
    public Long getId() { return id; }

    /** @param id el identificador */
    public void setId(Long id) { this.id = id; }

    /** @return el nombre */
    public String getNombre() { return nombre; }

    /** @param nombre el nombre */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return la descripción */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion la descripción */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return {@code true} si la categoría está activa */
    public Boolean getActiva() { return activa; }

    /** @param activa si la categoría está activa */
    public void setActiva(Boolean activa) { this.activa = activa; }

    /** @return la lista de videojuegos de la categoría */
    public List<Videojuego> getVideojuegos() { return videojuegos; }

    /** @param videojuegos la lista de videojuegos de la categoría */
    public void setVideojuegos(List<Videojuego> videojuegos) { this.videojuegos = videojuegos; }
}
