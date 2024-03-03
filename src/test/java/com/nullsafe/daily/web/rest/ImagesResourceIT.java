package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Images;
import com.nullsafe.daily.repository.ImagesRepository;
import com.nullsafe.daily.service.dto.ImagesDTO;
import com.nullsafe.daily.service.mapper.ImagesMapper;
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
 * Integration tests for the {@link ImagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImagesResourceIT {

    private static final String DEFAULT_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TABLE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TABLE_ID = 1L;
    private static final Long UPDATED_TABLE_ID = 2L;
    private static final Long SMALLER_TABLE_ID = 1L - 1L;

    private static final Boolean DEFAULT_IMAGE_TYPE = false;
    private static final Boolean UPDATED_IMAGE_TYPE = true;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private ImagesMapper imagesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImagesMockMvc;

    private Images images;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Images createEntity(EntityManager em) {
        Images images = new Images()
            .tableName(DEFAULT_TABLE_NAME)
            .tableId(DEFAULT_TABLE_ID)
            .imageType(DEFAULT_IMAGE_TYPE)
            .image(DEFAULT_IMAGE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return images;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Images createUpdatedEntity(EntityManager em) {
        Images images = new Images()
            .tableName(UPDATED_TABLE_NAME)
            .tableId(UPDATED_TABLE_ID)
            .imageType(UPDATED_IMAGE_TYPE)
            .image(UPDATED_IMAGE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return images;
    }

    @BeforeEach
    public void initTest() {
        images = createEntity(em);
    }

    @Test
    @Transactional
    void createImages() throws Exception {
        int databaseSizeBeforeCreate = imagesRepository.findAll().size();
        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);
        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isCreated());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeCreate + 1);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testImages.getTableId()).isEqualTo(DEFAULT_TABLE_ID);
        assertThat(testImages.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testImages.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testImages.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testImages.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createImagesWithExistingId() throws Exception {
        // Create the Images with an existing ID
        images.setId(1L);
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        int databaseSizeBeforeCreate = imagesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTableNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        // set the field null
        images.setTableName(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTableIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        // set the field null
        images.setTableId(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        // set the field null
        images.setImageType(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        // set the field null
        images.setImage(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(images.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].tableId").value(hasItem(DEFAULT_TABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].imageType").value(hasItem(DEFAULT_IMAGE_TYPE.booleanValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get the images
        restImagesMockMvc
            .perform(get(ENTITY_API_URL_ID, images.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(images.getId().intValue()))
            .andExpect(jsonPath("$.tableName").value(DEFAULT_TABLE_NAME))
            .andExpect(jsonPath("$.tableId").value(DEFAULT_TABLE_ID.intValue()))
            .andExpect(jsonPath("$.imageType").value(DEFAULT_IMAGE_TYPE.booleanValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getImagesByIdFiltering() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        Long id = images.getId();

        defaultImagesShouldBeFound("id.equals=" + id);
        defaultImagesShouldNotBeFound("id.notEquals=" + id);

        defaultImagesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImagesShouldNotBeFound("id.greaterThan=" + id);

        defaultImagesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImagesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagesByTableNameIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableName equals to DEFAULT_TABLE_NAME
        defaultImagesShouldBeFound("tableName.equals=" + DEFAULT_TABLE_NAME);

        // Get all the imagesList where tableName equals to UPDATED_TABLE_NAME
        defaultImagesShouldNotBeFound("tableName.equals=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByTableNameIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableName in DEFAULT_TABLE_NAME or UPDATED_TABLE_NAME
        defaultImagesShouldBeFound("tableName.in=" + DEFAULT_TABLE_NAME + "," + UPDATED_TABLE_NAME);

        // Get all the imagesList where tableName equals to UPDATED_TABLE_NAME
        defaultImagesShouldNotBeFound("tableName.in=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByTableNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableName is not null
        defaultImagesShouldBeFound("tableName.specified=true");

        // Get all the imagesList where tableName is null
        defaultImagesShouldNotBeFound("tableName.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByTableNameContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableName contains DEFAULT_TABLE_NAME
        defaultImagesShouldBeFound("tableName.contains=" + DEFAULT_TABLE_NAME);

        // Get all the imagesList where tableName contains UPDATED_TABLE_NAME
        defaultImagesShouldNotBeFound("tableName.contains=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByTableNameNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableName does not contain DEFAULT_TABLE_NAME
        defaultImagesShouldNotBeFound("tableName.doesNotContain=" + DEFAULT_TABLE_NAME);

        // Get all the imagesList where tableName does not contain UPDATED_TABLE_NAME
        defaultImagesShouldBeFound("tableName.doesNotContain=" + UPDATED_TABLE_NAME);
    }

    @Test
    @Transactional
    void getAllImagesByTableIdIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableId equals to DEFAULT_TABLE_ID
        defaultImagesShouldBeFound("tableId.equals=" + DEFAULT_TABLE_ID);

        // Get all the imagesList where tableId equals to UPDATED_TABLE_ID
        defaultImagesShouldNotBeFound("tableId.equals=" + UPDATED_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllImagesByTableIdIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableId in DEFAULT_TABLE_ID or UPDATED_TABLE_ID
        defaultImagesShouldBeFound("tableId.in=" + DEFAULT_TABLE_ID + "," + UPDATED_TABLE_ID);

        // Get all the imagesList where tableId equals to UPDATED_TABLE_ID
        defaultImagesShouldNotBeFound("tableId.in=" + UPDATED_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllImagesByTableIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableId is not null
        defaultImagesShouldBeFound("tableId.specified=true");

        // Get all the imagesList where tableId is null
        defaultImagesShouldNotBeFound("tableId.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByTableIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableId is greater than or equal to DEFAULT_TABLE_ID
        defaultImagesShouldBeFound("tableId.greaterThanOrEqual=" + DEFAULT_TABLE_ID);

        // Get all the imagesList where tableId is greater than or equal to UPDATED_TABLE_ID
        defaultImagesShouldNotBeFound("tableId.greaterThanOrEqual=" + UPDATED_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllImagesByTableIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableId is less than or equal to DEFAULT_TABLE_ID
        defaultImagesShouldBeFound("tableId.lessThanOrEqual=" + DEFAULT_TABLE_ID);

        // Get all the imagesList where tableId is less than or equal to SMALLER_TABLE_ID
        defaultImagesShouldNotBeFound("tableId.lessThanOrEqual=" + SMALLER_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllImagesByTableIdIsLessThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableId is less than DEFAULT_TABLE_ID
        defaultImagesShouldNotBeFound("tableId.lessThan=" + DEFAULT_TABLE_ID);

        // Get all the imagesList where tableId is less than UPDATED_TABLE_ID
        defaultImagesShouldBeFound("tableId.lessThan=" + UPDATED_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllImagesByTableIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where tableId is greater than DEFAULT_TABLE_ID
        defaultImagesShouldNotBeFound("tableId.greaterThan=" + DEFAULT_TABLE_ID);

        // Get all the imagesList where tableId is greater than SMALLER_TABLE_ID
        defaultImagesShouldBeFound("tableId.greaterThan=" + SMALLER_TABLE_ID);
    }

    @Test
    @Transactional
    void getAllImagesByImageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageType equals to DEFAULT_IMAGE_TYPE
        defaultImagesShouldBeFound("imageType.equals=" + DEFAULT_IMAGE_TYPE);

        // Get all the imagesList where imageType equals to UPDATED_IMAGE_TYPE
        defaultImagesShouldNotBeFound("imageType.equals=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllImagesByImageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageType in DEFAULT_IMAGE_TYPE or UPDATED_IMAGE_TYPE
        defaultImagesShouldBeFound("imageType.in=" + DEFAULT_IMAGE_TYPE + "," + UPDATED_IMAGE_TYPE);

        // Get all the imagesList where imageType equals to UPDATED_IMAGE_TYPE
        defaultImagesShouldNotBeFound("imageType.in=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllImagesByImageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where imageType is not null
        defaultImagesShouldBeFound("imageType.specified=true");

        // Get all the imagesList where imageType is null
        defaultImagesShouldNotBeFound("imageType.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where image equals to DEFAULT_IMAGE
        defaultImagesShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the imagesList where image equals to UPDATED_IMAGE
        defaultImagesShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllImagesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultImagesShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the imagesList where image equals to UPDATED_IMAGE
        defaultImagesShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllImagesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where image is not null
        defaultImagesShouldBeFound("image.specified=true");

        // Get all the imagesList where image is null
        defaultImagesShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByImageContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where image contains DEFAULT_IMAGE
        defaultImagesShouldBeFound("image.contains=" + DEFAULT_IMAGE);

        // Get all the imagesList where image contains UPDATED_IMAGE
        defaultImagesShouldNotBeFound("image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllImagesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where image does not contain DEFAULT_IMAGE
        defaultImagesShouldNotBeFound("image.doesNotContain=" + DEFAULT_IMAGE);

        // Get all the imagesList where image does not contain UPDATED_IMAGE
        defaultImagesShouldBeFound("image.doesNotContain=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllImagesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where createdAt equals to DEFAULT_CREATED_AT
        defaultImagesShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the imagesList where createdAt equals to UPDATED_CREATED_AT
        defaultImagesShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllImagesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultImagesShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the imagesList where createdAt equals to UPDATED_CREATED_AT
        defaultImagesShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllImagesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where createdAt is not null
        defaultImagesShouldBeFound("createdAt.specified=true");

        // Get all the imagesList where createdAt is null
        defaultImagesShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultImagesShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the imagesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultImagesShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllImagesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultImagesShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the imagesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultImagesShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllImagesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where updatedAt is not null
        defaultImagesShouldBeFound("updatedAt.specified=true");

        // Get all the imagesList where updatedAt is null
        defaultImagesShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImagesShouldBeFound(String filter) throws Exception {
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(images.getId().intValue())))
            .andExpect(jsonPath("$.[*].tableName").value(hasItem(DEFAULT_TABLE_NAME)))
            .andExpect(jsonPath("$.[*].tableId").value(hasItem(DEFAULT_TABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].imageType").value(hasItem(DEFAULT_IMAGE_TYPE.booleanValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImagesShouldNotBeFound(String filter) throws Exception {
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImages() throws Exception {
        // Get the images
        restImagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();

        // Update the images
        Images updatedImages = imagesRepository.findById(images.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImages are not directly saved in db
        em.detach(updatedImages);
        updatedImages
            .tableName(UPDATED_TABLE_NAME)
            .tableId(UPDATED_TABLE_ID)
            .imageType(UPDATED_IMAGE_TYPE)
            .image(UPDATED_IMAGE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ImagesDTO imagesDTO = imagesMapper.toDto(updatedImages);

        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testImages.getTableId()).isEqualTo(UPDATED_TABLE_ID);
        assertThat(testImages.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testImages.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImages.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testImages.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImagesWithPatch() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();

        // Update the images using partial update
        Images partialUpdatedImages = new Images();
        partialUpdatedImages.setId(images.getId());

        partialUpdatedImages.image(UPDATED_IMAGE).updatedAt(UPDATED_UPDATED_AT);

        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getTableName()).isEqualTo(DEFAULT_TABLE_NAME);
        assertThat(testImages.getTableId()).isEqualTo(DEFAULT_TABLE_ID);
        assertThat(testImages.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testImages.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImages.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testImages.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateImagesWithPatch() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();

        // Update the images using partial update
        Images partialUpdatedImages = new Images();
        partialUpdatedImages.setId(images.getId());

        partialUpdatedImages
            .tableName(UPDATED_TABLE_NAME)
            .tableId(UPDATED_TABLE_ID)
            .imageType(UPDATED_IMAGE_TYPE)
            .image(UPDATED_IMAGE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getTableName()).isEqualTo(UPDATED_TABLE_NAME);
        assertThat(testImages.getTableId()).isEqualTo(UPDATED_TABLE_ID);
        assertThat(testImages.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testImages.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImages.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testImages.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imagesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeDelete = imagesRepository.findAll().size();

        // Delete the images
        restImagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, images.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
