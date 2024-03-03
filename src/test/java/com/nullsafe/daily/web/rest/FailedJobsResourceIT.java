package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.FailedJobs;
import com.nullsafe.daily.repository.FailedJobsRepository;
import com.nullsafe.daily.service.dto.FailedJobsDTO;
import com.nullsafe.daily.service.mapper.FailedJobsMapper;
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
 * Integration tests for the {@link FailedJobsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FailedJobsResourceIT {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_CONNECTION = "AAAAAAAAAA";
    private static final String UPDATED_CONNECTION = "BBBBBBBBBB";

    private static final String DEFAULT_QUEUE = "AAAAAAAAAA";
    private static final String UPDATED_QUEUE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYLOAD = "AAAAAAAAAA";
    private static final String UPDATED_PAYLOAD = "BBBBBBBBBB";

    private static final String DEFAULT_EXCEPTION = "AAAAAAAAAA";
    private static final String UPDATED_EXCEPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FAILED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FAILED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/failed-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FailedJobsRepository failedJobsRepository;

    @Autowired
    private FailedJobsMapper failedJobsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFailedJobsMockMvc;

    private FailedJobs failedJobs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FailedJobs createEntity(EntityManager em) {
        FailedJobs failedJobs = new FailedJobs()
            .uuid(DEFAULT_UUID)
            .connection(DEFAULT_CONNECTION)
            .queue(DEFAULT_QUEUE)
            .payload(DEFAULT_PAYLOAD)
            .exception(DEFAULT_EXCEPTION)
            .failedAt(DEFAULT_FAILED_AT);
        return failedJobs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FailedJobs createUpdatedEntity(EntityManager em) {
        FailedJobs failedJobs = new FailedJobs()
            .uuid(UPDATED_UUID)
            .connection(UPDATED_CONNECTION)
            .queue(UPDATED_QUEUE)
            .payload(UPDATED_PAYLOAD)
            .exception(UPDATED_EXCEPTION)
            .failedAt(UPDATED_FAILED_AT);
        return failedJobs;
    }

    @BeforeEach
    public void initTest() {
        failedJobs = createEntity(em);
    }

    @Test
    @Transactional
    void createFailedJobs() throws Exception {
        int databaseSizeBeforeCreate = failedJobsRepository.findAll().size();
        // Create the FailedJobs
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);
        restFailedJobsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failedJobsDTO)))
            .andExpect(status().isCreated());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeCreate + 1);
        FailedJobs testFailedJobs = failedJobsList.get(failedJobsList.size() - 1);
        assertThat(testFailedJobs.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testFailedJobs.getConnection()).isEqualTo(DEFAULT_CONNECTION);
        assertThat(testFailedJobs.getQueue()).isEqualTo(DEFAULT_QUEUE);
        assertThat(testFailedJobs.getPayload()).isEqualTo(DEFAULT_PAYLOAD);
        assertThat(testFailedJobs.getException()).isEqualTo(DEFAULT_EXCEPTION);
        assertThat(testFailedJobs.getFailedAt()).isEqualTo(DEFAULT_FAILED_AT);
    }

    @Test
    @Transactional
    void createFailedJobsWithExistingId() throws Exception {
        // Create the FailedJobs with an existing ID
        failedJobs.setId(1L);
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        int databaseSizeBeforeCreate = failedJobsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFailedJobsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failedJobsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = failedJobsRepository.findAll().size();
        // set the field null
        failedJobs.setUuid(null);

        // Create the FailedJobs, which fails.
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        restFailedJobsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failedJobsDTO)))
            .andExpect(status().isBadRequest());

        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConnectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = failedJobsRepository.findAll().size();
        // set the field null
        failedJobs.setConnection(null);

        // Create the FailedJobs, which fails.
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        restFailedJobsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failedJobsDTO)))
            .andExpect(status().isBadRequest());

        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQueueIsRequired() throws Exception {
        int databaseSizeBeforeTest = failedJobsRepository.findAll().size();
        // set the field null
        failedJobs.setQueue(null);

        // Create the FailedJobs, which fails.
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        restFailedJobsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failedJobsDTO)))
            .andExpect(status().isBadRequest());

        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFailedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = failedJobsRepository.findAll().size();
        // set the field null
        failedJobs.setFailedAt(null);

        // Create the FailedJobs, which fails.
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        restFailedJobsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failedJobsDTO)))
            .andExpect(status().isBadRequest());

        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFailedJobs() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList
        restFailedJobsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(failedJobs.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].connection").value(hasItem(DEFAULT_CONNECTION)))
            .andExpect(jsonPath("$.[*].queue").value(hasItem(DEFAULT_QUEUE)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD.toString())))
            .andExpect(jsonPath("$.[*].exception").value(hasItem(DEFAULT_EXCEPTION.toString())))
            .andExpect(jsonPath("$.[*].failedAt").value(hasItem(DEFAULT_FAILED_AT.toString())));
    }

    @Test
    @Transactional
    void getFailedJobs() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get the failedJobs
        restFailedJobsMockMvc
            .perform(get(ENTITY_API_URL_ID, failedJobs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(failedJobs.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.connection").value(DEFAULT_CONNECTION))
            .andExpect(jsonPath("$.queue").value(DEFAULT_QUEUE))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD.toString()))
            .andExpect(jsonPath("$.exception").value(DEFAULT_EXCEPTION.toString()))
            .andExpect(jsonPath("$.failedAt").value(DEFAULT_FAILED_AT.toString()));
    }

    @Test
    @Transactional
    void getFailedJobsByIdFiltering() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        Long id = failedJobs.getId();

        defaultFailedJobsShouldBeFound("id.equals=" + id);
        defaultFailedJobsShouldNotBeFound("id.notEquals=" + id);

        defaultFailedJobsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFailedJobsShouldNotBeFound("id.greaterThan=" + id);

        defaultFailedJobsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFailedJobsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFailedJobsByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where uuid equals to DEFAULT_UUID
        defaultFailedJobsShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the failedJobsList where uuid equals to UPDATED_UUID
        defaultFailedJobsShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllFailedJobsByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultFailedJobsShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the failedJobsList where uuid equals to UPDATED_UUID
        defaultFailedJobsShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllFailedJobsByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where uuid is not null
        defaultFailedJobsShouldBeFound("uuid.specified=true");

        // Get all the failedJobsList where uuid is null
        defaultFailedJobsShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedJobsByUuidContainsSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where uuid contains DEFAULT_UUID
        defaultFailedJobsShouldBeFound("uuid.contains=" + DEFAULT_UUID);

        // Get all the failedJobsList where uuid contains UPDATED_UUID
        defaultFailedJobsShouldNotBeFound("uuid.contains=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllFailedJobsByUuidNotContainsSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where uuid does not contain DEFAULT_UUID
        defaultFailedJobsShouldNotBeFound("uuid.doesNotContain=" + DEFAULT_UUID);

        // Get all the failedJobsList where uuid does not contain UPDATED_UUID
        defaultFailedJobsShouldBeFound("uuid.doesNotContain=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    void getAllFailedJobsByConnectionIsEqualToSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where connection equals to DEFAULT_CONNECTION
        defaultFailedJobsShouldBeFound("connection.equals=" + DEFAULT_CONNECTION);

        // Get all the failedJobsList where connection equals to UPDATED_CONNECTION
        defaultFailedJobsShouldNotBeFound("connection.equals=" + UPDATED_CONNECTION);
    }

    @Test
    @Transactional
    void getAllFailedJobsByConnectionIsInShouldWork() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where connection in DEFAULT_CONNECTION or UPDATED_CONNECTION
        defaultFailedJobsShouldBeFound("connection.in=" + DEFAULT_CONNECTION + "," + UPDATED_CONNECTION);

        // Get all the failedJobsList where connection equals to UPDATED_CONNECTION
        defaultFailedJobsShouldNotBeFound("connection.in=" + UPDATED_CONNECTION);
    }

    @Test
    @Transactional
    void getAllFailedJobsByConnectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where connection is not null
        defaultFailedJobsShouldBeFound("connection.specified=true");

        // Get all the failedJobsList where connection is null
        defaultFailedJobsShouldNotBeFound("connection.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedJobsByConnectionContainsSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where connection contains DEFAULT_CONNECTION
        defaultFailedJobsShouldBeFound("connection.contains=" + DEFAULT_CONNECTION);

        // Get all the failedJobsList where connection contains UPDATED_CONNECTION
        defaultFailedJobsShouldNotBeFound("connection.contains=" + UPDATED_CONNECTION);
    }

    @Test
    @Transactional
    void getAllFailedJobsByConnectionNotContainsSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where connection does not contain DEFAULT_CONNECTION
        defaultFailedJobsShouldNotBeFound("connection.doesNotContain=" + DEFAULT_CONNECTION);

        // Get all the failedJobsList where connection does not contain UPDATED_CONNECTION
        defaultFailedJobsShouldBeFound("connection.doesNotContain=" + UPDATED_CONNECTION);
    }

    @Test
    @Transactional
    void getAllFailedJobsByQueueIsEqualToSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where queue equals to DEFAULT_QUEUE
        defaultFailedJobsShouldBeFound("queue.equals=" + DEFAULT_QUEUE);

        // Get all the failedJobsList where queue equals to UPDATED_QUEUE
        defaultFailedJobsShouldNotBeFound("queue.equals=" + UPDATED_QUEUE);
    }

    @Test
    @Transactional
    void getAllFailedJobsByQueueIsInShouldWork() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where queue in DEFAULT_QUEUE or UPDATED_QUEUE
        defaultFailedJobsShouldBeFound("queue.in=" + DEFAULT_QUEUE + "," + UPDATED_QUEUE);

        // Get all the failedJobsList where queue equals to UPDATED_QUEUE
        defaultFailedJobsShouldNotBeFound("queue.in=" + UPDATED_QUEUE);
    }

    @Test
    @Transactional
    void getAllFailedJobsByQueueIsNullOrNotNull() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where queue is not null
        defaultFailedJobsShouldBeFound("queue.specified=true");

        // Get all the failedJobsList where queue is null
        defaultFailedJobsShouldNotBeFound("queue.specified=false");
    }

    @Test
    @Transactional
    void getAllFailedJobsByQueueContainsSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where queue contains DEFAULT_QUEUE
        defaultFailedJobsShouldBeFound("queue.contains=" + DEFAULT_QUEUE);

        // Get all the failedJobsList where queue contains UPDATED_QUEUE
        defaultFailedJobsShouldNotBeFound("queue.contains=" + UPDATED_QUEUE);
    }

    @Test
    @Transactional
    void getAllFailedJobsByQueueNotContainsSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where queue does not contain DEFAULT_QUEUE
        defaultFailedJobsShouldNotBeFound("queue.doesNotContain=" + DEFAULT_QUEUE);

        // Get all the failedJobsList where queue does not contain UPDATED_QUEUE
        defaultFailedJobsShouldBeFound("queue.doesNotContain=" + UPDATED_QUEUE);
    }

    @Test
    @Transactional
    void getAllFailedJobsByFailedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where failedAt equals to DEFAULT_FAILED_AT
        defaultFailedJobsShouldBeFound("failedAt.equals=" + DEFAULT_FAILED_AT);

        // Get all the failedJobsList where failedAt equals to UPDATED_FAILED_AT
        defaultFailedJobsShouldNotBeFound("failedAt.equals=" + UPDATED_FAILED_AT);
    }

    @Test
    @Transactional
    void getAllFailedJobsByFailedAtIsInShouldWork() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where failedAt in DEFAULT_FAILED_AT or UPDATED_FAILED_AT
        defaultFailedJobsShouldBeFound("failedAt.in=" + DEFAULT_FAILED_AT + "," + UPDATED_FAILED_AT);

        // Get all the failedJobsList where failedAt equals to UPDATED_FAILED_AT
        defaultFailedJobsShouldNotBeFound("failedAt.in=" + UPDATED_FAILED_AT);
    }

    @Test
    @Transactional
    void getAllFailedJobsByFailedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        // Get all the failedJobsList where failedAt is not null
        defaultFailedJobsShouldBeFound("failedAt.specified=true");

        // Get all the failedJobsList where failedAt is null
        defaultFailedJobsShouldNotBeFound("failedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFailedJobsShouldBeFound(String filter) throws Exception {
        restFailedJobsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(failedJobs.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].connection").value(hasItem(DEFAULT_CONNECTION)))
            .andExpect(jsonPath("$.[*].queue").value(hasItem(DEFAULT_QUEUE)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD.toString())))
            .andExpect(jsonPath("$.[*].exception").value(hasItem(DEFAULT_EXCEPTION.toString())))
            .andExpect(jsonPath("$.[*].failedAt").value(hasItem(DEFAULT_FAILED_AT.toString())));

        // Check, that the count call also returns 1
        restFailedJobsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFailedJobsShouldNotBeFound(String filter) throws Exception {
        restFailedJobsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFailedJobsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFailedJobs() throws Exception {
        // Get the failedJobs
        restFailedJobsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFailedJobs() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();

        // Update the failedJobs
        FailedJobs updatedFailedJobs = failedJobsRepository.findById(failedJobs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFailedJobs are not directly saved in db
        em.detach(updatedFailedJobs);
        updatedFailedJobs
            .uuid(UPDATED_UUID)
            .connection(UPDATED_CONNECTION)
            .queue(UPDATED_QUEUE)
            .payload(UPDATED_PAYLOAD)
            .exception(UPDATED_EXCEPTION)
            .failedAt(UPDATED_FAILED_AT);
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(updatedFailedJobs);

        restFailedJobsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, failedJobsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(failedJobsDTO))
            )
            .andExpect(status().isOk());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
        FailedJobs testFailedJobs = failedJobsList.get(failedJobsList.size() - 1);
        assertThat(testFailedJobs.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testFailedJobs.getConnection()).isEqualTo(UPDATED_CONNECTION);
        assertThat(testFailedJobs.getQueue()).isEqualTo(UPDATED_QUEUE);
        assertThat(testFailedJobs.getPayload()).isEqualTo(UPDATED_PAYLOAD);
        assertThat(testFailedJobs.getException()).isEqualTo(UPDATED_EXCEPTION);
        assertThat(testFailedJobs.getFailedAt()).isEqualTo(UPDATED_FAILED_AT);
    }

    @Test
    @Transactional
    void putNonExistingFailedJobs() throws Exception {
        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();
        failedJobs.setId(longCount.incrementAndGet());

        // Create the FailedJobs
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFailedJobsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, failedJobsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(failedJobsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFailedJobs() throws Exception {
        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();
        failedJobs.setId(longCount.incrementAndGet());

        // Create the FailedJobs
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedJobsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(failedJobsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFailedJobs() throws Exception {
        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();
        failedJobs.setId(longCount.incrementAndGet());

        // Create the FailedJobs
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedJobsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(failedJobsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFailedJobsWithPatch() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();

        // Update the failedJobs using partial update
        FailedJobs partialUpdatedFailedJobs = new FailedJobs();
        partialUpdatedFailedJobs.setId(failedJobs.getId());

        partialUpdatedFailedJobs.uuid(UPDATED_UUID).connection(UPDATED_CONNECTION).exception(UPDATED_EXCEPTION).failedAt(UPDATED_FAILED_AT);

        restFailedJobsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFailedJobs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFailedJobs))
            )
            .andExpect(status().isOk());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
        FailedJobs testFailedJobs = failedJobsList.get(failedJobsList.size() - 1);
        assertThat(testFailedJobs.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testFailedJobs.getConnection()).isEqualTo(UPDATED_CONNECTION);
        assertThat(testFailedJobs.getQueue()).isEqualTo(DEFAULT_QUEUE);
        assertThat(testFailedJobs.getPayload()).isEqualTo(DEFAULT_PAYLOAD);
        assertThat(testFailedJobs.getException()).isEqualTo(UPDATED_EXCEPTION);
        assertThat(testFailedJobs.getFailedAt()).isEqualTo(UPDATED_FAILED_AT);
    }

    @Test
    @Transactional
    void fullUpdateFailedJobsWithPatch() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();

        // Update the failedJobs using partial update
        FailedJobs partialUpdatedFailedJobs = new FailedJobs();
        partialUpdatedFailedJobs.setId(failedJobs.getId());

        partialUpdatedFailedJobs
            .uuid(UPDATED_UUID)
            .connection(UPDATED_CONNECTION)
            .queue(UPDATED_QUEUE)
            .payload(UPDATED_PAYLOAD)
            .exception(UPDATED_EXCEPTION)
            .failedAt(UPDATED_FAILED_AT);

        restFailedJobsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFailedJobs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFailedJobs))
            )
            .andExpect(status().isOk());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
        FailedJobs testFailedJobs = failedJobsList.get(failedJobsList.size() - 1);
        assertThat(testFailedJobs.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testFailedJobs.getConnection()).isEqualTo(UPDATED_CONNECTION);
        assertThat(testFailedJobs.getQueue()).isEqualTo(UPDATED_QUEUE);
        assertThat(testFailedJobs.getPayload()).isEqualTo(UPDATED_PAYLOAD);
        assertThat(testFailedJobs.getException()).isEqualTo(UPDATED_EXCEPTION);
        assertThat(testFailedJobs.getFailedAt()).isEqualTo(UPDATED_FAILED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingFailedJobs() throws Exception {
        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();
        failedJobs.setId(longCount.incrementAndGet());

        // Create the FailedJobs
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFailedJobsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, failedJobsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(failedJobsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFailedJobs() throws Exception {
        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();
        failedJobs.setId(longCount.incrementAndGet());

        // Create the FailedJobs
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedJobsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(failedJobsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFailedJobs() throws Exception {
        int databaseSizeBeforeUpdate = failedJobsRepository.findAll().size();
        failedJobs.setId(longCount.incrementAndGet());

        // Create the FailedJobs
        FailedJobsDTO failedJobsDTO = failedJobsMapper.toDto(failedJobs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFailedJobsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(failedJobsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FailedJobs in the database
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFailedJobs() throws Exception {
        // Initialize the database
        failedJobsRepository.saveAndFlush(failedJobs);

        int databaseSizeBeforeDelete = failedJobsRepository.findAll().size();

        // Delete the failedJobs
        restFailedJobsMockMvc
            .perform(delete(ENTITY_API_URL_ID, failedJobs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FailedJobs> failedJobsList = failedJobsRepository.findAll();
        assertThat(failedJobsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
