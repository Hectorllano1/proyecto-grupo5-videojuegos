package es.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad principal del catálogo: un videojuego a la venta.
 * <p>
 * Relaciones:
 * </p>
 * <ul>
 *   <li>{@link Categoria}: ManyToOne (muchos videojuegos pertenecen a una categoría).</li>
 *   <li>{@link FichaTecnica}: OneToOne (un videojuego tiene como máximo una ficha técnica).</li>
 * </ul>
 * <p>
 * Nota para el resto del equipo: las columnas {@code plataforma_id} y {@code desarrolladora_id}
 * ya existen en la tabla (ver schema.sql). Darío debe añadir aquí sus dos relaciones ManyToOne
 * y Julio la relación ManyToMany con Pedido.
 * </p>
 */
@Entity
@Table(name = "videojuego")
public class Videojuego {

    /** Identificador autogenerado. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Título del juego. Obligatorio y único. */
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 120, message = "El título no puede superar los 120 caracteres")
    @Column(nullable = false, unique = true, length = 120)
    private String titulo;

    /** Descripción del juego. Obligatoria. */
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String descripcion;

    /** Precio en euros. */
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precio;

    /** Unidades disponibles en stock. */
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    /** Fecha de lanzamiento. */
    @NotNull(message = "La fecha de lanzamiento es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDate fechaLanzamiento;

    /** Nombre del fichero de la carátula guardado en el servidor (no es obligatorio). */
    @Column(length = 255)
    private String imagen;

    /** Indica si el juego tiene modo multijugador. */
    @NotNull
    @Column(name = "es_multijugador", nullable = false)
    private Boolean esMultijugador = false;

    /** Categoría a la que pertenece (ManyToOne, obligatoria). */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /** Ficha técnica del juego (OneToOne, opcional). Este lado es el propietario de la relación. */
    @OneToOne
    @JoinColumn(name = "ficha_tecnica_id", unique = true)
    private FichaTecnica fichaTecnica;

    /** Constructor vacío requerido por JPA. */
    public Videojuego() {
    }

    /** @return el identificador */
    public Long getId() { return id; }

    /** @param id el identificador */
    public void setId(Long id) { this.id = id; }

    /** @return el título */
    public String getTitulo() { return titulo; }

    /** @param titulo el título */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /** @return la descripción */
    public String getDescripcion() { return descripcion; }

    /** @param descripcion la descripción */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return el precio */
    public BigDecimal getPrecio() { return precio; }

    /** @param precio el precio */
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    /** @return el stock */
    public Integer getStock() { return stock; }

    /** @param stock el stock */
    public void setStock(Integer stock) { this.stock = stock; }

    /** @return la fecha de lanzamiento */
    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }

    /** @param fechaLanzamiento la fecha de lanzamiento */
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    /** @return el nombre del fichero de imagen, o {@code null} si no tiene */
    public String getImagen() { return imagen; }

    /** @param imagen el nombre del fichero de imagen */
    public void setImagen(String imagen) { this.imagen = imagen; }

    /** @return si es multijugador */
    public Boolean getEsMultijugador() { return esMultijugador; }

    /** @param esMultijugador si es multijugador */
    public void setEsMultijugador(Boolean esMultijugador) { this.esMultijugador = esMultijugador; }

    /** @return la categoría */
    public Categoria getCategoria() { return categoria; }

    /** @param categoria la categoría */
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    /** @return la ficha técnica, o {@code null} si no tiene */
    public FichaTecnica getFichaTecnica() { return fichaTecnica; }

    /** @param fichaTecnica la ficha técnica */
    public void setFichaTecnica(FichaTecnica fichaTecnica) { this.fichaTecnica = fichaTecnica; }
}
