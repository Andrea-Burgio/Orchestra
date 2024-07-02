package tech.catenate.orchestra.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.Insegnante;

/**
 * Spring Data JPA repository for the Insegnante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsegnanteRepository extends JpaRepository<Insegnante, Long> {}
