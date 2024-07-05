package tech.catenate.orchestra.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.Foto;

/**
 * Spring Data JPA repository for the Foto entity.
 */
@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    default Optional<Foto> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Foto> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Foto> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select foto from Foto foto left join fetch foto.concerto", countQuery = "select count(foto) from Foto foto")
    Page<Foto> findAllWithToOneRelationships(Pageable pageable);

    @Query("select foto from Foto foto left join fetch foto.concerto")
    List<Foto> findAllWithToOneRelationships();

    @Query("select foto from Foto foto left join fetch foto.concerto where foto.id =:id")
    Optional<Foto> findOneWithToOneRelationships(@Param("id") Long id);
}
