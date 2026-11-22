package es.gamestore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidad auxiliar con los requisitos técnicos de un videojuego.
 * <p>
 * Relación OneToOne con {@link Videojuego}: cada ficha pertenece como máximo a un videojuego.
 * El lado propietario (clave foránea) es {@link Videojuego#getFichaTecnica()}.
 * </p>
 */
@Entity
@Table(name = "ficha_tecnica")
public class FichaTecnica {

    /** Identificador autogenerado. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Sistema operativo requerido. */
    @NotBlank(message = "El sistema operativo es obligatorio")
    @Size(max = 80, message = "Máximo 80 caracteres")
    @Column(name = "sistema_operativo", nullable = false, length = 80)
    private String sistemaOperativo;

    /** Procesador mínimo recomendado. */
    @NotBlank(message = "El procesador es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String procesador;

    /** Memoria RAM mínima en GB. */
    @NotNull(message = "La memoria RAM es obligatoria")
    @Min(value = 1, message = "La RAM debe ser al menos 1 GB")
    @Column(name = "memoria_ram_gb", nullable = false)
    private Integer memoriaRamGb;

    /** Espacio en disco necesario en GB. */
    @NotNull(message = "El espacio en disco es obligatorio")
    @Min(value = 1, message = "El espacio debe ser al menos 1 GB")
    @Column(name = "espacio_disco_gb", nullable = false)
    private Integer espacioDiscoGb;

    /** Indica si el juego necesita conexión a internet. */
    @NotNull
    @Column(name = "requiere_internet", nullable = false)
    private Boolean requiereInternet = false;

    /** Videojuego al que pertenece la ficha (lado inverso de la relación OneToOne). */
    @OneToOne(mappedBy = "fichaTecnica")
    private Videojuego videojuego;

    /** Constructor vacío requerido por JPA. */
    public FichaTecnica() {
    }

    /**
     * Texto legible de la ficha, usado en listados y desplegables.
     *
     * @return resumen con sistema operativo, procesador y RAM
     */
    public String getResumen() {
        return sistemaOperativo + " · " + procesador + " · " + memoriaRamGb + " GB RAM";
    }

    /** @return el identificador */
    public Long getId() { return id; }

    /** @param id el identificador */
    public void setId(Long id) { this.id = id; }

    /** @return el sistema operativo */
    public String getSistemaOperativo() { return sistemaOperativo; }

    /** @param sistemaOperativo el sistema operativo */
    public void setSistemaOperativo(String sistemaOperativo) { this.sistemaOperativo = sistemaOperativo; }

    /** @return el procesador */
    public String getProcesador() { return procesador; }

    /** @param procesador el procesador */
    public void setProcesador(String procesador) { this.procesador = procesador; }

    /** @return la RAM en GB */
    public Integer getMemoriaRamGb() { return memoriaRamGb; }

    /** @param memoriaRamGb la RAM en GB */
    public void setMemoriaRamGb(Integer memoriaRamGb) { this.memoriaRamGb = memoriaRamGb; }

    /** @return el espacio en disco en GB */
    public Integer getEspacioDiscoGb() { return espacioDiscoGb; }

    /** @param espacioDiscoGb el espacio en disco en GB */
    public void setEspacioDiscoGb(Integer espacioDiscoGb) { this.espacioDiscoGb = espacioDiscoGb; }

    /** @return si requiere conexión a internet */
    public Boolean getRequiereInternet() { return requiereInternet; }

    /** @param requiereInternet si requiere conexión a internet */
    public void setRequiereInternet(Boolean requiereInternet) { this.requiereInternet = requiereInternet; }

    /** @return el videojuego asociado, o {@code null} si está libre */
    public Videojuego getVideojuego() { return videojuego; }

    /** @param videojuego el videojuego asociado */
    public void setVideojuego(Videojuego videojuego) { this.videojuego = videojuego; }
}
