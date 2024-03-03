package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.BannerImage;
import com.nullsafe.daily.repository.BannerImageRepository;
import com.nullsafe.daily.service.dto.BannerImageDTO;
import com.nullsafe.daily.service.mapper.BannerImageMapper;
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
 * Integration tests for the {@link BannerImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BannerImageResourceIT {

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IMAGE_TYPE = false;
    private static final Boolean UPDATED_IMAGE_TYPE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/banner-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BannerImageRepository bannerImageRepository;

    @Autowired
    private BannerImageMapper bannerImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBannerImageMockMvc;

    private BannerImage bannerImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BannerImage createEntity(EntityManager em) {
        BannerImage bannerImage = new BannerImage()
            .image(DEFAULT_IMAGE)
            .imageType(DEFAULT_IMAGE_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return bannerImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BannerImage createUpdatedEntity(EntityManager em) {
        BannerImage bannerImage = new BannerImage()
            .image(UPDATED_IMAGE)
            .imageType(UPDATED_IMAGE_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return bannerImage;
    }

    @BeforeEach
    public void initTest() {
        bannerImage = createEntity(em);
    }

    @Test
    @Transactional
    void createBannerImage() throws Exception {
        int databaseSizeBeforeCreate = bannerImageRepository.findAll().size();
        // Create the BannerImage
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);
        restBannerImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeCreate + 1);
        BannerImage testBannerImage = bannerImageList.get(bannerImageList.size() - 1);
        assertThat(testBannerImage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBannerImage.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testBannerImage.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testBannerImage.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createBannerImageWithExistingId() throws Exception {
        // Create the BannerImage with an existing ID
        bannerImage.setId(1L);
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        int databaseSizeBeforeCreate = bannerImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBannerImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = bannerImageRepository.findAll().size();
        // set the field null
        bannerImage.setImage(null);

        // Create the BannerImage, which fails.
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        restBannerImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isBadRequest());

        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bannerImageRepository.findAll().size();
        // set the field null
        bannerImage.setImageType(null);

        // Create the BannerImage, which fails.
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        restBannerImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isBadRequest());

        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBannerImages() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList
        restBannerImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bannerImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].imageType").value(hasItem(DEFAULT_IMAGE_TYPE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getBannerImage() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get the bannerImage
        restBannerImageMockMvc
            .perform(get(ENTITY_API_URL_ID, bannerImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bannerImage.getId().intValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.imageType").value(DEFAULT_IMAGE_TYPE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getBannerImagesByIdFiltering() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        Long id = bannerImage.getId();

        defaultBannerImageShouldBeFound("id.equals=" + id);
        defaultBannerImageShouldNotBeFound("id.notEquals=" + id);

        defaultBannerImageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBannerImageShouldNotBeFound("id.greaterThan=" + id);

        defaultBannerImageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBannerImageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBannerImagesByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where image equals to DEFAULT_IMAGE
        defaultBannerImageShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the bannerImageList where image equals to UPDATED_IMAGE
        defaultBannerImageShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllBannerImagesByImageIsInShouldWork() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultBannerImageShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the bannerImageList where image equals to UPDATED_IMAGE
        defaultBannerImageShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllBannerImagesByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where image is not null
        defaultBannerImageShouldBeFound("image.specified=true");

        // Get all the bannerImageList where image is null
        defaultBannerImageShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    void getAllBannerImagesByImageContainsSomething() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where image contains DEFAULT_IMAGE
        defaultBannerImageShouldBeFound("image.contains=" + DEFAULT_IMAGE);

        // Get all the bannerImageList where image contains UPDATED_IMAGE
        defaultBannerImageShouldNotBeFound("image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllBannerImagesByImageNotContainsSomething() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where image does not contain DEFAULT_IMAGE
        defaultBannerImageShouldNotBeFound("image.doesNotContain=" + DEFAULT_IMAGE);

        // Get all the bannerImageList where image does not contain UPDATED_IMAGE
        defaultBannerImageShouldBeFound("image.doesNotContain=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllBannerImagesByImageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where imageType equals to DEFAULT_IMAGE_TYPE
        defaultBannerImageShouldBeFound("imageType.equals=" + DEFAULT_IMAGE_TYPE);

        // Get all the bannerImageList where imageType equals to UPDATED_IMAGE_TYPE
        defaultBannerImageShouldNotBeFound("imageType.equals=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllBannerImagesByImageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where imageType in DEFAULT_IMAGE_TYPE or UPDATED_IMAGE_TYPE
        defaultBannerImageShouldBeFound("imageType.in=" + DEFAULT_IMAGE_TYPE + "," + UPDATED_IMAGE_TYPE);

        // Get all the bannerImageList where imageType equals to UPDATED_IMAGE_TYPE
        defaultBannerImageShouldNotBeFound("imageType.in=" + UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllBannerImagesByImageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where imageType is not null
        defaultBannerImageShouldBeFound("imageType.specified=true");

        // Get all the bannerImageList where imageType is null
        defaultBannerImageShouldNotBeFound("imageType.specified=false");
    }

    @Test
    @Transactional
    void getAllBannerImagesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where createdAt equals to DEFAULT_CREATED_AT
        defaultBannerImageShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the bannerImageList where createdAt equals to UPDATED_CREATED_AT
        defaultBannerImageShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBannerImagesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultBannerImageShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the bannerImageList where createdAt equals to UPDATED_CREATED_AT
        defaultBannerImageShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllBannerImagesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where createdAt is not null
        defaultBannerImageShouldBeFound("createdAt.specified=true");

        // Get all the bannerImageList where createdAt is null
        defaultBannerImageShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBannerImagesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultBannerImageShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the bannerImageList where updatedAt equals to UPDATED_UPDATED_AT
        defaultBannerImageShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBannerImagesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultBannerImageShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the bannerImageList where updatedAt equals to UPDATED_UPDATED_AT
        defaultBannerImageShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllBannerImagesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        // Get all the bannerImageList where updatedAt is not null
        defaultBannerImageShouldBeFound("updatedAt.specified=true");

        // Get all the bannerImageList where updatedAt is null
        defaultBannerImageShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBannerImageShouldBeFound(String filter) throws Exception {
        restBannerImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bannerImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].imageType").value(hasItem(DEFAULT_IMAGE_TYPE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restBannerImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBannerImageShouldNotBeFound(String filter) throws Exception {
        restBannerImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBannerImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBannerImage() throws Exception {
        // Get the bannerImage
        restBannerImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBannerImage() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();

        // Update the bannerImage
        BannerImage updatedBannerImage = bannerImageRepository.findById(bannerImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBannerImage are not directly saved in db
        em.detach(updatedBannerImage);
        updatedBannerImage.image(UPDATED_IMAGE).imageType(UPDATED_IMAGE_TYPE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(updatedBannerImage);

        restBannerImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bannerImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
        BannerImage testBannerImage = bannerImageList.get(bannerImageList.size() - 1);
        assertThat(testBannerImage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBannerImage.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testBannerImage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBannerImage.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingBannerImage() throws Exception {
        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();
        bannerImage.setId(longCount.incrementAndGet());

        // Create the BannerImage
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBannerImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bannerImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBannerImage() throws Exception {
        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();
        bannerImage.setId(longCount.incrementAndGet());

        // Create the BannerImage
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannerImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBannerImage() throws Exception {
        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();
        bannerImage.setId(longCount.incrementAndGet());

        // Create the BannerImage
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannerImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bannerImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBannerImageWithPatch() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();

        // Update the bannerImage using partial update
        BannerImage partialUpdatedBannerImage = new BannerImage();
        partialUpdatedBannerImage.setId(bannerImage.getId());

        partialUpdatedBannerImage.imageType(UPDATED_IMAGE_TYPE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restBannerImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBannerImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBannerImage))
            )
            .andExpect(status().isOk());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
        BannerImage testBannerImage = bannerImageList.get(bannerImageList.size() - 1);
        assertThat(testBannerImage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBannerImage.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testBannerImage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBannerImage.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateBannerImageWithPatch() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();

        // Update the bannerImage using partial update
        BannerImage partialUpdatedBannerImage = new BannerImage();
        partialUpdatedBannerImage.setId(bannerImage.getId());

        partialUpdatedBannerImage
            .image(UPDATED_IMAGE)
            .imageType(UPDATED_IMAGE_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBannerImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBannerImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBannerImage))
            )
            .andExpect(status().isOk());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
        BannerImage testBannerImage = bannerImageList.get(bannerImageList.size() - 1);
        assertThat(testBannerImage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBannerImage.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testBannerImage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBannerImage.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingBannerImage() throws Exception {
        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();
        bannerImage.setId(longCount.incrementAndGet());

        // Create the BannerImage
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBannerImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bannerImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBannerImage() throws Exception {
        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();
        bannerImage.setId(longCount.incrementAndGet());

        // Create the BannerImage
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannerImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBannerImage() throws Exception {
        int databaseSizeBeforeUpdate = bannerImageRepository.findAll().size();
        bannerImage.setId(longCount.incrementAndGet());

        // Create the BannerImage
        BannerImageDTO bannerImageDTO = bannerImageMapper.toDto(bannerImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBannerImageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bannerImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BannerImage in the database
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBannerImage() throws Exception {
        // Initialize the database
        bannerImageRepository.saveAndFlush(bannerImage);

        int databaseSizeBeforeDelete = bannerImageRepository.findAll().size();

        // Delete the bannerImage
        restBannerImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, bannerImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BannerImage> bannerImageList = bannerImageRepository.findAll();
        assertThat(bannerImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
