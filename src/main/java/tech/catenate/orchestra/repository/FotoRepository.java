package tech.catenate.orchestra.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.catenate.orchestra.domain.Foto;

/**
 * Spring Data JPA repository for the Foto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {}
