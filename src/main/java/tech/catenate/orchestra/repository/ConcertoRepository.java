package tech.catenate.orchestra.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.Concerto;

/**
 * Spring Data JPA repository for the Concerto entity.
 */
@Repository
public interface ConcertoRepository extends JpaRepository<Concerto, Long> {
    default Optional<Concerto> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Concerto> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Concerto> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select concerto from Concerto concerto left join fetch concerto.corso",
        countQuery = "select count(concerto) from Concerto concerto"
    )
    Page<Concerto> findAllWithToOneRelationships(Pageable pageable);

    @Query("select concerto from Concerto concerto left join fetch concerto.corso")
    List<Concerto> findAllWithToOneRelationships();

    @Query("select concerto from Concerto concerto left join fetch concerto.corso where concerto.id =:id")
    Optional<Concerto> findOneWithToOneRelationships(@Param("id") Long id);
}
