package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.domain.SubscribedOrders;
import com.nullsafe.daily.domain.Transactions;
import com.nullsafe.daily.domain.UserAddress;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.SubscribedOrdersRepository;
import com.nullsafe.daily.service.SubscribedOrdersService;
import com.nullsafe.daily.service.dto.SubscribedOrdersDTO;
import com.nullsafe.daily.service.mapper.SubscribedOrdersMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SubscribedOrdersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubscribedOrdersResourceIT {

    private static final Integer DEFAULT_PAYMENT_TYPE = 1;
    private static final Integer UPDATED_PAYMENT_TYPE = 2;
    private static final Integer SMALLER_PAYMENT_TYPE = 1 - 1;

    private static final Float DEFAULT_ORDER_AMOUNT = 1F;
    private static final Float UPDATED_ORDER_AMOUNT = 2F;
    private static final Float SMALLER_ORDER_AMOUNT = 1F - 1F;

    private static final Float DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT = 1F;
    private static final Float UPDATED_SUBSCRIPTION_BALANCE_AMOUNT = 2F;
    private static final Float SMALLER_SUBSCRIPTION_BALANCE_AMOUNT = 1F - 1F;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final Float DEFAULT_MRP = 1F;
    private static final Float UPDATED_MRP = 2F;
    private static final Float SMALLER_MRP = 1F - 1F;

    private static final Float DEFAULT_TAX = 1F;
    private static final Float UPDATED_TAX = 2F;
    private static final Float SMALLER_TAX = 1F - 1F;

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;
    private static final Integer SMALLER_QTY = 1 - 1;

    private static final Integer DEFAULT_OFFER_ID = 1;
    private static final Integer UPDATED_OFFER_ID = 2;
    private static final Integer SMALLER_OFFER_ID = 1 - 1;

    private static final String DEFAULT_SELECTED_DAYS_FOR_WEEKLY = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_DAYS_FOR_WEEKLY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_RENEWAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_RENEWAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_RENEWAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_SUBSCRIPTION_TYPE = 1;
    private static final Integer UPDATED_SUBSCRIPTION_TYPE = 2;
    private static final Integer SMALLER_SUBSCRIPTION_TYPE = 1 - 1;

    private static final Integer DEFAULT_APPROVAL_STATUS = 1;
    private static final Integer UPDATED_APPROVAL_STATUS = 2;
    private static final Integer SMALLER_APPROVAL_STATUS = 1 - 1;

    private static final Boolean DEFAULT_ORDER_STATUS = false;
    private static final Boolean UPDATED_ORDER_STATUS = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subscribed-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscribedOrdersRepository subscribedOrdersRepository;

    @Mock
    private SubscribedOrdersRepository subscribedOrdersRepositoryMock;

    @Autowired
    private SubscribedOrdersMapper subscribedOrdersMapper;

    @Mock
    private SubscribedOrdersService subscribedOrdersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscribedOrdersMockMvc;

    private SubscribedOrders subscribedOrders;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscribedOrders createEntity(EntityManager em) {
        SubscribedOrders subscribedOrders = new SubscribedOrders()
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .orderAmount(DEFAULT_ORDER_AMOUNT)
            .subscriptionBalanceAmount(DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT)
            .price(DEFAULT_PRICE)
            .mrp(DEFAULT_MRP)
            .tax(DEFAULT_TAX)
            .qty(DEFAULT_QTY)
            .offerId(DEFAULT_OFFER_ID)
            .selectedDaysForWeekly(DEFAULT_SELECTED_DAYS_FOR_WEEKLY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .lastRenewalDate(DEFAULT_LAST_RENEWAL_DATE)
            .subscriptionType(DEFAULT_SUBSCRIPTION_TYPE)
            .approvalStatus(DEFAULT_APPROVAL_STATUS)
            .orderStatus(DEFAULT_ORDER_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        subscribedOrders.setUser(users);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        subscribedOrders.setProduct(product);
        // Add required entity
        UserAddress userAddress;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            userAddress = UserAddressResourceIT.createEntity(em);
            em.persist(userAddress);
            em.flush();
        } else {
            userAddress = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        subscribedOrders.setAddress(userAddress);
        return subscribedOrders;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscribedOrders createUpdatedEntity(EntityManager em) {
        SubscribedOrders subscribedOrders = new SubscribedOrders()
            .paymentType(UPDATED_PAYMENT_TYPE)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .subscriptionBalanceAmount(UPDATED_SUBSCRIPTION_BALANCE_AMOUNT)
            .price(UPDATED_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qty(UPDATED_QTY)
            .offerId(UPDATED_OFFER_ID)
            .selectedDaysForWeekly(UPDATED_SELECTED_DAYS_FOR_WEEKLY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastRenewalDate(UPDATED_LAST_RENEWAL_DATE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .orderStatus(UPDATED_ORDER_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createUpdatedEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        subscribedOrders.setUser(users);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        subscribedOrders.setProduct(product);
        // Add required entity
        UserAddress userAddress;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            userAddress = UserAddressResourceIT.createUpdatedEntity(em);
            em.persist(userAddress);
            em.flush();
        } else {
            userAddress = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        subscribedOrders.setAddress(userAddress);
        return subscribedOrders;
    }

    @BeforeEach
    public void initTest() {
        subscribedOrders = createEntity(em);
    }

    @Test
    @Transactional
    void createSubscribedOrders() throws Exception {
        int databaseSizeBeforeCreate = subscribedOrdersRepository.findAll().size();
        // Create the SubscribedOrders
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);
        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeCreate + 1);
        SubscribedOrders testSubscribedOrders = subscribedOrdersList.get(subscribedOrdersList.size() - 1);
        assertThat(testSubscribedOrders.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testSubscribedOrders.getOrderAmount()).isEqualTo(DEFAULT_ORDER_AMOUNT);
        assertThat(testSubscribedOrders.getSubscriptionBalanceAmount()).isEqualTo(DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT);
        assertThat(testSubscribedOrders.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSubscribedOrders.getMrp()).isEqualTo(DEFAULT_MRP);
        assertThat(testSubscribedOrders.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testSubscribedOrders.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testSubscribedOrders.getOfferId()).isEqualTo(DEFAULT_OFFER_ID);
        assertThat(testSubscribedOrders.getSelectedDaysForWeekly()).isEqualTo(DEFAULT_SELECTED_DAYS_FOR_WEEKLY);
        assertThat(testSubscribedOrders.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSubscribedOrders.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSubscribedOrders.getLastRenewalDate()).isEqualTo(DEFAULT_LAST_RENEWAL_DATE);
        assertThat(testSubscribedOrders.getSubscriptionType()).isEqualTo(DEFAULT_SUBSCRIPTION_TYPE);
        assertThat(testSubscribedOrders.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testSubscribedOrders.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testSubscribedOrders.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubscribedOrders.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSubscribedOrders.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSubscribedOrders.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createSubscribedOrdersWithExistingId() throws Exception {
        // Create the SubscribedOrders with an existing ID
        subscribedOrders.setId(1L);
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        int databaseSizeBeforeCreate = subscribedOrdersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscribedOrdersRepository.findAll().size();
        // set the field null
        subscribedOrders.setOrderAmount(null);

        // Create the SubscribedOrders, which fails.
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscribedOrdersRepository.findAll().size();
        // set the field null
        subscribedOrders.setPrice(null);

        // Create the SubscribedOrders, which fails.
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMrpIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscribedOrdersRepository.findAll().size();
        // set the field null
        subscribedOrders.setMrp(null);

        // Create the SubscribedOrders, which fails.
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscribedOrdersRepository.findAll().size();
        // set the field null
        subscribedOrders.setTax(null);

        // Create the SubscribedOrders, which fails.
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscribedOrdersRepository.findAll().size();
        // set the field null
        subscribedOrders.setStartDate(null);

        // Create the SubscribedOrders, which fails.
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscribedOrdersRepository.findAll().size();
        // set the field null
        subscribedOrders.setEndDate(null);

        // Create the SubscribedOrders, which fails.
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApprovalStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscribedOrdersRepository.findAll().size();
        // set the field null
        subscribedOrders.setApprovalStatus(null);

        // Create the SubscribedOrders, which fails.
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscribedOrdersRepository.findAll().size();
        // set the field null
        subscribedOrders.setOrderStatus(null);

        // Create the SubscribedOrders, which fails.
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscribedOrders() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList
        restSubscribedOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribedOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].orderAmount").value(hasItem(DEFAULT_ORDER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].subscriptionBalanceAmount").value(hasItem(DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].offerId").value(hasItem(DEFAULT_OFFER_ID)))
            .andExpect(jsonPath("$.[*].selectedDaysForWeekly").value(hasItem(DEFAULT_SELECTED_DAYS_FOR_WEEKLY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRenewalDate").value(hasItem(DEFAULT_LAST_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionType").value(hasItem(DEFAULT_SUBSCRIPTION_TYPE)))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS)))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscribedOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(subscribedOrdersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscribedOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subscribedOrdersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscribedOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subscribedOrdersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscribedOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(subscribedOrdersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSubscribedOrders() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get the subscribedOrders
        restSubscribedOrdersMockMvc
            .perform(get(ENTITY_API_URL_ID, subscribedOrders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscribedOrders.getId().intValue()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE))
            .andExpect(jsonPath("$.orderAmount").value(DEFAULT_ORDER_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.subscriptionBalanceAmount").value(DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.mrp").value(DEFAULT_MRP.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.offerId").value(DEFAULT_OFFER_ID))
            .andExpect(jsonPath("$.selectedDaysForWeekly").value(DEFAULT_SELECTED_DAYS_FOR_WEEKLY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.lastRenewalDate").value(DEFAULT_LAST_RENEWAL_DATE.toString()))
            .andExpect(jsonPath("$.subscriptionType").value(DEFAULT_SUBSCRIPTION_TYPE))
            .andExpect(jsonPath("$.approvalStatus").value(DEFAULT_APPROVAL_STATUS))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getSubscribedOrdersByIdFiltering() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        Long id = subscribedOrders.getId();

        defaultSubscribedOrdersShouldBeFound("id.equals=" + id);
        defaultSubscribedOrdersShouldNotBeFound("id.notEquals=" + id);

        defaultSubscribedOrdersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubscribedOrdersShouldNotBeFound("id.greaterThan=" + id);

        defaultSubscribedOrdersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubscribedOrdersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPaymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where paymentType equals to DEFAULT_PAYMENT_TYPE
        defaultSubscribedOrdersShouldBeFound("paymentType.equals=" + DEFAULT_PAYMENT_TYPE);

        // Get all the subscribedOrdersList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultSubscribedOrdersShouldNotBeFound("paymentType.equals=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPaymentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where paymentType in DEFAULT_PAYMENT_TYPE or UPDATED_PAYMENT_TYPE
        defaultSubscribedOrdersShouldBeFound("paymentType.in=" + DEFAULT_PAYMENT_TYPE + "," + UPDATED_PAYMENT_TYPE);

        // Get all the subscribedOrdersList where paymentType equals to UPDATED_PAYMENT_TYPE
        defaultSubscribedOrdersShouldNotBeFound("paymentType.in=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPaymentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where paymentType is not null
        defaultSubscribedOrdersShouldBeFound("paymentType.specified=true");

        // Get all the subscribedOrdersList where paymentType is null
        defaultSubscribedOrdersShouldNotBeFound("paymentType.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPaymentTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where paymentType is greater than or equal to DEFAULT_PAYMENT_TYPE
        defaultSubscribedOrdersShouldBeFound("paymentType.greaterThanOrEqual=" + DEFAULT_PAYMENT_TYPE);

        // Get all the subscribedOrdersList where paymentType is greater than or equal to UPDATED_PAYMENT_TYPE
        defaultSubscribedOrdersShouldNotBeFound("paymentType.greaterThanOrEqual=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPaymentTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where paymentType is less than or equal to DEFAULT_PAYMENT_TYPE
        defaultSubscribedOrdersShouldBeFound("paymentType.lessThanOrEqual=" + DEFAULT_PAYMENT_TYPE);

        // Get all the subscribedOrdersList where paymentType is less than or equal to SMALLER_PAYMENT_TYPE
        defaultSubscribedOrdersShouldNotBeFound("paymentType.lessThanOrEqual=" + SMALLER_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPaymentTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where paymentType is less than DEFAULT_PAYMENT_TYPE
        defaultSubscribedOrdersShouldNotBeFound("paymentType.lessThan=" + DEFAULT_PAYMENT_TYPE);

        // Get all the subscribedOrdersList where paymentType is less than UPDATED_PAYMENT_TYPE
        defaultSubscribedOrdersShouldBeFound("paymentType.lessThan=" + UPDATED_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPaymentTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where paymentType is greater than DEFAULT_PAYMENT_TYPE
        defaultSubscribedOrdersShouldNotBeFound("paymentType.greaterThan=" + DEFAULT_PAYMENT_TYPE);

        // Get all the subscribedOrdersList where paymentType is greater than SMALLER_PAYMENT_TYPE
        defaultSubscribedOrdersShouldBeFound("paymentType.greaterThan=" + SMALLER_PAYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderAmount equals to DEFAULT_ORDER_AMOUNT
        defaultSubscribedOrdersShouldBeFound("orderAmount.equals=" + DEFAULT_ORDER_AMOUNT);

        // Get all the subscribedOrdersList where orderAmount equals to UPDATED_ORDER_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("orderAmount.equals=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderAmountIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderAmount in DEFAULT_ORDER_AMOUNT or UPDATED_ORDER_AMOUNT
        defaultSubscribedOrdersShouldBeFound("orderAmount.in=" + DEFAULT_ORDER_AMOUNT + "," + UPDATED_ORDER_AMOUNT);

        // Get all the subscribedOrdersList where orderAmount equals to UPDATED_ORDER_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("orderAmount.in=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderAmount is not null
        defaultSubscribedOrdersShouldBeFound("orderAmount.specified=true");

        // Get all the subscribedOrdersList where orderAmount is null
        defaultSubscribedOrdersShouldNotBeFound("orderAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderAmount is greater than or equal to DEFAULT_ORDER_AMOUNT
        defaultSubscribedOrdersShouldBeFound("orderAmount.greaterThanOrEqual=" + DEFAULT_ORDER_AMOUNT);

        // Get all the subscribedOrdersList where orderAmount is greater than or equal to UPDATED_ORDER_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("orderAmount.greaterThanOrEqual=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderAmount is less than or equal to DEFAULT_ORDER_AMOUNT
        defaultSubscribedOrdersShouldBeFound("orderAmount.lessThanOrEqual=" + DEFAULT_ORDER_AMOUNT);

        // Get all the subscribedOrdersList where orderAmount is less than or equal to SMALLER_ORDER_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("orderAmount.lessThanOrEqual=" + SMALLER_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderAmount is less than DEFAULT_ORDER_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("orderAmount.lessThan=" + DEFAULT_ORDER_AMOUNT);

        // Get all the subscribedOrdersList where orderAmount is less than UPDATED_ORDER_AMOUNT
        defaultSubscribedOrdersShouldBeFound("orderAmount.lessThan=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderAmount is greater than DEFAULT_ORDER_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("orderAmount.greaterThan=" + DEFAULT_ORDER_AMOUNT);

        // Get all the subscribedOrdersList where orderAmount is greater than SMALLER_ORDER_AMOUNT
        defaultSubscribedOrdersShouldBeFound("orderAmount.greaterThan=" + SMALLER_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionBalanceAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount equals to DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldBeFound("subscriptionBalanceAmount.equals=" + DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount equals to UPDATED_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("subscriptionBalanceAmount.equals=" + UPDATED_SUBSCRIPTION_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionBalanceAmountIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount in DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT or UPDATED_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldBeFound(
            "subscriptionBalanceAmount.in=" + DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT + "," + UPDATED_SUBSCRIPTION_BALANCE_AMOUNT
        );

        // Get all the subscribedOrdersList where subscriptionBalanceAmount equals to UPDATED_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("subscriptionBalanceAmount.in=" + UPDATED_SUBSCRIPTION_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionBalanceAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is not null
        defaultSubscribedOrdersShouldBeFound("subscriptionBalanceAmount.specified=true");

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is null
        defaultSubscribedOrdersShouldNotBeFound("subscriptionBalanceAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionBalanceAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is greater than or equal to DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldBeFound("subscriptionBalanceAmount.greaterThanOrEqual=" + DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is greater than or equal to UPDATED_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("subscriptionBalanceAmount.greaterThanOrEqual=" + UPDATED_SUBSCRIPTION_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionBalanceAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is less than or equal to DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldBeFound("subscriptionBalanceAmount.lessThanOrEqual=" + DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is less than or equal to SMALLER_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("subscriptionBalanceAmount.lessThanOrEqual=" + SMALLER_SUBSCRIPTION_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionBalanceAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is less than DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("subscriptionBalanceAmount.lessThan=" + DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is less than UPDATED_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldBeFound("subscriptionBalanceAmount.lessThan=" + UPDATED_SUBSCRIPTION_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionBalanceAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is greater than DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldNotBeFound("subscriptionBalanceAmount.greaterThan=" + DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT);

        // Get all the subscribedOrdersList where subscriptionBalanceAmount is greater than SMALLER_SUBSCRIPTION_BALANCE_AMOUNT
        defaultSubscribedOrdersShouldBeFound("subscriptionBalanceAmount.greaterThan=" + SMALLER_SUBSCRIPTION_BALANCE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where price equals to DEFAULT_PRICE
        defaultSubscribedOrdersShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the subscribedOrdersList where price equals to UPDATED_PRICE
        defaultSubscribedOrdersShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultSubscribedOrdersShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the subscribedOrdersList where price equals to UPDATED_PRICE
        defaultSubscribedOrdersShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where price is not null
        defaultSubscribedOrdersShouldBeFound("price.specified=true");

        // Get all the subscribedOrdersList where price is null
        defaultSubscribedOrdersShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where price is greater than or equal to DEFAULT_PRICE
        defaultSubscribedOrdersShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the subscribedOrdersList where price is greater than or equal to UPDATED_PRICE
        defaultSubscribedOrdersShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where price is less than or equal to DEFAULT_PRICE
        defaultSubscribedOrdersShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the subscribedOrdersList where price is less than or equal to SMALLER_PRICE
        defaultSubscribedOrdersShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where price is less than DEFAULT_PRICE
        defaultSubscribedOrdersShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the subscribedOrdersList where price is less than UPDATED_PRICE
        defaultSubscribedOrdersShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where price is greater than DEFAULT_PRICE
        defaultSubscribedOrdersShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the subscribedOrdersList where price is greater than SMALLER_PRICE
        defaultSubscribedOrdersShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByMrpIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where mrp equals to DEFAULT_MRP
        defaultSubscribedOrdersShouldBeFound("mrp.equals=" + DEFAULT_MRP);

        // Get all the subscribedOrdersList where mrp equals to UPDATED_MRP
        defaultSubscribedOrdersShouldNotBeFound("mrp.equals=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByMrpIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where mrp in DEFAULT_MRP or UPDATED_MRP
        defaultSubscribedOrdersShouldBeFound("mrp.in=" + DEFAULT_MRP + "," + UPDATED_MRP);

        // Get all the subscribedOrdersList where mrp equals to UPDATED_MRP
        defaultSubscribedOrdersShouldNotBeFound("mrp.in=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByMrpIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where mrp is not null
        defaultSubscribedOrdersShouldBeFound("mrp.specified=true");

        // Get all the subscribedOrdersList where mrp is null
        defaultSubscribedOrdersShouldNotBeFound("mrp.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByMrpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where mrp is greater than or equal to DEFAULT_MRP
        defaultSubscribedOrdersShouldBeFound("mrp.greaterThanOrEqual=" + DEFAULT_MRP);

        // Get all the subscribedOrdersList where mrp is greater than or equal to UPDATED_MRP
        defaultSubscribedOrdersShouldNotBeFound("mrp.greaterThanOrEqual=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByMrpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where mrp is less than or equal to DEFAULT_MRP
        defaultSubscribedOrdersShouldBeFound("mrp.lessThanOrEqual=" + DEFAULT_MRP);

        // Get all the subscribedOrdersList where mrp is less than or equal to SMALLER_MRP
        defaultSubscribedOrdersShouldNotBeFound("mrp.lessThanOrEqual=" + SMALLER_MRP);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByMrpIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where mrp is less than DEFAULT_MRP
        defaultSubscribedOrdersShouldNotBeFound("mrp.lessThan=" + DEFAULT_MRP);

        // Get all the subscribedOrdersList where mrp is less than UPDATED_MRP
        defaultSubscribedOrdersShouldBeFound("mrp.lessThan=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByMrpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where mrp is greater than DEFAULT_MRP
        defaultSubscribedOrdersShouldNotBeFound("mrp.greaterThan=" + DEFAULT_MRP);

        // Get all the subscribedOrdersList where mrp is greater than SMALLER_MRP
        defaultSubscribedOrdersShouldBeFound("mrp.greaterThan=" + SMALLER_MRP);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where tax equals to DEFAULT_TAX
        defaultSubscribedOrdersShouldBeFound("tax.equals=" + DEFAULT_TAX);

        // Get all the subscribedOrdersList where tax equals to UPDATED_TAX
        defaultSubscribedOrdersShouldNotBeFound("tax.equals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByTaxIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where tax in DEFAULT_TAX or UPDATED_TAX
        defaultSubscribedOrdersShouldBeFound("tax.in=" + DEFAULT_TAX + "," + UPDATED_TAX);

        // Get all the subscribedOrdersList where tax equals to UPDATED_TAX
        defaultSubscribedOrdersShouldNotBeFound("tax.in=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where tax is not null
        defaultSubscribedOrdersShouldBeFound("tax.specified=true");

        // Get all the subscribedOrdersList where tax is null
        defaultSubscribedOrdersShouldNotBeFound("tax.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where tax is greater than or equal to DEFAULT_TAX
        defaultSubscribedOrdersShouldBeFound("tax.greaterThanOrEqual=" + DEFAULT_TAX);

        // Get all the subscribedOrdersList where tax is greater than or equal to UPDATED_TAX
        defaultSubscribedOrdersShouldNotBeFound("tax.greaterThanOrEqual=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where tax is less than or equal to DEFAULT_TAX
        defaultSubscribedOrdersShouldBeFound("tax.lessThanOrEqual=" + DEFAULT_TAX);

        // Get all the subscribedOrdersList where tax is less than or equal to SMALLER_TAX
        defaultSubscribedOrdersShouldNotBeFound("tax.lessThanOrEqual=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where tax is less than DEFAULT_TAX
        defaultSubscribedOrdersShouldNotBeFound("tax.lessThan=" + DEFAULT_TAX);

        // Get all the subscribedOrdersList where tax is less than UPDATED_TAX
        defaultSubscribedOrdersShouldBeFound("tax.lessThan=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where tax is greater than DEFAULT_TAX
        defaultSubscribedOrdersShouldNotBeFound("tax.greaterThan=" + DEFAULT_TAX);

        // Get all the subscribedOrdersList where tax is greater than SMALLER_TAX
        defaultSubscribedOrdersShouldBeFound("tax.greaterThan=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where qty equals to DEFAULT_QTY
        defaultSubscribedOrdersShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the subscribedOrdersList where qty equals to UPDATED_QTY
        defaultSubscribedOrdersShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultSubscribedOrdersShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the subscribedOrdersList where qty equals to UPDATED_QTY
        defaultSubscribedOrdersShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where qty is not null
        defaultSubscribedOrdersShouldBeFound("qty.specified=true");

        // Get all the subscribedOrdersList where qty is null
        defaultSubscribedOrdersShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where qty is greater than or equal to DEFAULT_QTY
        defaultSubscribedOrdersShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the subscribedOrdersList where qty is greater than or equal to UPDATED_QTY
        defaultSubscribedOrdersShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where qty is less than or equal to DEFAULT_QTY
        defaultSubscribedOrdersShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the subscribedOrdersList where qty is less than or equal to SMALLER_QTY
        defaultSubscribedOrdersShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where qty is less than DEFAULT_QTY
        defaultSubscribedOrdersShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the subscribedOrdersList where qty is less than UPDATED_QTY
        defaultSubscribedOrdersShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where qty is greater than DEFAULT_QTY
        defaultSubscribedOrdersShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the subscribedOrdersList where qty is greater than SMALLER_QTY
        defaultSubscribedOrdersShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOfferIdIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where offerId equals to DEFAULT_OFFER_ID
        defaultSubscribedOrdersShouldBeFound("offerId.equals=" + DEFAULT_OFFER_ID);

        // Get all the subscribedOrdersList where offerId equals to UPDATED_OFFER_ID
        defaultSubscribedOrdersShouldNotBeFound("offerId.equals=" + UPDATED_OFFER_ID);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOfferIdIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where offerId in DEFAULT_OFFER_ID or UPDATED_OFFER_ID
        defaultSubscribedOrdersShouldBeFound("offerId.in=" + DEFAULT_OFFER_ID + "," + UPDATED_OFFER_ID);

        // Get all the subscribedOrdersList where offerId equals to UPDATED_OFFER_ID
        defaultSubscribedOrdersShouldNotBeFound("offerId.in=" + UPDATED_OFFER_ID);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOfferIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where offerId is not null
        defaultSubscribedOrdersShouldBeFound("offerId.specified=true");

        // Get all the subscribedOrdersList where offerId is null
        defaultSubscribedOrdersShouldNotBeFound("offerId.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOfferIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where offerId is greater than or equal to DEFAULT_OFFER_ID
        defaultSubscribedOrdersShouldBeFound("offerId.greaterThanOrEqual=" + DEFAULT_OFFER_ID);

        // Get all the subscribedOrdersList where offerId is greater than or equal to UPDATED_OFFER_ID
        defaultSubscribedOrdersShouldNotBeFound("offerId.greaterThanOrEqual=" + UPDATED_OFFER_ID);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOfferIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where offerId is less than or equal to DEFAULT_OFFER_ID
        defaultSubscribedOrdersShouldBeFound("offerId.lessThanOrEqual=" + DEFAULT_OFFER_ID);

        // Get all the subscribedOrdersList where offerId is less than or equal to SMALLER_OFFER_ID
        defaultSubscribedOrdersShouldNotBeFound("offerId.lessThanOrEqual=" + SMALLER_OFFER_ID);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOfferIdIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where offerId is less than DEFAULT_OFFER_ID
        defaultSubscribedOrdersShouldNotBeFound("offerId.lessThan=" + DEFAULT_OFFER_ID);

        // Get all the subscribedOrdersList where offerId is less than UPDATED_OFFER_ID
        defaultSubscribedOrdersShouldBeFound("offerId.lessThan=" + UPDATED_OFFER_ID);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOfferIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where offerId is greater than DEFAULT_OFFER_ID
        defaultSubscribedOrdersShouldNotBeFound("offerId.greaterThan=" + DEFAULT_OFFER_ID);

        // Get all the subscribedOrdersList where offerId is greater than SMALLER_OFFER_ID
        defaultSubscribedOrdersShouldBeFound("offerId.greaterThan=" + SMALLER_OFFER_ID);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySelectedDaysForWeeklyIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where selectedDaysForWeekly equals to DEFAULT_SELECTED_DAYS_FOR_WEEKLY
        defaultSubscribedOrdersShouldBeFound("selectedDaysForWeekly.equals=" + DEFAULT_SELECTED_DAYS_FOR_WEEKLY);

        // Get all the subscribedOrdersList where selectedDaysForWeekly equals to UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultSubscribedOrdersShouldNotBeFound("selectedDaysForWeekly.equals=" + UPDATED_SELECTED_DAYS_FOR_WEEKLY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySelectedDaysForWeeklyIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where selectedDaysForWeekly in DEFAULT_SELECTED_DAYS_FOR_WEEKLY or UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultSubscribedOrdersShouldBeFound(
            "selectedDaysForWeekly.in=" + DEFAULT_SELECTED_DAYS_FOR_WEEKLY + "," + UPDATED_SELECTED_DAYS_FOR_WEEKLY
        );

        // Get all the subscribedOrdersList where selectedDaysForWeekly equals to UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultSubscribedOrdersShouldNotBeFound("selectedDaysForWeekly.in=" + UPDATED_SELECTED_DAYS_FOR_WEEKLY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySelectedDaysForWeeklyIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where selectedDaysForWeekly is not null
        defaultSubscribedOrdersShouldBeFound("selectedDaysForWeekly.specified=true");

        // Get all the subscribedOrdersList where selectedDaysForWeekly is null
        defaultSubscribedOrdersShouldNotBeFound("selectedDaysForWeekly.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySelectedDaysForWeeklyContainsSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where selectedDaysForWeekly contains DEFAULT_SELECTED_DAYS_FOR_WEEKLY
        defaultSubscribedOrdersShouldBeFound("selectedDaysForWeekly.contains=" + DEFAULT_SELECTED_DAYS_FOR_WEEKLY);

        // Get all the subscribedOrdersList where selectedDaysForWeekly contains UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultSubscribedOrdersShouldNotBeFound("selectedDaysForWeekly.contains=" + UPDATED_SELECTED_DAYS_FOR_WEEKLY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySelectedDaysForWeeklyNotContainsSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where selectedDaysForWeekly does not contain DEFAULT_SELECTED_DAYS_FOR_WEEKLY
        defaultSubscribedOrdersShouldNotBeFound("selectedDaysForWeekly.doesNotContain=" + DEFAULT_SELECTED_DAYS_FOR_WEEKLY);

        // Get all the subscribedOrdersList where selectedDaysForWeekly does not contain UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultSubscribedOrdersShouldBeFound("selectedDaysForWeekly.doesNotContain=" + UPDATED_SELECTED_DAYS_FOR_WEEKLY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where startDate equals to DEFAULT_START_DATE
        defaultSubscribedOrdersShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the subscribedOrdersList where startDate equals to UPDATED_START_DATE
        defaultSubscribedOrdersShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultSubscribedOrdersShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the subscribedOrdersList where startDate equals to UPDATED_START_DATE
        defaultSubscribedOrdersShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where startDate is not null
        defaultSubscribedOrdersShouldBeFound("startDate.specified=true");

        // Get all the subscribedOrdersList where startDate is null
        defaultSubscribedOrdersShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultSubscribedOrdersShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the subscribedOrdersList where startDate is greater than or equal to UPDATED_START_DATE
        defaultSubscribedOrdersShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where startDate is less than or equal to DEFAULT_START_DATE
        defaultSubscribedOrdersShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the subscribedOrdersList where startDate is less than or equal to SMALLER_START_DATE
        defaultSubscribedOrdersShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where startDate is less than DEFAULT_START_DATE
        defaultSubscribedOrdersShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the subscribedOrdersList where startDate is less than UPDATED_START_DATE
        defaultSubscribedOrdersShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where startDate is greater than DEFAULT_START_DATE
        defaultSubscribedOrdersShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the subscribedOrdersList where startDate is greater than SMALLER_START_DATE
        defaultSubscribedOrdersShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where endDate equals to DEFAULT_END_DATE
        defaultSubscribedOrdersShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the subscribedOrdersList where endDate equals to UPDATED_END_DATE
        defaultSubscribedOrdersShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultSubscribedOrdersShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the subscribedOrdersList where endDate equals to UPDATED_END_DATE
        defaultSubscribedOrdersShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where endDate is not null
        defaultSubscribedOrdersShouldBeFound("endDate.specified=true");

        // Get all the subscribedOrdersList where endDate is null
        defaultSubscribedOrdersShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultSubscribedOrdersShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the subscribedOrdersList where endDate is greater than or equal to UPDATED_END_DATE
        defaultSubscribedOrdersShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where endDate is less than or equal to DEFAULT_END_DATE
        defaultSubscribedOrdersShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the subscribedOrdersList where endDate is less than or equal to SMALLER_END_DATE
        defaultSubscribedOrdersShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where endDate is less than DEFAULT_END_DATE
        defaultSubscribedOrdersShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the subscribedOrdersList where endDate is less than UPDATED_END_DATE
        defaultSubscribedOrdersShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where endDate is greater than DEFAULT_END_DATE
        defaultSubscribedOrdersShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the subscribedOrdersList where endDate is greater than SMALLER_END_DATE
        defaultSubscribedOrdersShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByLastRenewalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where lastRenewalDate equals to DEFAULT_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldBeFound("lastRenewalDate.equals=" + DEFAULT_LAST_RENEWAL_DATE);

        // Get all the subscribedOrdersList where lastRenewalDate equals to UPDATED_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldNotBeFound("lastRenewalDate.equals=" + UPDATED_LAST_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByLastRenewalDateIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where lastRenewalDate in DEFAULT_LAST_RENEWAL_DATE or UPDATED_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldBeFound("lastRenewalDate.in=" + DEFAULT_LAST_RENEWAL_DATE + "," + UPDATED_LAST_RENEWAL_DATE);

        // Get all the subscribedOrdersList where lastRenewalDate equals to UPDATED_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldNotBeFound("lastRenewalDate.in=" + UPDATED_LAST_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByLastRenewalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where lastRenewalDate is not null
        defaultSubscribedOrdersShouldBeFound("lastRenewalDate.specified=true");

        // Get all the subscribedOrdersList where lastRenewalDate is null
        defaultSubscribedOrdersShouldNotBeFound("lastRenewalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByLastRenewalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where lastRenewalDate is greater than or equal to DEFAULT_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldBeFound("lastRenewalDate.greaterThanOrEqual=" + DEFAULT_LAST_RENEWAL_DATE);

        // Get all the subscribedOrdersList where lastRenewalDate is greater than or equal to UPDATED_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldNotBeFound("lastRenewalDate.greaterThanOrEqual=" + UPDATED_LAST_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByLastRenewalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where lastRenewalDate is less than or equal to DEFAULT_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldBeFound("lastRenewalDate.lessThanOrEqual=" + DEFAULT_LAST_RENEWAL_DATE);

        // Get all the subscribedOrdersList where lastRenewalDate is less than or equal to SMALLER_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldNotBeFound("lastRenewalDate.lessThanOrEqual=" + SMALLER_LAST_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByLastRenewalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where lastRenewalDate is less than DEFAULT_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldNotBeFound("lastRenewalDate.lessThan=" + DEFAULT_LAST_RENEWAL_DATE);

        // Get all the subscribedOrdersList where lastRenewalDate is less than UPDATED_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldBeFound("lastRenewalDate.lessThan=" + UPDATED_LAST_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByLastRenewalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where lastRenewalDate is greater than DEFAULT_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldNotBeFound("lastRenewalDate.greaterThan=" + DEFAULT_LAST_RENEWAL_DATE);

        // Get all the subscribedOrdersList where lastRenewalDate is greater than SMALLER_LAST_RENEWAL_DATE
        defaultSubscribedOrdersShouldBeFound("lastRenewalDate.greaterThan=" + SMALLER_LAST_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionType equals to DEFAULT_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldBeFound("subscriptionType.equals=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the subscribedOrdersList where subscriptionType equals to UPDATED_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldNotBeFound("subscriptionType.equals=" + UPDATED_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionType in DEFAULT_SUBSCRIPTION_TYPE or UPDATED_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldBeFound("subscriptionType.in=" + DEFAULT_SUBSCRIPTION_TYPE + "," + UPDATED_SUBSCRIPTION_TYPE);

        // Get all the subscribedOrdersList where subscriptionType equals to UPDATED_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldNotBeFound("subscriptionType.in=" + UPDATED_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionType is not null
        defaultSubscribedOrdersShouldBeFound("subscriptionType.specified=true");

        // Get all the subscribedOrdersList where subscriptionType is null
        defaultSubscribedOrdersShouldNotBeFound("subscriptionType.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionType is greater than or equal to DEFAULT_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldBeFound("subscriptionType.greaterThanOrEqual=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the subscribedOrdersList where subscriptionType is greater than or equal to UPDATED_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldNotBeFound("subscriptionType.greaterThanOrEqual=" + UPDATED_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionType is less than or equal to DEFAULT_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldBeFound("subscriptionType.lessThanOrEqual=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the subscribedOrdersList where subscriptionType is less than or equal to SMALLER_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldNotBeFound("subscriptionType.lessThanOrEqual=" + SMALLER_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionType is less than DEFAULT_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldNotBeFound("subscriptionType.lessThan=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the subscribedOrdersList where subscriptionType is less than UPDATED_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldBeFound("subscriptionType.lessThan=" + UPDATED_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersBySubscriptionTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where subscriptionType is greater than DEFAULT_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldNotBeFound("subscriptionType.greaterThan=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the subscribedOrdersList where subscriptionType is greater than SMALLER_SUBSCRIPTION_TYPE
        defaultSubscribedOrdersShouldBeFound("subscriptionType.greaterThan=" + SMALLER_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByApprovalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where approvalStatus equals to DEFAULT_APPROVAL_STATUS
        defaultSubscribedOrdersShouldBeFound("approvalStatus.equals=" + DEFAULT_APPROVAL_STATUS);

        // Get all the subscribedOrdersList where approvalStatus equals to UPDATED_APPROVAL_STATUS
        defaultSubscribedOrdersShouldNotBeFound("approvalStatus.equals=" + UPDATED_APPROVAL_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByApprovalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where approvalStatus in DEFAULT_APPROVAL_STATUS or UPDATED_APPROVAL_STATUS
        defaultSubscribedOrdersShouldBeFound("approvalStatus.in=" + DEFAULT_APPROVAL_STATUS + "," + UPDATED_APPROVAL_STATUS);

        // Get all the subscribedOrdersList where approvalStatus equals to UPDATED_APPROVAL_STATUS
        defaultSubscribedOrdersShouldNotBeFound("approvalStatus.in=" + UPDATED_APPROVAL_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByApprovalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where approvalStatus is not null
        defaultSubscribedOrdersShouldBeFound("approvalStatus.specified=true");

        // Get all the subscribedOrdersList where approvalStatus is null
        defaultSubscribedOrdersShouldNotBeFound("approvalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByApprovalStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where approvalStatus is greater than or equal to DEFAULT_APPROVAL_STATUS
        defaultSubscribedOrdersShouldBeFound("approvalStatus.greaterThanOrEqual=" + DEFAULT_APPROVAL_STATUS);

        // Get all the subscribedOrdersList where approvalStatus is greater than or equal to UPDATED_APPROVAL_STATUS
        defaultSubscribedOrdersShouldNotBeFound("approvalStatus.greaterThanOrEqual=" + UPDATED_APPROVAL_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByApprovalStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where approvalStatus is less than or equal to DEFAULT_APPROVAL_STATUS
        defaultSubscribedOrdersShouldBeFound("approvalStatus.lessThanOrEqual=" + DEFAULT_APPROVAL_STATUS);

        // Get all the subscribedOrdersList where approvalStatus is less than or equal to SMALLER_APPROVAL_STATUS
        defaultSubscribedOrdersShouldNotBeFound("approvalStatus.lessThanOrEqual=" + SMALLER_APPROVAL_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByApprovalStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where approvalStatus is less than DEFAULT_APPROVAL_STATUS
        defaultSubscribedOrdersShouldNotBeFound("approvalStatus.lessThan=" + DEFAULT_APPROVAL_STATUS);

        // Get all the subscribedOrdersList where approvalStatus is less than UPDATED_APPROVAL_STATUS
        defaultSubscribedOrdersShouldBeFound("approvalStatus.lessThan=" + UPDATED_APPROVAL_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByApprovalStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where approvalStatus is greater than DEFAULT_APPROVAL_STATUS
        defaultSubscribedOrdersShouldNotBeFound("approvalStatus.greaterThan=" + DEFAULT_APPROVAL_STATUS);

        // Get all the subscribedOrdersList where approvalStatus is greater than SMALLER_APPROVAL_STATUS
        defaultSubscribedOrdersShouldBeFound("approvalStatus.greaterThan=" + SMALLER_APPROVAL_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderStatus equals to DEFAULT_ORDER_STATUS
        defaultSubscribedOrdersShouldBeFound("orderStatus.equals=" + DEFAULT_ORDER_STATUS);

        // Get all the subscribedOrdersList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultSubscribedOrdersShouldNotBeFound("orderStatus.equals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderStatusIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderStatus in DEFAULT_ORDER_STATUS or UPDATED_ORDER_STATUS
        defaultSubscribedOrdersShouldBeFound("orderStatus.in=" + DEFAULT_ORDER_STATUS + "," + UPDATED_ORDER_STATUS);

        // Get all the subscribedOrdersList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultSubscribedOrdersShouldNotBeFound("orderStatus.in=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByOrderStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where orderStatus is not null
        defaultSubscribedOrdersShouldBeFound("orderStatus.specified=true");

        // Get all the subscribedOrdersList where orderStatus is null
        defaultSubscribedOrdersShouldNotBeFound("orderStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where createdAt equals to DEFAULT_CREATED_AT
        defaultSubscribedOrdersShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the subscribedOrdersList where createdAt equals to UPDATED_CREATED_AT
        defaultSubscribedOrdersShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSubscribedOrdersShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the subscribedOrdersList where createdAt equals to UPDATED_CREATED_AT
        defaultSubscribedOrdersShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where createdAt is not null
        defaultSubscribedOrdersShouldBeFound("createdAt.specified=true");

        // Get all the subscribedOrdersList where createdAt is null
        defaultSubscribedOrdersShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSubscribedOrdersShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the subscribedOrdersList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSubscribedOrdersShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSubscribedOrdersShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the subscribedOrdersList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSubscribedOrdersShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where updatedAt is not null
        defaultSubscribedOrdersShouldBeFound("updatedAt.specified=true");

        // Get all the subscribedOrdersList where updatedAt is null
        defaultSubscribedOrdersShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where createdBy equals to DEFAULT_CREATED_BY
        defaultSubscribedOrdersShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the subscribedOrdersList where createdBy equals to UPDATED_CREATED_BY
        defaultSubscribedOrdersShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultSubscribedOrdersShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the subscribedOrdersList where createdBy equals to UPDATED_CREATED_BY
        defaultSubscribedOrdersShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where createdBy is not null
        defaultSubscribedOrdersShouldBeFound("createdBy.specified=true");

        // Get all the subscribedOrdersList where createdBy is null
        defaultSubscribedOrdersShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where createdBy contains DEFAULT_CREATED_BY
        defaultSubscribedOrdersShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the subscribedOrdersList where createdBy contains UPDATED_CREATED_BY
        defaultSubscribedOrdersShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where createdBy does not contain DEFAULT_CREATED_BY
        defaultSubscribedOrdersShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the subscribedOrdersList where createdBy does not contain UPDATED_CREATED_BY
        defaultSubscribedOrdersShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultSubscribedOrdersShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the subscribedOrdersList where updatedBy equals to UPDATED_UPDATED_BY
        defaultSubscribedOrdersShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultSubscribedOrdersShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the subscribedOrdersList where updatedBy equals to UPDATED_UPDATED_BY
        defaultSubscribedOrdersShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where updatedBy is not null
        defaultSubscribedOrdersShouldBeFound("updatedBy.specified=true");

        // Get all the subscribedOrdersList where updatedBy is null
        defaultSubscribedOrdersShouldNotBeFound("updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUpdatedByContainsSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where updatedBy contains DEFAULT_UPDATED_BY
        defaultSubscribedOrdersShouldBeFound("updatedBy.contains=" + DEFAULT_UPDATED_BY);

        // Get all the subscribedOrdersList where updatedBy contains UPDATED_UPDATED_BY
        defaultSubscribedOrdersShouldNotBeFound("updatedBy.contains=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUpdatedByNotContainsSomething() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        // Get all the subscribedOrdersList where updatedBy does not contain DEFAULT_UPDATED_BY
        defaultSubscribedOrdersShouldNotBeFound("updatedBy.doesNotContain=" + DEFAULT_UPDATED_BY);

        // Get all the subscribedOrdersList where updatedBy does not contain UPDATED_UPDATED_BY
        defaultSubscribedOrdersShouldBeFound("updatedBy.doesNotContain=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByUserIsEqualToSomething() throws Exception {
        Users user;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            subscribedOrdersRepository.saveAndFlush(subscribedOrders);
            user = UsersResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(user);
        em.flush();
        subscribedOrders.setUser(user);
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);
        Long userId = user.getId();
        // Get all the subscribedOrdersList where user equals to userId
        defaultSubscribedOrdersShouldBeFound("userId.equals=" + userId);

        // Get all the subscribedOrdersList where user equals to (userId + 1)
        defaultSubscribedOrdersShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByTransactionIsEqualToSomething() throws Exception {
        Transactions transaction;
        if (TestUtil.findAll(em, Transactions.class).isEmpty()) {
            subscribedOrdersRepository.saveAndFlush(subscribedOrders);
            transaction = TransactionsResourceIT.createEntity(em);
        } else {
            transaction = TestUtil.findAll(em, Transactions.class).get(0);
        }
        em.persist(transaction);
        em.flush();
        subscribedOrders.setTransaction(transaction);
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);
        Long transactionId = transaction.getId();
        // Get all the subscribedOrdersList where transaction equals to transactionId
        defaultSubscribedOrdersShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the subscribedOrdersList where transaction equals to (transactionId + 1)
        defaultSubscribedOrdersShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            subscribedOrdersRepository.saveAndFlush(subscribedOrders);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        subscribedOrders.setProduct(product);
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);
        Long productId = product.getId();
        // Get all the subscribedOrdersList where product equals to productId
        defaultSubscribedOrdersShouldBeFound("productId.equals=" + productId);

        // Get all the subscribedOrdersList where product equals to (productId + 1)
        defaultSubscribedOrdersShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllSubscribedOrdersByAddressIsEqualToSomething() throws Exception {
        UserAddress address;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            subscribedOrdersRepository.saveAndFlush(subscribedOrders);
            address = UserAddressResourceIT.createEntity(em);
        } else {
            address = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        em.persist(address);
        em.flush();
        subscribedOrders.setAddress(address);
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);
        Long addressId = address.getId();
        // Get all the subscribedOrdersList where address equals to addressId
        defaultSubscribedOrdersShouldBeFound("addressId.equals=" + addressId);

        // Get all the subscribedOrdersList where address equals to (addressId + 1)
        defaultSubscribedOrdersShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubscribedOrdersShouldBeFound(String filter) throws Exception {
        restSubscribedOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribedOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].orderAmount").value(hasItem(DEFAULT_ORDER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].subscriptionBalanceAmount").value(hasItem(DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].offerId").value(hasItem(DEFAULT_OFFER_ID)))
            .andExpect(jsonPath("$.[*].selectedDaysForWeekly").value(hasItem(DEFAULT_SELECTED_DAYS_FOR_WEEKLY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastRenewalDate").value(hasItem(DEFAULT_LAST_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionType").value(hasItem(DEFAULT_SUBSCRIPTION_TYPE)))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS)))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));

        // Check, that the count call also returns 1
        restSubscribedOrdersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubscribedOrdersShouldNotBeFound(String filter) throws Exception {
        restSubscribedOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubscribedOrdersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubscribedOrders() throws Exception {
        // Get the subscribedOrders
        restSubscribedOrdersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscribedOrders() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();

        // Update the subscribedOrders
        SubscribedOrders updatedSubscribedOrders = subscribedOrdersRepository.findById(subscribedOrders.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubscribedOrders are not directly saved in db
        em.detach(updatedSubscribedOrders);
        updatedSubscribedOrders
            .paymentType(UPDATED_PAYMENT_TYPE)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .subscriptionBalanceAmount(UPDATED_SUBSCRIPTION_BALANCE_AMOUNT)
            .price(UPDATED_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qty(UPDATED_QTY)
            .offerId(UPDATED_OFFER_ID)
            .selectedDaysForWeekly(UPDATED_SELECTED_DAYS_FOR_WEEKLY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastRenewalDate(UPDATED_LAST_RENEWAL_DATE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .orderStatus(UPDATED_ORDER_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(updatedSubscribedOrders);

        restSubscribedOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscribedOrdersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
        SubscribedOrders testSubscribedOrders = subscribedOrdersList.get(subscribedOrdersList.size() - 1);
        assertThat(testSubscribedOrders.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testSubscribedOrders.getOrderAmount()).isEqualTo(UPDATED_ORDER_AMOUNT);
        assertThat(testSubscribedOrders.getSubscriptionBalanceAmount()).isEqualTo(UPDATED_SUBSCRIPTION_BALANCE_AMOUNT);
        assertThat(testSubscribedOrders.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSubscribedOrders.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testSubscribedOrders.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testSubscribedOrders.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testSubscribedOrders.getOfferId()).isEqualTo(UPDATED_OFFER_ID);
        assertThat(testSubscribedOrders.getSelectedDaysForWeekly()).isEqualTo(UPDATED_SELECTED_DAYS_FOR_WEEKLY);
        assertThat(testSubscribedOrders.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubscribedOrders.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSubscribedOrders.getLastRenewalDate()).isEqualTo(UPDATED_LAST_RENEWAL_DATE);
        assertThat(testSubscribedOrders.getSubscriptionType()).isEqualTo(UPDATED_SUBSCRIPTION_TYPE);
        assertThat(testSubscribedOrders.getApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testSubscribedOrders.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testSubscribedOrders.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubscribedOrders.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSubscribedOrders.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubscribedOrders.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingSubscribedOrders() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();
        subscribedOrders.setId(longCount.incrementAndGet());

        // Create the SubscribedOrders
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscribedOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscribedOrdersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscribedOrders() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();
        subscribedOrders.setId(longCount.incrementAndGet());

        // Create the SubscribedOrders
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribedOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscribedOrders() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();
        subscribedOrders.setId(longCount.incrementAndGet());

        // Create the SubscribedOrders
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribedOrdersMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscribedOrdersWithPatch() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();

        // Update the subscribedOrders using partial update
        SubscribedOrders partialUpdatedSubscribedOrders = new SubscribedOrders();
        partialUpdatedSubscribedOrders.setId(subscribedOrders.getId());

        partialUpdatedSubscribedOrders
            .price(UPDATED_PRICE)
            .mrp(UPDATED_MRP)
            .offerId(UPDATED_OFFER_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastRenewalDate(UPDATED_LAST_RENEWAL_DATE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .createdBy(UPDATED_CREATED_BY);

        restSubscribedOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscribedOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscribedOrders))
            )
            .andExpect(status().isOk());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
        SubscribedOrders testSubscribedOrders = subscribedOrdersList.get(subscribedOrdersList.size() - 1);
        assertThat(testSubscribedOrders.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testSubscribedOrders.getOrderAmount()).isEqualTo(DEFAULT_ORDER_AMOUNT);
        assertThat(testSubscribedOrders.getSubscriptionBalanceAmount()).isEqualTo(DEFAULT_SUBSCRIPTION_BALANCE_AMOUNT);
        assertThat(testSubscribedOrders.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSubscribedOrders.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testSubscribedOrders.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testSubscribedOrders.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testSubscribedOrders.getOfferId()).isEqualTo(UPDATED_OFFER_ID);
        assertThat(testSubscribedOrders.getSelectedDaysForWeekly()).isEqualTo(DEFAULT_SELECTED_DAYS_FOR_WEEKLY);
        assertThat(testSubscribedOrders.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubscribedOrders.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSubscribedOrders.getLastRenewalDate()).isEqualTo(UPDATED_LAST_RENEWAL_DATE);
        assertThat(testSubscribedOrders.getSubscriptionType()).isEqualTo(UPDATED_SUBSCRIPTION_TYPE);
        assertThat(testSubscribedOrders.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testSubscribedOrders.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testSubscribedOrders.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubscribedOrders.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSubscribedOrders.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubscribedOrders.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateSubscribedOrdersWithPatch() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();

        // Update the subscribedOrders using partial update
        SubscribedOrders partialUpdatedSubscribedOrders = new SubscribedOrders();
        partialUpdatedSubscribedOrders.setId(subscribedOrders.getId());

        partialUpdatedSubscribedOrders
            .paymentType(UPDATED_PAYMENT_TYPE)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .subscriptionBalanceAmount(UPDATED_SUBSCRIPTION_BALANCE_AMOUNT)
            .price(UPDATED_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qty(UPDATED_QTY)
            .offerId(UPDATED_OFFER_ID)
            .selectedDaysForWeekly(UPDATED_SELECTED_DAYS_FOR_WEEKLY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastRenewalDate(UPDATED_LAST_RENEWAL_DATE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .orderStatus(UPDATED_ORDER_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restSubscribedOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscribedOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscribedOrders))
            )
            .andExpect(status().isOk());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
        SubscribedOrders testSubscribedOrders = subscribedOrdersList.get(subscribedOrdersList.size() - 1);
        assertThat(testSubscribedOrders.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testSubscribedOrders.getOrderAmount()).isEqualTo(UPDATED_ORDER_AMOUNT);
        assertThat(testSubscribedOrders.getSubscriptionBalanceAmount()).isEqualTo(UPDATED_SUBSCRIPTION_BALANCE_AMOUNT);
        assertThat(testSubscribedOrders.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSubscribedOrders.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testSubscribedOrders.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testSubscribedOrders.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testSubscribedOrders.getOfferId()).isEqualTo(UPDATED_OFFER_ID);
        assertThat(testSubscribedOrders.getSelectedDaysForWeekly()).isEqualTo(UPDATED_SELECTED_DAYS_FOR_WEEKLY);
        assertThat(testSubscribedOrders.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubscribedOrders.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSubscribedOrders.getLastRenewalDate()).isEqualTo(UPDATED_LAST_RENEWAL_DATE);
        assertThat(testSubscribedOrders.getSubscriptionType()).isEqualTo(UPDATED_SUBSCRIPTION_TYPE);
        assertThat(testSubscribedOrders.getApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testSubscribedOrders.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testSubscribedOrders.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubscribedOrders.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSubscribedOrders.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubscribedOrders.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingSubscribedOrders() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();
        subscribedOrders.setId(longCount.incrementAndGet());

        // Create the SubscribedOrders
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscribedOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscribedOrdersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscribedOrders() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();
        subscribedOrders.setId(longCount.incrementAndGet());

        // Create the SubscribedOrders
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribedOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscribedOrders() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrdersRepository.findAll().size();
        subscribedOrders.setId(longCount.incrementAndGet());

        // Create the SubscribedOrders
        SubscribedOrdersDTO subscribedOrdersDTO = subscribedOrdersMapper.toDto(subscribedOrders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribedOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrdersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscribedOrders in the database
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscribedOrders() throws Exception {
        // Initialize the database
        subscribedOrdersRepository.saveAndFlush(subscribedOrders);

        int databaseSizeBeforeDelete = subscribedOrdersRepository.findAll().size();

        // Delete the subscribedOrders
        restSubscribedOrdersMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscribedOrders.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscribedOrders> subscribedOrdersList = subscribedOrdersRepository.findAll();
        assertThat(subscribedOrdersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
