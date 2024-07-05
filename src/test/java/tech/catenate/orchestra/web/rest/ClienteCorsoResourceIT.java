package tech.catenate.orchestra.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tech.catenate.orchestra.domain.ClienteCorsoAsserts.*;
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
import tech.catenate.orchestra.domain.ClienteCorso;
import tech.catenate.orchestra.repository.ClienteCorsoRepository;
import tech.catenate.orchestra.service.ClienteCorsoService;

/**
 * Integration tests for the {@link ClienteCorsoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClienteCorsoResourceIT {

    private static final Integer DEFAULT_MESE = 1;
    private static final Integer UPDATED_MESE = 2;

    private static final Boolean DEFAULT_PAGATO = false;
    private static final Boolean UPDATED_PAGATO = true;

    private static final String ENTITY_API_URL = "/api/cliente-corsos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClienteCorsoRepository clienteCorsoRepository;

    @Mock
    private ClienteCorsoRepository clienteCorsoRepositoryMock;

    @Mock
    private ClienteCorsoService clienteCorsoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteCorsoMockMvc;

    private ClienteCorso clienteCorso;

    private ClienteCorso insertedClienteCorso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClienteCorso createEntity(EntityManager em) {
        ClienteCorso clienteCorso = new ClienteCorso().mese(DEFAULT_MESE).pagato(DEFAULT_PAGATO);
        return clienteCorso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClienteCorso createUpdatedEntity(EntityManager em) {
        ClienteCorso clienteCorso = new ClienteCorso().mese(UPDATED_MESE).pagato(UPDATED_PAGATO);
        return clienteCorso;
    }

    @BeforeEach
    public void initTest() {
        clienteCorso = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedClienteCorso != null) {
            clienteCorsoRepository.delete(insertedClienteCorso);
            insertedClienteCorso = null;
        }
    }

    @Test
    @Transactional
    void createClienteCorso() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClienteCorso
        var returnedClienteCorso = om.readValue(
            restClienteCorsoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteCorso)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClienteCorso.class
        );

        // Validate the ClienteCorso in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertClienteCorsoUpdatableFieldsEquals(returnedClienteCorso, getPersistedClienteCorso(returnedClienteCorso));

        insertedClienteCorso = returnedClienteCorso;
    }

    @Test
    @Transactional
    void createClienteCorsoWithExistingId() throws Exception {
        // Create the ClienteCorso with an existing ID
        clienteCorso.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteCorsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteCorso)))
            .andExpect(status().isBadRequest());

        // Validate the ClienteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClienteCorsos() throws Exception {
        // Initialize the database
        insertedClienteCorso = clienteCorsoRepository.saveAndFlush(clienteCorso);

        // Get all the clienteCorsoList
        restClienteCorsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clienteCorso.getId().intValue())))
            .andExpect(jsonPath("$.[*].mese").value(hasItem(DEFAULT_MESE)))
            .andExpect(jsonPath("$.[*].pagato").value(hasItem(DEFAULT_PAGATO.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClienteCorsosWithEagerRelationshipsIsEnabled() throws Exception {
        when(clienteCorsoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClienteCorsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(clienteCorsoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClienteCorsosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(clienteCorsoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClienteCorsoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(clienteCorsoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getClienteCorso() throws Exception {
        // Initialize the database
        insertedClienteCorso = clienteCorsoRepository.saveAndFlush(clienteCorso);

        // Get the clienteCorso
        restClienteCorsoMockMvc
            .perform(get(ENTITY_API_URL_ID, clienteCorso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clienteCorso.getId().intValue()))
            .andExpect(jsonPath("$.mese").value(DEFAULT_MESE))
            .andExpect(jsonPath("$.pagato").value(DEFAULT_PAGATO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingClienteCorso() throws Exception {
        // Get the clienteCorso
        restClienteCorsoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClienteCorso() throws Exception {
        // Initialize the database
        insertedClienteCorso = clienteCorsoRepository.saveAndFlush(clienteCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clienteCorso
        ClienteCorso updatedClienteCorso = clienteCorsoRepository.findById(clienteCorso.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClienteCorso are not directly saved in db
        em.detach(updatedClienteCorso);
        updatedClienteCorso.mese(UPDATED_MESE).pagato(UPDATED_PAGATO);

        restClienteCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClienteCorso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedClienteCorso))
            )
            .andExpect(status().isOk());

        // Validate the ClienteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClienteCorsoToMatchAllProperties(updatedClienteCorso);
    }

    @Test
    @Transactional
    void putNonExistingClienteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteCorso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteCorso.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteCorso))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClienteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteCorso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteCorsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clienteCorso))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClienteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteCorso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteCorsoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clienteCorso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClienteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedClienteCorso = clienteCorsoRepository.saveAndFlush(clienteCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clienteCorso using partial update
        ClienteCorso partialUpdatedClienteCorso = new ClienteCorso();
        partialUpdatedClienteCorso.setId(clienteCorso.getId());

        partialUpdatedClienteCorso.pagato(UPDATED_PAGATO);

        restClienteCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClienteCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClienteCorso))
            )
            .andExpect(status().isOk());

        // Validate the ClienteCorso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteCorsoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClienteCorso, clienteCorso),
            getPersistedClienteCorso(clienteCorso)
        );
    }

    @Test
    @Transactional
    void fullUpdateClienteCorsoWithPatch() throws Exception {
        // Initialize the database
        insertedClienteCorso = clienteCorsoRepository.saveAndFlush(clienteCorso);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clienteCorso using partial update
        ClienteCorso partialUpdatedClienteCorso = new ClienteCorso();
        partialUpdatedClienteCorso.setId(clienteCorso.getId());

        partialUpdatedClienteCorso.mese(UPDATED_MESE).pagato(UPDATED_PAGATO);

        restClienteCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClienteCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClienteCorso))
            )
            .andExpect(status().isOk());

        // Validate the ClienteCorso in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClienteCorsoUpdatableFieldsEquals(partialUpdatedClienteCorso, getPersistedClienteCorso(partialUpdatedClienteCorso));
    }

    @Test
    @Transactional
    void patchNonExistingClienteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteCorso.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clienteCorso.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clienteCorso))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClienteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteCorso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteCorsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clienteCorso))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClienteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClienteCorso() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clienteCorso.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteCorsoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clienteCorso)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClienteCorso in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClienteCorso() throws Exception {
        // Initialize the database
        insertedClienteCorso = clienteCorsoRepository.saveAndFlush(clienteCorso);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the clienteCorso
        restClienteCorsoMockMvc
            .perform(delete(ENTITY_API_URL_ID, clienteCorso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clienteCorsoRepository.count();
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

    protected ClienteCorso getPersistedClienteCorso(ClienteCorso clienteCorso) {
        return clienteCorsoRepository.findById(clienteCorso.getId()).orElseThrow();
    }

    protected void assertPersistedClienteCorsoToMatchAllProperties(ClienteCorso expectedClienteCorso) {
        assertClienteCorsoAllPropertiesEquals(expectedClienteCorso, getPersistedClienteCorso(expectedClienteCorso));
    }

    protected void assertPersistedClienteCorsoToMatchUpdatableProperties(ClienteCorso expectedClienteCorso) {
        assertClienteCorsoAllUpdatablePropertiesEquals(expectedClienteCorso, getPersistedClienteCorso(expectedClienteCorso));
    }
}
