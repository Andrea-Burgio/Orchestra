package tech.catenate.orchestra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tech.catenate.orchestra.domain.CorsoAsserts.*;
import static tech.catenate.orchestra.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tech.catenate.orchestra.IntegrationTest;
import tech.catenate.orchestra.domain.Corso;
import tech.catenate.orchestra.repository.CorsoRepository;

/**
 * Integration tests for the {@link CorsoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorsoResourceIT {

    private static final Integer DEFAULT_ANNO = 1;
    private static final Integer UPDATED_ANNO = 2;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/corsos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CorsoRepository corsoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorsoMockMvc;

    private Corso corso;

    private Corso insertedCorso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corso createEntity(EntityManager em) {
        Corso corso = new Corso().anno(DEFAULT_ANNO).nome(DEFAULT_NOME);
        return corso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corso createUpdatedEntity(EntityManager em) {
        Corso corso = new Corso().anno(UPDATED_ANNO).nome(UPDATED_NOME);
        return corso;
    }

    @BeforeEach
    public void initTest() {
        corso = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCorso != null) {
            corsoRepository.delete(insertedCorso);
            insertedCorso = null;
        }
    }

    @Test
    @Transactional
    void createCorso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Corso
        var returnedCorso = om.readValue(
            restCorsoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corso)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Corso.class
        );

        // Validate the Corso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCorsoUpdatableFieldsEquals(returnedCorso, getPersistedCorso(returnedCorso));

        insertedCorso = returnedCorso;
    }

    @Test
    @Transactional
    void createCorsoWithExistingId() throws Exception {
        // Create the Corso with an existing ID
        corso.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corso)))
            .andExpect(status().isBadRequest());

        // Validate the Corso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCorsos() throws Exception {
        // Initialize the database
        insertedCorso = corsoRepository.saveAndFlush(corso);

        // Get all the corsoList
        restCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corso.getId().intValue())))
            .andExpect(jsonPath("$.[*].anno").value(hasItem(DEFAULT_ANNO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getCorso() throws Exception {
        // Initialize the database
        insertedCorso = corsoRepository.saveAndFlush(corso);

        // Get the corso
        restCorsoMockMvc
            .perform(get(ENTITY_API_URL_ID, corso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(corso.getId().intValue()))
            .andExpect(jsonPath("$.anno").value(DEFAULT_ANNO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingCorso() throws Exception {
        // Get the corso
        restCorsoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCorso() throws Exception {
        // Initialize the database
        insertedCorso = corsoRepository.saveAndFlush(corso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corso
        Corso updatedCorso = corsoRepository.findById(corso.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCorso are not directly saved in db
        em.detach(updatedCorso);
        updatedCorso.anno(UPDATED_ANNO).nome(UPDATED_NOME);

        restCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCorso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCorso))
            )
            .andExpect(status().isOk());

        // Validate the Corso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCorsoToMatchAllProperties(updatedCorso);
    }

    @Test
    @Transactional
    void putNonExistingCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorsoMockMvc
            .perform(put(ENTITY_API_URL_ID, corso.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corso)))
            .andExpect(status().isBadRequest());

        // Validate the Corso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(corso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(corso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedCorso = corsoRepository.saveAndFlush(corso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corso using partial update
        Corso partialUpdatedCorso = new Corso();
        partialUpdatedCorso.setId(corso.getId());

        restCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorso))
            )
            .andExpect(status().isOk());

        // Validate the Corso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorsoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCorso, corso), getPersistedCorso(corso));
    }

    @Test
    @Transactional
    void fullUpdateCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedCorso = corsoRepository.saveAndFlush(corso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the corso using partial update
        Corso partialUpdatedCorso = new Corso();
        partialUpdatedCorso.setId(corso.getId());

        partialUpdatedCorso.anno(UPDATED_ANNO).nome(UPDATED_NOME);

        restCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorso))
            )
            .andExpect(status().isOk());

        // Validate the Corso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorsoUpdatableFieldsEquals(partialUpdatedCorso, getPersistedCorso(partialUpdatedCorso));
    }

    @Test
    @Transactional
    void patchNonExistingCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, corso.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(corso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(corso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        corso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorsoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(corso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorso() throws Exception {
        // Initialize the database
        insertedCorso = corsoRepository.saveAndFlush(corso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the corso
        restCorsoMockMvc
            .perform(delete(ENTITY_API_URL_ID, corso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return corsoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Corso getPersistedCorso(Corso corso) {
        return corsoRepository.findById(corso.getId()).orElseThrow();
    }

    protected void assertPersistedCorsoToMatchAllProperties(Corso expectedCorso) {
        assertCorsoAllPropertiesEquals(expectedCorso, getPersistedCorso(expectedCorso));
    }

    protected void assertPersistedCorsoToMatchUpdatableProperties(Corso expectedCorso) {
        assertCorsoAllUpdatablePropertiesEquals(expectedCorso, getPersistedCorso(expectedCorso));
    }
}
