package es.gamestore.repository;

import es.gamestore.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA de {@link Categoria}.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca una categoría por nombre ignorando mayúsculas/minúsculas.
     * Se usa para comprobar que el nombre no esté repetido.
     *
     * @param nombre nombre a buscar
     * @return la categoría, si existe
     */
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
