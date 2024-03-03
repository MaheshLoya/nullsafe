package com.nullsafe.daily.web.rest;

import static com.nullsafe.daily.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Refunds;
import com.nullsafe.daily.repository.RefundsRepository;
import com.nullsafe.daily.service.dto.RefundsDTO;
import com.nullsafe.daily.service.mapper.RefundsMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link RefundsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RefundsResourceIT {

    private static final Integer DEFAULT_ORDER_ID = 1;
    private static final Integer UPDATED_ORDER_ID = 2;
    private static final Integer SMALLER_ORDER_ID = 1 - 1;

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RAZORPAY_REFUND_ID = "AAAAAAAAAA";
    private static final String UPDATED_RAZORPAY_REFUND_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RAZORPAY_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_RAZORPAY_PAYMENT_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/refunds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private RefundsRepository refundsRepository;

    @Autowired
    private RefundsMapper refundsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRefundsMockMvc;

    private Refunds refunds;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refunds createEntity(EntityManager em) {
        Refunds refunds = new Refunds()
            .orderId(DEFAULT_ORDER_ID)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .razorpayRefundId(DEFAULT_RAZORPAY_REFUND_ID)
            .razorpayPaymentId(DEFAULT_RAZORPAY_PAYMENT_ID)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return refunds;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refunds createUpdatedEntity(EntityManager em) {
        Refunds refunds = new Refunds()
            .orderId(UPDATED_ORDER_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .razorpayRefundId(UPDATED_RAZORPAY_REFUND_ID)
            .razorpayPaymentId(UPDATED_RAZORPAY_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return refunds;
    }

    @BeforeEach
    public void initTest() {
        refunds = createEntity(em);
    }

    @Test
    @Transactional
    void createRefunds() throws Exception {
        int databaseSizeBeforeCreate = refundsRepository.findAll().size();
        // Create the Refunds
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);
        restRefundsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundsDTO)))
            .andExpect(status().isCreated());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeCreate + 1);
        Refunds testRefunds = refundsList.get(refundsList.size() - 1);
        assertThat(testRefunds.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testRefunds.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testRefunds.getRazorpayRefundId()).isEqualTo(DEFAULT_RAZORPAY_REFUND_ID);
        assertThat(testRefunds.getRazorpayPaymentId()).isEqualTo(DEFAULT_RAZORPAY_PAYMENT_ID);
        assertThat(testRefunds.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testRefunds.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testRefunds.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRefunds.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRefunds.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRefunds.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createRefundsWithExistingId() throws Exception {
        // Create the Refunds with an existing ID
        refunds.setId(1);
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        int databaseSizeBeforeCreate = refundsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefundsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundsRepository.findAll().size();
        // set the field null
        refunds.setCreatedAt(null);

        // Create the Refunds, which fails.
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        restRefundsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundsDTO)))
            .andExpect(status().isBadRequest());

        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundsRepository.findAll().size();
        // set the field null
        refunds.setUpdatedAt(null);

        // Create the Refunds, which fails.
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        restRefundsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundsDTO)))
            .andExpect(status().isBadRequest());

        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRefunds() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList
        restRefundsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refunds.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].razorpayRefundId").value(hasItem(DEFAULT_RAZORPAY_REFUND_ID)))
            .andExpect(jsonPath("$.[*].razorpayPaymentId").value(hasItem(DEFAULT_RAZORPAY_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getRefunds() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get the refunds
        restRefundsMockMvc
            .perform(get(ENTITY_API_URL_ID, refunds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(refunds.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.razorpayRefundId").value(DEFAULT_RAZORPAY_REFUND_ID))
            .andExpect(jsonPath("$.razorpayPaymentId").value(DEFAULT_RAZORPAY_PAYMENT_ID))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getRefundsByIdFiltering() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        Integer id = refunds.getId();

        defaultRefundsShouldBeFound("id.equals=" + id);
        defaultRefundsShouldNotBeFound("id.notEquals=" + id);

        defaultRefundsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRefundsShouldNotBeFound("id.greaterThan=" + id);

        defaultRefundsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRefundsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRefundsByOrderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where orderId equals to DEFAULT_ORDER_ID
        defaultRefundsShouldBeFound("orderId.equals=" + DEFAULT_ORDER_ID);

        // Get all the refundsList where orderId equals to UPDATED_ORDER_ID
        defaultRefundsShouldNotBeFound("orderId.equals=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByOrderIdIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where orderId in DEFAULT_ORDER_ID or UPDATED_ORDER_ID
        defaultRefundsShouldBeFound("orderId.in=" + DEFAULT_ORDER_ID + "," + UPDATED_ORDER_ID);

        // Get all the refundsList where orderId equals to UPDATED_ORDER_ID
        defaultRefundsShouldNotBeFound("orderId.in=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByOrderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where orderId is not null
        defaultRefundsShouldBeFound("orderId.specified=true");

        // Get all the refundsList where orderId is null
        defaultRefundsShouldNotBeFound("orderId.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByOrderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where orderId is greater than or equal to DEFAULT_ORDER_ID
        defaultRefundsShouldBeFound("orderId.greaterThanOrEqual=" + DEFAULT_ORDER_ID);

        // Get all the refundsList where orderId is greater than or equal to UPDATED_ORDER_ID
        defaultRefundsShouldNotBeFound("orderId.greaterThanOrEqual=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByOrderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where orderId is less than or equal to DEFAULT_ORDER_ID
        defaultRefundsShouldBeFound("orderId.lessThanOrEqual=" + DEFAULT_ORDER_ID);

        // Get all the refundsList where orderId is less than or equal to SMALLER_ORDER_ID
        defaultRefundsShouldNotBeFound("orderId.lessThanOrEqual=" + SMALLER_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByOrderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where orderId is less than DEFAULT_ORDER_ID
        defaultRefundsShouldNotBeFound("orderId.lessThan=" + DEFAULT_ORDER_ID);

        // Get all the refundsList where orderId is less than UPDATED_ORDER_ID
        defaultRefundsShouldBeFound("orderId.lessThan=" + UPDATED_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByOrderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where orderId is greater than DEFAULT_ORDER_ID
        defaultRefundsShouldNotBeFound("orderId.greaterThan=" + DEFAULT_ORDER_ID);

        // Get all the refundsList where orderId is greater than SMALLER_ORDER_ID
        defaultRefundsShouldBeFound("orderId.greaterThan=" + SMALLER_ORDER_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where transactionId equals to DEFAULT_TRANSACTION_ID
        defaultRefundsShouldBeFound("transactionId.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the refundsList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultRefundsShouldNotBeFound("transactionId.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where transactionId in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultRefundsShouldBeFound("transactionId.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the refundsList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultRefundsShouldNotBeFound("transactionId.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where transactionId is not null
        defaultRefundsShouldBeFound("transactionId.specified=true");

        // Get all the refundsList where transactionId is null
        defaultRefundsShouldNotBeFound("transactionId.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionIdContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where transactionId contains DEFAULT_TRANSACTION_ID
        defaultRefundsShouldBeFound("transactionId.contains=" + DEFAULT_TRANSACTION_ID);

        // Get all the refundsList where transactionId contains UPDATED_TRANSACTION_ID
        defaultRefundsShouldNotBeFound("transactionId.contains=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionIdNotContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where transactionId does not contain DEFAULT_TRANSACTION_ID
        defaultRefundsShouldNotBeFound("transactionId.doesNotContain=" + DEFAULT_TRANSACTION_ID);

        // Get all the refundsList where transactionId does not contain UPDATED_TRANSACTION_ID
        defaultRefundsShouldBeFound("transactionId.doesNotContain=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayRefundIdIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayRefundId equals to DEFAULT_RAZORPAY_REFUND_ID
        defaultRefundsShouldBeFound("razorpayRefundId.equals=" + DEFAULT_RAZORPAY_REFUND_ID);

        // Get all the refundsList where razorpayRefundId equals to UPDATED_RAZORPAY_REFUND_ID
        defaultRefundsShouldNotBeFound("razorpayRefundId.equals=" + UPDATED_RAZORPAY_REFUND_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayRefundIdIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayRefundId in DEFAULT_RAZORPAY_REFUND_ID or UPDATED_RAZORPAY_REFUND_ID
        defaultRefundsShouldBeFound("razorpayRefundId.in=" + DEFAULT_RAZORPAY_REFUND_ID + "," + UPDATED_RAZORPAY_REFUND_ID);

        // Get all the refundsList where razorpayRefundId equals to UPDATED_RAZORPAY_REFUND_ID
        defaultRefundsShouldNotBeFound("razorpayRefundId.in=" + UPDATED_RAZORPAY_REFUND_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayRefundIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayRefundId is not null
        defaultRefundsShouldBeFound("razorpayRefundId.specified=true");

        // Get all the refundsList where razorpayRefundId is null
        defaultRefundsShouldNotBeFound("razorpayRefundId.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayRefundIdContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayRefundId contains DEFAULT_RAZORPAY_REFUND_ID
        defaultRefundsShouldBeFound("razorpayRefundId.contains=" + DEFAULT_RAZORPAY_REFUND_ID);

        // Get all the refundsList where razorpayRefundId contains UPDATED_RAZORPAY_REFUND_ID
        defaultRefundsShouldNotBeFound("razorpayRefundId.contains=" + UPDATED_RAZORPAY_REFUND_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayRefundIdNotContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayRefundId does not contain DEFAULT_RAZORPAY_REFUND_ID
        defaultRefundsShouldNotBeFound("razorpayRefundId.doesNotContain=" + DEFAULT_RAZORPAY_REFUND_ID);

        // Get all the refundsList where razorpayRefundId does not contain UPDATED_RAZORPAY_REFUND_ID
        defaultRefundsShouldBeFound("razorpayRefundId.doesNotContain=" + UPDATED_RAZORPAY_REFUND_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayPaymentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayPaymentId equals to DEFAULT_RAZORPAY_PAYMENT_ID
        defaultRefundsShouldBeFound("razorpayPaymentId.equals=" + DEFAULT_RAZORPAY_PAYMENT_ID);

        // Get all the refundsList where razorpayPaymentId equals to UPDATED_RAZORPAY_PAYMENT_ID
        defaultRefundsShouldNotBeFound("razorpayPaymentId.equals=" + UPDATED_RAZORPAY_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayPaymentIdIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayPaymentId in DEFAULT_RAZORPAY_PAYMENT_ID or UPDATED_RAZORPAY_PAYMENT_ID
        defaultRefundsShouldBeFound("razorpayPaymentId.in=" + DEFAULT_RAZORPAY_PAYMENT_ID + "," + UPDATED_RAZORPAY_PAYMENT_ID);

        // Get all the refundsList where razorpayPaymentId equals to UPDATED_RAZORPAY_PAYMENT_ID
        defaultRefundsShouldNotBeFound("razorpayPaymentId.in=" + UPDATED_RAZORPAY_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayPaymentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayPaymentId is not null
        defaultRefundsShouldBeFound("razorpayPaymentId.specified=true");

        // Get all the refundsList where razorpayPaymentId is null
        defaultRefundsShouldNotBeFound("razorpayPaymentId.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayPaymentIdContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayPaymentId contains DEFAULT_RAZORPAY_PAYMENT_ID
        defaultRefundsShouldBeFound("razorpayPaymentId.contains=" + DEFAULT_RAZORPAY_PAYMENT_ID);

        // Get all the refundsList where razorpayPaymentId contains UPDATED_RAZORPAY_PAYMENT_ID
        defaultRefundsShouldNotBeFound("razorpayPaymentId.contains=" + UPDATED_RAZORPAY_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByRazorpayPaymentIdNotContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where razorpayPaymentId does not contain DEFAULT_RAZORPAY_PAYMENT_ID
        defaultRefundsShouldNotBeFound("razorpayPaymentId.doesNotContain=" + DEFAULT_RAZORPAY_PAYMENT_ID);

        // Get all the refundsList where razorpayPaymentId does not contain UPDATED_RAZORPAY_PAYMENT_ID
        defaultRefundsShouldBeFound("razorpayPaymentId.doesNotContain=" + UPDATED_RAZORPAY_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllRefundsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where amount equals to DEFAULT_AMOUNT
        defaultRefundsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the refundsList where amount equals to UPDATED_AMOUNT
        defaultRefundsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRefundsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultRefundsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the refundsList where amount equals to UPDATED_AMOUNT
        defaultRefundsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRefundsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where amount is not null
        defaultRefundsShouldBeFound("amount.specified=true");

        // Get all the refundsList where amount is null
        defaultRefundsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultRefundsShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the refundsList where amount is greater than or equal to UPDATED_AMOUNT
        defaultRefundsShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRefundsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where amount is less than or equal to DEFAULT_AMOUNT
        defaultRefundsShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the refundsList where amount is less than or equal to SMALLER_AMOUNT
        defaultRefundsShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRefundsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where amount is less than DEFAULT_AMOUNT
        defaultRefundsShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the refundsList where amount is less than UPDATED_AMOUNT
        defaultRefundsShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRefundsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where amount is greater than DEFAULT_AMOUNT
        defaultRefundsShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the refundsList where amount is greater than SMALLER_AMOUNT
        defaultRefundsShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllRefundsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where currency equals to DEFAULT_CURRENCY
        defaultRefundsShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the refundsList where currency equals to UPDATED_CURRENCY
        defaultRefundsShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllRefundsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultRefundsShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the refundsList where currency equals to UPDATED_CURRENCY
        defaultRefundsShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllRefundsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where currency is not null
        defaultRefundsShouldBeFound("currency.specified=true");

        // Get all the refundsList where currency is null
        defaultRefundsShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where currency contains DEFAULT_CURRENCY
        defaultRefundsShouldBeFound("currency.contains=" + DEFAULT_CURRENCY);

        // Get all the refundsList where currency contains UPDATED_CURRENCY
        defaultRefundsShouldNotBeFound("currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllRefundsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where currency does not contain DEFAULT_CURRENCY
        defaultRefundsShouldNotBeFound("currency.doesNotContain=" + DEFAULT_CURRENCY);

        // Get all the refundsList where currency does not contain UPDATED_CURRENCY
        defaultRefundsShouldBeFound("currency.doesNotContain=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllRefundsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where status equals to DEFAULT_STATUS
        defaultRefundsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the refundsList where status equals to UPDATED_STATUS
        defaultRefundsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRefundsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultRefundsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the refundsList where status equals to UPDATED_STATUS
        defaultRefundsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRefundsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where status is not null
        defaultRefundsShouldBeFound("status.specified=true");

        // Get all the refundsList where status is null
        defaultRefundsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByStatusContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where status contains DEFAULT_STATUS
        defaultRefundsShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the refundsList where status contains UPDATED_STATUS
        defaultRefundsShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRefundsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where status does not contain DEFAULT_STATUS
        defaultRefundsShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the refundsList where status does not contain UPDATED_STATUS
        defaultRefundsShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where createdBy equals to DEFAULT_CREATED_BY
        defaultRefundsShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the refundsList where createdBy equals to UPDATED_CREATED_BY
        defaultRefundsShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultRefundsShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the refundsList where createdBy equals to UPDATED_CREATED_BY
        defaultRefundsShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where createdBy is not null
        defaultRefundsShouldBeFound("createdBy.specified=true");

        // Get all the refundsList where createdBy is null
        defaultRefundsShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where createdBy contains DEFAULT_CREATED_BY
        defaultRefundsShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the refundsList where createdBy contains UPDATED_CREATED_BY
        defaultRefundsShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where createdBy does not contain DEFAULT_CREATED_BY
        defaultRefundsShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the refundsList where createdBy does not contain UPDATED_CREATED_BY
        defaultRefundsShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where createdAt equals to DEFAULT_CREATED_AT
        defaultRefundsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the refundsList where createdAt equals to UPDATED_CREATED_AT
        defaultRefundsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultRefundsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the refundsList where createdAt equals to UPDATED_CREATED_AT
        defaultRefundsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where createdAt is not null
        defaultRefundsShouldBeFound("createdAt.specified=true");

        // Get all the refundsList where createdAt is null
        defaultRefundsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultRefundsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the refundsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultRefundsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllRefundsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultRefundsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the refundsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultRefundsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllRefundsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        // Get all the refundsList where updatedAt is not null
        defaultRefundsShouldBeFound("updatedAt.specified=true");

        // Get all the refundsList where updatedAt is null
        defaultRefundsShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRefundsShouldBeFound(String filter) throws Exception {
        restRefundsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refunds.getId())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].razorpayRefundId").value(hasItem(DEFAULT_RAZORPAY_REFUND_ID)))
            .andExpect(jsonPath("$.[*].razorpayPaymentId").value(hasItem(DEFAULT_RAZORPAY_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restRefundsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRefundsShouldNotBeFound(String filter) throws Exception {
        restRefundsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRefundsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRefunds() throws Exception {
        // Get the refunds
        restRefundsMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRefunds() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();

        // Update the refunds
        Refunds updatedRefunds = refundsRepository.findById(refunds.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRefunds are not directly saved in db
        em.detach(updatedRefunds);
        updatedRefunds
            .orderId(UPDATED_ORDER_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .razorpayRefundId(UPDATED_RAZORPAY_REFUND_ID)
            .razorpayPaymentId(UPDATED_RAZORPAY_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        RefundsDTO refundsDTO = refundsMapper.toDto(updatedRefunds);

        restRefundsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refundsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refundsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
        Refunds testRefunds = refundsList.get(refundsList.size() - 1);
        assertThat(testRefunds.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testRefunds.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testRefunds.getRazorpayRefundId()).isEqualTo(UPDATED_RAZORPAY_REFUND_ID);
        assertThat(testRefunds.getRazorpayPaymentId()).isEqualTo(UPDATED_RAZORPAY_PAYMENT_ID);
        assertThat(testRefunds.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testRefunds.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testRefunds.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRefunds.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRefunds.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRefunds.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingRefunds() throws Exception {
        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();
        refunds.setId(intCount.incrementAndGet());

        // Create the Refunds
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefundsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refundsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refundsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRefunds() throws Exception {
        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();
        refunds.setId(intCount.incrementAndGet());

        // Create the Refunds
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refundsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRefunds() throws Exception {
        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();
        refunds.setId(intCount.incrementAndGet());

        // Create the Refunds
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRefundsWithPatch() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();

        // Update the refunds using partial update
        Refunds partialUpdatedRefunds = new Refunds();
        partialUpdatedRefunds.setId(refunds.getId());

        partialUpdatedRefunds.currency(UPDATED_CURRENCY).status(UPDATED_STATUS).updatedAt(UPDATED_UPDATED_AT);

        restRefundsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefunds.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefunds))
            )
            .andExpect(status().isOk());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
        Refunds testRefunds = refundsList.get(refundsList.size() - 1);
        assertThat(testRefunds.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testRefunds.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testRefunds.getRazorpayRefundId()).isEqualTo(DEFAULT_RAZORPAY_REFUND_ID);
        assertThat(testRefunds.getRazorpayPaymentId()).isEqualTo(DEFAULT_RAZORPAY_PAYMENT_ID);
        assertThat(testRefunds.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testRefunds.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testRefunds.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRefunds.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRefunds.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRefunds.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateRefundsWithPatch() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();

        // Update the refunds using partial update
        Refunds partialUpdatedRefunds = new Refunds();
        partialUpdatedRefunds.setId(refunds.getId());

        partialUpdatedRefunds
            .orderId(UPDATED_ORDER_ID)
            .transactionId(UPDATED_TRANSACTION_ID)
            .razorpayRefundId(UPDATED_RAZORPAY_REFUND_ID)
            .razorpayPaymentId(UPDATED_RAZORPAY_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restRefundsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefunds.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefunds))
            )
            .andExpect(status().isOk());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
        Refunds testRefunds = refundsList.get(refundsList.size() - 1);
        assertThat(testRefunds.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testRefunds.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testRefunds.getRazorpayRefundId()).isEqualTo(UPDATED_RAZORPAY_REFUND_ID);
        assertThat(testRefunds.getRazorpayPaymentId()).isEqualTo(UPDATED_RAZORPAY_PAYMENT_ID);
        assertThat(testRefunds.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testRefunds.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testRefunds.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRefunds.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRefunds.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRefunds.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingRefunds() throws Exception {
        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();
        refunds.setId(intCount.incrementAndGet());

        // Create the Refunds
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefundsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, refundsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refundsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRefunds() throws Exception {
        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();
        refunds.setId(intCount.incrementAndGet());

        // Create the Refunds
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refundsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRefunds() throws Exception {
        int databaseSizeBeforeUpdate = refundsRepository.findAll().size();
        refunds.setId(intCount.incrementAndGet());

        // Create the Refunds
        RefundsDTO refundsDTO = refundsMapper.toDto(refunds);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(refundsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refunds in the database
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRefunds() throws Exception {
        // Initialize the database
        refundsRepository.saveAndFlush(refunds);

        int databaseSizeBeforeDelete = refundsRepository.findAll().size();

        // Delete the refunds
        restRefundsMockMvc
            .perform(delete(ENTITY_API_URL_ID, refunds.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Refunds> refundsList = refundsRepository.findAll();
        assertThat(refundsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
