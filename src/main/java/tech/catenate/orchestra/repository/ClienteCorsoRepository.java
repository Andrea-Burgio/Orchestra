package tech.catenate.orchestra.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.ClienteCorso;

/**
 * Spring Data JPA repository for the ClienteCorso entity.
 */
@Repository
public interface ClienteCorsoRepository extends JpaRepository<ClienteCorso, Long> {
    default Optional<ClienteCorso> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ClienteCorso> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ClienteCorso> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select clienteCorso from ClienteCorso clienteCorso left join fetch clienteCorso.cliente left join fetch clienteCorso.corso",
        countQuery = "select count(clienteCorso) from ClienteCorso clienteCorso"
    )
    Page<ClienteCorso> findAllWithToOneRelationships(Pageable pageable);

    @Query("select clienteCorso from ClienteCorso clienteCorso left join fetch clienteCorso.cliente left join fetch clienteCorso.corso")
    List<ClienteCorso> findAllWithToOneRelationships();

    @Query(
        "select clienteCorso from ClienteCorso clienteCorso left join fetch clienteCorso.cliente left join fetch clienteCorso.corso where clienteCorso.id =:id"
    )
    Optional<ClienteCorso> findOneWithToOneRelationships(@Param("id") Long id);
}
