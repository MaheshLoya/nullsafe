package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.SubscribedOrderDelivery;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.SubscribedOrderDeliveryRepository;
import com.nullsafe.daily.service.SubscribedOrderDeliveryService;
import com.nullsafe.daily.service.dto.SubscribedOrderDeliveryDTO;
import com.nullsafe.daily.service.mapper.SubscribedOrderDeliveryMapper;
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
 * Integration tests for the {@link SubscribedOrderDeliveryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubscribedOrderDeliveryResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_PAYMENT_MODE = 1;
    private static final Integer UPDATED_PAYMENT_MODE = 2;
    private static final Integer SMALLER_PAYMENT_MODE = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/subscribed-order-deliveries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscribedOrderDeliveryRepository subscribedOrderDeliveryRepository;

    @Mock
    private SubscribedOrderDeliveryRepository subscribedOrderDeliveryRepositoryMock;

    @Autowired
    private SubscribedOrderDeliveryMapper subscribedOrderDeliveryMapper;

    @Mock
    private SubscribedOrderDeliveryService subscribedOrderDeliveryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscribedOrderDeliveryMockMvc;

    private SubscribedOrderDelivery subscribedOrderDelivery;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscribedOrderDelivery createEntity(EntityManager em) {
        SubscribedOrderDelivery subscribedOrderDelivery = new SubscribedOrderDelivery()
            .date(DEFAULT_DATE)
            .paymentMode(DEFAULT_PAYMENT_MODE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            orders = OrdersResourceIT.createEntity(em);
            em.persist(orders);
            em.flush();
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        subscribedOrderDelivery.setOrder(orders);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        subscribedOrderDelivery.setEntryUser(users);
        return subscribedOrderDelivery;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscribedOrderDelivery createUpdatedEntity(EntityManager em) {
        SubscribedOrderDelivery subscribedOrderDelivery = new SubscribedOrderDelivery()
            .date(UPDATED_DATE)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            orders = OrdersResourceIT.createUpdatedEntity(em);
            em.persist(orders);
            em.flush();
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        subscribedOrderDelivery.setOrder(orders);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createUpdatedEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        subscribedOrderDelivery.setEntryUser(users);
        return subscribedOrderDelivery;
    }

    @BeforeEach
    public void initTest() {
        subscribedOrderDelivery = createEntity(em);
    }

    @Test
    @Transactional
    void createSubscribedOrderDelivery() throws Exception {
        int databaseSizeBeforeCreate = subscribedOrderDeliveryRepository.findAll().size();
        // Create the SubscribedOrderDelivery
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);
        restSubscribedOrderDeliveryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeCreate + 1);
        SubscribedOrderDelivery testSubscribedOrderDelivery = subscribedOrderDeliveryList.get(subscribedOrderDeliveryList.size() - 1);
        assertThat(testSubscribedOrderDelivery.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSubscribedOrderDelivery.getPaymentMode()).isEqualTo(DEFAULT_PAYMENT_MODE);
        assertThat(testSubscribedOrderDelivery.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubscribedOrderDelivery.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSubscribedOrderDeliveryWithExistingId() throws Exception {
        // Create the SubscribedOrderDelivery with an existing ID
        subscribedOrderDelivery.setId(1L);
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);

        int databaseSizeBeforeCreate = subscribedOrderDeliveryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscribedOrderDeliveryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveries() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList
        restSubscribedOrderDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribedOrderDelivery.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscribedOrderDeliveriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(subscribedOrderDeliveryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscribedOrderDeliveryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subscribedOrderDeliveryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubscribedOrderDeliveriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subscribedOrderDeliveryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubscribedOrderDeliveryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(subscribedOrderDeliveryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSubscribedOrderDelivery() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get the subscribedOrderDelivery
        restSubscribedOrderDeliveryMockMvc
            .perform(get(ENTITY_API_URL_ID, subscribedOrderDelivery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscribedOrderDelivery.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.paymentMode").value(DEFAULT_PAYMENT_MODE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getSubscribedOrderDeliveriesByIdFiltering() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        Long id = subscribedOrderDelivery.getId();

        defaultSubscribedOrderDeliveryShouldBeFound("id.equals=" + id);
        defaultSubscribedOrderDeliveryShouldNotBeFound("id.notEquals=" + id);

        defaultSubscribedOrderDeliveryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubscribedOrderDeliveryShouldNotBeFound("id.greaterThan=" + id);

        defaultSubscribedOrderDeliveryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubscribedOrderDeliveryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where date equals to DEFAULT_DATE
        defaultSubscribedOrderDeliveryShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the subscribedOrderDeliveryList where date equals to UPDATED_DATE
        defaultSubscribedOrderDeliveryShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where date in DEFAULT_DATE or UPDATED_DATE
        defaultSubscribedOrderDeliveryShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the subscribedOrderDeliveryList where date equals to UPDATED_DATE
        defaultSubscribedOrderDeliveryShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where date is not null
        defaultSubscribedOrderDeliveryShouldBeFound("date.specified=true");

        // Get all the subscribedOrderDeliveryList where date is null
        defaultSubscribedOrderDeliveryShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where date is greater than or equal to DEFAULT_DATE
        defaultSubscribedOrderDeliveryShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the subscribedOrderDeliveryList where date is greater than or equal to UPDATED_DATE
        defaultSubscribedOrderDeliveryShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where date is less than or equal to DEFAULT_DATE
        defaultSubscribedOrderDeliveryShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the subscribedOrderDeliveryList where date is less than or equal to SMALLER_DATE
        defaultSubscribedOrderDeliveryShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where date is less than DEFAULT_DATE
        defaultSubscribedOrderDeliveryShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the subscribedOrderDeliveryList where date is less than UPDATED_DATE
        defaultSubscribedOrderDeliveryShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where date is greater than DEFAULT_DATE
        defaultSubscribedOrderDeliveryShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the subscribedOrderDeliveryList where date is greater than SMALLER_DATE
        defaultSubscribedOrderDeliveryShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByPaymentModeIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where paymentMode equals to DEFAULT_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldBeFound("paymentMode.equals=" + DEFAULT_PAYMENT_MODE);

        // Get all the subscribedOrderDeliveryList where paymentMode equals to UPDATED_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldNotBeFound("paymentMode.equals=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByPaymentModeIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where paymentMode in DEFAULT_PAYMENT_MODE or UPDATED_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldBeFound("paymentMode.in=" + DEFAULT_PAYMENT_MODE + "," + UPDATED_PAYMENT_MODE);

        // Get all the subscribedOrderDeliveryList where paymentMode equals to UPDATED_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldNotBeFound("paymentMode.in=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByPaymentModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where paymentMode is not null
        defaultSubscribedOrderDeliveryShouldBeFound("paymentMode.specified=true");

        // Get all the subscribedOrderDeliveryList where paymentMode is null
        defaultSubscribedOrderDeliveryShouldNotBeFound("paymentMode.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByPaymentModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where paymentMode is greater than or equal to DEFAULT_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldBeFound("paymentMode.greaterThanOrEqual=" + DEFAULT_PAYMENT_MODE);

        // Get all the subscribedOrderDeliveryList where paymentMode is greater than or equal to UPDATED_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldNotBeFound("paymentMode.greaterThanOrEqual=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByPaymentModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where paymentMode is less than or equal to DEFAULT_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldBeFound("paymentMode.lessThanOrEqual=" + DEFAULT_PAYMENT_MODE);

        // Get all the subscribedOrderDeliveryList where paymentMode is less than or equal to SMALLER_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldNotBeFound("paymentMode.lessThanOrEqual=" + SMALLER_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByPaymentModeIsLessThanSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where paymentMode is less than DEFAULT_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldNotBeFound("paymentMode.lessThan=" + DEFAULT_PAYMENT_MODE);

        // Get all the subscribedOrderDeliveryList where paymentMode is less than UPDATED_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldBeFound("paymentMode.lessThan=" + UPDATED_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByPaymentModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where paymentMode is greater than DEFAULT_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldNotBeFound("paymentMode.greaterThan=" + DEFAULT_PAYMENT_MODE);

        // Get all the subscribedOrderDeliveryList where paymentMode is greater than SMALLER_PAYMENT_MODE
        defaultSubscribedOrderDeliveryShouldBeFound("paymentMode.greaterThan=" + SMALLER_PAYMENT_MODE);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where createdAt equals to DEFAULT_CREATED_AT
        defaultSubscribedOrderDeliveryShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the subscribedOrderDeliveryList where createdAt equals to UPDATED_CREATED_AT
        defaultSubscribedOrderDeliveryShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSubscribedOrderDeliveryShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the subscribedOrderDeliveryList where createdAt equals to UPDATED_CREATED_AT
        defaultSubscribedOrderDeliveryShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where createdAt is not null
        defaultSubscribedOrderDeliveryShouldBeFound("createdAt.specified=true");

        // Get all the subscribedOrderDeliveryList where createdAt is null
        defaultSubscribedOrderDeliveryShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSubscribedOrderDeliveryShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the subscribedOrderDeliveryList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSubscribedOrderDeliveryShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSubscribedOrderDeliveryShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the subscribedOrderDeliveryList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSubscribedOrderDeliveryShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        // Get all the subscribedOrderDeliveryList where updatedAt is not null
        defaultSubscribedOrderDeliveryShouldBeFound("updatedAt.specified=true");

        // Get all the subscribedOrderDeliveryList where updatedAt is null
        defaultSubscribedOrderDeliveryShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByOrderIsEqualToSomething() throws Exception {
        Orders order;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);
            order = OrdersResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Orders.class).get(0);
        }
        em.persist(order);
        em.flush();
        subscribedOrderDelivery.setOrder(order);
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);
        Long orderId = order.getId();
        // Get all the subscribedOrderDeliveryList where order equals to orderId
        defaultSubscribedOrderDeliveryShouldBeFound("orderId.equals=" + orderId);

        // Get all the subscribedOrderDeliveryList where order equals to (orderId + 1)
        defaultSubscribedOrderDeliveryShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllSubscribedOrderDeliveriesByEntryUserIsEqualToSomething() throws Exception {
        Users entryUser;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);
            entryUser = UsersResourceIT.createEntity(em);
        } else {
            entryUser = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(entryUser);
        em.flush();
        subscribedOrderDelivery.setEntryUser(entryUser);
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);
        Long entryUserId = entryUser.getId();
        // Get all the subscribedOrderDeliveryList where entryUser equals to entryUserId
        defaultSubscribedOrderDeliveryShouldBeFound("entryUserId.equals=" + entryUserId);

        // Get all the subscribedOrderDeliveryList where entryUser equals to (entryUserId + 1)
        defaultSubscribedOrderDeliveryShouldNotBeFound("entryUserId.equals=" + (entryUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubscribedOrderDeliveryShouldBeFound(String filter) throws Exception {
        restSubscribedOrderDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscribedOrderDelivery.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentMode").value(hasItem(DEFAULT_PAYMENT_MODE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restSubscribedOrderDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubscribedOrderDeliveryShouldNotBeFound(String filter) throws Exception {
        restSubscribedOrderDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubscribedOrderDeliveryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubscribedOrderDelivery() throws Exception {
        // Get the subscribedOrderDelivery
        restSubscribedOrderDeliveryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubscribedOrderDelivery() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();

        // Update the subscribedOrderDelivery
        SubscribedOrderDelivery updatedSubscribedOrderDelivery = subscribedOrderDeliveryRepository
            .findById(subscribedOrderDelivery.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSubscribedOrderDelivery are not directly saved in db
        em.detach(updatedSubscribedOrderDelivery);
        updatedSubscribedOrderDelivery
            .date(UPDATED_DATE)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(updatedSubscribedOrderDelivery);

        restSubscribedOrderDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscribedOrderDeliveryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
        SubscribedOrderDelivery testSubscribedOrderDelivery = subscribedOrderDeliveryList.get(subscribedOrderDeliveryList.size() - 1);
        assertThat(testSubscribedOrderDelivery.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSubscribedOrderDelivery.getPaymentMode()).isEqualTo(UPDATED_PAYMENT_MODE);
        assertThat(testSubscribedOrderDelivery.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubscribedOrderDelivery.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSubscribedOrderDelivery() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();
        subscribedOrderDelivery.setId(longCount.incrementAndGet());

        // Create the SubscribedOrderDelivery
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscribedOrderDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscribedOrderDeliveryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscribedOrderDelivery() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();
        subscribedOrderDelivery.setId(longCount.incrementAndGet());

        // Create the SubscribedOrderDelivery
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribedOrderDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscribedOrderDelivery() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();
        subscribedOrderDelivery.setId(longCount.incrementAndGet());

        // Create the SubscribedOrderDelivery
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribedOrderDeliveryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscribedOrderDeliveryWithPatch() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();

        // Update the subscribedOrderDelivery using partial update
        SubscribedOrderDelivery partialUpdatedSubscribedOrderDelivery = new SubscribedOrderDelivery();
        partialUpdatedSubscribedOrderDelivery.setId(subscribedOrderDelivery.getId());

        partialUpdatedSubscribedOrderDelivery.date(UPDATED_DATE);

        restSubscribedOrderDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscribedOrderDelivery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscribedOrderDelivery))
            )
            .andExpect(status().isOk());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
        SubscribedOrderDelivery testSubscribedOrderDelivery = subscribedOrderDeliveryList.get(subscribedOrderDeliveryList.size() - 1);
        assertThat(testSubscribedOrderDelivery.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSubscribedOrderDelivery.getPaymentMode()).isEqualTo(DEFAULT_PAYMENT_MODE);
        assertThat(testSubscribedOrderDelivery.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubscribedOrderDelivery.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSubscribedOrderDeliveryWithPatch() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();

        // Update the subscribedOrderDelivery using partial update
        SubscribedOrderDelivery partialUpdatedSubscribedOrderDelivery = new SubscribedOrderDelivery();
        partialUpdatedSubscribedOrderDelivery.setId(subscribedOrderDelivery.getId());

        partialUpdatedSubscribedOrderDelivery
            .date(UPDATED_DATE)
            .paymentMode(UPDATED_PAYMENT_MODE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSubscribedOrderDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscribedOrderDelivery.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscribedOrderDelivery))
            )
            .andExpect(status().isOk());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
        SubscribedOrderDelivery testSubscribedOrderDelivery = subscribedOrderDeliveryList.get(subscribedOrderDeliveryList.size() - 1);
        assertThat(testSubscribedOrderDelivery.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSubscribedOrderDelivery.getPaymentMode()).isEqualTo(UPDATED_PAYMENT_MODE);
        assertThat(testSubscribedOrderDelivery.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubscribedOrderDelivery.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSubscribedOrderDelivery() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();
        subscribedOrderDelivery.setId(longCount.incrementAndGet());

        // Create the SubscribedOrderDelivery
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscribedOrderDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscribedOrderDeliveryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscribedOrderDelivery() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();
        subscribedOrderDelivery.setId(longCount.incrementAndGet());

        // Create the SubscribedOrderDelivery
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribedOrderDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscribedOrderDelivery() throws Exception {
        int databaseSizeBeforeUpdate = subscribedOrderDeliveryRepository.findAll().size();
        subscribedOrderDelivery.setId(longCount.incrementAndGet());

        // Create the SubscribedOrderDelivery
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO = subscribedOrderDeliveryMapper.toDto(subscribedOrderDelivery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscribedOrderDeliveryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscribedOrderDeliveryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubscribedOrderDelivery in the database
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscribedOrderDelivery() throws Exception {
        // Initialize the database
        subscribedOrderDeliveryRepository.saveAndFlush(subscribedOrderDelivery);

        int databaseSizeBeforeDelete = subscribedOrderDeliveryRepository.findAll().size();

        // Delete the subscribedOrderDelivery
        restSubscribedOrderDeliveryMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscribedOrderDelivery.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubscribedOrderDelivery> subscribedOrderDeliveryList = subscribedOrderDeliveryRepository.findAll();
        assertThat(subscribedOrderDeliveryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
