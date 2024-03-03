package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.UserHoliday;
import com.nullsafe.daily.repository.UserHolidayRepository;
import com.nullsafe.daily.service.dto.UserHolidayDTO;
import com.nullsafe.daily.service.mapper.UserHolidayMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link UserHolidayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserHolidayResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-holidays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserHolidayRepository userHolidayRepository;

    @Autowired
    private UserHolidayMapper userHolidayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserHolidayMockMvc;

    private UserHoliday userHoliday;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserHoliday createEntity(EntityManager em) {
        UserHoliday userHoliday = new UserHoliday()
            .userId(DEFAULT_USER_ID)
            .date(DEFAULT_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return userHoliday;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserHoliday createUpdatedEntity(EntityManager em) {
        UserHoliday userHoliday = new UserHoliday()
            .userId(UPDATED_USER_ID)
            .date(UPDATED_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return userHoliday;
    }

    @BeforeEach
    public void initTest() {
        userHoliday = createEntity(em);
    }

    @Test
    @Transactional
    void createUserHoliday() throws Exception {
        int databaseSizeBeforeCreate = userHolidayRepository.findAll().size();
        // Create the UserHoliday
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);
        restUserHolidayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeCreate + 1);
        UserHoliday testUserHoliday = userHolidayList.get(userHolidayList.size() - 1);
        assertThat(testUserHoliday.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserHoliday.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testUserHoliday.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserHoliday.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createUserHolidayWithExistingId() throws Exception {
        // Create the UserHoliday with an existing ID
        userHoliday.setId(1L);
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        int databaseSizeBeforeCreate = userHolidayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserHolidayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userHolidayRepository.findAll().size();
        // set the field null
        userHoliday.setUserId(null);

        // Create the UserHoliday, which fails.
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        restUserHolidayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userHolidayRepository.findAll().size();
        // set the field null
        userHoliday.setDate(null);

        // Create the UserHoliday, which fails.
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        restUserHolidayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserHolidays() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList
        restUserHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userHoliday.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getUserHoliday() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get the userHoliday
        restUserHolidayMockMvc
            .perform(get(ENTITY_API_URL_ID, userHoliday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userHoliday.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getUserHolidaysByIdFiltering() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        Long id = userHoliday.getId();

        defaultUserHolidayShouldBeFound("id.equals=" + id);
        defaultUserHolidayShouldNotBeFound("id.notEquals=" + id);

        defaultUserHolidayShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserHolidayShouldNotBeFound("id.greaterThan=" + id);

        defaultUserHolidayShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserHolidayShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where userId equals to DEFAULT_USER_ID
        defaultUserHolidayShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the userHolidayList where userId equals to UPDATED_USER_ID
        defaultUserHolidayShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultUserHolidayShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the userHolidayList where userId equals to UPDATED_USER_ID
        defaultUserHolidayShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where userId is not null
        defaultUserHolidayShouldBeFound("userId.specified=true");

        // Get all the userHolidayList where userId is null
        defaultUserHolidayShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where userId is greater than or equal to DEFAULT_USER_ID
        defaultUserHolidayShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the userHolidayList where userId is greater than or equal to UPDATED_USER_ID
        defaultUserHolidayShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where userId is less than or equal to DEFAULT_USER_ID
        defaultUserHolidayShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the userHolidayList where userId is less than or equal to SMALLER_USER_ID
        defaultUserHolidayShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where userId is less than DEFAULT_USER_ID
        defaultUserHolidayShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the userHolidayList where userId is less than UPDATED_USER_ID
        defaultUserHolidayShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where userId is greater than DEFAULT_USER_ID
        defaultUserHolidayShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the userHolidayList where userId is greater than SMALLER_USER_ID
        defaultUserHolidayShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where date equals to DEFAULT_DATE
        defaultUserHolidayShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the userHolidayList where date equals to UPDATED_DATE
        defaultUserHolidayShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByDateIsInShouldWork() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where date in DEFAULT_DATE or UPDATED_DATE
        defaultUserHolidayShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the userHolidayList where date equals to UPDATED_DATE
        defaultUserHolidayShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where date is not null
        defaultUserHolidayShouldBeFound("date.specified=true");

        // Get all the userHolidayList where date is null
        defaultUserHolidayShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllUserHolidaysByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where date is greater than or equal to DEFAULT_DATE
        defaultUserHolidayShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the userHolidayList where date is greater than or equal to UPDATED_DATE
        defaultUserHolidayShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where date is less than or equal to DEFAULT_DATE
        defaultUserHolidayShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the userHolidayList where date is less than or equal to SMALLER_DATE
        defaultUserHolidayShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where date is less than DEFAULT_DATE
        defaultUserHolidayShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the userHolidayList where date is less than UPDATED_DATE
        defaultUserHolidayShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where date is greater than DEFAULT_DATE
        defaultUserHolidayShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the userHolidayList where date is greater than SMALLER_DATE
        defaultUserHolidayShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where createdAt equals to DEFAULT_CREATED_AT
        defaultUserHolidayShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the userHolidayList where createdAt equals to UPDATED_CREATED_AT
        defaultUserHolidayShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUserHolidayShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the userHolidayList where createdAt equals to UPDATED_CREATED_AT
        defaultUserHolidayShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where createdAt is not null
        defaultUserHolidayShouldBeFound("createdAt.specified=true");

        // Get all the userHolidayList where createdAt is null
        defaultUserHolidayShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUserHolidayShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the userHolidayList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserHolidayShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUserHolidayShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the userHolidayList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserHolidayShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserHolidaysByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        // Get all the userHolidayList where updatedAt is not null
        defaultUserHolidayShouldBeFound("updatedAt.specified=true");

        // Get all the userHolidayList where updatedAt is null
        defaultUserHolidayShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserHolidayShouldBeFound(String filter) throws Exception {
        restUserHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userHoliday.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restUserHolidayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserHolidayShouldNotBeFound(String filter) throws Exception {
        restUserHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserHolidayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserHoliday() throws Exception {
        // Get the userHoliday
        restUserHolidayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserHoliday() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();

        // Update the userHoliday
        UserHoliday updatedUserHoliday = userHolidayRepository.findById(userHoliday.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserHoliday are not directly saved in db
        em.detach(updatedUserHoliday);
        updatedUserHoliday.userId(UPDATED_USER_ID).date(UPDATED_DATE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(updatedUserHoliday);

        restUserHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userHolidayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
        UserHoliday testUserHoliday = userHolidayList.get(userHolidayList.size() - 1);
        assertThat(testUserHoliday.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserHoliday.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserHoliday.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserHoliday.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUserHoliday() throws Exception {
        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();
        userHoliday.setId(longCount.incrementAndGet());

        // Create the UserHoliday
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userHolidayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserHoliday() throws Exception {
        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();
        userHoliday.setId(longCount.incrementAndGet());

        // Create the UserHoliday
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserHoliday() throws Exception {
        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();
        userHoliday.setId(longCount.incrementAndGet());

        // Create the UserHoliday
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHolidayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHolidayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserHolidayWithPatch() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();

        // Update the userHoliday using partial update
        UserHoliday partialUpdatedUserHoliday = new UserHoliday();
        partialUpdatedUserHoliday.setId(userHoliday.getId());

        partialUpdatedUserHoliday.date(UPDATED_DATE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restUserHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserHoliday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserHoliday))
            )
            .andExpect(status().isOk());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
        UserHoliday testUserHoliday = userHolidayList.get(userHolidayList.size() - 1);
        assertThat(testUserHoliday.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserHoliday.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserHoliday.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserHoliday.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUserHolidayWithPatch() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();

        // Update the userHoliday using partial update
        UserHoliday partialUpdatedUserHoliday = new UserHoliday();
        partialUpdatedUserHoliday.setId(userHoliday.getId());

        partialUpdatedUserHoliday.userId(UPDATED_USER_ID).date(UPDATED_DATE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restUserHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserHoliday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserHoliday))
            )
            .andExpect(status().isOk());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
        UserHoliday testUserHoliday = userHolidayList.get(userHolidayList.size() - 1);
        assertThat(testUserHoliday.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserHoliday.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserHoliday.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserHoliday.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUserHoliday() throws Exception {
        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();
        userHoliday.setId(longCount.incrementAndGet());

        // Create the UserHoliday
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userHolidayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserHoliday() throws Exception {
        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();
        userHoliday.setId(longCount.incrementAndGet());

        // Create the UserHoliday
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserHoliday() throws Exception {
        int databaseSizeBeforeUpdate = userHolidayRepository.findAll().size();
        userHoliday.setId(longCount.incrementAndGet());

        // Create the UserHoliday
        UserHolidayDTO userHolidayDTO = userHolidayMapper.toDto(userHoliday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userHolidayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserHoliday in the database
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserHoliday() throws Exception {
        // Initialize the database
        userHolidayRepository.saveAndFlush(userHoliday);

        int databaseSizeBeforeDelete = userHolidayRepository.findAll().size();

        // Delete the userHoliday
        restUserHolidayMockMvc
            .perform(delete(ENTITY_API_URL_ID, userHoliday.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserHoliday> userHolidayList = userHolidayRepository.findAll();
        assertThat(userHolidayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
