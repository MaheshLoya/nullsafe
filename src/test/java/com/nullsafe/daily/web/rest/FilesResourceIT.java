package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Files;
import com.nullsafe.daily.repository.FilesRepository;
import com.nullsafe.daily.service.dto.FilesDTO;
import com.nullsafe.daily.service.mapper.FilesMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FilesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FilesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Integer DEFAULT_FILE_FOR = 1;
    private static final Integer UPDATED_FILE_FOR = 2;
    private static final Integer SMALLER_FILE_FOR = 1 - 1;

    private static final Integer DEFAULT_FILE_FOR_ID = 1;
    private static final Integer UPDATED_FILE_FOR_ID = 2;
    private static final Integer SMALLER_FILE_FOR_ID = 1 - 1;

    private static final Boolean DEFAULT_FILE_CAT = false;
    private static final Boolean UPDATED_FILE_CAT = true;

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private FilesMapper filesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilesMockMvc;

    private Files files;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Files createEntity(EntityManager em) {
        Files files = new Files()
            .name(DEFAULT_NAME)
            .fileUrl(DEFAULT_FILE_URL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deleted(DEFAULT_DELETED)
            .fileFor(DEFAULT_FILE_FOR)
            .fileForId(DEFAULT_FILE_FOR_ID)
            .fileCat(DEFAULT_FILE_CAT);
        return files;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Files createUpdatedEntity(EntityManager em) {
        Files files = new Files()
            .name(UPDATED_NAME)
            .fileUrl(UPDATED_FILE_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deleted(UPDATED_DELETED)
            .fileFor(UPDATED_FILE_FOR)
            .fileForId(UPDATED_FILE_FOR_ID)
            .fileCat(UPDATED_FILE_CAT);
        return files;
    }

    @BeforeEach
    public void initTest() {
        files = createEntity(em);
    }

    @Test
    @Transactional
    void createFiles() throws Exception {
        int databaseSizeBeforeCreate = filesRepository.findAll().size();
        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);
        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isCreated());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeCreate + 1);
        Files testFiles = filesList.get(filesList.size() - 1);
        assertThat(testFiles.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFiles.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testFiles.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testFiles.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testFiles.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testFiles.getFileFor()).isEqualTo(DEFAULT_FILE_FOR);
        assertThat(testFiles.getFileForId()).isEqualTo(DEFAULT_FILE_FOR_ID);
        assertThat(testFiles.getFileCat()).isEqualTo(DEFAULT_FILE_CAT);
    }

    @Test
    @Transactional
    void createFilesWithExistingId() throws Exception {
        // Create the Files with an existing ID
        files.setId(1L);
        FilesDTO filesDTO = filesMapper.toDto(files);

        int databaseSizeBeforeCreate = filesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = filesRepository.findAll().size();
        // set the field null
        files.setName(null);

        // Create the Files, which fails.
        FilesDTO filesDTO = filesMapper.toDto(files);

        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isBadRequest());

        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = filesRepository.findAll().size();
        // set the field null
        files.setFileUrl(null);

        // Create the Files, which fails.
        FilesDTO filesDTO = filesMapper.toDto(files);

        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isBadRequest());

        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = filesRepository.findAll().size();
        // set the field null
        files.setDeleted(null);

        // Create the Files, which fails.
        FilesDTO filesDTO = filesMapper.toDto(files);

        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isBadRequest());

        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileForIsRequired() throws Exception {
        int databaseSizeBeforeTest = filesRepository.findAll().size();
        // set the field null
        files.setFileFor(null);

        // Create the Files, which fails.
        FilesDTO filesDTO = filesMapper.toDto(files);

        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isBadRequest());

        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileForIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = filesRepository.findAll().size();
        // set the field null
        files.setFileForId(null);

        // Create the Files, which fails.
        FilesDTO filesDTO = filesMapper.toDto(files);

        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isBadRequest());

        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileCatIsRequired() throws Exception {
        int databaseSizeBeforeTest = filesRepository.findAll().size();
        // set the field null
        files.setFileCat(null);

        // Create the Files, which fails.
        FilesDTO filesDTO = filesMapper.toDto(files);

        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isBadRequest());

        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiles() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList
        restFilesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(files.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileFor").value(hasItem(DEFAULT_FILE_FOR)))
            .andExpect(jsonPath("$.[*].fileForId").value(hasItem(DEFAULT_FILE_FOR_ID)))
            .andExpect(jsonPath("$.[*].fileCat").value(hasItem(DEFAULT_FILE_CAT.booleanValue())));
    }

    @Test
    @Transactional
    void getFiles() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get the files
        restFilesMockMvc
            .perform(get(ENTITY_API_URL_ID, files.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(files.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.fileFor").value(DEFAULT_FILE_FOR))
            .andExpect(jsonPath("$.fileForId").value(DEFAULT_FILE_FOR_ID))
            .andExpect(jsonPath("$.fileCat").value(DEFAULT_FILE_CAT.booleanValue()));
    }

    @Test
    @Transactional
    void getFilesByIdFiltering() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        Long id = files.getId();

        defaultFilesShouldBeFound("id.equals=" + id);
        defaultFilesShouldNotBeFound("id.notEquals=" + id);

        defaultFilesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFilesShouldNotBeFound("id.greaterThan=" + id);

        defaultFilesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFilesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where name equals to DEFAULT_NAME
        defaultFilesShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the filesList where name equals to UPDATED_NAME
        defaultFilesShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFilesShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the filesList where name equals to UPDATED_NAME
        defaultFilesShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where name is not null
        defaultFilesShouldBeFound("name.specified=true");

        // Get all the filesList where name is null
        defaultFilesShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByNameContainsSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where name contains DEFAULT_NAME
        defaultFilesShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the filesList where name contains UPDATED_NAME
        defaultFilesShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where name does not contain DEFAULT_NAME
        defaultFilesShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the filesList where name does not contain UPDATED_NAME
        defaultFilesShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFilesByFileUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileUrl equals to DEFAULT_FILE_URL
        defaultFilesShouldBeFound("fileUrl.equals=" + DEFAULT_FILE_URL);

        // Get all the filesList where fileUrl equals to UPDATED_FILE_URL
        defaultFilesShouldNotBeFound("fileUrl.equals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllFilesByFileUrlIsInShouldWork() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileUrl in DEFAULT_FILE_URL or UPDATED_FILE_URL
        defaultFilesShouldBeFound("fileUrl.in=" + DEFAULT_FILE_URL + "," + UPDATED_FILE_URL);

        // Get all the filesList where fileUrl equals to UPDATED_FILE_URL
        defaultFilesShouldNotBeFound("fileUrl.in=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllFilesByFileUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileUrl is not null
        defaultFilesShouldBeFound("fileUrl.specified=true");

        // Get all the filesList where fileUrl is null
        defaultFilesShouldNotBeFound("fileUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByFileUrlContainsSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileUrl contains DEFAULT_FILE_URL
        defaultFilesShouldBeFound("fileUrl.contains=" + DEFAULT_FILE_URL);

        // Get all the filesList where fileUrl contains UPDATED_FILE_URL
        defaultFilesShouldNotBeFound("fileUrl.contains=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllFilesByFileUrlNotContainsSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileUrl does not contain DEFAULT_FILE_URL
        defaultFilesShouldNotBeFound("fileUrl.doesNotContain=" + DEFAULT_FILE_URL);

        // Get all the filesList where fileUrl does not contain UPDATED_FILE_URL
        defaultFilesShouldBeFound("fileUrl.doesNotContain=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllFilesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where createdAt equals to DEFAULT_CREATED_AT
        defaultFilesShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the filesList where createdAt equals to UPDATED_CREATED_AT
        defaultFilesShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllFilesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultFilesShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the filesList where createdAt equals to UPDATED_CREATED_AT
        defaultFilesShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllFilesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where createdAt is not null
        defaultFilesShouldBeFound("createdAt.specified=true");

        // Get all the filesList where createdAt is null
        defaultFilesShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultFilesShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the filesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultFilesShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllFilesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultFilesShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the filesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultFilesShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllFilesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where updatedAt is not null
        defaultFilesShouldBeFound("updatedAt.specified=true");

        // Get all the filesList where updatedAt is null
        defaultFilesShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where deleted equals to DEFAULT_DELETED
        defaultFilesShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the filesList where deleted equals to UPDATED_DELETED
        defaultFilesShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllFilesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultFilesShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the filesList where deleted equals to UPDATED_DELETED
        defaultFilesShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllFilesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where deleted is not null
        defaultFilesShouldBeFound("deleted.specified=true");

        // Get all the filesList where deleted is null
        defaultFilesShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByFileForIsEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileFor equals to DEFAULT_FILE_FOR
        defaultFilesShouldBeFound("fileFor.equals=" + DEFAULT_FILE_FOR);

        // Get all the filesList where fileFor equals to UPDATED_FILE_FOR
        defaultFilesShouldNotBeFound("fileFor.equals=" + UPDATED_FILE_FOR);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIsInShouldWork() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileFor in DEFAULT_FILE_FOR or UPDATED_FILE_FOR
        defaultFilesShouldBeFound("fileFor.in=" + DEFAULT_FILE_FOR + "," + UPDATED_FILE_FOR);

        // Get all the filesList where fileFor equals to UPDATED_FILE_FOR
        defaultFilesShouldNotBeFound("fileFor.in=" + UPDATED_FILE_FOR);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileFor is not null
        defaultFilesShouldBeFound("fileFor.specified=true");

        // Get all the filesList where fileFor is null
        defaultFilesShouldNotBeFound("fileFor.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByFileForIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileFor is greater than or equal to DEFAULT_FILE_FOR
        defaultFilesShouldBeFound("fileFor.greaterThanOrEqual=" + DEFAULT_FILE_FOR);

        // Get all the filesList where fileFor is greater than or equal to UPDATED_FILE_FOR
        defaultFilesShouldNotBeFound("fileFor.greaterThanOrEqual=" + UPDATED_FILE_FOR);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileFor is less than or equal to DEFAULT_FILE_FOR
        defaultFilesShouldBeFound("fileFor.lessThanOrEqual=" + DEFAULT_FILE_FOR);

        // Get all the filesList where fileFor is less than or equal to SMALLER_FILE_FOR
        defaultFilesShouldNotBeFound("fileFor.lessThanOrEqual=" + SMALLER_FILE_FOR);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIsLessThanSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileFor is less than DEFAULT_FILE_FOR
        defaultFilesShouldNotBeFound("fileFor.lessThan=" + DEFAULT_FILE_FOR);

        // Get all the filesList where fileFor is less than UPDATED_FILE_FOR
        defaultFilesShouldBeFound("fileFor.lessThan=" + UPDATED_FILE_FOR);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIsGreaterThanSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileFor is greater than DEFAULT_FILE_FOR
        defaultFilesShouldNotBeFound("fileFor.greaterThan=" + DEFAULT_FILE_FOR);

        // Get all the filesList where fileFor is greater than SMALLER_FILE_FOR
        defaultFilesShouldBeFound("fileFor.greaterThan=" + SMALLER_FILE_FOR);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIdIsEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileForId equals to DEFAULT_FILE_FOR_ID
        defaultFilesShouldBeFound("fileForId.equals=" + DEFAULT_FILE_FOR_ID);

        // Get all the filesList where fileForId equals to UPDATED_FILE_FOR_ID
        defaultFilesShouldNotBeFound("fileForId.equals=" + UPDATED_FILE_FOR_ID);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIdIsInShouldWork() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileForId in DEFAULT_FILE_FOR_ID or UPDATED_FILE_FOR_ID
        defaultFilesShouldBeFound("fileForId.in=" + DEFAULT_FILE_FOR_ID + "," + UPDATED_FILE_FOR_ID);

        // Get all the filesList where fileForId equals to UPDATED_FILE_FOR_ID
        defaultFilesShouldNotBeFound("fileForId.in=" + UPDATED_FILE_FOR_ID);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileForId is not null
        defaultFilesShouldBeFound("fileForId.specified=true");

        // Get all the filesList where fileForId is null
        defaultFilesShouldNotBeFound("fileForId.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByFileForIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileForId is greater than or equal to DEFAULT_FILE_FOR_ID
        defaultFilesShouldBeFound("fileForId.greaterThanOrEqual=" + DEFAULT_FILE_FOR_ID);

        // Get all the filesList where fileForId is greater than or equal to UPDATED_FILE_FOR_ID
        defaultFilesShouldNotBeFound("fileForId.greaterThanOrEqual=" + UPDATED_FILE_FOR_ID);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileForId is less than or equal to DEFAULT_FILE_FOR_ID
        defaultFilesShouldBeFound("fileForId.lessThanOrEqual=" + DEFAULT_FILE_FOR_ID);

        // Get all the filesList where fileForId is less than or equal to SMALLER_FILE_FOR_ID
        defaultFilesShouldNotBeFound("fileForId.lessThanOrEqual=" + SMALLER_FILE_FOR_ID);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIdIsLessThanSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileForId is less than DEFAULT_FILE_FOR_ID
        defaultFilesShouldNotBeFound("fileForId.lessThan=" + DEFAULT_FILE_FOR_ID);

        // Get all the filesList where fileForId is less than UPDATED_FILE_FOR_ID
        defaultFilesShouldBeFound("fileForId.lessThan=" + UPDATED_FILE_FOR_ID);
    }

    @Test
    @Transactional
    void getAllFilesByFileForIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileForId is greater than DEFAULT_FILE_FOR_ID
        defaultFilesShouldNotBeFound("fileForId.greaterThan=" + DEFAULT_FILE_FOR_ID);

        // Get all the filesList where fileForId is greater than SMALLER_FILE_FOR_ID
        defaultFilesShouldBeFound("fileForId.greaterThan=" + SMALLER_FILE_FOR_ID);
    }

    @Test
    @Transactional
    void getAllFilesByFileCatIsEqualToSomething() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileCat equals to DEFAULT_FILE_CAT
        defaultFilesShouldBeFound("fileCat.equals=" + DEFAULT_FILE_CAT);

        // Get all the filesList where fileCat equals to UPDATED_FILE_CAT
        defaultFilesShouldNotBeFound("fileCat.equals=" + UPDATED_FILE_CAT);
    }

    @Test
    @Transactional
    void getAllFilesByFileCatIsInShouldWork() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileCat in DEFAULT_FILE_CAT or UPDATED_FILE_CAT
        defaultFilesShouldBeFound("fileCat.in=" + DEFAULT_FILE_CAT + "," + UPDATED_FILE_CAT);

        // Get all the filesList where fileCat equals to UPDATED_FILE_CAT
        defaultFilesShouldNotBeFound("fileCat.in=" + UPDATED_FILE_CAT);
    }

    @Test
    @Transactional
    void getAllFilesByFileCatIsNullOrNotNull() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList where fileCat is not null
        defaultFilesShouldBeFound("fileCat.specified=true");

        // Get all the filesList where fileCat is null
        defaultFilesShouldNotBeFound("fileCat.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFilesShouldBeFound(String filter) throws Exception {
        restFilesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(files.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileFor").value(hasItem(DEFAULT_FILE_FOR)))
            .andExpect(jsonPath("$.[*].fileForId").value(hasItem(DEFAULT_FILE_FOR_ID)))
            .andExpect(jsonPath("$.[*].fileCat").value(hasItem(DEFAULT_FILE_CAT.booleanValue())));

        // Check, that the count call also returns 1
        restFilesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFilesShouldNotBeFound(String filter) throws Exception {
        restFilesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFilesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFiles() throws Exception {
        // Get the files
        restFilesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFiles() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        int databaseSizeBeforeUpdate = filesRepository.findAll().size();

        // Update the files
        Files updatedFiles = filesRepository.findById(files.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFiles are not directly saved in db
        em.detach(updatedFiles);
        updatedFiles
            .name(UPDATED_NAME)
            .fileUrl(UPDATED_FILE_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deleted(UPDATED_DELETED)
            .fileFor(UPDATED_FILE_FOR)
            .fileForId(UPDATED_FILE_FOR_ID)
            .fileCat(UPDATED_FILE_CAT);
        FilesDTO filesDTO = filesMapper.toDto(updatedFiles);

        restFilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
        Files testFiles = filesList.get(filesList.size() - 1);
        assertThat(testFiles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFiles.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testFiles.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFiles.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testFiles.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testFiles.getFileFor()).isEqualTo(UPDATED_FILE_FOR);
        assertThat(testFiles.getFileForId()).isEqualTo(UPDATED_FILE_FOR_ID);
        assertThat(testFiles.getFileCat()).isEqualTo(UPDATED_FILE_CAT);
    }

    @Test
    @Transactional
    void putNonExistingFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(longCount.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(longCount.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(longCount.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilesWithPatch() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        int databaseSizeBeforeUpdate = filesRepository.findAll().size();

        // Update the files using partial update
        Files partialUpdatedFiles = new Files();
        partialUpdatedFiles.setId(files.getId());

        partialUpdatedFiles.name(UPDATED_NAME).createdAt(UPDATED_CREATED_AT).fileForId(UPDATED_FILE_FOR_ID);

        restFilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiles))
            )
            .andExpect(status().isOk());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
        Files testFiles = filesList.get(filesList.size() - 1);
        assertThat(testFiles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFiles.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testFiles.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFiles.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testFiles.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testFiles.getFileFor()).isEqualTo(DEFAULT_FILE_FOR);
        assertThat(testFiles.getFileForId()).isEqualTo(UPDATED_FILE_FOR_ID);
        assertThat(testFiles.getFileCat()).isEqualTo(DEFAULT_FILE_CAT);
    }

    @Test
    @Transactional
    void fullUpdateFilesWithPatch() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        int databaseSizeBeforeUpdate = filesRepository.findAll().size();

        // Update the files using partial update
        Files partialUpdatedFiles = new Files();
        partialUpdatedFiles.setId(files.getId());

        partialUpdatedFiles
            .name(UPDATED_NAME)
            .fileUrl(UPDATED_FILE_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deleted(UPDATED_DELETED)
            .fileFor(UPDATED_FILE_FOR)
            .fileForId(UPDATED_FILE_FOR_ID)
            .fileCat(UPDATED_FILE_CAT);

        restFilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiles))
            )
            .andExpect(status().isOk());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
        Files testFiles = filesList.get(filesList.size() - 1);
        assertThat(testFiles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFiles.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testFiles.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFiles.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testFiles.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testFiles.getFileFor()).isEqualTo(UPDATED_FILE_FOR);
        assertThat(testFiles.getFileForId()).isEqualTo(UPDATED_FILE_FOR_ID);
        assertThat(testFiles.getFileCat()).isEqualTo(UPDATED_FILE_CAT);
    }

    @Test
    @Transactional
    void patchNonExistingFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(longCount.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(longCount.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(longCount.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFiles() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        int databaseSizeBeforeDelete = filesRepository.findAll().size();

        // Delete the files
        restFilesMockMvc
            .perform(delete(ENTITY_API_URL_ID, files.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
