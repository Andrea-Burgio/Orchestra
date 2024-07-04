package tech.catenate.orchestra.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.InsegnanteCorso;

/**
 * Spring Data JPA repository for the InsegnanteCorso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsegnanteCorsoRepository extends JpaRepository<InsegnanteCorso, Long> {}
