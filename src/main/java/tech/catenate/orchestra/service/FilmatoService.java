package tech.catenate.orchestra.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.catenate.orchestra.domain.Filmato;
import tech.catenate.orchestra.repository.FilmatoRepository;

/**
 * Service Implementation for managing {@link tech.catenate.orchestra.domain.Filmato}.
 */
@Service
@Transactional
public class FilmatoService {

    private final Logger log = LoggerFactory.getLogger(FilmatoService.class);

    private final FilmatoRepository filmatoRepository;

    public FilmatoService(FilmatoRepository filmatoRepository) {
        this.filmatoRepository = filmatoRepository;
    }

    /**
     * Save a filmato.
     *
     * @param filmato the entity to save.
     * @return the persisted entity.
     */
    public Filmato save(Filmato filmato) {
        log.debug("Request to save Filmato : {}", filmato);
        return filmatoRepository.save(filmato);
    }

    /**
     * Update a filmato.
     *
     * @param filmato the entity to save.
     * @return the persisted entity.
     */
    public Filmato update(Filmato filmato) {
        log.debug("Request to update Filmato : {}", filmato);
        return filmatoRepository.save(filmato);
    }

    /**
     * Partially update a filmato.
     *
     * @param filmato the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Filmato> partialUpdate(Filmato filmato) {
        log.debug("Request to partially update Filmato : {}", filmato);

        return filmatoRepository
            .findById(filmato.getId())
            .map(existingFilmato -> {
                if (filmato.getBlob() != null) {
                    existingFilmato.setBlob(filmato.getBlob());
                }
                if (filmato.getBlobContentType() != null) {
                    existingFilmato.setBlobContentType(filmato.getBlobContentType());
                }
                if (filmato.getNome_file() != null) {
                    existingFilmato.setNome_file(filmato.getNome_file());
                }

                return existingFilmato;
            })
            .map(filmatoRepository::save);
    }

    /**
     * Get all the filmatoes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Filmato> findAll() {
        log.debug("Request to get all Filmatoes");
        return filmatoRepository.findAll();
    }

    /**
     * Get all the filmatoes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Filmato> findAllWithEagerRelationships(Pageable pageable) {
        return filmatoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one filmato by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Filmato> findOne(Long id) {
        log.debug("Request to get Filmato : {}", id);
        return filmatoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the filmato by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Filmato : {}", id);
        filmatoRepository.deleteById(id);
    }

    /**
     * Get all the Filmatoes along with their associated Concerto entities.
     *
     * @return the list of Filmatoes entities with their associated Concerto entities.
     */
    @Transactional(readOnly = true)
    public List<Filmato> findAllWithToOneRelationships() {
        log.debug("Request to get all Concertos");
        return filmatoRepository.findAllWithToOneRelationships();
    }
}
