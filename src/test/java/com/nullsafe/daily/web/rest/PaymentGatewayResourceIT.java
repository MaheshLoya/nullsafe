package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.PaymentGateway;
import com.nullsafe.daily.repository.PaymentGatewayRepository;
import com.nullsafe.daily.service.dto.PaymentGatewayDTO;
import com.nullsafe.daily.service.mapper.PaymentGatewayMapper;
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
 * Integration tests for the {@link PaymentGatewayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentGatewayResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_ID = "AAAAAAAAAA";
    private static final String UPDATED_KEY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SECRET_ID = "AAAAAAAAAA";
    private static final String UPDATED_SECRET_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/payment-gateways";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentGatewayRepository paymentGatewayRepository;

    @Autowired
    private PaymentGatewayMapper paymentGatewayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentGatewayMockMvc;

    private PaymentGateway paymentGateway;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentGateway createEntity(EntityManager em) {
        PaymentGateway paymentGateway = new PaymentGateway()
            .active(DEFAULT_ACTIVE)
            .title(DEFAULT_TITLE)
            .keyId(DEFAULT_KEY_ID)
            .secretId(DEFAULT_SECRET_ID)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return paymentGateway;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentGateway createUpdatedEntity(EntityManager em) {
        PaymentGateway paymentGateway = new PaymentGateway()
            .active(UPDATED_ACTIVE)
            .title(UPDATED_TITLE)
            .keyId(UPDATED_KEY_ID)
            .secretId(UPDATED_SECRET_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return paymentGateway;
    }

    @BeforeEach
    public void initTest() {
        paymentGateway = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentGateway() throws Exception {
        int databaseSizeBeforeCreate = paymentGatewayRepository.findAll().size();
        // Create the PaymentGateway
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);
        restPaymentGatewayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentGateway testPaymentGateway = paymentGatewayList.get(paymentGatewayList.size() - 1);
        assertThat(testPaymentGateway.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPaymentGateway.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPaymentGateway.getKeyId()).isEqualTo(DEFAULT_KEY_ID);
        assertThat(testPaymentGateway.getSecretId()).isEqualTo(DEFAULT_SECRET_ID);
        assertThat(testPaymentGateway.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPaymentGateway.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createPaymentGatewayWithExistingId() throws Exception {
        // Create the PaymentGateway with an existing ID
        paymentGateway.setId(1L);
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        int databaseSizeBeforeCreate = paymentGatewayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentGatewayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentGatewayRepository.findAll().size();
        // set the field null
        paymentGateway.setActive(null);

        // Create the PaymentGateway, which fails.
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        restPaymentGatewayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentGatewayRepository.findAll().size();
        // set the field null
        paymentGateway.setTitle(null);

        // Create the PaymentGateway, which fails.
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        restPaymentGatewayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKeyIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentGatewayRepository.findAll().size();
        // set the field null
        paymentGateway.setKeyId(null);

        // Create the PaymentGateway, which fails.
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        restPaymentGatewayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSecretIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentGatewayRepository.findAll().size();
        // set the field null
        paymentGateway.setSecretId(null);

        // Create the PaymentGateway, which fails.
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        restPaymentGatewayMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentGateways() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList
        restPaymentGatewayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentGateway.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].keyId").value(hasItem(DEFAULT_KEY_ID)))
            .andExpect(jsonPath("$.[*].secretId").value(hasItem(DEFAULT_SECRET_ID)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getPaymentGateway() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get the paymentGateway
        restPaymentGatewayMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentGateway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentGateway.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.keyId").value(DEFAULT_KEY_ID))
            .andExpect(jsonPath("$.secretId").value(DEFAULT_SECRET_ID))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getPaymentGatewaysByIdFiltering() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        Long id = paymentGateway.getId();

        defaultPaymentGatewayShouldBeFound("id.equals=" + id);
        defaultPaymentGatewayShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentGatewayShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentGatewayShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentGatewayShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentGatewayShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where active equals to DEFAULT_ACTIVE
        defaultPaymentGatewayShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the paymentGatewayList where active equals to UPDATED_ACTIVE
        defaultPaymentGatewayShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultPaymentGatewayShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the paymentGatewayList where active equals to UPDATED_ACTIVE
        defaultPaymentGatewayShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where active is not null
        defaultPaymentGatewayShouldBeFound("active.specified=true");

        // Get all the paymentGatewayList where active is null
        defaultPaymentGatewayShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where title equals to DEFAULT_TITLE
        defaultPaymentGatewayShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the paymentGatewayList where title equals to UPDATED_TITLE
        defaultPaymentGatewayShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPaymentGatewayShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the paymentGatewayList where title equals to UPDATED_TITLE
        defaultPaymentGatewayShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where title is not null
        defaultPaymentGatewayShouldBeFound("title.specified=true");

        // Get all the paymentGatewayList where title is null
        defaultPaymentGatewayShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByTitleContainsSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where title contains DEFAULT_TITLE
        defaultPaymentGatewayShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the paymentGatewayList where title contains UPDATED_TITLE
        defaultPaymentGatewayShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where title does not contain DEFAULT_TITLE
        defaultPaymentGatewayShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the paymentGatewayList where title does not contain UPDATED_TITLE
        defaultPaymentGatewayShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByKeyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where keyId equals to DEFAULT_KEY_ID
        defaultPaymentGatewayShouldBeFound("keyId.equals=" + DEFAULT_KEY_ID);

        // Get all the paymentGatewayList where keyId equals to UPDATED_KEY_ID
        defaultPaymentGatewayShouldNotBeFound("keyId.equals=" + UPDATED_KEY_ID);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByKeyIdIsInShouldWork() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where keyId in DEFAULT_KEY_ID or UPDATED_KEY_ID
        defaultPaymentGatewayShouldBeFound("keyId.in=" + DEFAULT_KEY_ID + "," + UPDATED_KEY_ID);

        // Get all the paymentGatewayList where keyId equals to UPDATED_KEY_ID
        defaultPaymentGatewayShouldNotBeFound("keyId.in=" + UPDATED_KEY_ID);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByKeyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where keyId is not null
        defaultPaymentGatewayShouldBeFound("keyId.specified=true");

        // Get all the paymentGatewayList where keyId is null
        defaultPaymentGatewayShouldNotBeFound("keyId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByKeyIdContainsSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where keyId contains DEFAULT_KEY_ID
        defaultPaymentGatewayShouldBeFound("keyId.contains=" + DEFAULT_KEY_ID);

        // Get all the paymentGatewayList where keyId contains UPDATED_KEY_ID
        defaultPaymentGatewayShouldNotBeFound("keyId.contains=" + UPDATED_KEY_ID);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByKeyIdNotContainsSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where keyId does not contain DEFAULT_KEY_ID
        defaultPaymentGatewayShouldNotBeFound("keyId.doesNotContain=" + DEFAULT_KEY_ID);

        // Get all the paymentGatewayList where keyId does not contain UPDATED_KEY_ID
        defaultPaymentGatewayShouldBeFound("keyId.doesNotContain=" + UPDATED_KEY_ID);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysBySecretIdIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where secretId equals to DEFAULT_SECRET_ID
        defaultPaymentGatewayShouldBeFound("secretId.equals=" + DEFAULT_SECRET_ID);

        // Get all the paymentGatewayList where secretId equals to UPDATED_SECRET_ID
        defaultPaymentGatewayShouldNotBeFound("secretId.equals=" + UPDATED_SECRET_ID);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysBySecretIdIsInShouldWork() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where secretId in DEFAULT_SECRET_ID or UPDATED_SECRET_ID
        defaultPaymentGatewayShouldBeFound("secretId.in=" + DEFAULT_SECRET_ID + "," + UPDATED_SECRET_ID);

        // Get all the paymentGatewayList where secretId equals to UPDATED_SECRET_ID
        defaultPaymentGatewayShouldNotBeFound("secretId.in=" + UPDATED_SECRET_ID);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysBySecretIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where secretId is not null
        defaultPaymentGatewayShouldBeFound("secretId.specified=true");

        // Get all the paymentGatewayList where secretId is null
        defaultPaymentGatewayShouldNotBeFound("secretId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysBySecretIdContainsSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where secretId contains DEFAULT_SECRET_ID
        defaultPaymentGatewayShouldBeFound("secretId.contains=" + DEFAULT_SECRET_ID);

        // Get all the paymentGatewayList where secretId contains UPDATED_SECRET_ID
        defaultPaymentGatewayShouldNotBeFound("secretId.contains=" + UPDATED_SECRET_ID);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysBySecretIdNotContainsSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where secretId does not contain DEFAULT_SECRET_ID
        defaultPaymentGatewayShouldNotBeFound("secretId.doesNotContain=" + DEFAULT_SECRET_ID);

        // Get all the paymentGatewayList where secretId does not contain UPDATED_SECRET_ID
        defaultPaymentGatewayShouldBeFound("secretId.doesNotContain=" + UPDATED_SECRET_ID);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where createdAt equals to DEFAULT_CREATED_AT
        defaultPaymentGatewayShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the paymentGatewayList where createdAt equals to UPDATED_CREATED_AT
        defaultPaymentGatewayShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPaymentGatewayShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the paymentGatewayList where createdAt equals to UPDATED_CREATED_AT
        defaultPaymentGatewayShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where createdAt is not null
        defaultPaymentGatewayShouldBeFound("createdAt.specified=true");

        // Get all the paymentGatewayList where createdAt is null
        defaultPaymentGatewayShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultPaymentGatewayShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the paymentGatewayList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPaymentGatewayShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultPaymentGatewayShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the paymentGatewayList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPaymentGatewayShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPaymentGatewaysByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        // Get all the paymentGatewayList where updatedAt is not null
        defaultPaymentGatewayShouldBeFound("updatedAt.specified=true");

        // Get all the paymentGatewayList where updatedAt is null
        defaultPaymentGatewayShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentGatewayShouldBeFound(String filter) throws Exception {
        restPaymentGatewayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentGateway.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].keyId").value(hasItem(DEFAULT_KEY_ID)))
            .andExpect(jsonPath("$.[*].secretId").value(hasItem(DEFAULT_SECRET_ID)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restPaymentGatewayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentGatewayShouldNotBeFound(String filter) throws Exception {
        restPaymentGatewayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentGatewayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentGateway() throws Exception {
        // Get the paymentGateway
        restPaymentGatewayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentGateway() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();

        // Update the paymentGateway
        PaymentGateway updatedPaymentGateway = paymentGatewayRepository.findById(paymentGateway.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentGateway are not directly saved in db
        em.detach(updatedPaymentGateway);
        updatedPaymentGateway
            .active(UPDATED_ACTIVE)
            .title(UPDATED_TITLE)
            .keyId(UPDATED_KEY_ID)
            .secretId(UPDATED_SECRET_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(updatedPaymentGateway);

        restPaymentGatewayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentGatewayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
        PaymentGateway testPaymentGateway = paymentGatewayList.get(paymentGatewayList.size() - 1);
        assertThat(testPaymentGateway.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPaymentGateway.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPaymentGateway.getKeyId()).isEqualTo(UPDATED_KEY_ID);
        assertThat(testPaymentGateway.getSecretId()).isEqualTo(UPDATED_SECRET_ID);
        assertThat(testPaymentGateway.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPaymentGateway.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingPaymentGateway() throws Exception {
        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();
        paymentGateway.setId(longCount.incrementAndGet());

        // Create the PaymentGateway
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentGatewayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentGatewayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentGateway() throws Exception {
        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();
        paymentGateway.setId(longCount.incrementAndGet());

        // Create the PaymentGateway
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentGateway() throws Exception {
        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();
        paymentGateway.setId(longCount.incrementAndGet());

        // Create the PaymentGateway
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentGatewayWithPatch() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();

        // Update the paymentGateway using partial update
        PaymentGateway partialUpdatedPaymentGateway = new PaymentGateway();
        partialUpdatedPaymentGateway.setId(paymentGateway.getId());

        partialUpdatedPaymentGateway.active(UPDATED_ACTIVE).title(UPDATED_TITLE).secretId(UPDATED_SECRET_ID);

        restPaymentGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentGateway.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentGateway))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
        PaymentGateway testPaymentGateway = paymentGatewayList.get(paymentGatewayList.size() - 1);
        assertThat(testPaymentGateway.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPaymentGateway.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPaymentGateway.getKeyId()).isEqualTo(DEFAULT_KEY_ID);
        assertThat(testPaymentGateway.getSecretId()).isEqualTo(UPDATED_SECRET_ID);
        assertThat(testPaymentGateway.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPaymentGateway.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdatePaymentGatewayWithPatch() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();

        // Update the paymentGateway using partial update
        PaymentGateway partialUpdatedPaymentGateway = new PaymentGateway();
        partialUpdatedPaymentGateway.setId(paymentGateway.getId());

        partialUpdatedPaymentGateway
            .active(UPDATED_ACTIVE)
            .title(UPDATED_TITLE)
            .keyId(UPDATED_KEY_ID)
            .secretId(UPDATED_SECRET_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restPaymentGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentGateway.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentGateway))
            )
            .andExpect(status().isOk());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
        PaymentGateway testPaymentGateway = paymentGatewayList.get(paymentGatewayList.size() - 1);
        assertThat(testPaymentGateway.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPaymentGateway.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPaymentGateway.getKeyId()).isEqualTo(UPDATED_KEY_ID);
        assertThat(testPaymentGateway.getSecretId()).isEqualTo(UPDATED_SECRET_ID);
        assertThat(testPaymentGateway.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPaymentGateway.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentGateway() throws Exception {
        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();
        paymentGateway.setId(longCount.incrementAndGet());

        // Create the PaymentGateway
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentGatewayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentGateway() throws Exception {
        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();
        paymentGateway.setId(longCount.incrementAndGet());

        // Create the PaymentGateway
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentGateway() throws Exception {
        int databaseSizeBeforeUpdate = paymentGatewayRepository.findAll().size();
        paymentGateway.setId(longCount.incrementAndGet());

        // Create the PaymentGateway
        PaymentGatewayDTO paymentGatewayDTO = paymentGatewayMapper.toDto(paymentGateway);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentGatewayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentGateway in the database
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentGateway() throws Exception {
        // Initialize the database
        paymentGatewayRepository.saveAndFlush(paymentGateway);

        int databaseSizeBeforeDelete = paymentGatewayRepository.findAll().size();

        // Delete the paymentGateway
        restPaymentGatewayMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentGateway.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentGateway> paymentGatewayList = paymentGatewayRepository.findAll();
        assertThat(paymentGatewayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
