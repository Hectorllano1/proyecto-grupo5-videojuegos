package proyecto5.tiendavideojugos.repositories;

import es.gamestore.model.FichaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA de {@link FichaTecnica}.
 */
@Repository
public interface FichaTecnicaRepository extends JpaRepository<FichaTecnica, Long> {

    /**
     * Devuelve las fichas técnicas que aún no están asignadas a ningún videojuego,
     * más la que ya tenga asignada el videojuego indicado (para poder editarlo).
     *
     * @param videojuegoId id del videojuego que se está editando ({@code -1} si es nuevo)
     * @return lista de fichas seleccionables
     */
    @Query("""
           select f from FichaTecnica f
           where f.id not in (
               select v.fichaTecnica.id from Videojuego v
               where v.fichaTecnica is not null and v.id <> :videojuegoId)
           order by f.id
           """)
    List<FichaTecnica> findDisponibles(@Param("videojuegoId") Long videojuegoId);
}
