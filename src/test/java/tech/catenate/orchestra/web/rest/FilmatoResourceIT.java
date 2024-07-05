package tech.catenate.orchestra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tech.catenate.orchestra.domain.FilmatoAsserts.*;
import static tech.catenate.orchestra.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Base64;
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
import tech.catenate.orchestra.domain.Filmato;
import tech.catenate.orchestra.repository.FilmatoRepository;
import tech.catenate.orchestra.service.FilmatoService;

/**
 * Integration tests for the {@link FilmatoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FilmatoResourceIT {

    private static final byte[] DEFAULT_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BLOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BLOB_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NOME_FILE = "AAAAAAAAAA";
    private static final String UPDATED_NOME_FILE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/filmatoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FilmatoRepository filmatoRepository;

    @Mock
    private FilmatoRepository filmatoRepositoryMock;

    @Mock
    private FilmatoService filmatoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilmatoMockMvc;

    private Filmato filmato;

    private Filmato insertedFilmato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filmato createEntity(EntityManager em) {
        Filmato filmato = new Filmato().blob(DEFAULT_BLOB).blobContentType(DEFAULT_BLOB_CONTENT_TYPE).nome_file(DEFAULT_NOME_FILE);
        return filmato;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filmato createUpdatedEntity(EntityManager em) {
        Filmato filmato = new Filmato().blob(UPDATED_BLOB).blobContentType(UPDATED_BLOB_CONTENT_TYPE).nome_file(UPDATED_NOME_FILE);
        return filmato;
    }

    @BeforeEach
    public void initTest() {
        filmato = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFilmato != null) {
            filmatoRepository.delete(insertedFilmato);
            insertedFilmato = null;
        }
    }

    @Test
    @Transactional
    void createFilmato() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Filmato
        var returnedFilmato = om.readValue(
            restFilmatoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filmato)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Filmato.class
        );

        // Validate the Filmato in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFilmatoUpdatableFieldsEquals(returnedFilmato, getPersistedFilmato(returnedFilmato));

        insertedFilmato = returnedFilmato;
    }

    @Test
    @Transactional
    void createFilmatoWithExistingId() throws Exception {
        // Create the Filmato with an existing ID
        filmato.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilmatoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filmato)))
            .andExpect(status().isBadRequest());

        // Validate the Filmato in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFilmatoes() throws Exception {
        // Initialize the database
        insertedFilmato = filmatoRepository.saveAndFlush(filmato);

        // Get all the filmatoList
        restFilmatoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filmato.getId().intValue())))
            .andExpect(jsonPath("$.[*].blobContentType").value(hasItem(DEFAULT_BLOB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].blob").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BLOB))))
            .andExpect(jsonPath("$.[*].nome_file").value(hasItem(DEFAULT_NOME_FILE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilmatoesWithEagerRelationshipsIsEnabled() throws Exception {
        when(filmatoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilmatoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(filmatoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFilmatoesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(filmatoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFilmatoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(filmatoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFilmato() throws Exception {
        // Initialize the database
        insertedFilmato = filmatoRepository.saveAndFlush(filmato);

        // Get the filmato
        restFilmatoMockMvc
            .perform(get(ENTITY_API_URL_ID, filmato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filmato.getId().intValue()))
            .andExpect(jsonPath("$.blobContentType").value(DEFAULT_BLOB_CONTENT_TYPE))
            .andExpect(jsonPath("$.blob").value(Base64.getEncoder().encodeToString(DEFAULT_BLOB)))
            .andExpect(jsonPath("$.nome_file").value(DEFAULT_NOME_FILE));
    }

    @Test
    @Transactional
    void getNonExistingFilmato() throws Exception {
        // Get the filmato
        restFilmatoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFilmato() throws Exception {
        // Initialize the database
        insertedFilmato = filmatoRepository.saveAndFlush(filmato);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filmato
        Filmato updatedFilmato = filmatoRepository.findById(filmato.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFilmato are not directly saved in db
        em.detach(updatedFilmato);
        updatedFilmato.blob(UPDATED_BLOB).blobContentType(UPDATED_BLOB_CONTENT_TYPE).nome_file(UPDATED_NOME_FILE);

        restFilmatoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFilmato.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFilmato))
            )
            .andExpect(status().isOk());

        // Validate the Filmato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFilmatoToMatchAllProperties(updatedFilmato);
    }

    @Test
    @Transactional
    void putNonExistingFilmato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filmato.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmatoMockMvc
            .perform(put(ENTITY_API_URL_ID, filmato.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filmato)))
            .andExpect(status().isBadRequest());

        // Validate the Filmato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilmato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filmato.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmatoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(filmato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filmato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilmato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filmato.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmatoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(filmato)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filmato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilmatoWithPatch() throws Exception {
        // Initialize the database
        insertedFilmato = filmatoRepository.saveAndFlush(filmato);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filmato using partial update
        Filmato partialUpdatedFilmato = new Filmato();
        partialUpdatedFilmato.setId(filmato.getId());

        partialUpdatedFilmato.blob(UPDATED_BLOB).blobContentType(UPDATED_BLOB_CONTENT_TYPE).nome_file(UPDATED_NOME_FILE);

        restFilmatoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilmato.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFilmato))
            )
            .andExpect(status().isOk());

        // Validate the Filmato in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFilmatoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFilmato, filmato), getPersistedFilmato(filmato));
    }

    @Test
    @Transactional
    void fullUpdateFilmatoWithPatch() throws Exception {
        // Initialize the database
        insertedFilmato = filmatoRepository.saveAndFlush(filmato);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the filmato using partial update
        Filmato partialUpdatedFilmato = new Filmato();
        partialUpdatedFilmato.setId(filmato.getId());

        partialUpdatedFilmato.blob(UPDATED_BLOB).blobContentType(UPDATED_BLOB_CONTENT_TYPE).nome_file(UPDATED_NOME_FILE);

        restFilmatoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilmato.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFilmato))
            )
            .andExpect(status().isOk());

        // Validate the Filmato in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFilmatoUpdatableFieldsEquals(partialUpdatedFilmato, getPersistedFilmato(partialUpdatedFilmato));
    }

    @Test
    @Transactional
    void patchNonExistingFilmato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filmato.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmatoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filmato.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(filmato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filmato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilmato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filmato.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmatoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(filmato))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filmato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilmato() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        filmato.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmatoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(filmato)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filmato in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilmato() throws Exception {
        // Initialize the database
        insertedFilmato = filmatoRepository.saveAndFlush(filmato);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the filmato
        restFilmatoMockMvc
            .perform(delete(ENTITY_API_URL_ID, filmato.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return filmatoRepository.count();
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

    protected Filmato getPersistedFilmato(Filmato filmato) {
        return filmatoRepository.findById(filmato.getId()).orElseThrow();
    }

    protected void assertPersistedFilmatoToMatchAllProperties(Filmato expectedFilmato) {
        assertFilmatoAllPropertiesEquals(expectedFilmato, getPersistedFilmato(expectedFilmato));
    }

    protected void assertPersistedFilmatoToMatchUpdatableProperties(Filmato expectedFilmato) {
        assertFilmatoAllUpdatablePropertiesEquals(expectedFilmato, getPersistedFilmato(expectedFilmato));
    }
}
