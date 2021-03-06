package com.adriankubala.web.rest;

import com.adriankubala.AngularExplorerApp;

import com.adriankubala.domain.Directory;
import com.adriankubala.domain.Root;
import com.adriankubala.repository.DirectoryRepository;
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
 * Test class for the DirectoryResource REST controller.
 *
 * @see DirectoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AngularExplorerApp.class)
public class DirectoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDirectoryMockMvc;

    private Directory directory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DirectoryResource directoryResource = new DirectoryResource(directoryRepository);
        this.restDirectoryMockMvc = MockMvcBuilders.standaloneSetup(directoryResource)
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
    public static Directory createEntity(EntityManager em) {
        Directory directory = new Directory()
            .name(DEFAULT_NAME);
        // Add required entity
        Root root = RootResourceIntTest.createEntity(em);
        em.persist(root);
        em.flush();
        directory.setRoot(root);
        return directory;
    }

    @Before
    public void initTest() {
        directory = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirectory() throws Exception {
        int databaseSizeBeforeCreate = directoryRepository.findAll().size();

        // Create the Directory
        restDirectoryMockMvc.perform(post("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directory)))
            .andExpect(status().isCreated());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeCreate + 1);
        Directory testDirectory = directoryList.get(directoryList.size() - 1);
        assertThat(testDirectory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDirectoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = directoryRepository.findAll().size();

        // Create the Directory with an existing ID
        directory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectoryMockMvc.perform(post("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = directoryRepository.findAll().size();
        // set the field null
        directory.setName(null);

        // Create the Directory, which fails.

        restDirectoryMockMvc.perform(post("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directory)))
            .andExpect(status().isBadRequest());

        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirectories() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList
        restDirectoryMockMvc.perform(get("/api/directories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get the directory
        restDirectoryMockMvc.perform(get("/api/directories/{id}", directory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(directory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDirectory() throws Exception {
        // Get the directory
        restDirectoryMockMvc.perform(get("/api/directories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Update the directory
        Directory updatedDirectory = directoryRepository.findOne(directory.getId());
        updatedDirectory
            .name(UPDATED_NAME);

        restDirectoryMockMvc.perform(put("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirectory)))
            .andExpect(status().isOk());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
        Directory testDirectory = directoryList.get(directoryList.size() - 1);
        assertThat(testDirectory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDirectory() throws Exception {
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Create the Directory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDirectoryMockMvc.perform(put("/api/directories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(directory)))
            .andExpect(status().isCreated());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);
        int databaseSizeBeforeDelete = directoryRepository.findAll().size();

        // Get the directory
        restDirectoryMockMvc.perform(delete("/api/directories/{id}", directory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Directory.class);
    }
}
