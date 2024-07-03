package tech.catenate.orchestra.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.Concerto;

/**
 * Spring Data JPA repository for the Concerto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConcertoRepository extends JpaRepository<Concerto, Long> {}
