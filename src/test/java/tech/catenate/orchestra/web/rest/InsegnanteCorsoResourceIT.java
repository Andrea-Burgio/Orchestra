package tech.catenate.orchestra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tech.catenate.orchestra.domain.InsegnanteCorsoAsserts.*;
import static tech.catenate.orchestra.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tech.catenate.orchestra.IntegrationTest;
import tech.catenate.orchestra.domain.InsegnanteCorso;
import tech.catenate.orchestra.repository.InsegnanteCorsoRepository;
import tech.catenate.orchestra.service.InsegnanteCorsoService;

/**
 * Integration tests for the {@link InsegnanteCorsoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InsegnanteCorsoResourceIT {

    private static final Integer DEFAULT_MESE = 1;
    private static final Integer UPDATED_MESE = 2;

    private static final String ENTITY_API_URL = "/api/insegnante-corsos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsegnanteCorsoRepository insegnanteCorsoRepository;

    @Mock
    private InsegnanteCorsoRepository insegnanteCorsoRepositoryMock;

    @Mock
    private InsegnanteCorsoService insegnanteCorsoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsegnanteCorsoMockMvc;

    private InsegnanteCorso insegnanteCorso;

    private InsegnanteCorso insertedInsegnanteCorso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsegnanteCorso createEntity(EntityManager em) {
        InsegnanteCorso insegnanteCorso = new InsegnanteCorso().mese(DEFAULT_MESE);
        return insegnanteCorso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsegnanteCorso createUpdatedEntity(EntityManager em) {
        InsegnanteCorso insegnanteCorso = new InsegnanteCorso().mese(UPDATED_MESE);
        return insegnanteCorso;
    }

    @BeforeEach
    public void initTest() {
        insegnanteCorso = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedInsegnanteCorso != null) {
            insegnanteCorsoRepository.delete(insertedInsegnanteCorso);
            insertedInsegnanteCorso = null;
        }
    }

    @Test
    @Transactional
    void createInsegnanteCorso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InsegnanteCorso
        var returnedInsegnanteCorso = om.readValue(
            restInsegnanteCorsoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insegnanteCorso)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsegnanteCorso.class
        );

        // Validate the InsegnanteCorso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInsegnanteCorsoUpdatableFieldsEquals(returnedInsegnanteCorso, getPersistedInsegnanteCorso(returnedInsegnanteCorso));

        insertedInsegnanteCorso = returnedInsegnanteCorso;
    }

    @Test
    @Transactional
    void createInsegnanteCorsoWithExistingId() throws Exception {
        // Create the InsegnanteCorso with an existing ID
        insegnanteCorso.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsegnanteCorsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insegnanteCorso)))
            .andExpect(status().isBadRequest());

        // Validate the InsegnanteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInsegnanteCorsos() throws Exception {
        // Initialize the database
        insertedInsegnanteCorso = insegnanteCorsoRepository.saveAndFlush(insegnanteCorso);

        // Get all the insegnanteCorsoList
        restInsegnanteCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insegnanteCorso.getId().intValue())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsegnanteCorsosWithEagerRelationshipsIsEnabled() throws Exception {
        when(insegnanteCorsoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsegnanteCorsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(insegnanteCorsoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsegnanteCorsosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(insegnanteCorsoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsegnanteCorsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(insegnanteCorsoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInsegnanteCorso() throws Exception {
        // Initialize the database
        insertedInsegnanteCorso = insegnanteCorsoRepository.saveAndFlush(insegnanteCorso);

        // Get the insegnanteCorso
        restInsegnanteCorsoMockMvc
            .perform(get(ENTITY_API_URL_ID, insegnanteCorso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insegnanteCorso.getId().intValue()))
            .andExpect(jsonPath("$.mese").value(DEFAULT_MESE));
    }

    @Test
    @Transactional
    void getNonExistingInsegnanteCorso() throws Exception {
        // Get the insegnanteCorso
        restInsegnanteCorsoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsegnanteCorso() throws Exception {
        // Initialize the database
        insertedInsegnanteCorso = insegnanteCorsoRepository.saveAndFlush(insegnanteCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insegnanteCorso
        InsegnanteCorso updatedInsegnanteCorso = insegnanteCorsoRepository.findById(insegnanteCorso.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsegnanteCorso are not directly saved in db
        em.detach(updatedInsegnanteCorso);
        updatedInsegnanteCorso.mese(UPDATED_MESE);

        restInsegnanteCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInsegnanteCorso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInsegnanteCorso))
            )
            .andExpect(status().isOk());

        // Validate the InsegnanteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsegnanteCorsoToMatchAllProperties(updatedInsegnanteCorso);
    }

    @Test
    @Transactional
    void putNonExistingInsegnanteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnanteCorso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsegnanteCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insegnanteCorso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insegnanteCorso))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsegnanteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsegnanteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnanteCorso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsegnanteCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insegnanteCorso))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsegnanteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsegnanteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnanteCorso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsegnanteCorsoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insegnanteCorso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsegnanteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsegnanteCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedInsegnanteCorso = insegnanteCorsoRepository.saveAndFlush(insegnanteCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insegnanteCorso using partial update
        InsegnanteCorso partialUpdatedInsegnanteCorso = new InsegnanteCorso();
        partialUpdatedInsegnanteCorso.setId(insegnanteCorso.getId());

        restInsegnanteCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsegnanteCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsegnanteCorso))
            )
            .andExpect(status().isOk());

        // Validate the InsegnanteCorso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsegnanteCorsoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsegnanteCorso, insegnanteCorso),
            getPersistedInsegnanteCorso(insegnanteCorso)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsegnanteCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedInsegnanteCorso = insegnanteCorsoRepository.saveAndFlush(insegnanteCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insegnanteCorso using partial update
        InsegnanteCorso partialUpdatedInsegnanteCorso = new InsegnanteCorso();
        partialUpdatedInsegnanteCorso.setId(insegnanteCorso.getId());

        partialUpdatedInsegnanteCorso.mese(UPDATED_MESE);

        restInsegnanteCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsegnanteCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsegnanteCorso))
            )
            .andExpect(status().isOk());

        // Validate the InsegnanteCorso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsegnanteCorsoUpdatableFieldsEquals(
            partialUpdatedInsegnanteCorso,
            getPersistedInsegnanteCorso(partialUpdatedInsegnanteCorso)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInsegnanteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnanteCorso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsegnanteCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insegnanteCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insegnanteCorso))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsegnanteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsegnanteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnanteCorso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsegnanteCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insegnanteCorso))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsegnanteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsegnanteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        insegnanteCorso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsegnanteCorsoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(insegnanteCorso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsegnanteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsegnanteCorso() throws Exception {
        // Initialize the database
        insertedInsegnanteCorso = insegnanteCorsoRepository.saveAndFlush(insegnanteCorso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the insegnanteCorso
        restInsegnanteCorsoMockMvc
            .perform(delete(ENTITY_API_URL_ID, insegnanteCorso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return insegnanteCorsoRepository.count();
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

    protected InsegnanteCorso getPersistedInsegnanteCorso(InsegnanteCorso insegnanteCorso) {
        return insegnanteCorsoRepository.findById(insegnanteCorso.getId()).orElseThrow();
    }

    protected void assertPersistedInsegnanteCorsoToMatchAllProperties(InsegnanteCorso expectedInsegnanteCorso) {
        assertInsegnanteCorsoAllPropertiesEquals(expectedInsegnanteCorso, getPersistedInsegnanteCorso(expectedInsegnanteCorso));
    }

    protected void assertPersistedInsegnanteCorsoToMatchUpdatableProperties(InsegnanteCorso expectedInsegnanteCorso) {
        assertInsegnanteCorsoAllUpdatablePropertiesEquals(expectedInsegnanteCorso, getPersistedInsegnanteCorso(expectedInsegnanteCorso));
    }
}
