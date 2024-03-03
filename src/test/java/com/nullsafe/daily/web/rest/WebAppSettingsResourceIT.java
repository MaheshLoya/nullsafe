package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.WebAppSettings;
import com.nullsafe.daily.repository.WebAppSettingsRepository;
import com.nullsafe.daily.service.dto.WebAppSettingsDTO;
import com.nullsafe.daily.service.mapper.WebAppSettingsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WebAppSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WebAppSettingsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/web-app-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private WebAppSettingsRepository webAppSettingsRepository;

    @Autowired
    private WebAppSettingsMapper webAppSettingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWebAppSettingsMockMvc;

    private WebAppSettings webAppSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebAppSettings createEntity(EntityManager em) {
        WebAppSettings webAppSettings = new WebAppSettings()
            .title(DEFAULT_TITLE)
            .value(DEFAULT_VALUE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return webAppSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebAppSettings createUpdatedEntity(EntityManager em) {
        WebAppSettings webAppSettings = new WebAppSettings()
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return webAppSettings;
    }

    @BeforeEach
    public void initTest() {
        webAppSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createWebAppSettings() throws Exception {
        int databaseSizeBeforeCreate = webAppSettingsRepository.findAll().size();
        // Create the WebAppSettings
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);
        restWebAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        WebAppSettings testWebAppSettings = webAppSettingsList.get(webAppSettingsList.size() - 1);
        assertThat(testWebAppSettings.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWebAppSettings.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testWebAppSettings.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testWebAppSettings.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createWebAppSettingsWithExistingId() throws Exception {
        // Create the WebAppSettings with an existing ID
        webAppSettings.setId(1);
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        int databaseSizeBeforeCreate = webAppSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = webAppSettingsRepository.findAll().size();
        // set the field null
        webAppSettings.setTitle(null);

        // Create the WebAppSettings, which fails.
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        restWebAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = webAppSettingsRepository.findAll().size();
        // set the field null
        webAppSettings.setValue(null);

        // Create the WebAppSettings, which fails.
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        restWebAppSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWebAppSettings() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList
        restWebAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webAppSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getWebAppSettings() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get the webAppSettings
        restWebAppSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, webAppSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(webAppSettings.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getWebAppSettingsByIdFiltering() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        Integer id = webAppSettings.getId();

        defaultWebAppSettingsShouldBeFound("id.equals=" + id);
        defaultWebAppSettingsShouldNotBeFound("id.notEquals=" + id);

        defaultWebAppSettingsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWebAppSettingsShouldNotBeFound("id.greaterThan=" + id);

        defaultWebAppSettingsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWebAppSettingsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where title equals to DEFAULT_TITLE
        defaultWebAppSettingsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the webAppSettingsList where title equals to UPDATED_TITLE
        defaultWebAppSettingsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultWebAppSettingsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the webAppSettingsList where title equals to UPDATED_TITLE
        defaultWebAppSettingsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where title is not null
        defaultWebAppSettingsShouldBeFound("title.specified=true");

        // Get all the webAppSettingsList where title is null
        defaultWebAppSettingsShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByTitleContainsSomething() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where title contains DEFAULT_TITLE
        defaultWebAppSettingsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the webAppSettingsList where title contains UPDATED_TITLE
        defaultWebAppSettingsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where title does not contain DEFAULT_TITLE
        defaultWebAppSettingsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the webAppSettingsList where title does not contain UPDATED_TITLE
        defaultWebAppSettingsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where value equals to DEFAULT_VALUE
        defaultWebAppSettingsShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the webAppSettingsList where value equals to UPDATED_VALUE
        defaultWebAppSettingsShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultWebAppSettingsShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the webAppSettingsList where value equals to UPDATED_VALUE
        defaultWebAppSettingsShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where value is not null
        defaultWebAppSettingsShouldBeFound("value.specified=true");

        // Get all the webAppSettingsList where value is null
        defaultWebAppSettingsShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByValueContainsSomething() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where value contains DEFAULT_VALUE
        defaultWebAppSettingsShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the webAppSettingsList where value contains UPDATED_VALUE
        defaultWebAppSettingsShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where value does not contain DEFAULT_VALUE
        defaultWebAppSettingsShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the webAppSettingsList where value does not contain UPDATED_VALUE
        defaultWebAppSettingsShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where createdAt equals to DEFAULT_CREATED_AT
        defaultWebAppSettingsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the webAppSettingsList where createdAt equals to UPDATED_CREATED_AT
        defaultWebAppSettingsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultWebAppSettingsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the webAppSettingsList where createdAt equals to UPDATED_CREATED_AT
        defaultWebAppSettingsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where createdAt is not null
        defaultWebAppSettingsShouldBeFound("createdAt.specified=true");

        // Get all the webAppSettingsList where createdAt is null
        defaultWebAppSettingsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultWebAppSettingsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the webAppSettingsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultWebAppSettingsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultWebAppSettingsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the webAppSettingsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultWebAppSettingsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllWebAppSettingsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        // Get all the webAppSettingsList where updatedAt is not null
        defaultWebAppSettingsShouldBeFound("updatedAt.specified=true");

        // Get all the webAppSettingsList where updatedAt is null
        defaultWebAppSettingsShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWebAppSettingsShouldBeFound(String filter) throws Exception {
        restWebAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webAppSettings.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restWebAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWebAppSettingsShouldNotBeFound(String filter) throws Exception {
        restWebAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWebAppSettingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWebAppSettings() throws Exception {
        // Get the webAppSettings
        restWebAppSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWebAppSettings() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();

        // Update the webAppSettings
        WebAppSettings updatedWebAppSettings = webAppSettingsRepository.findById(webAppSettings.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWebAppSettings are not directly saved in db
        em.detach(updatedWebAppSettings);
        updatedWebAppSettings.title(UPDATED_TITLE).value(UPDATED_VALUE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(updatedWebAppSettings);

        restWebAppSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webAppSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
        WebAppSettings testWebAppSettings = webAppSettingsList.get(webAppSettingsList.size() - 1);
        assertThat(testWebAppSettings.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWebAppSettings.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWebAppSettings.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWebAppSettings.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingWebAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();
        webAppSettings.setId(intCount.incrementAndGet());

        // Create the WebAppSettings
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebAppSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webAppSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWebAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();
        webAppSettings.setId(intCount.incrementAndGet());

        // Create the WebAppSettings
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebAppSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWebAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();
        webAppSettings.setId(intCount.incrementAndGet());

        // Create the WebAppSettings
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebAppSettingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWebAppSettingsWithPatch() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();

        // Update the webAppSettings using partial update
        WebAppSettings partialUpdatedWebAppSettings = new WebAppSettings();
        partialUpdatedWebAppSettings.setId(webAppSettings.getId());

        partialUpdatedWebAppSettings.value(UPDATED_VALUE);

        restWebAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebAppSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWebAppSettings))
            )
            .andExpect(status().isOk());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
        WebAppSettings testWebAppSettings = webAppSettingsList.get(webAppSettingsList.size() - 1);
        assertThat(testWebAppSettings.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWebAppSettings.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWebAppSettings.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testWebAppSettings.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateWebAppSettingsWithPatch() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();

        // Update the webAppSettings using partial update
        WebAppSettings partialUpdatedWebAppSettings = new WebAppSettings();
        partialUpdatedWebAppSettings.setId(webAppSettings.getId());

        partialUpdatedWebAppSettings.title(UPDATED_TITLE).value(UPDATED_VALUE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restWebAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebAppSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWebAppSettings))
            )
            .andExpect(status().isOk());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
        WebAppSettings testWebAppSettings = webAppSettingsList.get(webAppSettingsList.size() - 1);
        assertThat(testWebAppSettings.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWebAppSettings.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWebAppSettings.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWebAppSettings.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingWebAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();
        webAppSettings.setId(intCount.incrementAndGet());

        // Create the WebAppSettings
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, webAppSettingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWebAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();
        webAppSettings.setId(intCount.incrementAndGet());

        // Create the WebAppSettings
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWebAppSettings() throws Exception {
        int databaseSizeBeforeUpdate = webAppSettingsRepository.findAll().size();
        webAppSettings.setId(intCount.incrementAndGet());

        // Create the WebAppSettings
        WebAppSettingsDTO webAppSettingsDTO = webAppSettingsMapper.toDto(webAppSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebAppSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(webAppSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebAppSettings in the database
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWebAppSettings() throws Exception {
        // Initialize the database
        webAppSettingsRepository.saveAndFlush(webAppSettings);

        int databaseSizeBeforeDelete = webAppSettingsRepository.findAll().size();

        // Delete the webAppSettings
        restWebAppSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, webAppSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WebAppSettings> webAppSettingsList = webAppSettingsRepository.findAll();
        assertThat(webAppSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
