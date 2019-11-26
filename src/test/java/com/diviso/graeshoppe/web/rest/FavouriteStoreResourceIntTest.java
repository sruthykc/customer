package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.CustomerApp;

import com.diviso.graeshoppe.domain.FavouriteStore;
import com.diviso.graeshoppe.repository.FavouriteStoreRepository;
import com.diviso.graeshoppe.repository.search.FavouriteStoreSearchRepository;
import com.diviso.graeshoppe.service.FavouriteStoreService;
import com.diviso.graeshoppe.service.dto.FavouriteStoreDTO;
import com.diviso.graeshoppe.service.mapper.FavouriteStoreMapper;
import com.diviso.graeshoppe.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.diviso.graeshoppe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FavouriteStoreResource REST controller.
 *
 * @see FavouriteStoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApp.class)
public class FavouriteStoreResourceIntTest {

    private static final Long DEFAULT_STORE_ID = 1L;
    private static final Long UPDATED_STORE_ID = 2L;

    @Autowired
    private FavouriteStoreRepository favouriteStoreRepository;

    @Autowired
    private FavouriteStoreMapper favouriteStoreMapper;

    @Autowired
    private FavouriteStoreService favouriteStoreService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.FavouriteStoreSearchRepositoryMockConfiguration
     */
    @Autowired
    private FavouriteStoreSearchRepository mockFavouriteStoreSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFavouriteStoreMockMvc;

