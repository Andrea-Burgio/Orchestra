package tech.catenate.orchestra.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.Corso;

/**
 * Spring Data JPA repository for the Corso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorsoRepository extends JpaRepository<Corso, Long> {}
