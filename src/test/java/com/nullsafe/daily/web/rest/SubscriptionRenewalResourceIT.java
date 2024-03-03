package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.SubscriptionRenewal;
import com.nullsafe.daily.repository.SubscriptionRenewalRepository;
import com.nullsafe.daily.service.dto.SubscriptionRenewalDTO;
import com.nullsafe.daily.service.mapper.SubscriptionRenewalMapper;
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
 * Integration tests for the {@link SubscriptionRenewalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriptionRenewalResourceIT {

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;
    private static final Integer SMALLER_USER_ID = 1 - 1;

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;
    private static final Long SMALLER_ORDER_ID = 1L - 1L;

    private static final Long DEFAULT_TRANSACTION_ID = 1L;
    private static final Long UPDATED_TRANSACTION_ID = 2L;
    private static final Long SMALLER_TRANSACTION_ID = 1L - 1L;

    private static final LocalDate DEFAULT_RENEWAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RENEWAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RENEWAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_PAID_RENEWAL_AMOUNT = 1F;
    private static final Float UPDATED_PAID_RENEWAL_AMOUNT = 2F;
    private static final Float SMALLER_PAID_RENEWAL_AMOUNT = 1F - 1F;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/subscription-renewals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscriptionRenewalRepository subscriptionRenewalRepository;

    @Autowired
    private SubscriptionRenewalMapper subscriptionRenewalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriptionRenewalMockMvc;

    private SubscriptionRenewal subscriptionRenewal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionRenewal createEntity(EntityManager em) {
        SubscriptionRenewal subscriptionRenewal = new SubscriptionRenewal()
            .userId(DEFAULT_USER_ID)
            .orderId(DEFAULT_ORDER_ID)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .renewalDate(DEFAULT_RENEWAL_DATE)
            .paidRenewalAmount(DEFAULT_PAID_RENEWAL_AMOUNT)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return subscriptionRenewal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionRenewal createUpdatedEntity(EntityManager em) {
        SubscriptionRenewal subscriptionRenewal = new SubscriptionRenewal()
            .userId(UPDATED_USER_ID)
            .orderId(UPDATED_ORDER_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .renewalDate(UPDATED_RENEWAL_DATE)
            .paidRenewalAmount(UPDATED_PAID_RENEWAL_AMOUNT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return subscriptionRenewal;
    }

    @BeforeEach
    public void initTest() {
        subscriptionRenewal = createEntity(em);
    }

    @Test
    @Transactional
    void createSubscriptionRenewal() throws Exception {
        int databaseSizeBeforeCreate = subscriptionRenewalRepository.findAll().size();
        // Create the SubscriptionRenewal
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);
        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeCreate + 1);
        SubscriptionRenewal testSubscriptionRenewal = subscriptionRenewalList.get(subscriptionRenewalList.size() - 1);
        assertThat(testSubscriptionRenewal.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testSubscriptionRenewal.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testSubscriptionRenewal.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSubscriptionRenewal.getRenewalDate()).isEqualTo(DEFAULT_RENEWAL_DATE);
        assertThat(testSubscriptionRenewal.getPaidRenewalAmount()).isEqualTo(DEFAULT_PAID_RENEWAL_AMOUNT);
        assertThat(testSubscriptionRenewal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSubscriptionRenewal.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSubscriptionRenewal.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSubscriptionRenewal.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubscriptionRenewal.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSubscriptionRenewalWithExistingId() throws Exception {
        // Create the SubscriptionRenewal with an existing ID
        subscriptionRenewal.setId(1L);
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        int databaseSizeBeforeCreate = subscriptionRenewalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setUserId(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setOrderId(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRenewalDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setRenewalDate(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaidRenewalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setPaidRenewalAmount(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setStatus(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setStartDate(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setEndDate(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setCreatedAt(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionRenewalRepository.findAll().size();
        // set the field null
        subscriptionRenewal.setUpdatedAt(null);

        // Create the SubscriptionRenewal, which fails.
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewals() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList
        restSubscriptionRenewalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionRenewal.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].renewalDate").value(hasItem(DEFAULT_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].paidRenewalAmount").value(hasItem(DEFAULT_PAID_RENEWAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getSubscriptionRenewal() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get the subscriptionRenewal
        restSubscriptionRenewalMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriptionRenewal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriptionRenewal.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.renewalDate").value(DEFAULT_RENEWAL_DATE.toString()))
            .andExpect(jsonPath("$.paidRenewalAmount").value(DEFAULT_PAID_RENEWAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getSubscriptionRenewalsByIdFiltering() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        Long id = subscriptionRenewal.getId();

        defaultSubscriptionRenewalShouldBeFound("id.equals=" + id);
        defaultSubscriptionRenewalShouldNotBeFound("id.notEquals=" + id);

        defaultSubscriptionRenewalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubscriptionRenewalShouldNotBeFound("id.greaterThan=" + id);

        defaultSubscriptionRenewalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubscriptionRenewalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where userId equals to DEFAULT_USER_ID
        defaultSubscriptionRenewalShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the subscriptionRenewalList where userId equals to UPDATED_USER_ID
        defaultSubscriptionRenewalShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultSubscriptionRenewalShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the subscriptionRenewalList where userId equals to UPDATED_USER_ID
        defaultSubscriptionRenewalShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where userId is not null
        defaultSubscriptionRenewalShouldBeFound("userId.specified=true");

        // Get all the subscriptionRenewalList where userId is null
        defaultSubscriptionRenewalShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where userId is greater than or equal to DEFAULT_USER_ID
        defaultSubscriptionRenewalShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the subscriptionRenewalList where userId is greater than or equal to UPDATED_USER_ID
        defaultSubscriptionRenewalShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where userId is less than or equal to DEFAULT_USER_ID
        defaultSubscriptionRenewalShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the subscriptionRenewalList where userId is less than or equal to SMALLER_USER_ID
        defaultSubscriptionRenewalShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where userId is less than DEFAULT_USER_ID
        defaultSubscriptionRenewalShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the subscriptionRenewalList where userId is less than UPDATED_USER_ID
        defaultSubscriptionRenewalShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where userId is greater than DEFAULT_USER_ID
        defaultSubscriptionRenewalShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the subscriptionRenewalList where userId is greater than SMALLER_USER_ID
        defaultSubscriptionRenewalShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByOrderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where orderId equals to DEFAULT_ORDER_ID
        defaultSubscriptionRenewalShouldBeFound("orderId.equals=" + DEFAULT_ORDER_ID);

        // Get all the subscriptionRenewalList where orderId equals to UPDATED_ORDER_ID
        defaultSubscriptionRenewalShouldNotBeFound("orderId.equals=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByOrderIdIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where orderId in DEFAULT_ORDER_ID or UPDATED_ORDER_ID
        defaultSubscriptionRenewalShouldBeFound("orderId.in=" + DEFAULT_ORDER_ID + "," + UPDATED_ORDER_ID);

        // Get all the subscriptionRenewalList where orderId equals to UPDATED_ORDER_ID
        defaultSubscriptionRenewalShouldNotBeFound("orderId.in=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByOrderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where orderId is not null
        defaultSubscriptionRenewalShouldBeFound("orderId.specified=true");

        // Get all the subscriptionRenewalList where orderId is null
        defaultSubscriptionRenewalShouldNotBeFound("orderId.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByOrderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where orderId is greater than or equal to DEFAULT_ORDER_ID
        defaultSubscriptionRenewalShouldBeFound("orderId.greaterThanOrEqual=" + DEFAULT_ORDER_ID);

        // Get all the subscriptionRenewalList where orderId is greater than or equal to UPDATED_ORDER_ID
        defaultSubscriptionRenewalShouldNotBeFound("orderId.greaterThanOrEqual=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByOrderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where orderId is less than or equal to DEFAULT_ORDER_ID
        defaultSubscriptionRenewalShouldBeFound("orderId.lessThanOrEqual=" + DEFAULT_ORDER_ID);

        // Get all the subscriptionRenewalList where orderId is less than or equal to SMALLER_ORDER_ID
        defaultSubscriptionRenewalShouldNotBeFound("orderId.lessThanOrEqual=" + SMALLER_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByOrderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where orderId is less than DEFAULT_ORDER_ID
        defaultSubscriptionRenewalShouldNotBeFound("orderId.lessThan=" + DEFAULT_ORDER_ID);

        // Get all the subscriptionRenewalList where orderId is less than UPDATED_ORDER_ID
        defaultSubscriptionRenewalShouldBeFound("orderId.lessThan=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByOrderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where orderId is greater than DEFAULT_ORDER_ID
        defaultSubscriptionRenewalShouldNotBeFound("orderId.greaterThan=" + DEFAULT_ORDER_ID);

        // Get all the subscriptionRenewalList where orderId is greater than SMALLER_ORDER_ID
        defaultSubscriptionRenewalShouldBeFound("orderId.greaterThan=" + SMALLER_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where transactionId equals to DEFAULT_TRANSACTION_ID
        defaultSubscriptionRenewalShouldBeFound("transactionId.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the subscriptionRenewalList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultSubscriptionRenewalShouldNotBeFound("transactionId.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where transactionId in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultSubscriptionRenewalShouldBeFound("transactionId.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the subscriptionRenewalList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultSubscriptionRenewalShouldNotBeFound("transactionId.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where transactionId is not null
        defaultSubscriptionRenewalShouldBeFound("transactionId.specified=true");

        // Get all the subscriptionRenewalList where transactionId is null
        defaultSubscriptionRenewalShouldNotBeFound("transactionId.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByTransactionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where transactionId is greater than or equal to DEFAULT_TRANSACTION_ID
        defaultSubscriptionRenewalShouldBeFound("transactionId.greaterThanOrEqual=" + DEFAULT_TRANSACTION_ID);

        // Get all the subscriptionRenewalList where transactionId is greater than or equal to UPDATED_TRANSACTION_ID
        defaultSubscriptionRenewalShouldNotBeFound("transactionId.greaterThanOrEqual=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByTransactionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where transactionId is less than or equal to DEFAULT_TRANSACTION_ID
        defaultSubscriptionRenewalShouldBeFound("transactionId.lessThanOrEqual=" + DEFAULT_TRANSACTION_ID);

        // Get all the subscriptionRenewalList where transactionId is less than or equal to SMALLER_TRANSACTION_ID
        defaultSubscriptionRenewalShouldNotBeFound("transactionId.lessThanOrEqual=" + SMALLER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByTransactionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where transactionId is less than DEFAULT_TRANSACTION_ID
        defaultSubscriptionRenewalShouldNotBeFound("transactionId.lessThan=" + DEFAULT_TRANSACTION_ID);

        // Get all the subscriptionRenewalList where transactionId is less than UPDATED_TRANSACTION_ID
        defaultSubscriptionRenewalShouldBeFound("transactionId.lessThan=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByTransactionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where transactionId is greater than DEFAULT_TRANSACTION_ID
        defaultSubscriptionRenewalShouldNotBeFound("transactionId.greaterThan=" + DEFAULT_TRANSACTION_ID);

        // Get all the subscriptionRenewalList where transactionId is greater than SMALLER_TRANSACTION_ID
        defaultSubscriptionRenewalShouldBeFound("transactionId.greaterThan=" + SMALLER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByRenewalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where renewalDate equals to DEFAULT_RENEWAL_DATE
        defaultSubscriptionRenewalShouldBeFound("renewalDate.equals=" + DEFAULT_RENEWAL_DATE);

        // Get all the subscriptionRenewalList where renewalDate equals to UPDATED_RENEWAL_DATE
        defaultSubscriptionRenewalShouldNotBeFound("renewalDate.equals=" + UPDATED_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByRenewalDateIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where renewalDate in DEFAULT_RENEWAL_DATE or UPDATED_RENEWAL_DATE
        defaultSubscriptionRenewalShouldBeFound("renewalDate.in=" + DEFAULT_RENEWAL_DATE + "," + UPDATED_RENEWAL_DATE);

        // Get all the subscriptionRenewalList where renewalDate equals to UPDATED_RENEWAL_DATE
        defaultSubscriptionRenewalShouldNotBeFound("renewalDate.in=" + UPDATED_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByRenewalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where renewalDate is not null
        defaultSubscriptionRenewalShouldBeFound("renewalDate.specified=true");

        // Get all the subscriptionRenewalList where renewalDate is null
        defaultSubscriptionRenewalShouldNotBeFound("renewalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByRenewalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where renewalDate is greater than or equal to DEFAULT_RENEWAL_DATE
        defaultSubscriptionRenewalShouldBeFound("renewalDate.greaterThanOrEqual=" + DEFAULT_RENEWAL_DATE);

        // Get all the subscriptionRenewalList where renewalDate is greater than or equal to UPDATED_RENEWAL_DATE
        defaultSubscriptionRenewalShouldNotBeFound("renewalDate.greaterThanOrEqual=" + UPDATED_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByRenewalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where renewalDate is less than or equal to DEFAULT_RENEWAL_DATE
        defaultSubscriptionRenewalShouldBeFound("renewalDate.lessThanOrEqual=" + DEFAULT_RENEWAL_DATE);

        // Get all the subscriptionRenewalList where renewalDate is less than or equal to SMALLER_RENEWAL_DATE
        defaultSubscriptionRenewalShouldNotBeFound("renewalDate.lessThanOrEqual=" + SMALLER_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByRenewalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where renewalDate is less than DEFAULT_RENEWAL_DATE
        defaultSubscriptionRenewalShouldNotBeFound("renewalDate.lessThan=" + DEFAULT_RENEWAL_DATE);

        // Get all the subscriptionRenewalList where renewalDate is less than UPDATED_RENEWAL_DATE
        defaultSubscriptionRenewalShouldBeFound("renewalDate.lessThan=" + UPDATED_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByRenewalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where renewalDate is greater than DEFAULT_RENEWAL_DATE
        defaultSubscriptionRenewalShouldNotBeFound("renewalDate.greaterThan=" + DEFAULT_RENEWAL_DATE);

        // Get all the subscriptionRenewalList where renewalDate is greater than SMALLER_RENEWAL_DATE
        defaultSubscriptionRenewalShouldBeFound("renewalDate.greaterThan=" + SMALLER_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByPaidRenewalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where paidRenewalAmount equals to DEFAULT_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldBeFound("paidRenewalAmount.equals=" + DEFAULT_PAID_RENEWAL_AMOUNT);

        // Get all the subscriptionRenewalList where paidRenewalAmount equals to UPDATED_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldNotBeFound("paidRenewalAmount.equals=" + UPDATED_PAID_RENEWAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByPaidRenewalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where paidRenewalAmount in DEFAULT_PAID_RENEWAL_AMOUNT or UPDATED_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldBeFound("paidRenewalAmount.in=" + DEFAULT_PAID_RENEWAL_AMOUNT + "," + UPDATED_PAID_RENEWAL_AMOUNT);

        // Get all the subscriptionRenewalList where paidRenewalAmount equals to UPDATED_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldNotBeFound("paidRenewalAmount.in=" + UPDATED_PAID_RENEWAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByPaidRenewalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where paidRenewalAmount is not null
        defaultSubscriptionRenewalShouldBeFound("paidRenewalAmount.specified=true");

        // Get all the subscriptionRenewalList where paidRenewalAmount is null
        defaultSubscriptionRenewalShouldNotBeFound("paidRenewalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByPaidRenewalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where paidRenewalAmount is greater than or equal to DEFAULT_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldBeFound("paidRenewalAmount.greaterThanOrEqual=" + DEFAULT_PAID_RENEWAL_AMOUNT);

        // Get all the subscriptionRenewalList where paidRenewalAmount is greater than or equal to UPDATED_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldNotBeFound("paidRenewalAmount.greaterThanOrEqual=" + UPDATED_PAID_RENEWAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByPaidRenewalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where paidRenewalAmount is less than or equal to DEFAULT_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldBeFound("paidRenewalAmount.lessThanOrEqual=" + DEFAULT_PAID_RENEWAL_AMOUNT);

        // Get all the subscriptionRenewalList where paidRenewalAmount is less than or equal to SMALLER_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldNotBeFound("paidRenewalAmount.lessThanOrEqual=" + SMALLER_PAID_RENEWAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByPaidRenewalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where paidRenewalAmount is less than DEFAULT_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldNotBeFound("paidRenewalAmount.lessThan=" + DEFAULT_PAID_RENEWAL_AMOUNT);

        // Get all the subscriptionRenewalList where paidRenewalAmount is less than UPDATED_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldBeFound("paidRenewalAmount.lessThan=" + UPDATED_PAID_RENEWAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByPaidRenewalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where paidRenewalAmount is greater than DEFAULT_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldNotBeFound("paidRenewalAmount.greaterThan=" + DEFAULT_PAID_RENEWAL_AMOUNT);

        // Get all the subscriptionRenewalList where paidRenewalAmount is greater than SMALLER_PAID_RENEWAL_AMOUNT
        defaultSubscriptionRenewalShouldBeFound("paidRenewalAmount.greaterThan=" + SMALLER_PAID_RENEWAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where status equals to DEFAULT_STATUS
        defaultSubscriptionRenewalShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the subscriptionRenewalList where status equals to UPDATED_STATUS
        defaultSubscriptionRenewalShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSubscriptionRenewalShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the subscriptionRenewalList where status equals to UPDATED_STATUS
        defaultSubscriptionRenewalShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where status is not null
        defaultSubscriptionRenewalShouldBeFound("status.specified=true");

        // Get all the subscriptionRenewalList where status is null
        defaultSubscriptionRenewalShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where startDate equals to DEFAULT_START_DATE
        defaultSubscriptionRenewalShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the subscriptionRenewalList where startDate equals to UPDATED_START_DATE
        defaultSubscriptionRenewalShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultSubscriptionRenewalShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the subscriptionRenewalList where startDate equals to UPDATED_START_DATE
        defaultSubscriptionRenewalShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where startDate is not null
        defaultSubscriptionRenewalShouldBeFound("startDate.specified=true");

        // Get all the subscriptionRenewalList where startDate is null
        defaultSubscriptionRenewalShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultSubscriptionRenewalShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the subscriptionRenewalList where startDate is greater than or equal to UPDATED_START_DATE
        defaultSubscriptionRenewalShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where startDate is less than or equal to DEFAULT_START_DATE
        defaultSubscriptionRenewalShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the subscriptionRenewalList where startDate is less than or equal to SMALLER_START_DATE
        defaultSubscriptionRenewalShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where startDate is less than DEFAULT_START_DATE
        defaultSubscriptionRenewalShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the subscriptionRenewalList where startDate is less than UPDATED_START_DATE
        defaultSubscriptionRenewalShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where startDate is greater than DEFAULT_START_DATE
        defaultSubscriptionRenewalShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the subscriptionRenewalList where startDate is greater than SMALLER_START_DATE
        defaultSubscriptionRenewalShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where endDate equals to DEFAULT_END_DATE
        defaultSubscriptionRenewalShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the subscriptionRenewalList where endDate equals to UPDATED_END_DATE
        defaultSubscriptionRenewalShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultSubscriptionRenewalShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the subscriptionRenewalList where endDate equals to UPDATED_END_DATE
        defaultSubscriptionRenewalShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where endDate is not null
        defaultSubscriptionRenewalShouldBeFound("endDate.specified=true");

        // Get all the subscriptionRenewalList where endDate is null
        defaultSubscriptionRenewalShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultSubscriptionRenewalShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the subscriptionRenewalList where endDate is greater than or equal to UPDATED_END_DATE
        defaultSubscriptionRenewalShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where endDate is less than or equal to DEFAULT_END_DATE
        defaultSubscriptionRenewalShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the subscriptionRenewalList where endDate is less than or equal to SMALLER_END_DATE
        defaultSubscriptionRenewalShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where endDate is less than DEFAULT_END_DATE
        defaultSubscriptionRenewalShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the subscriptionRenewalList where endDate is less than UPDATED_END_DATE
        defaultSubscriptionRenewalShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where endDate is greater than DEFAULT_END_DATE
        defaultSubscriptionRenewalShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the subscriptionRenewalList where endDate is greater than SMALLER_END_DATE
        defaultSubscriptionRenewalShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where createdAt equals to DEFAULT_CREATED_AT
        defaultSubscriptionRenewalShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the subscriptionRenewalList where createdAt equals to UPDATED_CREATED_AT
        defaultSubscriptionRenewalShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSubscriptionRenewalShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the subscriptionRenewalList where createdAt equals to UPDATED_CREATED_AT
        defaultSubscriptionRenewalShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where createdAt is not null
        defaultSubscriptionRenewalShouldBeFound("createdAt.specified=true");

        // Get all the subscriptionRenewalList where createdAt is null
        defaultSubscriptionRenewalShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSubscriptionRenewalShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the subscriptionRenewalList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSubscriptionRenewalShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSubscriptionRenewalShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the subscriptionRenewalList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSubscriptionRenewalShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscriptionRenewalsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        // Get all the subscriptionRenewalList where updatedAt is not null
        defaultSubscriptionRenewalShouldBeFound("updatedAt.specified=true");

        // Get all the subscriptionRenewalList where updatedAt is null
        defaultSubscriptionRenewalShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubscriptionRenewalShouldBeFound(String filter) throws Exception {
        restSubscriptionRenewalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriptionRenewal.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].renewalDate").value(hasItem(DEFAULT_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].paidRenewalAmount").value(hasItem(DEFAULT_PAID_RENEWAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restSubscriptionRenewalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubscriptionRenewalShouldNotBeFound(String filter) throws Exception {
        restSubscriptionRenewalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubscriptionRenewalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubscriptionRenewal() throws Exception {
        // Get the subscriptionRenewal
        restSubscriptionRenewalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscriptionRenewal() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();

        // Update the subscriptionRenewal
        SubscriptionRenewal updatedSubscriptionRenewal = subscriptionRenewalRepository.findById(subscriptionRenewal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscriptionRenewal are not directly saved in db
        em.detach(updatedSubscriptionRenewal);
        updatedSubscriptionRenewal
            .userId(UPDATED_USER_ID)
            .orderId(UPDATED_ORDER_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .renewalDate(UPDATED_RENEWAL_DATE)
            .paidRenewalAmount(UPDATED_PAID_RENEWAL_AMOUNT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(updatedSubscriptionRenewal);

        restSubscriptionRenewalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionRenewalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionRenewal testSubscriptionRenewal = subscriptionRenewalList.get(subscriptionRenewalList.size() - 1);
        assertThat(testSubscriptionRenewal.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSubscriptionRenewal.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testSubscriptionRenewal.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSubscriptionRenewal.getRenewalDate()).isEqualTo(UPDATED_RENEWAL_DATE);
        assertThat(testSubscriptionRenewal.getPaidRenewalAmount()).isEqualTo(UPDATED_PAID_RENEWAL_AMOUNT);
        assertThat(testSubscriptionRenewal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSubscriptionRenewal.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubscriptionRenewal.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSubscriptionRenewal.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubscriptionRenewal.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSubscriptionRenewal() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();
        subscriptionRenewal.setId(longCount.incrementAndGet());

        // Create the SubscriptionRenewal
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionRenewalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriptionRenewalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriptionRenewal() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();
        subscriptionRenewal.setId(longCount.incrementAndGet());

        // Create the SubscriptionRenewal
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionRenewalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriptionRenewal() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();
        subscriptionRenewal.setId(longCount.incrementAndGet());

        // Create the SubscriptionRenewal
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionRenewalMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriptionRenewalWithPatch() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();

        // Update the subscriptionRenewal using partial update
        SubscriptionRenewal partialUpdatedSubscriptionRenewal = new SubscriptionRenewal();
        partialUpdatedSubscriptionRenewal.setId(subscriptionRenewal.getId());

        partialUpdatedSubscriptionRenewal
            .userId(UPDATED_USER_ID)
            .orderId(UPDATED_ORDER_ID)
            .renewalDate(UPDATED_RENEWAL_DATE)
            .startDate(UPDATED_START_DATE)
            .updatedAt(UPDATED_UPDATED_AT);

        restSubscriptionRenewalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionRenewal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionRenewal))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionRenewal testSubscriptionRenewal = subscriptionRenewalList.get(subscriptionRenewalList.size() - 1);
        assertThat(testSubscriptionRenewal.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSubscriptionRenewal.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testSubscriptionRenewal.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSubscriptionRenewal.getRenewalDate()).isEqualTo(UPDATED_RENEWAL_DATE);
        assertThat(testSubscriptionRenewal.getPaidRenewalAmount()).isEqualTo(DEFAULT_PAID_RENEWAL_AMOUNT);
        assertThat(testSubscriptionRenewal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSubscriptionRenewal.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubscriptionRenewal.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSubscriptionRenewal.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubscriptionRenewal.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSubscriptionRenewalWithPatch() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();

        // Update the subscriptionRenewal using partial update
        SubscriptionRenewal partialUpdatedSubscriptionRenewal = new SubscriptionRenewal();
        partialUpdatedSubscriptionRenewal.setId(subscriptionRenewal.getId());

        partialUpdatedSubscriptionRenewal
            .userId(UPDATED_USER_ID)
            .orderId(UPDATED_ORDER_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .renewalDate(UPDATED_RENEWAL_DATE)
            .paidRenewalAmount(UPDATED_PAID_RENEWAL_AMOUNT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSubscriptionRenewalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriptionRenewal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionRenewal))
            )
            .andExpect(status().isOk());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionRenewal testSubscriptionRenewal = subscriptionRenewalList.get(subscriptionRenewalList.size() - 1);
        assertThat(testSubscriptionRenewal.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testSubscriptionRenewal.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testSubscriptionRenewal.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSubscriptionRenewal.getRenewalDate()).isEqualTo(UPDATED_RENEWAL_DATE);
        assertThat(testSubscriptionRenewal.getPaidRenewalAmount()).isEqualTo(UPDATED_PAID_RENEWAL_AMOUNT);
        assertThat(testSubscriptionRenewal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSubscriptionRenewal.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubscriptionRenewal.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSubscriptionRenewal.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubscriptionRenewal.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSubscriptionRenewal() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();
        subscriptionRenewal.setId(longCount.incrementAndGet());

        // Create the SubscriptionRenewal
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriptionRenewalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriptionRenewalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriptionRenewal() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();
        subscriptionRenewal.setId(longCount.incrementAndGet());

        // Create the SubscriptionRenewal
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionRenewalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriptionRenewal() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionRenewalRepository.findAll().size();
        subscriptionRenewal.setId(longCount.incrementAndGet());

        // Create the SubscriptionRenewal
        SubscriptionRenewalDTO subscriptionRenewalDTO = subscriptionRenewalMapper.toDto(subscriptionRenewal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriptionRenewalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriptionRenewalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscriptionRenewal in the database
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriptionRenewal() throws Exception {
        // Initialize the database
        subscriptionRenewalRepository.saveAndFlush(subscriptionRenewal);

        int databaseSizeBeforeDelete = subscriptionRenewalRepository.findAll().size();

        // Delete the subscriptionRenewal
        restSubscriptionRenewalMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriptionRenewal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscriptionRenewal> subscriptionRenewalList = subscriptionRenewalRepository.findAll();
        assertThat(subscriptionRenewalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
