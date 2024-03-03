package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.SocialMedia;
import com.nullsafe.daily.repository.SocialMediaRepository;
import com.nullsafe.daily.service.dto.SocialMediaDTO;
import com.nullsafe.daily.service.mapper.SocialMediaMapper;
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
 * Integration tests for the {@link SocialMediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocialMediaResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/social-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    @Autowired
    private SocialMediaMapper socialMediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialMediaMockMvc;

    private SocialMedia socialMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialMedia createEntity(EntityManager em) {
        SocialMedia socialMedia = new SocialMedia()
            .title(DEFAULT_TITLE)
            .image(DEFAULT_IMAGE)
            .url(DEFAULT_URL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return socialMedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialMedia createUpdatedEntity(EntityManager em) {
        SocialMedia socialMedia = new SocialMedia()
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return socialMedia;
    }

    @BeforeEach
    public void initTest() {
        socialMedia = createEntity(em);
    }

    @Test
    @Transactional
    void createSocialMedia() throws Exception {
        int databaseSizeBeforeCreate = socialMediaRepository.findAll().size();
        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);
        restSocialMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeCreate + 1);
        SocialMedia testSocialMedia = socialMediaList.get(socialMediaList.size() - 1);
        assertThat(testSocialMedia.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSocialMedia.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSocialMedia.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSocialMedia.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSocialMedia.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSocialMediaWithExistingId() throws Exception {
        // Create the SocialMedia with an existing ID
        socialMedia.setId(1L);
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        int databaseSizeBeforeCreate = socialMediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialMediaRepository.findAll().size();
        // set the field null
        socialMedia.setTitle(null);

        // Create the SocialMedia, which fails.
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        restSocialMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialMediaRepository.findAll().size();
        // set the field null
        socialMedia.setImage(null);

        // Create the SocialMedia, which fails.
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        restSocialMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialMediaRepository.findAll().size();
        // set the field null
        socialMedia.setUrl(null);

        // Create the SocialMedia, which fails.
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        restSocialMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocialMedias() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList
        restSocialMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getSocialMedia() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get the socialMedia
        restSocialMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, socialMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialMedia.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getSocialMediasByIdFiltering() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        Long id = socialMedia.getId();

        defaultSocialMediaShouldBeFound("id.equals=" + id);
        defaultSocialMediaShouldNotBeFound("id.notEquals=" + id);

        defaultSocialMediaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSocialMediaShouldNotBeFound("id.greaterThan=" + id);

        defaultSocialMediaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSocialMediaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSocialMediasByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where title equals to DEFAULT_TITLE
        defaultSocialMediaShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the socialMediaList where title equals to UPDATED_TITLE
        defaultSocialMediaShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSocialMediasByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSocialMediaShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the socialMediaList where title equals to UPDATED_TITLE
        defaultSocialMediaShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSocialMediasByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where title is not null
        defaultSocialMediaShouldBeFound("title.specified=true");

        // Get all the socialMediaList where title is null
        defaultSocialMediaShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialMediasByTitleContainsSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where title contains DEFAULT_TITLE
        defaultSocialMediaShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the socialMediaList where title contains UPDATED_TITLE
        defaultSocialMediaShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSocialMediasByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where title does not contain DEFAULT_TITLE
        defaultSocialMediaShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the socialMediaList where title does not contain UPDATED_TITLE
        defaultSocialMediaShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSocialMediasByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where image equals to DEFAULT_IMAGE
        defaultSocialMediaShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the socialMediaList where image equals to UPDATED_IMAGE
        defaultSocialMediaShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllSocialMediasByImageIsInShouldWork() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultSocialMediaShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the socialMediaList where image equals to UPDATED_IMAGE
        defaultSocialMediaShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllSocialMediasByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where image is not null
        defaultSocialMediaShouldBeFound("image.specified=true");

        // Get all the socialMediaList where image is null
        defaultSocialMediaShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialMediasByImageContainsSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where image contains DEFAULT_IMAGE
        defaultSocialMediaShouldBeFound("image.contains=" + DEFAULT_IMAGE);

        // Get all the socialMediaList where image contains UPDATED_IMAGE
        defaultSocialMediaShouldNotBeFound("image.contains=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllSocialMediasByImageNotContainsSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where image does not contain DEFAULT_IMAGE
        defaultSocialMediaShouldNotBeFound("image.doesNotContain=" + DEFAULT_IMAGE);

        // Get all the socialMediaList where image does not contain UPDATED_IMAGE
        defaultSocialMediaShouldBeFound("image.doesNotContain=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void getAllSocialMediasByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where url equals to DEFAULT_URL
        defaultSocialMediaShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the socialMediaList where url equals to UPDATED_URL
        defaultSocialMediaShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllSocialMediasByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where url in DEFAULT_URL or UPDATED_URL
        defaultSocialMediaShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the socialMediaList where url equals to UPDATED_URL
        defaultSocialMediaShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllSocialMediasByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where url is not null
        defaultSocialMediaShouldBeFound("url.specified=true");

        // Get all the socialMediaList where url is null
        defaultSocialMediaShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialMediasByUrlContainsSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where url contains DEFAULT_URL
        defaultSocialMediaShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the socialMediaList where url contains UPDATED_URL
        defaultSocialMediaShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllSocialMediasByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where url does not contain DEFAULT_URL
        defaultSocialMediaShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the socialMediaList where url does not contain UPDATED_URL
        defaultSocialMediaShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllSocialMediasByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where createdAt equals to DEFAULT_CREATED_AT
        defaultSocialMediaShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the socialMediaList where createdAt equals to UPDATED_CREATED_AT
        defaultSocialMediaShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSocialMediasByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSocialMediaShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the socialMediaList where createdAt equals to UPDATED_CREATED_AT
        defaultSocialMediaShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSocialMediasByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where createdAt is not null
        defaultSocialMediaShouldBeFound("createdAt.specified=true");

        // Get all the socialMediaList where createdAt is null
        defaultSocialMediaShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialMediasByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSocialMediaShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the socialMediaList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSocialMediaShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSocialMediasByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSocialMediaShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the socialMediaList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSocialMediaShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSocialMediasByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        // Get all the socialMediaList where updatedAt is not null
        defaultSocialMediaShouldBeFound("updatedAt.specified=true");

        // Get all the socialMediaList where updatedAt is null
        defaultSocialMediaShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSocialMediaShouldBeFound(String filter) throws Exception {
        restSocialMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restSocialMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSocialMediaShouldNotBeFound(String filter) throws Exception {
        restSocialMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSocialMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSocialMedia() throws Exception {
        // Get the socialMedia
        restSocialMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSocialMedia() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();

        // Update the socialMedia
        SocialMedia updatedSocialMedia = socialMediaRepository.findById(socialMedia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSocialMedia are not directly saved in db
        em.detach(updatedSocialMedia);
        updatedSocialMedia
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(updatedSocialMedia);

        restSocialMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
        SocialMedia testSocialMedia = socialMediaList.get(socialMediaList.size() - 1);
        assertThat(testSocialMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSocialMedia.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSocialMedia.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSocialMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSocialMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSocialMedia() throws Exception {
        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialMedia() throws Exception {
        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialMedia() throws Exception {
        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialMediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialMediaWithPatch() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();

        // Update the socialMedia using partial update
        SocialMedia partialUpdatedSocialMedia = new SocialMedia();
        partialUpdatedSocialMedia.setId(socialMedia.getId());

        partialUpdatedSocialMedia.image(UPDATED_IMAGE).url(UPDATED_URL).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restSocialMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialMedia))
            )
            .andExpect(status().isOk());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
        SocialMedia testSocialMedia = socialMediaList.get(socialMediaList.size() - 1);
        assertThat(testSocialMedia.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSocialMedia.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSocialMedia.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSocialMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSocialMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSocialMediaWithPatch() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();

        // Update the socialMedia using partial update
        SocialMedia partialUpdatedSocialMedia = new SocialMedia();
        partialUpdatedSocialMedia.setId(socialMedia.getId());

        partialUpdatedSocialMedia
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .url(UPDATED_URL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSocialMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialMedia))
            )
            .andExpect(status().isOk());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
        SocialMedia testSocialMedia = socialMediaList.get(socialMediaList.size() - 1);
        assertThat(testSocialMedia.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSocialMedia.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSocialMedia.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSocialMedia.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSocialMedia.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSocialMedia() throws Exception {
        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialMedia() throws Exception {
        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialMedia() throws Exception {
        int databaseSizeBeforeUpdate = socialMediaRepository.findAll().size();
        socialMedia.setId(longCount.incrementAndGet());

        // Create the SocialMedia
        SocialMediaDTO socialMediaDTO = socialMediaMapper.toDto(socialMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialMediaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(socialMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialMedia in the database
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialMedia() throws Exception {
        // Initialize the database
        socialMediaRepository.saveAndFlush(socialMedia);

        int databaseSizeBeforeDelete = socialMediaRepository.findAll().size();

        // Delete the socialMedia
        restSocialMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        assertThat(socialMediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
