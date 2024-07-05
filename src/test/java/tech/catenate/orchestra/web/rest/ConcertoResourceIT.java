package tech.catenate.orchestra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tech.catenate.orchestra.domain.ConcertoAsserts.*;
import static tech.catenate.orchestra.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import tech.catenate.orchestra.domain.Concerto;
import tech.catenate.orchestra.repository.ConcertoRepository;

/**
 * Integration tests for the {@link ConcertoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConcertoResourceIT {

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/concertos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConcertoRepository concertoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConcertoMockMvc;

    private Concerto concerto;

    private Concerto insertedConcerto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Concerto createEntity(EntityManager em) {
        Concerto concerto = new Concerto().data(DEFAULT_DATA).nome(DEFAULT_NOME);
        return concerto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Concerto createUpdatedEntity(EntityManager em) {
        Concerto concerto = new Concerto().data(UPDATED_DATA).nome(UPDATED_NOME);
        return concerto;
    }

    @BeforeEach
    public void initTest() {
        concerto = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedConcerto != null) {
            concertoRepository.delete(insertedConcerto);
            insertedConcerto = null;
        }
    }

    @Test
    @Transactional
    void createConcerto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Concerto
        var returnedConcerto = om.readValue(
            restConcertoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concerto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Concerto.class
        );

        // Validate the Concerto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertConcertoUpdatableFieldsEquals(returnedConcerto, getPersistedConcerto(returnedConcerto));

        insertedConcerto = returnedConcerto;
    }

    @Test
    @Transactional
    void createConcertoWithExistingId() throws Exception {
        // Create the Concerto with an existing ID
        concerto.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConcertoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concerto)))
            .andExpect(status().isBadRequest());

        // Validate the Concerto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConcertos() throws Exception {
        // Initialize the database
        insertedConcerto = concertoRepository.saveAndFlush(concerto);

        // Get all the concertoList
        restConcertoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(concerto.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getConcerto() throws Exception {
        // Initialize the database
        insertedConcerto = concertoRepository.saveAndFlush(concerto);

        // Get the concerto
        restConcertoMockMvc
            .perform(get(ENTITY_API_URL_ID, concerto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(concerto.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingConcerto() throws Exception {
        // Get the concerto
        restConcertoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConcerto() throws Exception {
        // Initialize the database
        insertedConcerto = concertoRepository.saveAndFlush(concerto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the concerto
        Concerto updatedConcerto = concertoRepository.findById(concerto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConcerto are not directly saved in db
        em.detach(updatedConcerto);
        updatedConcerto.data(UPDATED_DATA).nome(UPDATED_NOME);

        restConcertoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConcerto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedConcerto))
            )
            .andExpect(status().isOk());

        // Validate the Concerto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConcertoToMatchAllProperties(updatedConcerto);
    }

    @Test
    @Transactional
    void putNonExistingConcerto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concerto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConcertoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, concerto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concerto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concerto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConcerto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concerto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcertoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(concerto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concerto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConcerto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concerto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcertoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(concerto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Concerto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConcertoWithPatch() throws Exception {
        // Initialize the database
        insertedConcerto = concertoRepository.saveAndFlush(concerto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the concerto using partial update
        Concerto partialUpdatedConcerto = new Concerto();
        partialUpdatedConcerto.setId(concerto.getId());

        restConcertoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcerto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConcerto))
            )
            .andExpect(status().isOk());

        // Validate the Concerto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConcertoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedConcerto, concerto), getPersistedConcerto(concerto));
    }

    @Test
    @Transactional
    void fullUpdateConcertoWithPatch() throws Exception {
        // Initialize the database
        insertedConcerto = concertoRepository.saveAndFlush(concerto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the concerto using partial update
        Concerto partialUpdatedConcerto = new Concerto();
        partialUpdatedConcerto.setId(concerto.getId());

        partialUpdatedConcerto.data(UPDATED_DATA).nome(UPDATED_NOME);

        restConcertoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcerto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConcerto))
            )
            .andExpect(status().isOk());

        // Validate the Concerto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConcertoUpdatableFieldsEquals(partialUpdatedConcerto, getPersistedConcerto(partialUpdatedConcerto));
    }

    @Test
    @Transactional
    void patchNonExistingConcerto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concerto.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConcertoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, concerto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(concerto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concerto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConcerto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concerto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcertoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(concerto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concerto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConcerto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        concerto.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcertoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(concerto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Concerto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConcerto() throws Exception {
        // Initialize the database
        insertedConcerto = concertoRepository.saveAndFlush(concerto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the concerto
        restConcertoMockMvc
            .perform(delete(ENTITY_API_URL_ID, concerto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return concertoRepository.count();
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

    protected Concerto getPersistedConcerto(Concerto concerto) {
        return concertoRepository.findById(concerto.getId()).orElseThrow();
    }

    protected void assertPersistedConcertoToMatchAllProperties(Concerto expectedConcerto) {
        assertConcertoAllPropertiesEquals(expectedConcerto, getPersistedConcerto(expectedConcerto));
    }

    protected void assertPersistedConcertoToMatchUpdatableProperties(Concerto expectedConcerto) {
        assertConcertoAllUpdatablePropertiesEquals(expectedConcerto, getPersistedConcerto(expectedConcerto));
    }
}
