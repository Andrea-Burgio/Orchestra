package tech.catenate.orchestra.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.InsegnanteCorso;

/**
 * Spring Data JPA repository for the InsegnanteCorso entity.
 */
@Repository
public interface InsegnanteCorsoRepository extends JpaRepository<InsegnanteCorso, Long> {
    default Optional<InsegnanteCorso> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InsegnanteCorso> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InsegnanteCorso> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select insegnanteCorso from InsegnanteCorso insegnanteCorso left join fetch insegnanteCorso.insegnante",
        countQuery = "select count(insegnanteCorso) from InsegnanteCorso insegnanteCorso"
    )
    Page<InsegnanteCorso> findAllWithToOneRelationships(Pageable pageable);

    @Query("select insegnanteCorso from InsegnanteCorso insegnanteCorso left join fetch insegnanteCorso.insegnante")
    List<InsegnanteCorso> findAllWithToOneRelationships();

    @Query(
        "select insegnanteCorso from InsegnanteCorso insegnanteCorso left join fetch insegnanteCorso.insegnante where insegnanteCorso.id =:id"
    )
    Optional<InsegnanteCorso> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select i from InsegnanteCorso i left join fetch i.insegnante")
    List<InsegnanteCorso> findAll();
}
