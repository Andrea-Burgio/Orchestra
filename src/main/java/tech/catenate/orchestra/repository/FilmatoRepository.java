package tech.catenate.orchestra.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.Filmato;

/**
 * Spring Data JPA repository for the Filmato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilmatoRepository extends JpaRepository<Filmato, Long> {}
