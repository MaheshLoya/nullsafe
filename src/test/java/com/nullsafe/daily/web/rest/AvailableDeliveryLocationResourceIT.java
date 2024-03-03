package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.AvailableDeliveryLocation;
import com.nullsafe.daily.repository.AvailableDeliveryLocationRepository;
import com.nullsafe.daily.service.dto.AvailableDeliveryLocationDTO;
import com.nullsafe.daily.service.mapper.AvailableDeliveryLocationMapper;
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
 * Integration tests for the {@link AvailableDeliveryLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvailableDeliveryLocationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/available-delivery-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvailableDeliveryLocationRepository availableDeliveryLocationRepository;

    @Autowired
    private AvailableDeliveryLocationMapper availableDeliveryLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvailableDeliveryLocationMockMvc;

    private AvailableDeliveryLocation availableDeliveryLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvailableDeliveryLocation createEntity(EntityManager em) {
        AvailableDeliveryLocation availableDeliveryLocation = new AvailableDeliveryLocation()
            .title(DEFAULT_TITLE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return availableDeliveryLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvailableDeliveryLocation createUpdatedEntity(EntityManager em) {
        AvailableDeliveryLocation availableDeliveryLocation = new AvailableDeliveryLocation()
            .title(UPDATED_TITLE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return availableDeliveryLocation;
    }

    @BeforeEach
    public void initTest() {
        availableDeliveryLocation = createEntity(em);
    }

    @Test
    @Transactional
    void createAvailableDeliveryLocation() throws Exception {
        int databaseSizeBeforeCreate = availableDeliveryLocationRepository.findAll().size();
        // Create the AvailableDeliveryLocation
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);
        restAvailableDeliveryLocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeCreate + 1);
        AvailableDeliveryLocation testAvailableDeliveryLocation = availableDeliveryLocationList.get(
            availableDeliveryLocationList.size() - 1
        );
        assertThat(testAvailableDeliveryLocation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAvailableDeliveryLocation.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAvailableDeliveryLocation.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createAvailableDeliveryLocationWithExistingId() throws Exception {
        // Create the AvailableDeliveryLocation with an existing ID
        availableDeliveryLocation.setId(1L);
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);

        int databaseSizeBeforeCreate = availableDeliveryLocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvailableDeliveryLocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = availableDeliveryLocationRepository.findAll().size();
        // set the field null
        availableDeliveryLocation.setTitle(null);

        // Create the AvailableDeliveryLocation, which fails.
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);

        restAvailableDeliveryLocationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocations() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList
        restAvailableDeliveryLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availableDeliveryLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAvailableDeliveryLocation() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get the availableDeliveryLocation
        restAvailableDeliveryLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, availableDeliveryLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(availableDeliveryLocation.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getAvailableDeliveryLocationsByIdFiltering() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        Long id = availableDeliveryLocation.getId();

        defaultAvailableDeliveryLocationShouldBeFound("id.equals=" + id);
        defaultAvailableDeliveryLocationShouldNotBeFound("id.notEquals=" + id);

        defaultAvailableDeliveryLocationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAvailableDeliveryLocationShouldNotBeFound("id.greaterThan=" + id);

        defaultAvailableDeliveryLocationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAvailableDeliveryLocationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where title equals to DEFAULT_TITLE
        defaultAvailableDeliveryLocationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the availableDeliveryLocationList where title equals to UPDATED_TITLE
        defaultAvailableDeliveryLocationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultAvailableDeliveryLocationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the availableDeliveryLocationList where title equals to UPDATED_TITLE
        defaultAvailableDeliveryLocationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where title is not null
        defaultAvailableDeliveryLocationShouldBeFound("title.specified=true");

        // Get all the availableDeliveryLocationList where title is null
        defaultAvailableDeliveryLocationShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where title contains DEFAULT_TITLE
        defaultAvailableDeliveryLocationShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the availableDeliveryLocationList where title contains UPDATED_TITLE
        defaultAvailableDeliveryLocationShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where title does not contain DEFAULT_TITLE
        defaultAvailableDeliveryLocationShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the availableDeliveryLocationList where title does not contain UPDATED_TITLE
        defaultAvailableDeliveryLocationShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where createdAt equals to DEFAULT_CREATED_AT
        defaultAvailableDeliveryLocationShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the availableDeliveryLocationList where createdAt equals to UPDATED_CREATED_AT
        defaultAvailableDeliveryLocationShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAvailableDeliveryLocationShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the availableDeliveryLocationList where createdAt equals to UPDATED_CREATED_AT
        defaultAvailableDeliveryLocationShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where createdAt is not null
        defaultAvailableDeliveryLocationShouldBeFound("createdAt.specified=true");

        // Get all the availableDeliveryLocationList where createdAt is null
        defaultAvailableDeliveryLocationShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultAvailableDeliveryLocationShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the availableDeliveryLocationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAvailableDeliveryLocationShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultAvailableDeliveryLocationShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the availableDeliveryLocationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAvailableDeliveryLocationShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAvailableDeliveryLocationsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        // Get all the availableDeliveryLocationList where updatedAt is not null
        defaultAvailableDeliveryLocationShouldBeFound("updatedAt.specified=true");

        // Get all the availableDeliveryLocationList where updatedAt is null
        defaultAvailableDeliveryLocationShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAvailableDeliveryLocationShouldBeFound(String filter) throws Exception {
        restAvailableDeliveryLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availableDeliveryLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restAvailableDeliveryLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAvailableDeliveryLocationShouldNotBeFound(String filter) throws Exception {
        restAvailableDeliveryLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAvailableDeliveryLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAvailableDeliveryLocation() throws Exception {
        // Get the availableDeliveryLocation
        restAvailableDeliveryLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvailableDeliveryLocation() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();

        // Update the availableDeliveryLocation
        AvailableDeliveryLocation updatedAvailableDeliveryLocation = availableDeliveryLocationRepository
            .findById(availableDeliveryLocation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAvailableDeliveryLocation are not directly saved in db
        em.detach(updatedAvailableDeliveryLocation);
        updatedAvailableDeliveryLocation.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(updatedAvailableDeliveryLocation);

        restAvailableDeliveryLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, availableDeliveryLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
        AvailableDeliveryLocation testAvailableDeliveryLocation = availableDeliveryLocationList.get(
            availableDeliveryLocationList.size() - 1
        );
        assertThat(testAvailableDeliveryLocation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAvailableDeliveryLocation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAvailableDeliveryLocation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAvailableDeliveryLocation() throws Exception {
        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();
        availableDeliveryLocation.setId(longCount.incrementAndGet());

        // Create the AvailableDeliveryLocation
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailableDeliveryLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, availableDeliveryLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvailableDeliveryLocation() throws Exception {
        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();
        availableDeliveryLocation.setId(longCount.incrementAndGet());

        // Create the AvailableDeliveryLocation
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailableDeliveryLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvailableDeliveryLocation() throws Exception {
        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();
        availableDeliveryLocation.setId(longCount.incrementAndGet());

        // Create the AvailableDeliveryLocation
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailableDeliveryLocationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvailableDeliveryLocationWithPatch() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();

        // Update the availableDeliveryLocation using partial update
        AvailableDeliveryLocation partialUpdatedAvailableDeliveryLocation = new AvailableDeliveryLocation();
        partialUpdatedAvailableDeliveryLocation.setId(availableDeliveryLocation.getId());

        partialUpdatedAvailableDeliveryLocation.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restAvailableDeliveryLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvailableDeliveryLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvailableDeliveryLocation))
            )
            .andExpect(status().isOk());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
        AvailableDeliveryLocation testAvailableDeliveryLocation = availableDeliveryLocationList.get(
            availableDeliveryLocationList.size() - 1
        );
        assertThat(testAvailableDeliveryLocation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAvailableDeliveryLocation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAvailableDeliveryLocation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAvailableDeliveryLocationWithPatch() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();

        // Update the availableDeliveryLocation using partial update
        AvailableDeliveryLocation partialUpdatedAvailableDeliveryLocation = new AvailableDeliveryLocation();
        partialUpdatedAvailableDeliveryLocation.setId(availableDeliveryLocation.getId());

        partialUpdatedAvailableDeliveryLocation.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restAvailableDeliveryLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvailableDeliveryLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvailableDeliveryLocation))
            )
            .andExpect(status().isOk());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
        AvailableDeliveryLocation testAvailableDeliveryLocation = availableDeliveryLocationList.get(
            availableDeliveryLocationList.size() - 1
        );
        assertThat(testAvailableDeliveryLocation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAvailableDeliveryLocation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAvailableDeliveryLocation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAvailableDeliveryLocation() throws Exception {
        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();
        availableDeliveryLocation.setId(longCount.incrementAndGet());

        // Create the AvailableDeliveryLocation
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvailableDeliveryLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, availableDeliveryLocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvailableDeliveryLocation() throws Exception {
        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();
        availableDeliveryLocation.setId(longCount.incrementAndGet());

        // Create the AvailableDeliveryLocation
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailableDeliveryLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvailableDeliveryLocation() throws Exception {
        int databaseSizeBeforeUpdate = availableDeliveryLocationRepository.findAll().size();
        availableDeliveryLocation.setId(longCount.incrementAndGet());

        // Create the AvailableDeliveryLocation
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO = availableDeliveryLocationMapper.toDto(availableDeliveryLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvailableDeliveryLocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(availableDeliveryLocationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AvailableDeliveryLocation in the database
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvailableDeliveryLocation() throws Exception {
        // Initialize the database
        availableDeliveryLocationRepository.saveAndFlush(availableDeliveryLocation);

        int databaseSizeBeforeDelete = availableDeliveryLocationRepository.findAll().size();

        // Delete the availableDeliveryLocation
        restAvailableDeliveryLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, availableDeliveryLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AvailableDeliveryLocation> availableDeliveryLocationList = availableDeliveryLocationRepository.findAll();
        assertThat(availableDeliveryLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
