package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.AppSettings;
import com.nullsafe.daily.repository.AppSettingsRepository;
import com.nullsafe.daily.service.dto.AppSettingsDTO;
import com.nullsafe.daily.service.mapper.AppSettingsMapper;
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
 * Integration tests for the {@link AppSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppSettingsResourceIT {

    private static final Integer DEFAULT_SETTING_ID = 1;
    private static final Integer UPDATED_SETTING_ID = 2;
    private static final Integer SMALLER_SETTING_ID = 1 - 1;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/app-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    @Autowired
    private AppSettingsMapper appSettingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppSettingsMockMvc;

    private AppSettings appSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppSettings createEntity(EntityManager em) {
        AppSettings appSettings = new AppSettings()
            .settingId(DEFAULT_SETTING_ID)
            .title(DEFAULT_TITLE)
            .value(DEFAULT_VALUE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return appSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppSettings createUpdatedEntity(EntityManager em) {
        AppSettings appSettings = new AppSettings()
            .settingId(UPDATED_SETTING_ID)
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return appSettings;
    }

    @BeforeEach
    public void initTest() {
        appSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createAppSettings() throws Exception {
        int databaseSizeBeforeCreate = appSettingsRepository.findAll().size();
        // Create the AppSettings
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);
        restAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        AppSettings testAppSettings = appSettingsList.get(appSettingsList.size() - 1);
        assertThat(testAppSettings.getSettingId()).isEqualTo(DEFAULT_SETTING_ID);
        assertThat(testAppSettings.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAppSettings.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testAppSettings.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAppSettings.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createAppSettingsWithExistingId() throws Exception {
        // Create the AppSettings with an existing ID
        appSettings.setId(1L);
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        int databaseSizeBeforeCreate = appSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSettingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = appSettingsRepository.findAll().size();
        // set the field null
        appSettings.setSettingId(null);

        // Create the AppSettings, which fails.
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        restAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = appSettingsRepository.findAll().size();
        // set the field null
        appSettings.setTitle(null);

        // Create the AppSettings, which fails.
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        restAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = appSettingsRepository.findAll().size();
        // set the field null
        appSettings.setValue(null);

        // Create the AppSettings, which fails.
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        restAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppSettings() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList
        restAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].settingId").value(hasItem(DEFAULT_SETTING_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAppSettings() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get the appSettings
        restAppSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, appSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appSettings.getId().intValue()))
            .andExpect(jsonPath("$.settingId").value(DEFAULT_SETTING_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getAppSettingsByIdFiltering() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        Long id = appSettings.getId();

        defaultAppSettingsShouldBeFound("id.equals=" + id);
        defaultAppSettingsShouldNotBeFound("id.notEquals=" + id);

        defaultAppSettingsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppSettingsShouldNotBeFound("id.greaterThan=" + id);

        defaultAppSettingsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppSettingsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppSettingsBySettingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where settingId equals to DEFAULT_SETTING_ID
        defaultAppSettingsShouldBeFound("settingId.equals=" + DEFAULT_SETTING_ID);

        // Get all the appSettingsList where settingId equals to UPDATED_SETTING_ID
        defaultAppSettingsShouldNotBeFound("settingId.equals=" + UPDATED_SETTING_ID);
    }

    @Test
    @Transactional
    void getAllAppSettingsBySettingIdIsInShouldWork() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where settingId in DEFAULT_SETTING_ID or UPDATED_SETTING_ID
        defaultAppSettingsShouldBeFound("settingId.in=" + DEFAULT_SETTING_ID + "," + UPDATED_SETTING_ID);

        // Get all the appSettingsList where settingId equals to UPDATED_SETTING_ID
        defaultAppSettingsShouldNotBeFound("settingId.in=" + UPDATED_SETTING_ID);
    }

    @Test
    @Transactional
    void getAllAppSettingsBySettingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where settingId is not null
        defaultAppSettingsShouldBeFound("settingId.specified=true");

        // Get all the appSettingsList where settingId is null
        defaultAppSettingsShouldNotBeFound("settingId.specified=false");
    }

    @Test
    @Transactional
    void getAllAppSettingsBySettingIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where settingId is greater than or equal to DEFAULT_SETTING_ID
        defaultAppSettingsShouldBeFound("settingId.greaterThanOrEqual=" + DEFAULT_SETTING_ID);

        // Get all the appSettingsList where settingId is greater than or equal to UPDATED_SETTING_ID
        defaultAppSettingsShouldNotBeFound("settingId.greaterThanOrEqual=" + UPDATED_SETTING_ID);
    }

    @Test
    @Transactional
    void getAllAppSettingsBySettingIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where settingId is less than or equal to DEFAULT_SETTING_ID
        defaultAppSettingsShouldBeFound("settingId.lessThanOrEqual=" + DEFAULT_SETTING_ID);

        // Get all the appSettingsList where settingId is less than or equal to SMALLER_SETTING_ID
        defaultAppSettingsShouldNotBeFound("settingId.lessThanOrEqual=" + SMALLER_SETTING_ID);
    }

    @Test
    @Transactional
    void getAllAppSettingsBySettingIdIsLessThanSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where settingId is less than DEFAULT_SETTING_ID
        defaultAppSettingsShouldNotBeFound("settingId.lessThan=" + DEFAULT_SETTING_ID);

        // Get all the appSettingsList where settingId is less than UPDATED_SETTING_ID
        defaultAppSettingsShouldBeFound("settingId.lessThan=" + UPDATED_SETTING_ID);
    }

    @Test
    @Transactional
    void getAllAppSettingsBySettingIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where settingId is greater than DEFAULT_SETTING_ID
        defaultAppSettingsShouldNotBeFound("settingId.greaterThan=" + DEFAULT_SETTING_ID);

        // Get all the appSettingsList where settingId is greater than SMALLER_SETTING_ID
        defaultAppSettingsShouldBeFound("settingId.greaterThan=" + SMALLER_SETTING_ID);
    }

    @Test
    @Transactional
    void getAllAppSettingsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where title equals to DEFAULT_TITLE
        defaultAppSettingsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the appSettingsList where title equals to UPDATED_TITLE
        defaultAppSettingsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAppSettingsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultAppSettingsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the appSettingsList where title equals to UPDATED_TITLE
        defaultAppSettingsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAppSettingsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where title is not null
        defaultAppSettingsShouldBeFound("title.specified=true");

        // Get all the appSettingsList where title is null
        defaultAppSettingsShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllAppSettingsByTitleContainsSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where title contains DEFAULT_TITLE
        defaultAppSettingsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the appSettingsList where title contains UPDATED_TITLE
        defaultAppSettingsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAppSettingsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where title does not contain DEFAULT_TITLE
        defaultAppSettingsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the appSettingsList where title does not contain UPDATED_TITLE
        defaultAppSettingsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAppSettingsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where value equals to DEFAULT_VALUE
        defaultAppSettingsShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the appSettingsList where value equals to UPDATED_VALUE
        defaultAppSettingsShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAppSettingsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultAppSettingsShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the appSettingsList where value equals to UPDATED_VALUE
        defaultAppSettingsShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAppSettingsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where value is not null
        defaultAppSettingsShouldBeFound("value.specified=true");

        // Get all the appSettingsList where value is null
        defaultAppSettingsShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllAppSettingsByValueContainsSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where value contains DEFAULT_VALUE
        defaultAppSettingsShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the appSettingsList where value contains UPDATED_VALUE
        defaultAppSettingsShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAppSettingsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where value does not contain DEFAULT_VALUE
        defaultAppSettingsShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the appSettingsList where value does not contain UPDATED_VALUE
        defaultAppSettingsShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllAppSettingsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where createdAt equals to DEFAULT_CREATED_AT
        defaultAppSettingsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the appSettingsList where createdAt equals to UPDATED_CREATED_AT
        defaultAppSettingsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAppSettingsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAppSettingsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the appSettingsList where createdAt equals to UPDATED_CREATED_AT
        defaultAppSettingsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAppSettingsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where createdAt is not null
        defaultAppSettingsShouldBeFound("createdAt.specified=true");

        // Get all the appSettingsList where createdAt is null
        defaultAppSettingsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAppSettingsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultAppSettingsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the appSettingsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAppSettingsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAppSettingsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultAppSettingsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the appSettingsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAppSettingsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAppSettingsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        // Get all the appSettingsList where updatedAt is not null
        defaultAppSettingsShouldBeFound("updatedAt.specified=true");

        // Get all the appSettingsList where updatedAt is null
        defaultAppSettingsShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppSettingsShouldBeFound(String filter) throws Exception {
        restAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].settingId").value(hasItem(DEFAULT_SETTING_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppSettingsShouldNotBeFound(String filter) throws Exception {
        restAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppSettings() throws Exception {
        // Get the appSettings
        restAppSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppSettings() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();

        // Update the appSettings
        AppSettings updatedAppSettings = appSettingsRepository.findById(appSettings.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppSettings are not directly saved in db
        em.detach(updatedAppSettings);
        updatedAppSettings
            .settingId(UPDATED_SETTING_ID)
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(updatedAppSettings);

        restAppSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppSettings testAppSettings = appSettingsList.get(appSettingsList.size() - 1);
        assertThat(testAppSettings.getSettingId()).isEqualTo(UPDATED_SETTING_ID);
        assertThat(testAppSettings.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAppSettings.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testAppSettings.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAppSettings.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();
        appSettings.setId(longCount.incrementAndGet());

        // Create the AppSettings
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();
        appSettings.setId(longCount.incrementAndGet());

        // Create the AppSettings
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();
        appSettings.setId(longCount.incrementAndGet());

        // Create the AppSettings
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppSettingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appSettingsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppSettingsWithPatch() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();

        // Update the appSettings using partial update
        AppSettings partialUpdatedAppSettings = new AppSettings();
        partialUpdatedAppSettings.setId(appSettings.getId());

        partialUpdatedAppSettings
            .settingId(UPDATED_SETTING_ID)
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppSettings))
            )
            .andExpect(status().isOk());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppSettings testAppSettings = appSettingsList.get(appSettingsList.size() - 1);
        assertThat(testAppSettings.getSettingId()).isEqualTo(UPDATED_SETTING_ID);
        assertThat(testAppSettings.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAppSettings.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testAppSettings.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAppSettings.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAppSettingsWithPatch() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();

        // Update the appSettings using partial update
        AppSettings partialUpdatedAppSettings = new AppSettings();
        partialUpdatedAppSettings.setId(appSettings.getId());

        partialUpdatedAppSettings
            .settingId(UPDATED_SETTING_ID)
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppSettings))
            )
            .andExpect(status().isOk());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppSettings testAppSettings = appSettingsList.get(appSettingsList.size() - 1);
        assertThat(testAppSettings.getSettingId()).isEqualTo(UPDATED_SETTING_ID);
        assertThat(testAppSettings.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAppSettings.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testAppSettings.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAppSettings.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();
        appSettings.setId(longCount.incrementAndGet());

        // Create the AppSettings
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appSettingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();
        appSettings.setId(longCount.incrementAndGet());

        // Create the AppSettings
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = appSettingsRepository.findAll().size();
        appSettings.setId(longCount.incrementAndGet());

        // Create the AppSettings
        AppSettingsDTO appSettingsDTO = appSettingsMapper.toDto(appSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppSettings in the database
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppSettings() throws Exception {
        // Initialize the database
        appSettingsRepository.saveAndFlush(appSettings);

        int databaseSizeBeforeDelete = appSettingsRepository.findAll().size();

        // Delete the appSettings
        restAppSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, appSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppSettings> appSettingsList = appSettingsRepository.findAll();
        assertThat(appSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
