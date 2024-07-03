package tech.catenate.orchestra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tech.catenate.orchestra.domain.InsegnanteAsserts.*;
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
import tech.catenate.orchestra.domain.Insegnante;
import tech.catenate.orchestra.repository.InsegnanteRepository;

/**
 * Integration tests for the {@link InsegnanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InsegnanteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COGNOME = "AAAAAAAAAA";
    private static final String UPDATED_COGNOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/insegnantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsegnanteRepository insegnanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsegnanteMockMvc;

    private Insegnante insegnante;

    private Insegnante insertedInsegnante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insegnante createEntity(EntityManager em) {
        Insegnante insegnante = new Insegnante().nome(DEFAULT_NOME).cognome(DEFAULT_COGNOME);
        return insegnante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Insegnante createUpdatedEntity(EntityManager em) {
        Insegnante insegnante = new Insegnante().nome(UPDATED_NOME).cognome(UPDATED_COGNOME);
        return insegnante;
    }

    @BeforeEach
    public void initTest() {
        insegnante = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedInsegnante != null) {
            insegnanteRepository.delete(insertedInsegnante);
            insertedInsegnante = null;
        }
    }

    @Test
    @Transactional
    void createInsegnante() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Insegnante
        var returnedInsegnante = om.readValue(
            restInsegnanteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insegnante)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Insegnante.class
        );

        // Validate the Insegnante in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsegnanteUpdatableFieldsEquals(returnedInsegnante, getPersistedInsegnante(returnedInsegnante));

        insertedInsegnante = returnedInsegnante;
    }

    @Test
    @Transactional
    void createInsegnanteWithExistingId() throws Exception {
        // Create the Insegnante with an existing ID
        insegnante.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsegnanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insegnante)))
            .andExpect(status().isBadRequest());

        // Validate the Insegnante in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsegnantes() throws Exception {
        // Initialize the database
        insertedInsegnante = insegnanteRepository.saveAndFlush(insegnante);

        // Get all the insegnanteList
        restInsegnanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insegnante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)));
    }

    @Test
    @Transactional
    void getInsegnante() throws Exception {
        // Initialize the database
        insertedInsegnante = insegnanteRepository.saveAndFlush(insegnante);

        // Get the insegnante
        restInsegnanteMockMvc
            .perform(get(ENTITY_API_URL_ID, insegnante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insegnante.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cognome").value(DEFAULT_COGNOME));
    }

    @Test
    @Transactional
    void getNonExistingInsegnante() throws Exception {
        // Get the insegnante
        restInsegnanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsegnante() throws Exception {
        // Initialize the database
        insertedInsegnante = insegnanteRepository.saveAndFlush(insegnante);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insegnante
        Insegnante updatedInsegnante = insegnanteRepository.findById(insegnante.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsegnante are not directly saved in db
        em.detach(updatedInsegnante);
        updatedInsegnante.nome(UPDATED_NOME).cognome(UPDATED_COGNOME);

        restInsegnanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsegnante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsegnante))
            )
            .andExpect(status().isOk());

        // Validate the Insegnante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsegnanteToMatchAllProperties(updatedInsegnante);
    }

    @Test
    @Transactional
    void putNonExistingInsegnante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnante.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsegnanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insegnante.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insegnante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insegnante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsegnante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnante.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsegnanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insegnante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insegnante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsegnante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnante.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsegnanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insegnante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Insegnante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsegnanteWithPatch() throws Exception {
        // Initialize the database
        insertedInsegnante = insegnanteRepository.saveAndFlush(insegnante);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insegnante using partial update
        Insegnante partialUpdatedInsegnante = new Insegnante();
        partialUpdatedInsegnante.setId(insegnante.getId());

        restInsegnanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsegnante.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsegnante))
            )
            .andExpect(status().isOk());

        // Validate the Insegnante in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsegnanteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsegnante, insegnante),
            getPersistedInsegnante(insegnante)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsegnanteWithPatch() throws Exception {
        // Initialize the database
        insertedInsegnante = insegnanteRepository.saveAndFlush(insegnante);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insegnante using partial update
        Insegnante partialUpdatedInsegnante = new Insegnante();
        partialUpdatedInsegnante.setId(insegnante.getId());

        partialUpdatedInsegnante.nome(UPDATED_NOME).cognome(UPDATED_COGNOME);

        restInsegnanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsegnante.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsegnante))
            )
            .andExpect(status().isOk());

        // Validate the Insegnante in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsegnanteUpdatableFieldsEquals(partialUpdatedInsegnante, getPersistedInsegnante(partialUpdatedInsegnante));
    }

    @Test
    @Transactional
    void patchNonExistingInsegnante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnante.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsegnanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insegnante.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insegnante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insegnante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsegnante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnante.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsegnanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insegnante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Insegnante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsegnante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnante.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsegnanteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(insegnante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Insegnante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsegnante() throws Exception {
        // Initialize the database
        insertedInsegnante = insegnanteRepository.saveAndFlush(insegnante);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insegnante
        restInsegnanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, insegnante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insegnanteRepository.count();
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

    protected Insegnante getPersistedInsegnante(Insegnante insegnante) {
        return insegnanteRepository.findById(insegnante.getId()).orElseThrow();
    }

    protected void assertPersistedInsegnanteToMatchAllProperties(Insegnante expectedInsegnante) {
        assertInsegnanteAllPropertiesEquals(expectedInsegnante, getPersistedInsegnante(expectedInsegnante));
    }

    protected void assertPersistedInsegnanteToMatchUpdatableProperties(Insegnante expectedInsegnante) {
        assertInsegnanteAllUpdatablePropertiesEquals(expectedInsegnante, getPersistedInsegnante(expectedInsegnante));
    }
}
