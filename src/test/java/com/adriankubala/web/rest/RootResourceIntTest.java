package com.adriankubala.web.rest;

import com.adriankubala.AngularExplorerApp;

import com.adriankubala.domain.Root;
import com.adriankubala.repository.RootRepository;
import com.adriankubala.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RootResource REST controller.
 *
 * @see RootResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AngularExplorerApp.class)
public class RootResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RootRepository rootRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRootMockMvc;

    private Root root;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RootResource rootResource = new RootResource(rootRepository);
        this.restRootMockMvc = MockMvcBuilders.standaloneSetup(rootResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Root createEntity(EntityManager em) {
        Root root = new Root()
            .name(DEFAULT_NAME);
        return root;
    }

    @Before
    public void initTest() {
        root = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoot() throws Exception {
        int databaseSizeBeforeCreate = rootRepository.findAll().size();

        // Create the Root
        restRootMockMvc.perform(post("/api/roots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(root)))
            .andExpect(status().isCreated());

        // Validate the Root in the database
        List<Root> rootList = rootRepository.findAll();
        assertThat(rootList).hasSize(databaseSizeBeforeCreate + 1);
        Root testRoot = rootList.get(rootList.size() - 1);
        assertThat(testRoot.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRootWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rootRepository.findAll().size();

        // Create the Root with an existing ID
        root.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRootMockMvc.perform(post("/api/roots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(root)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Root> rootList = rootRepository.findAll();
        assertThat(rootList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRoots() throws Exception {
        // Initialize the database
        rootRepository.saveAndFlush(root);

        // Get all the rootList
        restRootMockMvc.perform(get("/api/roots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(root.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRoot() throws Exception {
        // Initialize the database
        rootRepository.saveAndFlush(root);

        // Get the root
        restRootMockMvc.perform(get("/api/roots/{id}", root.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(root.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoot() throws Exception {
        // Get the root
        restRootMockMvc.perform(get("/api/roots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoot() throws Exception {
        // Initialize the database
        rootRepository.saveAndFlush(root);
        int databaseSizeBeforeUpdate = rootRepository.findAll().size();

        // Update the root
        Root updatedRoot = rootRepository.findOne(root.getId());
        updatedRoot
            .name(UPDATED_NAME);

        restRootMockMvc.perform(put("/api/roots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRoot)))
            .andExpect(status().isOk());

        // Validate the Root in the database
        List<Root> rootList = rootRepository.findAll();
        assertThat(rootList).hasSize(databaseSizeBeforeUpdate);
        Root testRoot = rootList.get(rootList.size() - 1);
        assertThat(testRoot.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRoot() throws Exception {
        int databaseSizeBeforeUpdate = rootRepository.findAll().size();

        // Create the Root

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRootMockMvc.perform(put("/api/roots")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(root)))
            .andExpect(status().isCreated());

        // Validate the Root in the database
        List<Root> rootList = rootRepository.findAll();
        assertThat(rootList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRoot() throws Exception {
        // Initialize the database
        rootRepository.saveAndFlush(root);
        int databaseSizeBeforeDelete = rootRepository.findAll().size();

        // Get the root
        restRootMockMvc.perform(delete("/api/roots/{id}", root.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Root> rootList = rootRepository.findAll();
        assertThat(rootList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Root.class);
    }
}
