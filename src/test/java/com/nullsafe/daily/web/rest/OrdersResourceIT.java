package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.OrderUserAssign;
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.domain.SubscribedOrderDelivery;
import com.nullsafe.daily.domain.Transactions;
import com.nullsafe.daily.domain.UserAddress;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.OrdersRepository;
import com.nullsafe.daily.service.OrdersService;
import com.nullsafe.daily.service.dto.OrdersDTO;
import com.nullsafe.daily.service.mapper.OrdersMapper;
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
 * Integration tests for the {@link OrdersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrdersResourceIT {

    private static final Integer DEFAULT_ORDER_TYPE = 1;
    private static final Integer UPDATED_ORDER_TYPE = 2;
    private static final Integer SMALLER_ORDER_TYPE = 1 - 1;

    private static final Double DEFAULT_ORDER_AMOUNT = 1D;
    private static final Double UPDATED_ORDER_AMOUNT = 2D;
    private static final Double SMALLER_ORDER_AMOUNT = 1D - 1D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final Double DEFAULT_MRP = 1D;
    private static final Double UPDATED_MRP = 2D;
    private static final Double SMALLER_MRP = 1D - 1D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;
    private static final Double SMALLER_TAX = 1D - 1D;

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;
    private static final Integer SMALLER_QTY = 1 - 1;

    private static final String DEFAULT_SELECTED_DAYS_FOR_WEEKLY = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_DAYS_FOR_WEEKLY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_SUBSCRIPTION_TYPE = 1;
    private static final Integer UPDATED_SUBSCRIPTION_TYPE = 2;
    private static final Integer SMALLER_SUBSCRIPTION_TYPE = 1 - 1;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final Integer DEFAULT_DELIVERY_STATUS = 1;
    private static final Integer UPDATED_DELIVERY_STATUS = 2;
    private static final Integer SMALLER_DELIVERY_STATUS = 1 - 1;

    private static final Boolean DEFAULT_ORDER_STATUS = false;
    private static final Boolean UPDATED_ORDER_STATUS = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdersRepository ordersRepository;

    @Mock
    private OrdersRepository ordersRepositoryMock;

    @Autowired
    private OrdersMapper ordersMapper;

    @Mock
    private OrdersService ordersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdersMockMvc;

    private Orders orders;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orders createEntity(EntityManager em) {
        Orders orders = new Orders()
            .orderType(DEFAULT_ORDER_TYPE)
            .orderAmount(DEFAULT_ORDER_AMOUNT)
            .price(DEFAULT_PRICE)
            .mrp(DEFAULT_MRP)
            .tax(DEFAULT_TAX)
            .qty(DEFAULT_QTY)
            .selectedDaysForWeekly(DEFAULT_SELECTED_DAYS_FOR_WEEKLY)
            .startDate(DEFAULT_START_DATE)
            .subscriptionType(DEFAULT_SUBSCRIPTION_TYPE)
            .status(DEFAULT_STATUS)
            .deliveryStatus(DEFAULT_DELIVERY_STATUS)
            .orderStatus(DEFAULT_ORDER_STATUS)
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
        orders.setUser(users);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        orders.setProduct(product);
        // Add required entity
        UserAddress userAddress;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            userAddress = UserAddressResourceIT.createEntity(em);
            em.persist(userAddress);
            em.flush();
        } else {
            userAddress = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        orders.setAddress(userAddress);
        return orders;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orders createUpdatedEntity(EntityManager em) {
        Orders orders = new Orders()
            .orderType(UPDATED_ORDER_TYPE)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .price(UPDATED_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qty(UPDATED_QTY)
            .selectedDaysForWeekly(UPDATED_SELECTED_DAYS_FOR_WEEKLY)
            .startDate(UPDATED_START_DATE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .status(UPDATED_STATUS)
            .deliveryStatus(UPDATED_DELIVERY_STATUS)
            .orderStatus(UPDATED_ORDER_STATUS)
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
        orders.setUser(users);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        orders.setProduct(product);
        // Add required entity
        UserAddress userAddress;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            userAddress = UserAddressResourceIT.createUpdatedEntity(em);
            em.persist(userAddress);
            em.flush();
        } else {
            userAddress = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        orders.setAddress(userAddress);
        return orders;
    }

    @BeforeEach
    public void initTest() {
        orders = createEntity(em);
    }

    @Test
    @Transactional
    void createOrders() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();
        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);
        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrderType()).isEqualTo(DEFAULT_ORDER_TYPE);
        assertThat(testOrders.getOrderAmount()).isEqualTo(DEFAULT_ORDER_AMOUNT);
        assertThat(testOrders.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrders.getMrp()).isEqualTo(DEFAULT_MRP);
        assertThat(testOrders.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testOrders.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testOrders.getSelectedDaysForWeekly()).isEqualTo(DEFAULT_SELECTED_DAYS_FOR_WEEKLY);
        assertThat(testOrders.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOrders.getSubscriptionType()).isEqualTo(DEFAULT_SUBSCRIPTION_TYPE);
        assertThat(testOrders.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrders.getDeliveryStatus()).isEqualTo(DEFAULT_DELIVERY_STATUS);
        assertThat(testOrders.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testOrders.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOrders.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createOrdersWithExistingId() throws Exception {
        // Create the Orders with an existing ID
        orders.setId(1L);
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setOrderAmount(null);

        // Create the Orders, which fails.
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setPrice(null);

        // Create the Orders, which fails.
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMrpIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setMrp(null);

        // Create the Orders, which fails.
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setTax(null);

        // Create the Orders, which fails.
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setStatus(null);

        // Create the Orders, which fails.
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordersRepository.findAll().size();
        // set the field null
        orders.setOrderStatus(null);

        // Create the Orders, which fails.
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isBadRequest());

        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderType").value(hasItem(DEFAULT_ORDER_TYPE)))
            .andExpect(jsonPath("$.[*].orderAmount").value(hasItem(DEFAULT_ORDER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].selectedDaysForWeekly").value(hasItem(DEFAULT_SELECTED_DAYS_FOR_WEEKLY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionType").value(hasItem(DEFAULT_SUBSCRIPTION_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].deliveryStatus").value(hasItem(DEFAULT_DELIVERY_STATUS)))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(ordersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ordersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ordersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ordersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get the orders
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL_ID, orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orders.getId().intValue()))
            .andExpect(jsonPath("$.orderType").value(DEFAULT_ORDER_TYPE))
            .andExpect(jsonPath("$.orderAmount").value(DEFAULT_ORDER_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.mrp").value(DEFAULT_MRP.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.selectedDaysForWeekly").value(DEFAULT_SELECTED_DAYS_FOR_WEEKLY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.subscriptionType").value(DEFAULT_SUBSCRIPTION_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.deliveryStatus").value(DEFAULT_DELIVERY_STATUS))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        Long id = orders.getId();

        defaultOrdersShouldBeFound("id.equals=" + id);
        defaultOrdersShouldNotBeFound("id.notEquals=" + id);

        defaultOrdersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrdersShouldNotBeFound("id.greaterThan=" + id);

        defaultOrdersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrdersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderType equals to DEFAULT_ORDER_TYPE
        defaultOrdersShouldBeFound("orderType.equals=" + DEFAULT_ORDER_TYPE);

        // Get all the ordersList where orderType equals to UPDATED_ORDER_TYPE
        defaultOrdersShouldNotBeFound("orderType.equals=" + UPDATED_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderTypeIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderType in DEFAULT_ORDER_TYPE or UPDATED_ORDER_TYPE
        defaultOrdersShouldBeFound("orderType.in=" + DEFAULT_ORDER_TYPE + "," + UPDATED_ORDER_TYPE);

        // Get all the ordersList where orderType equals to UPDATED_ORDER_TYPE
        defaultOrdersShouldNotBeFound("orderType.in=" + UPDATED_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderType is not null
        defaultOrdersShouldBeFound("orderType.specified=true");

        // Get all the ordersList where orderType is null
        defaultOrdersShouldNotBeFound("orderType.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByOrderTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderType is greater than or equal to DEFAULT_ORDER_TYPE
        defaultOrdersShouldBeFound("orderType.greaterThanOrEqual=" + DEFAULT_ORDER_TYPE);

        // Get all the ordersList where orderType is greater than or equal to UPDATED_ORDER_TYPE
        defaultOrdersShouldNotBeFound("orderType.greaterThanOrEqual=" + UPDATED_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderType is less than or equal to DEFAULT_ORDER_TYPE
        defaultOrdersShouldBeFound("orderType.lessThanOrEqual=" + DEFAULT_ORDER_TYPE);

        // Get all the ordersList where orderType is less than or equal to SMALLER_ORDER_TYPE
        defaultOrdersShouldNotBeFound("orderType.lessThanOrEqual=" + SMALLER_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderType is less than DEFAULT_ORDER_TYPE
        defaultOrdersShouldNotBeFound("orderType.lessThan=" + DEFAULT_ORDER_TYPE);

        // Get all the ordersList where orderType is less than UPDATED_ORDER_TYPE
        defaultOrdersShouldBeFound("orderType.lessThan=" + UPDATED_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderType is greater than DEFAULT_ORDER_TYPE
        defaultOrdersShouldNotBeFound("orderType.greaterThan=" + DEFAULT_ORDER_TYPE);

        // Get all the ordersList where orderType is greater than SMALLER_ORDER_TYPE
        defaultOrdersShouldBeFound("orderType.greaterThan=" + SMALLER_ORDER_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderAmount equals to DEFAULT_ORDER_AMOUNT
        defaultOrdersShouldBeFound("orderAmount.equals=" + DEFAULT_ORDER_AMOUNT);

        // Get all the ordersList where orderAmount equals to UPDATED_ORDER_AMOUNT
        defaultOrdersShouldNotBeFound("orderAmount.equals=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderAmount in DEFAULT_ORDER_AMOUNT or UPDATED_ORDER_AMOUNT
        defaultOrdersShouldBeFound("orderAmount.in=" + DEFAULT_ORDER_AMOUNT + "," + UPDATED_ORDER_AMOUNT);

        // Get all the ordersList where orderAmount equals to UPDATED_ORDER_AMOUNT
        defaultOrdersShouldNotBeFound("orderAmount.in=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderAmount is not null
        defaultOrdersShouldBeFound("orderAmount.specified=true");

        // Get all the ordersList where orderAmount is null
        defaultOrdersShouldNotBeFound("orderAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByOrderAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderAmount is greater than or equal to DEFAULT_ORDER_AMOUNT
        defaultOrdersShouldBeFound("orderAmount.greaterThanOrEqual=" + DEFAULT_ORDER_AMOUNT);

        // Get all the ordersList where orderAmount is greater than or equal to UPDATED_ORDER_AMOUNT
        defaultOrdersShouldNotBeFound("orderAmount.greaterThanOrEqual=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderAmount is less than or equal to DEFAULT_ORDER_AMOUNT
        defaultOrdersShouldBeFound("orderAmount.lessThanOrEqual=" + DEFAULT_ORDER_AMOUNT);

        // Get all the ordersList where orderAmount is less than or equal to SMALLER_ORDER_AMOUNT
        defaultOrdersShouldNotBeFound("orderAmount.lessThanOrEqual=" + SMALLER_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderAmount is less than DEFAULT_ORDER_AMOUNT
        defaultOrdersShouldNotBeFound("orderAmount.lessThan=" + DEFAULT_ORDER_AMOUNT);

        // Get all the ordersList where orderAmount is less than UPDATED_ORDER_AMOUNT
        defaultOrdersShouldBeFound("orderAmount.lessThan=" + UPDATED_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderAmount is greater than DEFAULT_ORDER_AMOUNT
        defaultOrdersShouldNotBeFound("orderAmount.greaterThan=" + DEFAULT_ORDER_AMOUNT);

        // Get all the ordersList where orderAmount is greater than SMALLER_ORDER_AMOUNT
        defaultOrdersShouldBeFound("orderAmount.greaterThan=" + SMALLER_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllOrdersByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where price equals to DEFAULT_PRICE
        defaultOrdersShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the ordersList where price equals to UPDATED_PRICE
        defaultOrdersShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultOrdersShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the ordersList where price equals to UPDATED_PRICE
        defaultOrdersShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where price is not null
        defaultOrdersShouldBeFound("price.specified=true");

        // Get all the ordersList where price is null
        defaultOrdersShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where price is greater than or equal to DEFAULT_PRICE
        defaultOrdersShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the ordersList where price is greater than or equal to UPDATED_PRICE
        defaultOrdersShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where price is less than or equal to DEFAULT_PRICE
        defaultOrdersShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the ordersList where price is less than or equal to SMALLER_PRICE
        defaultOrdersShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where price is less than DEFAULT_PRICE
        defaultOrdersShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the ordersList where price is less than UPDATED_PRICE
        defaultOrdersShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where price is greater than DEFAULT_PRICE
        defaultOrdersShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the ordersList where price is greater than SMALLER_PRICE
        defaultOrdersShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllOrdersByMrpIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where mrp equals to DEFAULT_MRP
        defaultOrdersShouldBeFound("mrp.equals=" + DEFAULT_MRP);

        // Get all the ordersList where mrp equals to UPDATED_MRP
        defaultOrdersShouldNotBeFound("mrp.equals=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllOrdersByMrpIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where mrp in DEFAULT_MRP or UPDATED_MRP
        defaultOrdersShouldBeFound("mrp.in=" + DEFAULT_MRP + "," + UPDATED_MRP);

        // Get all the ordersList where mrp equals to UPDATED_MRP
        defaultOrdersShouldNotBeFound("mrp.in=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllOrdersByMrpIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where mrp is not null
        defaultOrdersShouldBeFound("mrp.specified=true");

        // Get all the ordersList where mrp is null
        defaultOrdersShouldNotBeFound("mrp.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByMrpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where mrp is greater than or equal to DEFAULT_MRP
        defaultOrdersShouldBeFound("mrp.greaterThanOrEqual=" + DEFAULT_MRP);

        // Get all the ordersList where mrp is greater than or equal to UPDATED_MRP
        defaultOrdersShouldNotBeFound("mrp.greaterThanOrEqual=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllOrdersByMrpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where mrp is less than or equal to DEFAULT_MRP
        defaultOrdersShouldBeFound("mrp.lessThanOrEqual=" + DEFAULT_MRP);

        // Get all the ordersList where mrp is less than or equal to SMALLER_MRP
        defaultOrdersShouldNotBeFound("mrp.lessThanOrEqual=" + SMALLER_MRP);
    }

    @Test
    @Transactional
    void getAllOrdersByMrpIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where mrp is less than DEFAULT_MRP
        defaultOrdersShouldNotBeFound("mrp.lessThan=" + DEFAULT_MRP);

        // Get all the ordersList where mrp is less than UPDATED_MRP
        defaultOrdersShouldBeFound("mrp.lessThan=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllOrdersByMrpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where mrp is greater than DEFAULT_MRP
        defaultOrdersShouldNotBeFound("mrp.greaterThan=" + DEFAULT_MRP);

        // Get all the ordersList where mrp is greater than SMALLER_MRP
        defaultOrdersShouldBeFound("mrp.greaterThan=" + SMALLER_MRP);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where tax equals to DEFAULT_TAX
        defaultOrdersShouldBeFound("tax.equals=" + DEFAULT_TAX);

        // Get all the ordersList where tax equals to UPDATED_TAX
        defaultOrdersShouldNotBeFound("tax.equals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where tax in DEFAULT_TAX or UPDATED_TAX
        defaultOrdersShouldBeFound("tax.in=" + DEFAULT_TAX + "," + UPDATED_TAX);

        // Get all the ordersList where tax equals to UPDATED_TAX
        defaultOrdersShouldNotBeFound("tax.in=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where tax is not null
        defaultOrdersShouldBeFound("tax.specified=true");

        // Get all the ordersList where tax is null
        defaultOrdersShouldNotBeFound("tax.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where tax is greater than or equal to DEFAULT_TAX
        defaultOrdersShouldBeFound("tax.greaterThanOrEqual=" + DEFAULT_TAX);

        // Get all the ordersList where tax is greater than or equal to UPDATED_TAX
        defaultOrdersShouldNotBeFound("tax.greaterThanOrEqual=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where tax is less than or equal to DEFAULT_TAX
        defaultOrdersShouldBeFound("tax.lessThanOrEqual=" + DEFAULT_TAX);

        // Get all the ordersList where tax is less than or equal to SMALLER_TAX
        defaultOrdersShouldNotBeFound("tax.lessThanOrEqual=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where tax is less than DEFAULT_TAX
        defaultOrdersShouldNotBeFound("tax.lessThan=" + DEFAULT_TAX);

        // Get all the ordersList where tax is less than UPDATED_TAX
        defaultOrdersShouldBeFound("tax.lessThan=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllOrdersByTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where tax is greater than DEFAULT_TAX
        defaultOrdersShouldNotBeFound("tax.greaterThan=" + DEFAULT_TAX);

        // Get all the ordersList where tax is greater than SMALLER_TAX
        defaultOrdersShouldBeFound("tax.greaterThan=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllOrdersByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where qty equals to DEFAULT_QTY
        defaultOrdersShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the ordersList where qty equals to UPDATED_QTY
        defaultOrdersShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllOrdersByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultOrdersShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the ordersList where qty equals to UPDATED_QTY
        defaultOrdersShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllOrdersByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where qty is not null
        defaultOrdersShouldBeFound("qty.specified=true");

        // Get all the ordersList where qty is null
        defaultOrdersShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where qty is greater than or equal to DEFAULT_QTY
        defaultOrdersShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the ordersList where qty is greater than or equal to UPDATED_QTY
        defaultOrdersShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllOrdersByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where qty is less than or equal to DEFAULT_QTY
        defaultOrdersShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the ordersList where qty is less than or equal to SMALLER_QTY
        defaultOrdersShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllOrdersByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where qty is less than DEFAULT_QTY
        defaultOrdersShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the ordersList where qty is less than UPDATED_QTY
        defaultOrdersShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllOrdersByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where qty is greater than DEFAULT_QTY
        defaultOrdersShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the ordersList where qty is greater than SMALLER_QTY
        defaultOrdersShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllOrdersBySelectedDaysForWeeklyIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where selectedDaysForWeekly equals to DEFAULT_SELECTED_DAYS_FOR_WEEKLY
        defaultOrdersShouldBeFound("selectedDaysForWeekly.equals=" + DEFAULT_SELECTED_DAYS_FOR_WEEKLY);

        // Get all the ordersList where selectedDaysForWeekly equals to UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultOrdersShouldNotBeFound("selectedDaysForWeekly.equals=" + UPDATED_SELECTED_DAYS_FOR_WEEKLY);
    }

    @Test
    @Transactional
    void getAllOrdersBySelectedDaysForWeeklyIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where selectedDaysForWeekly in DEFAULT_SELECTED_DAYS_FOR_WEEKLY or UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultOrdersShouldBeFound("selectedDaysForWeekly.in=" + DEFAULT_SELECTED_DAYS_FOR_WEEKLY + "," + UPDATED_SELECTED_DAYS_FOR_WEEKLY);

        // Get all the ordersList where selectedDaysForWeekly equals to UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultOrdersShouldNotBeFound("selectedDaysForWeekly.in=" + UPDATED_SELECTED_DAYS_FOR_WEEKLY);
    }

    @Test
    @Transactional
    void getAllOrdersBySelectedDaysForWeeklyIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where selectedDaysForWeekly is not null
        defaultOrdersShouldBeFound("selectedDaysForWeekly.specified=true");

        // Get all the ordersList where selectedDaysForWeekly is null
        defaultOrdersShouldNotBeFound("selectedDaysForWeekly.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersBySelectedDaysForWeeklyContainsSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where selectedDaysForWeekly contains DEFAULT_SELECTED_DAYS_FOR_WEEKLY
        defaultOrdersShouldBeFound("selectedDaysForWeekly.contains=" + DEFAULT_SELECTED_DAYS_FOR_WEEKLY);

        // Get all the ordersList where selectedDaysForWeekly contains UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultOrdersShouldNotBeFound("selectedDaysForWeekly.contains=" + UPDATED_SELECTED_DAYS_FOR_WEEKLY);
    }

    @Test
    @Transactional
    void getAllOrdersBySelectedDaysForWeeklyNotContainsSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where selectedDaysForWeekly does not contain DEFAULT_SELECTED_DAYS_FOR_WEEKLY
        defaultOrdersShouldNotBeFound("selectedDaysForWeekly.doesNotContain=" + DEFAULT_SELECTED_DAYS_FOR_WEEKLY);

        // Get all the ordersList where selectedDaysForWeekly does not contain UPDATED_SELECTED_DAYS_FOR_WEEKLY
        defaultOrdersShouldBeFound("selectedDaysForWeekly.doesNotContain=" + UPDATED_SELECTED_DAYS_FOR_WEEKLY);
    }

    @Test
    @Transactional
    void getAllOrdersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where startDate equals to DEFAULT_START_DATE
        defaultOrdersShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the ordersList where startDate equals to UPDATED_START_DATE
        defaultOrdersShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultOrdersShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the ordersList where startDate equals to UPDATED_START_DATE
        defaultOrdersShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where startDate is not null
        defaultOrdersShouldBeFound("startDate.specified=true");

        // Get all the ordersList where startDate is null
        defaultOrdersShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultOrdersShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the ordersList where startDate is greater than or equal to UPDATED_START_DATE
        defaultOrdersShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where startDate is less than or equal to DEFAULT_START_DATE
        defaultOrdersShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the ordersList where startDate is less than or equal to SMALLER_START_DATE
        defaultOrdersShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where startDate is less than DEFAULT_START_DATE
        defaultOrdersShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the ordersList where startDate is less than UPDATED_START_DATE
        defaultOrdersShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where startDate is greater than DEFAULT_START_DATE
        defaultOrdersShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the ordersList where startDate is greater than SMALLER_START_DATE
        defaultOrdersShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllOrdersBySubscriptionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where subscriptionType equals to DEFAULT_SUBSCRIPTION_TYPE
        defaultOrdersShouldBeFound("subscriptionType.equals=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the ordersList where subscriptionType equals to UPDATED_SUBSCRIPTION_TYPE
        defaultOrdersShouldNotBeFound("subscriptionType.equals=" + UPDATED_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersBySubscriptionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where subscriptionType in DEFAULT_SUBSCRIPTION_TYPE or UPDATED_SUBSCRIPTION_TYPE
        defaultOrdersShouldBeFound("subscriptionType.in=" + DEFAULT_SUBSCRIPTION_TYPE + "," + UPDATED_SUBSCRIPTION_TYPE);

        // Get all the ordersList where subscriptionType equals to UPDATED_SUBSCRIPTION_TYPE
        defaultOrdersShouldNotBeFound("subscriptionType.in=" + UPDATED_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersBySubscriptionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where subscriptionType is not null
        defaultOrdersShouldBeFound("subscriptionType.specified=true");

        // Get all the ordersList where subscriptionType is null
        defaultOrdersShouldNotBeFound("subscriptionType.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersBySubscriptionTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where subscriptionType is greater than or equal to DEFAULT_SUBSCRIPTION_TYPE
        defaultOrdersShouldBeFound("subscriptionType.greaterThanOrEqual=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the ordersList where subscriptionType is greater than or equal to UPDATED_SUBSCRIPTION_TYPE
        defaultOrdersShouldNotBeFound("subscriptionType.greaterThanOrEqual=" + UPDATED_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersBySubscriptionTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where subscriptionType is less than or equal to DEFAULT_SUBSCRIPTION_TYPE
        defaultOrdersShouldBeFound("subscriptionType.lessThanOrEqual=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the ordersList where subscriptionType is less than or equal to SMALLER_SUBSCRIPTION_TYPE
        defaultOrdersShouldNotBeFound("subscriptionType.lessThanOrEqual=" + SMALLER_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersBySubscriptionTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where subscriptionType is less than DEFAULT_SUBSCRIPTION_TYPE
        defaultOrdersShouldNotBeFound("subscriptionType.lessThan=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the ordersList where subscriptionType is less than UPDATED_SUBSCRIPTION_TYPE
        defaultOrdersShouldBeFound("subscriptionType.lessThan=" + UPDATED_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersBySubscriptionTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where subscriptionType is greater than DEFAULT_SUBSCRIPTION_TYPE
        defaultOrdersShouldNotBeFound("subscriptionType.greaterThan=" + DEFAULT_SUBSCRIPTION_TYPE);

        // Get all the ordersList where subscriptionType is greater than SMALLER_SUBSCRIPTION_TYPE
        defaultOrdersShouldBeFound("subscriptionType.greaterThan=" + SMALLER_SUBSCRIPTION_TYPE);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status equals to DEFAULT_STATUS
        defaultOrdersShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the ordersList where status equals to UPDATED_STATUS
        defaultOrdersShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrdersShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the ordersList where status equals to UPDATED_STATUS
        defaultOrdersShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status is not null
        defaultOrdersShouldBeFound("status.specified=true");

        // Get all the ordersList where status is null
        defaultOrdersShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status is greater than or equal to DEFAULT_STATUS
        defaultOrdersShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the ordersList where status is greater than or equal to UPDATED_STATUS
        defaultOrdersShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status is less than or equal to DEFAULT_STATUS
        defaultOrdersShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the ordersList where status is less than or equal to SMALLER_STATUS
        defaultOrdersShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status is less than DEFAULT_STATUS
        defaultOrdersShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the ordersList where status is less than UPDATED_STATUS
        defaultOrdersShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status is greater than DEFAULT_STATUS
        defaultOrdersShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the ordersList where status is greater than SMALLER_STATUS
        defaultOrdersShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where deliveryStatus equals to DEFAULT_DELIVERY_STATUS
        defaultOrdersShouldBeFound("deliveryStatus.equals=" + DEFAULT_DELIVERY_STATUS);

        // Get all the ordersList where deliveryStatus equals to UPDATED_DELIVERY_STATUS
        defaultOrdersShouldNotBeFound("deliveryStatus.equals=" + UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where deliveryStatus in DEFAULT_DELIVERY_STATUS or UPDATED_DELIVERY_STATUS
        defaultOrdersShouldBeFound("deliveryStatus.in=" + DEFAULT_DELIVERY_STATUS + "," + UPDATED_DELIVERY_STATUS);

        // Get all the ordersList where deliveryStatus equals to UPDATED_DELIVERY_STATUS
        defaultOrdersShouldNotBeFound("deliveryStatus.in=" + UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where deliveryStatus is not null
        defaultOrdersShouldBeFound("deliveryStatus.specified=true");

        // Get all the ordersList where deliveryStatus is null
        defaultOrdersShouldNotBeFound("deliveryStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where deliveryStatus is greater than or equal to DEFAULT_DELIVERY_STATUS
        defaultOrdersShouldBeFound("deliveryStatus.greaterThanOrEqual=" + DEFAULT_DELIVERY_STATUS);

        // Get all the ordersList where deliveryStatus is greater than or equal to UPDATED_DELIVERY_STATUS
        defaultOrdersShouldNotBeFound("deliveryStatus.greaterThanOrEqual=" + UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where deliveryStatus is less than or equal to DEFAULT_DELIVERY_STATUS
        defaultOrdersShouldBeFound("deliveryStatus.lessThanOrEqual=" + DEFAULT_DELIVERY_STATUS);

        // Get all the ordersList where deliveryStatus is less than or equal to SMALLER_DELIVERY_STATUS
        defaultOrdersShouldNotBeFound("deliveryStatus.lessThanOrEqual=" + SMALLER_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where deliveryStatus is less than DEFAULT_DELIVERY_STATUS
        defaultOrdersShouldNotBeFound("deliveryStatus.lessThan=" + DEFAULT_DELIVERY_STATUS);

        // Get all the ordersList where deliveryStatus is less than UPDATED_DELIVERY_STATUS
        defaultOrdersShouldBeFound("deliveryStatus.lessThan=" + UPDATED_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where deliveryStatus is greater than DEFAULT_DELIVERY_STATUS
        defaultOrdersShouldNotBeFound("deliveryStatus.greaterThan=" + DEFAULT_DELIVERY_STATUS);

        // Get all the ordersList where deliveryStatus is greater than SMALLER_DELIVERY_STATUS
        defaultOrdersShouldBeFound("deliveryStatus.greaterThan=" + SMALLER_DELIVERY_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderStatus equals to DEFAULT_ORDER_STATUS
        defaultOrdersShouldBeFound("orderStatus.equals=" + DEFAULT_ORDER_STATUS);

        // Get all the ordersList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultOrdersShouldNotBeFound("orderStatus.equals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderStatus in DEFAULT_ORDER_STATUS or UPDATED_ORDER_STATUS
        defaultOrdersShouldBeFound("orderStatus.in=" + DEFAULT_ORDER_STATUS + "," + UPDATED_ORDER_STATUS);

        // Get all the ordersList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultOrdersShouldNotBeFound("orderStatus.in=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where orderStatus is not null
        defaultOrdersShouldBeFound("orderStatus.specified=true");

        // Get all the ordersList where orderStatus is null
        defaultOrdersShouldNotBeFound("orderStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where createdAt equals to DEFAULT_CREATED_AT
        defaultOrdersShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the ordersList where createdAt equals to UPDATED_CREATED_AT
        defaultOrdersShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultOrdersShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the ordersList where createdAt equals to UPDATED_CREATED_AT
        defaultOrdersShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrdersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where createdAt is not null
        defaultOrdersShouldBeFound("createdAt.specified=true");

        // Get all the ordersList where createdAt is null
        defaultOrdersShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultOrdersShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the ordersList where updatedAt equals to UPDATED_UPDATED_AT
        defaultOrdersShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrdersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultOrdersShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the ordersList where updatedAt equals to UPDATED_UPDATED_AT
        defaultOrdersShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrdersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where updatedAt is not null
        defaultOrdersShouldBeFound("updatedAt.specified=true");

        // Get all the ordersList where updatedAt is null
        defaultOrdersShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByUserIsEqualToSomething() throws Exception {
        Users user;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            user = UsersResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(user);
        em.flush();
        orders.setUser(user);
        ordersRepository.saveAndFlush(orders);
        Long userId = user.getId();
        // Get all the ordersList where user equals to userId
        defaultOrdersShouldBeFound("userId.equals=" + userId);

        // Get all the ordersList where user equals to (userId + 1)
        defaultOrdersShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByTrasationIsEqualToSomething() throws Exception {
        Transactions trasation;
        if (TestUtil.findAll(em, Transactions.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            trasation = TransactionsResourceIT.createEntity(em);
        } else {
            trasation = TestUtil.findAll(em, Transactions.class).get(0);
        }
        em.persist(trasation);
        em.flush();
        orders.setTrasation(trasation);
        ordersRepository.saveAndFlush(orders);
        Long trasationId = trasation.getId();
        // Get all the ordersList where trasation equals to trasationId
        defaultOrdersShouldBeFound("trasationId.equals=" + trasationId);

        // Get all the ordersList where trasation equals to (trasationId + 1)
        defaultOrdersShouldNotBeFound("trasationId.equals=" + (trasationId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        orders.setProduct(product);
        ordersRepository.saveAndFlush(orders);
        Long productId = product.getId();
        // Get all the ordersList where product equals to productId
        defaultOrdersShouldBeFound("productId.equals=" + productId);

        // Get all the ordersList where product equals to (productId + 1)
        defaultOrdersShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByAddressIsEqualToSomething() throws Exception {
        UserAddress address;
        if (TestUtil.findAll(em, UserAddress.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            address = UserAddressResourceIT.createEntity(em);
        } else {
            address = TestUtil.findAll(em, UserAddress.class).get(0);
        }
        em.persist(address);
        em.flush();
        orders.setAddress(address);
        ordersRepository.saveAndFlush(orders);
        Long addressId = address.getId();
        // Get all the ordersList where address equals to addressId
        defaultOrdersShouldBeFound("addressId.equals=" + addressId);

        // Get all the ordersList where address equals to (addressId + 1)
        defaultOrdersShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByOrderUserAssignIsEqualToSomething() throws Exception {
        OrderUserAssign orderUserAssign;
        if (TestUtil.findAll(em, OrderUserAssign.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            orderUserAssign = OrderUserAssignResourceIT.createEntity(em);
        } else {
            orderUserAssign = TestUtil.findAll(em, OrderUserAssign.class).get(0);
        }
        em.persist(orderUserAssign);
        em.flush();
        orders.addOrderUserAssign(orderUserAssign);
        ordersRepository.saveAndFlush(orders);
        Long orderUserAssignId = orderUserAssign.getId();
        // Get all the ordersList where orderUserAssign equals to orderUserAssignId
        defaultOrdersShouldBeFound("orderUserAssignId.equals=" + orderUserAssignId);

        // Get all the ordersList where orderUserAssign equals to (orderUserAssignId + 1)
        defaultOrdersShouldNotBeFound("orderUserAssignId.equals=" + (orderUserAssignId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersBySubscribedOrderDeliveryIsEqualToSomething() throws Exception {
        SubscribedOrderDelivery subscribedOrderDelivery;
        if (TestUtil.findAll(em, SubscribedOrderDelivery.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            subscribedOrderDelivery = SubscribedOrderDeliveryResourceIT.createEntity(em);
        } else {
            subscribedOrderDelivery = TestUtil.findAll(em, SubscribedOrderDelivery.class).get(0);
        }
        em.persist(subscribedOrderDelivery);
        em.flush();
        orders.addSubscribedOrderDelivery(subscribedOrderDelivery);
        ordersRepository.saveAndFlush(orders);
        Long subscribedOrderDeliveryId = subscribedOrderDelivery.getId();
        // Get all the ordersList where subscribedOrderDelivery equals to subscribedOrderDeliveryId
        defaultOrdersShouldBeFound("subscribedOrderDeliveryId.equals=" + subscribedOrderDeliveryId);

        // Get all the ordersList where subscribedOrderDelivery equals to (subscribedOrderDeliveryId + 1)
        defaultOrdersShouldNotBeFound("subscribedOrderDeliveryId.equals=" + (subscribedOrderDeliveryId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByTransactionsIsEqualToSomething() throws Exception {
        Transactions transactions;
        if (TestUtil.findAll(em, Transactions.class).isEmpty()) {
            ordersRepository.saveAndFlush(orders);
            transactions = TransactionsResourceIT.createEntity(em);
        } else {
            transactions = TestUtil.findAll(em, Transactions.class).get(0);
        }
        em.persist(transactions);
        em.flush();
        orders.addTransactions(transactions);
        ordersRepository.saveAndFlush(orders);
        Long transactionsId = transactions.getId();
        // Get all the ordersList where transactions equals to transactionsId
        defaultOrdersShouldBeFound("transactionsId.equals=" + transactionsId);

        // Get all the ordersList where transactions equals to (transactionsId + 1)
        defaultOrdersShouldNotBeFound("transactionsId.equals=" + (transactionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrdersShouldBeFound(String filter) throws Exception {
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderType").value(hasItem(DEFAULT_ORDER_TYPE)))
            .andExpect(jsonPath("$.[*].orderAmount").value(hasItem(DEFAULT_ORDER_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].selectedDaysForWeekly").value(hasItem(DEFAULT_SELECTED_DAYS_FOR_WEEKLY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionType").value(hasItem(DEFAULT_SUBSCRIPTION_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].deliveryStatus").value(hasItem(DEFAULT_DELIVERY_STATUS)))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrdersShouldNotBeFound(String filter) throws Exception {
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders
        Orders updatedOrders = ordersRepository.findById(orders.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrders are not directly saved in db
        em.detach(updatedOrders);
        updatedOrders
            .orderType(UPDATED_ORDER_TYPE)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .price(UPDATED_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qty(UPDATED_QTY)
            .selectedDaysForWeekly(UPDATED_SELECTED_DAYS_FOR_WEEKLY)
            .startDate(UPDATED_START_DATE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .status(UPDATED_STATUS)
            .deliveryStatus(UPDATED_DELIVERY_STATUS)
            .orderStatus(UPDATED_ORDER_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        OrdersDTO ordersDTO = ordersMapper.toDto(updatedOrders);

        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testOrders.getOrderAmount()).isEqualTo(UPDATED_ORDER_AMOUNT);
        assertThat(testOrders.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrders.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testOrders.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testOrders.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testOrders.getSelectedDaysForWeekly()).isEqualTo(UPDATED_SELECTED_DAYS_FOR_WEEKLY);
        assertThat(testOrders.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOrders.getSubscriptionType()).isEqualTo(UPDATED_SUBSCRIPTION_TYPE);
        assertThat(testOrders.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrders.getDeliveryStatus()).isEqualTo(UPDATED_DELIVERY_STATUS);
        assertThat(testOrders.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrders.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrders.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(longCount.incrementAndGet());

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(longCount.incrementAndGet());

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(longCount.incrementAndGet());

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdersWithPatch() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders using partial update
        Orders partialUpdatedOrders = new Orders();
        partialUpdatedOrders.setId(orders.getId());

        partialUpdatedOrders
            .orderType(UPDATED_ORDER_TYPE)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .price(UPDATED_PRICE)
            .mrp(UPDATED_MRP)
            .startDate(UPDATED_START_DATE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrders))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testOrders.getOrderAmount()).isEqualTo(UPDATED_ORDER_AMOUNT);
        assertThat(testOrders.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrders.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testOrders.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testOrders.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testOrders.getSelectedDaysForWeekly()).isEqualTo(DEFAULT_SELECTED_DAYS_FOR_WEEKLY);
        assertThat(testOrders.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOrders.getSubscriptionType()).isEqualTo(UPDATED_SUBSCRIPTION_TYPE);
        assertThat(testOrders.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrders.getDeliveryStatus()).isEqualTo(DEFAULT_DELIVERY_STATUS);
        assertThat(testOrders.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testOrders.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrders.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateOrdersWithPatch() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders using partial update
        Orders partialUpdatedOrders = new Orders();
        partialUpdatedOrders.setId(orders.getId());

        partialUpdatedOrders
            .orderType(UPDATED_ORDER_TYPE)
            .orderAmount(UPDATED_ORDER_AMOUNT)
            .price(UPDATED_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qty(UPDATED_QTY)
            .selectedDaysForWeekly(UPDATED_SELECTED_DAYS_FOR_WEEKLY)
            .startDate(UPDATED_START_DATE)
            .subscriptionType(UPDATED_SUBSCRIPTION_TYPE)
            .status(UPDATED_STATUS)
            .deliveryStatus(UPDATED_DELIVERY_STATUS)
            .orderStatus(UPDATED_ORDER_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrders))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testOrders.getOrderAmount()).isEqualTo(UPDATED_ORDER_AMOUNT);
        assertThat(testOrders.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrders.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testOrders.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testOrders.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testOrders.getSelectedDaysForWeekly()).isEqualTo(UPDATED_SELECTED_DAYS_FOR_WEEKLY);
        assertThat(testOrders.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOrders.getSubscriptionType()).isEqualTo(UPDATED_SUBSCRIPTION_TYPE);
        assertThat(testOrders.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrders.getDeliveryStatus()).isEqualTo(UPDATED_DELIVERY_STATUS);
        assertThat(testOrders.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrders.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrders.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(longCount.incrementAndGet());

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(longCount.incrementAndGet());

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();
        orders.setId(longCount.incrementAndGet());

        // Create the Orders
        OrdersDTO ordersDTO = ordersMapper.toDto(orders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        int databaseSizeBeforeDelete = ordersRepository.findAll().size();

        // Delete the orders
        restOrdersMockMvc
            .perform(delete(ENTITY_API_URL_ID, orders.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
