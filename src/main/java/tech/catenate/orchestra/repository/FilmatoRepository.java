package tech.catenate.orchestra.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.Filmato;

/**
 * Spring Data JPA repository for the Filmato entity.
 */
@Repository
public interface FilmatoRepository extends JpaRepository<Filmato, Long> {
    default Optional<Filmato> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Filmato> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Filmato> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select filmato from Filmato filmato left join fetch filmato.concerto",
        countQuery = "select count(filmato) from Filmato filmato"
    )
    Page<Filmato> findAllWithToOneRelationships(Pageable pageable);

    @Query("select filmato from Filmato filmato left join fetch filmato.concerto")
    List<Filmato> findAllWithToOneRelationships();

    @Query("select filmato from Filmato filmato left join fetch filmato.concerto where filmato.id =:id")
    Optional<Filmato> findOneWithToOneRelationships(@Param("id") Long id);
}
