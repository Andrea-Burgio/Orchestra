package tech.catenate.orchestra.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.ClienteCorso;

/**
 * Spring Data JPA repository for the ClienteCorso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteCorsoRepository extends JpaRepository<ClienteCorso, Long> {}
