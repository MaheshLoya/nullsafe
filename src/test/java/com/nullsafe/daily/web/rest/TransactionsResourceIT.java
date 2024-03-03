package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.SubscribedOrders;
import com.nullsafe.daily.domain.Transactions;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.TransactionsRepository;
import com.nullsafe.daily.service.TransactionsService;
import com.nullsafe.daily.service.dto.TransactionsDTO;
import com.nullsafe.daily.service.mapper.TransactionsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TransactionsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionsResourceIT {

    private static final String DEFAULT_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final Double SMALLER_AMOUNT = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;
    private static final Integer SMALLER_TYPE = 1 - 1;

    private static final Integer DEFAULT_PAYMENT_MODE = 1;
    private static final Integer UPDATED_PAYMENT_MODE = 2;
    private static final Integer SMALLER_PAYMENT_MODE = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Mock
    private TransactionsRepository transactionsRepositoryMock;

    @Autowired
    private TransactionsMapper transactionsMapper;

    @Mock
    private TransactionsService transactionsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionsMockMvc;

    private Transactions transactions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transactions createEntity(EntityManager em) {
        Transactions transactions = new Transactions()
            .paymentId(DEFAULT_PAYMENT_ID)
            .amount(DEFAULT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .paymentMode(DEFAULT_PAYMENT_MODE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        transactions.setUser(users);
        return transactions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transactions createUpdatedEntity(EntityManager em) {
        Transactions transactions = new Transactions()
            .paymentId(UPDATED_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createUpdatedEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        transactions.setUser(users);
        return transactions;
    }

    @BeforeEach
    public void initTest() {
        transactions = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactions() throws Exception {
        int databaseSizeBeforeCreate = transactionsRepository.findAll().size();
        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);
        restTransactionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeCreate + 1);
        Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
        assertThat(testTransactions.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
        assertThat(testTransactions.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransactions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTransactions.getPaymentMode()).isEqualTo(DEFAULT_PAYMENT_MODE);
        assertThat(testTransactions.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTransactions.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTransactionsWithExistingId() throws Exception {
        // Create the Transactions with an existing ID
        transactions.setId(1L);
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        int databaseSizeBeforeCreate = transactionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionsRepository.findAll().size();
        // set the field null
        transactions.setAmount(null);

        // Create the Transactions, which fails.
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        restTransactionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionsRepository.findAll().size();
        // set the field null
        transactions.setPaymentMode(null);

        // Create the Transactions, which fails.
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        restTransactionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList
        restTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(transactionsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get the transactions
        restTransactionsMockMvc
            .perform(get(ENTITY_API_URL_ID, transactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactions.getId().intValue()))
            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.paymentMode").value(DEFAULT_PAYMENT_MODE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        Long id = transactions.getId();

        defaultTransactionsShouldBeFound("id.equals=" + id);
        defaultTransactionsShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionsShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentId equals to DEFAULT_PAYMENT_ID
        defaultTransactionsShouldBeFound("paymentId.equals=" + DEFAULT_PAYMENT_ID);

        // Get all the transactionsList where paymentId equals to UPDATED_PAYMENT_ID
        defaultTransactionsShouldNotBeFound("paymentId.equals=" + UPDATED_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentIdIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentId in DEFAULT_PAYMENT_ID or UPDATED_PAYMENT_ID
        defaultTransactionsShouldBeFound("paymentId.in=" + DEFAULT_PAYMENT_ID + "," + UPDATED_PAYMENT_ID);

        // Get all the transactionsList where paymentId equals to UPDATED_PAYMENT_ID
        defaultTransactionsShouldNotBeFound("paymentId.in=" + UPDATED_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentId is not null
        defaultTransactionsShouldBeFound("paymentId.specified=true");

        // Get all the transactionsList where paymentId is null
        defaultTransactionsShouldNotBeFound("paymentId.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentIdContainsSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentId contains DEFAULT_PAYMENT_ID
        defaultTransactionsShouldBeFound("paymentId.contains=" + DEFAULT_PAYMENT_ID);

        // Get all the transactionsList where paymentId contains UPDATED_PAYMENT_ID
        defaultTransactionsShouldNotBeFound("paymentId.contains=" + UPDATED_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentIdNotContainsSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentId does not contain DEFAULT_PAYMENT_ID
        defaultTransactionsShouldNotBeFound("paymentId.doesNotContain=" + DEFAULT_PAYMENT_ID);

        // Get all the transactionsList where paymentId does not contain UPDATED_PAYMENT_ID
        defaultTransactionsShouldBeFound("paymentId.doesNotContain=" + UPDATED_PAYMENT_ID);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount equals to DEFAULT_AMOUNT
        defaultTransactionsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount equals to UPDATED_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTransactionsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the transactionsList where amount equals to UPDATED_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is not null
        defaultTransactionsShouldBeFound("amount.specified=true");

        // Get all the transactionsList where amount is null
        defaultTransactionsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultTransactionsShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount is greater than or equal to UPDATED_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is less than or equal to DEFAULT_AMOUNT
        defaultTransactionsShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount is less than or equal to SMALLER_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is less than DEFAULT_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount is less than UPDATED_AMOUNT
        defaultTransactionsShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where amount is greater than DEFAULT_AMOUNT
        defaultTransactionsShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the transactionsList where amount is greater than SMALLER_AMOUNT
        defaultTransactionsShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where description equals to DEFAULT_DESCRIPTION
        defaultTransactionsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the transactionsList where description equals to UPDATED_DESCRIPTION
        defaultTransactionsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTransactionsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the transactionsList where description equals to UPDATED_DESCRIPTION
        defaultTransactionsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where description is not null
        defaultTransactionsShouldBeFound("description.specified=true");

        // Get all the transactionsList where description is null
        defaultTransactionsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where description contains DEFAULT_DESCRIPTION
        defaultTransactionsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the transactionsList where description contains UPDATED_DESCRIPTION
        defaultTransactionsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where description does not contain DEFAULT_DESCRIPTION
        defaultTransactionsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the transactionsList where description does not contain UPDATED_DESCRIPTION
        defaultTransactionsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where type equals to DEFAULT_TYPE
        defaultTransactionsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the transactionsList where type equals to UPDATED_TYPE
        defaultTransactionsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTransactionsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the transactionsList where type equals to UPDATED_TYPE
        defaultTransactionsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where type is not null
        defaultTransactionsShouldBeFound("type.specified=true");

        // Get all the transactionsList where type is null
        defaultTransactionsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where type is greater than or equal to DEFAULT_TYPE
        defaultTransactionsShouldBeFound("type.greaterThanOrEqual=" + DEFAULT_TYPE);

        // Get all the transactionsList where type is greater than or equal to UPDATED_TYPE
        defaultTransactionsShouldNotBeFound("type.greaterThanOrEqual=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where type is less than or equal to DEFAULT_TYPE
        defaultTransactionsShouldBeFound("type.lessThanOrEqual=" + DEFAULT_TYPE);

        // Get all the transactionsList where type is less than or equal to SMALLER_TYPE
        defaultTransactionsShouldNotBeFound("type.lessThanOrEqual=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where type is less than DEFAULT_TYPE
        defaultTransactionsShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the transactionsList where type is less than UPDATED_TYPE
        defaultTransactionsShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where type is greater than DEFAULT_TYPE
        defaultTransactionsShouldNotBeFound("type.greaterThan=" + DEFAULT_TYPE);

        // Get all the transactionsList where type is greater than SMALLER_TYPE
        defaultTransactionsShouldBeFound("type.greaterThan=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentModeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentMode equals to DEFAULT_PAYMENT_MODE
        defaultTransactionsShouldBeFound("paymentMode.equals=" + DEFAULT_PAYMENT_MODE);

        // Get all the transactionsList where paymentMode equals to UPDATED_PAYMENT_MODE
        defaultTransactionsShouldNotBeFound("paymentMode.equals=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentModeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentMode in DEFAULT_PAYMENT_MODE or UPDATED_PAYMENT_MODE
        defaultTransactionsShouldBeFound("paymentMode.in=" + DEFAULT_PAYMENT_MODE + "," + UPDATED_PAYMENT_MODE);

        // Get all the transactionsList where paymentMode equals to UPDATED_PAYMENT_MODE
        defaultTransactionsShouldNotBeFound("paymentMode.in=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentMode is not null
        defaultTransactionsShouldBeFound("paymentMode.specified=true");

        // Get all the transactionsList where paymentMode is null
        defaultTransactionsShouldNotBeFound("paymentMode.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentMode is greater than or equal to DEFAULT_PAYMENT_MODE
        defaultTransactionsShouldBeFound("paymentMode.greaterThanOrEqual=" + DEFAULT_PAYMENT_MODE);

        // Get all the transactionsList where paymentMode is greater than or equal to UPDATED_PAYMENT_MODE
        defaultTransactionsShouldNotBeFound("paymentMode.greaterThanOrEqual=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentMode is less than or equal to DEFAULT_PAYMENT_MODE
        defaultTransactionsShouldBeFound("paymentMode.lessThanOrEqual=" + DEFAULT_PAYMENT_MODE);

        // Get all the transactionsList where paymentMode is less than or equal to SMALLER_PAYMENT_MODE
        defaultTransactionsShouldNotBeFound("paymentMode.lessThanOrEqual=" + SMALLER_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentModeIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentMode is less than DEFAULT_PAYMENT_MODE
        defaultTransactionsShouldNotBeFound("paymentMode.lessThan=" + DEFAULT_PAYMENT_MODE);

        // Get all the transactionsList where paymentMode is less than UPDATED_PAYMENT_MODE
        defaultTransactionsShouldBeFound("paymentMode.lessThan=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllTransactionsByPaymentModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where paymentMode is greater than DEFAULT_PAYMENT_MODE
        defaultTransactionsShouldNotBeFound("paymentMode.greaterThan=" + DEFAULT_PAYMENT_MODE);

        // Get all the transactionsList where paymentMode is greater than SMALLER_PAYMENT_MODE
        defaultTransactionsShouldBeFound("paymentMode.greaterThan=" + SMALLER_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where createdAt equals to DEFAULT_CREATED_AT
        defaultTransactionsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the transactionsList where createdAt equals to UPDATED_CREATED_AT
        defaultTransactionsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTransactionsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the transactionsList where createdAt equals to UPDATED_CREATED_AT
        defaultTransactionsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where createdAt is not null
        defaultTransactionsShouldBeFound("createdAt.specified=true");

        // Get all the transactionsList where createdAt is null
        defaultTransactionsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTransactionsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the transactionsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTransactionsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTransactionsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the transactionsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTransactionsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        // Get all the transactionsList where updatedAt is not null
        defaultTransactionsShouldBeFound("updatedAt.specified=true");

        // Get all the transactionsList where updatedAt is null
        defaultTransactionsShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByOrderIsEqualToSomething() throws Exception {
        Orders order;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            transactionsRepository.saveAndFlush(transactions);
            order = OrdersResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Orders.class).get(0);
        }
        em.persist(order);
        em.flush();
        transactions.setOrder(order);
        transactionsRepository.saveAndFlush(transactions);
        Long orderId = order.getId();
        // Get all the transactionsList where order equals to orderId
        defaultTransactionsShouldBeFound("orderId.equals=" + orderId);

        // Get all the transactionsList where order equals to (orderId + 1)
        defaultTransactionsShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsByUserIsEqualToSomething() throws Exception {
        Users user;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            transactionsRepository.saveAndFlush(transactions);
            user = UsersResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(user);
        em.flush();
        transactions.setUser(user);
        transactionsRepository.saveAndFlush(transactions);
        Long userId = user.getId();
        // Get all the transactionsList where user equals to userId
        defaultTransactionsShouldBeFound("userId.equals=" + userId);

        // Get all the transactionsList where user equals to (userId + 1)
        defaultTransactionsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsByOrdersIsEqualToSomething() throws Exception {
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            transactionsRepository.saveAndFlush(transactions);
            orders = OrdersResourceIT.createEntity(em);
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        em.persist(orders);
        em.flush();
        transactions.addOrders(orders);
        transactionsRepository.saveAndFlush(transactions);
        Long ordersId = orders.getId();
        // Get all the transactionsList where orders equals to ordersId
        defaultTransactionsShouldBeFound("ordersId.equals=" + ordersId);

        // Get all the transactionsList where orders equals to (ordersId + 1)
        defaultTransactionsShouldNotBeFound("ordersId.equals=" + (ordersId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsBySubscribedOrdersIsEqualToSomething() throws Exception {
        SubscribedOrders subscribedOrders;
        if (TestUtil.findAll(em, SubscribedOrders.class).isEmpty()) {
            transactionsRepository.saveAndFlush(transactions);
            subscribedOrders = SubscribedOrdersResourceIT.createEntity(em);
        } else {
            subscribedOrders = TestUtil.findAll(em, SubscribedOrders.class).get(0);
        }
        em.persist(subscribedOrders);
        em.flush();
        transactions.addSubscribedOrders(subscribedOrders);
        transactionsRepository.saveAndFlush(transactions);
        Long subscribedOrdersId = subscribedOrders.getId();
        // Get all the transactionsList where subscribedOrders equals to subscribedOrdersId
        defaultTransactionsShouldBeFound("subscribedOrdersId.equals=" + subscribedOrdersId);

        // Get all the transactionsList where subscribedOrders equals to (subscribedOrdersId + 1)
        defaultTransactionsShouldNotBeFound("subscribedOrdersId.equals=" + (subscribedOrdersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionsShouldBeFound(String filter) throws Exception {
        restTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionsShouldNotBeFound(String filter) throws Exception {
        restTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactions() throws Exception {
        // Get the transactions
        restTransactionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();

        // Update the transactions
        Transactions updatedTransactions = transactionsRepository.findById(transactions.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactions are not directly saved in db
        em.detach(updatedTransactions);
        updatedTransactions
            .paymentId(UPDATED_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(updatedTransactions);

        restTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
        Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
        assertThat(testTransactions.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testTransactions.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransactions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTransactions.getPaymentMode()).isEqualTo(UPDATED_PAYMENT_MODE);
        assertThat(testTransactions.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransactions.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTransactions() throws Exception {
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();
        transactions.setId(longCount.incrementAndGet());

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactions() throws Exception {
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();
        transactions.setId(longCount.incrementAndGet());

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactions() throws Exception {
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();
        transactions.setId(longCount.incrementAndGet());

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionsWithPatch() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();

        // Update the transactions using partial update
        Transactions partialUpdatedTransactions = new Transactions();
        partialUpdatedTransactions.setId(transactions.getId());

        partialUpdatedTransactions.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactions))
            )
            .andExpect(status().isOk());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
        Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
        assertThat(testTransactions.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
        assertThat(testTransactions.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTransactions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTransactions.getPaymentMode()).isEqualTo(DEFAULT_PAYMENT_MODE);
        assertThat(testTransactions.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransactions.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTransactionsWithPatch() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();

        // Update the transactions using partial update
        Transactions partialUpdatedTransactions = new Transactions();
        partialUpdatedTransactions.setId(transactions.getId());

        partialUpdatedTransactions
            .paymentId(UPDATED_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactions))
            )
            .andExpect(status().isOk());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
        Transactions testTransactions = transactionsList.get(transactionsList.size() - 1);
        assertThat(testTransactions.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testTransactions.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransactions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTransactions.getPaymentMode()).isEqualTo(UPDATED_PAYMENT_MODE);
        assertThat(testTransactions.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransactions.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTransactions() throws Exception {
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();
        transactions.setId(longCount.incrementAndGet());

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactions() throws Exception {
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();
        transactions.setId(longCount.incrementAndGet());

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactions() throws Exception {
        int databaseSizeBeforeUpdate = transactionsRepository.findAll().size();
        transactions.setId(longCount.incrementAndGet());

        // Create the Transactions
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transactions in the database
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactions() throws Exception {
        // Initialize the database
        transactionsRepository.saveAndFlush(transactions);

        int databaseSizeBeforeDelete = transactionsRepository.findAll().size();

        // Delete the transactions
        restTransactionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transactions> transactionsList = transactionsRepository.findAll();
        assertThat(transactionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