    private FavouriteStore favouriteStore;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FavouriteStoreResource favouriteStoreResource = new FavouriteStoreResource(favouriteStoreService);
        this.restFavouriteStoreMockMvc = MockMvcBuilders.standaloneSetup(favouriteStoreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavouriteStore createEntity(EntityManager em) {
        FavouriteStore favouriteStore = new FavouriteStore()
            .storeId(DEFAULT_STORE_ID);
        return favouriteStore;
    }

    @Before
    public void initTest() {
        favouriteStore = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavouriteStore() throws Exception {
        int databaseSizeBeforeCreate = favouriteStoreRepository.findAll().size();

        // Create the FavouriteStore
        FavouriteStoreDTO favouriteStoreDTO = favouriteStoreMapper.toDto(favouriteStore);
        restFavouriteStoreMockMvc.perform(post("/api/favourite-stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favouriteStoreDTO)))
            .andExpect(status().isCreated());

        // Validate the FavouriteStore in the database
        List<FavouriteStore> favouriteStoreList = favouriteStoreRepository.findAll();
        assertThat(favouriteStoreList).hasSize(databaseSizeBeforeCreate + 1);
        FavouriteStore testFavouriteStore = favouriteStoreList.get(favouriteStoreList.size() - 1);
        assertThat(testFavouriteStore.getStoreId()).isEqualTo(DEFAULT_STORE_ID);

        // Validate the FavouriteStore in Elasticsearch
        verify(mockFavouriteStoreSearchRepository, times(1)).save(testFavouriteStore);
    }

    @Test
    @Transactional
    public void createFavouriteStoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = favouriteStoreRepository.findAll().size();

        // Create the FavouriteStore with an existing ID
        favouriteStore.setId(1L);
        FavouriteStoreDTO favouriteStoreDTO = favouriteStoreMapper.toDto(favouriteStore);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavouriteStoreMockMvc.perform(post("/api/favourite-stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favouriteStoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavouriteStore in the database
        List<FavouriteStore> favouriteStoreList = favouriteStoreRepository.findAll();
        assertThat(favouriteStoreList).hasSize(databaseSizeBeforeCreate);

        // Validate the FavouriteStore in Elasticsearch
        verify(mockFavouriteStoreSearchRepository, times(0)).save(favouriteStore);
    }

    @Test
    @Transactional
    public void checkStoreIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = favouriteStoreRepository.findAll().size();
        // set the field null
        favouriteStore.setStoreId(null);

        // Create the FavouriteStore, which fails.
        FavouriteStoreDTO favouriteStoreDTO = favouriteStoreMapper.toDto(favouriteStore);

        restFavouriteStoreMockMvc.perform(post("/api/favourite-stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favouriteStoreDTO)))
            .andExpect(status().isBadRequest());

        List<FavouriteStore> favouriteStoreList = favouriteStoreRepository.findAll();
        assertThat(favouriteStoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFavouriteStores() throws Exception {
        // Initialize the database
        favouriteStoreRepository.saveAndFlush(favouriteStore);

        // Get all the favouriteStoreList
        restFavouriteStoreMockMvc.perform(get("/api/favourite-stores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].storeId").value(hasItem(DEFAULT_STORE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getFavouriteStore() throws Exception {
        // Initialize the database
        favouriteStoreRepository.saveAndFlush(favouriteStore);

        // Get the favouriteStore
        restFavouriteStoreMockMvc.perform(get("/api/favourite-stores/{id}", favouriteStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(favouriteStore.getId().intValue()))
            .andExpect(jsonPath("$.storeId").value(DEFAULT_STORE_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavouriteStore() throws Exception {
        // Get the favouriteStore
        restFavouriteStoreMockMvc.perform(get("/api/favourite-stores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavouriteStore() throws Exception {
        // Initialize the database
        favouriteStoreRepository.saveAndFlush(favouriteStore);

        int databaseSizeBeforeUpdate = favouriteStoreRepository.findAll().size();

        // Update the favouriteStore
        FavouriteStore updatedFavouriteStore = favouriteStoreRepository.findById(favouriteStore.getId()).get();
        // Disconnect from session so that the updates on updatedFavouriteStore are not directly saved in db
        em.detach(updatedFavouriteStore);
        updatedFavouriteStore
            .storeId(UPDATED_STORE_ID);
        FavouriteStoreDTO favouriteStoreDTO = favouriteStoreMapper.toDto(updatedFavouriteStore);

        restFavouriteStoreMockMvc.perform(put("/api/favourite-stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favouriteStoreDTO)))
            .andExpect(status().isOk());

        // Validate the FavouriteStore in the database
        List<FavouriteStore> favouriteStoreList = favouriteStoreRepository.findAll();
        assertThat(favouriteStoreList).hasSize(databaseSizeBeforeUpdate);
        FavouriteStore testFavouriteStore = favouriteStoreList.get(favouriteStoreList.size() - 1);
        assertThat(testFavouriteStore.getStoreId()).isEqualTo(UPDATED_STORE_ID);

        // Validate the FavouriteStore in Elasticsearch
        verify(mockFavouriteStoreSearchRepository, times(1)).save(testFavouriteStore);
    }

    @Test
    @Transactional
    public void updateNonExistingFavouriteStore() throws Exception {
        int databaseSizeBeforeUpdate = favouriteStoreRepository.findAll().size();

        // Create the FavouriteStore
        FavouriteStoreDTO favouriteStoreDTO = favouriteStoreMapper.toDto(favouriteStore);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavouriteStoreMockMvc.perform(put("/api/favourite-stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favouriteStoreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavouriteStore in the database
        List<FavouriteStore> favouriteStoreList = favouriteStoreRepository.findAll();
        assertThat(favouriteStoreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FavouriteStore in Elasticsearch
        verify(mockFavouriteStoreSearchRepository, times(0)).save(favouriteStore);
    }

    @Test
    @Transactional
    public void deleteFavouriteStore() throws Exception {
        // Initialize the database
        favouriteStoreRepository.saveAndFlush(favouriteStore);

        int databaseSizeBeforeDelete = favouriteStoreRepository.findAll().size();

        // Delete the favouriteStore
        restFavouriteStoreMockMvc.perform(delete("/api/favourite-stores/{id}", favouriteStore.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FavouriteStore> favouriteStoreList = favouriteStoreRepository.findAll();
        assertThat(favouriteStoreList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FavouriteStore in Elasticsearch
        verify(mockFavouriteStoreSearchRepository, times(1)).deleteById(favouriteStore.getId());
    }

    @Test
    @Transactional
    public void searchFavouriteStore() throws Exception {
        // Initialize the database
        favouriteStoreRepository.saveAndFlush(favouriteStore);
        when(mockFavouriteStoreSearchRepository.search(queryStringQuery("id:" + favouriteStore.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(favouriteStore), PageRequest.of(0, 1), 1));
        // Search the favouriteStore
        restFavouriteStoreMockMvc.perform(get("/api/_search/favourite-stores?query=id:" + favouriteStore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favouriteStore.getId().intValue())))
            .andExpect(jsonPath("$.[*].storeId").value(hasItem(DEFAULT_STORE_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavouriteStore.class);
        FavouriteStore favouriteStore1 = new FavouriteStore();
        favouriteStore1.setId(1L);
        FavouriteStore favouriteStore2 = new FavouriteStore();
        favouriteStore2.setId(favouriteStore1.getId());
        assertThat(favouriteStore1).isEqualTo(favouriteStore2);
        favouriteStore2.setId(2L);
        assertThat(favouriteStore1).isNotEqualTo(favouriteStore2);
        favouriteStore1.setId(null);
        assertThat(favouriteStore1).isNotEqualTo(favouriteStore2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavouriteStoreDTO.class);
        FavouriteStoreDTO favouriteStoreDTO1 = new FavouriteStoreDTO();
        favouriteStoreDTO1.setId(1L);
        FavouriteStoreDTO favouriteStoreDTO2 = new FavouriteStoreDTO();
        assertThat(favouriteStoreDTO1).isNotEqualTo(favouriteStoreDTO2);
        favouriteStoreDTO2.setId(favouriteStoreDTO1.getId());
        assertThat(favouriteStoreDTO1).isEqualTo(favouriteStoreDTO2);
        favouriteStoreDTO2.setId(2L);
        assertThat(favouriteStoreDTO1).isNotEqualTo(favouriteStoreDTO2);
        favouriteStoreDTO1.setId(null);
        assertThat(favouriteStoreDTO1).isNotEqualTo(favouriteStoreDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(favouriteStoreMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(favouriteStoreMapper.fromId(null)).isNull();
    }
}
