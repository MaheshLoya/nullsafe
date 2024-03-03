package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.OrderUserAssign;
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.OrderUserAssignRepository;
import com.nullsafe.daily.service.OrderUserAssignService;
import com.nullsafe.daily.service.dto.OrderUserAssignDTO;
import com.nullsafe.daily.service.mapper.OrderUserAssignMapper;
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
 * Integration tests for the {@link OrderUserAssignResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderUserAssignResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/order-user-assigns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderUserAssignRepository orderUserAssignRepository;

    @Mock
    private OrderUserAssignRepository orderUserAssignRepositoryMock;

    @Autowired
    private OrderUserAssignMapper orderUserAssignMapper;

    @Mock
    private OrderUserAssignService orderUserAssignServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderUserAssignMockMvc;

    private OrderUserAssign orderUserAssign;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderUserAssign createEntity(EntityManager em) {
        OrderUserAssign orderUserAssign = new OrderUserAssign().createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            orders = OrdersResourceIT.createEntity(em);
            em.persist(orders);
            em.flush();
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        orderUserAssign.setOrder(orders);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        orderUserAssign.setUser(users);
        return orderUserAssign;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderUserAssign createUpdatedEntity(EntityManager em) {
        OrderUserAssign orderUserAssign = new OrderUserAssign().createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            orders = OrdersResourceIT.createUpdatedEntity(em);
            em.persist(orders);
            em.flush();
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        orderUserAssign.setOrder(orders);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createUpdatedEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        orderUserAssign.setUser(users);
        return orderUserAssign;
    }

    @BeforeEach
    public void initTest() {
        orderUserAssign = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderUserAssign() throws Exception {
        int databaseSizeBeforeCreate = orderUserAssignRepository.findAll().size();
        // Create the OrderUserAssign
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(orderUserAssign);
        restOrderUserAssignMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeCreate + 1);
        OrderUserAssign testOrderUserAssign = orderUserAssignList.get(orderUserAssignList.size() - 1);
        assertThat(testOrderUserAssign.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOrderUserAssign.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createOrderUserAssignWithExistingId() throws Exception {
        // Create the OrderUserAssign with an existing ID
        orderUserAssign.setId(1L);
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(orderUserAssign);

        int databaseSizeBeforeCreate = orderUserAssignRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderUserAssignMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderUserAssigns() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        // Get all the orderUserAssignList
        restOrderUserAssignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderUserAssign.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderUserAssignsWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderUserAssignServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderUserAssignMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderUserAssignServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderUserAssignsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderUserAssignServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderUserAssignMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(orderUserAssignRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrderUserAssign() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        // Get the orderUserAssign
        restOrderUserAssignMockMvc
            .perform(get(ENTITY_API_URL_ID, orderUserAssign.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderUserAssign.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getOrderUserAssignsByIdFiltering() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        Long id = orderUserAssign.getId();

        defaultOrderUserAssignShouldBeFound("id.equals=" + id);
        defaultOrderUserAssignShouldNotBeFound("id.notEquals=" + id);

        defaultOrderUserAssignShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderUserAssignShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderUserAssignShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderUserAssignShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderUserAssignsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        // Get all the orderUserAssignList where createdAt equals to DEFAULT_CREATED_AT
        defaultOrderUserAssignShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the orderUserAssignList where createdAt equals to UPDATED_CREATED_AT
        defaultOrderUserAssignShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrderUserAssignsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        // Get all the orderUserAssignList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultOrderUserAssignShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the orderUserAssignList where createdAt equals to UPDATED_CREATED_AT
        defaultOrderUserAssignShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllOrderUserAssignsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        // Get all the orderUserAssignList where createdAt is not null
        defaultOrderUserAssignShouldBeFound("createdAt.specified=true");

        // Get all the orderUserAssignList where createdAt is null
        defaultOrderUserAssignShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderUserAssignsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        // Get all the orderUserAssignList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultOrderUserAssignShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the orderUserAssignList where updatedAt equals to UPDATED_UPDATED_AT
        defaultOrderUserAssignShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrderUserAssignsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        // Get all the orderUserAssignList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultOrderUserAssignShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the orderUserAssignList where updatedAt equals to UPDATED_UPDATED_AT
        defaultOrderUserAssignShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllOrderUserAssignsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        // Get all the orderUserAssignList where updatedAt is not null
        defaultOrderUserAssignShouldBeFound("updatedAt.specified=true");

        // Get all the orderUserAssignList where updatedAt is null
        defaultOrderUserAssignShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderUserAssignsByOrderIsEqualToSomething() throws Exception {
        Orders order;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            orderUserAssignRepository.saveAndFlush(orderUserAssign);
            order = OrdersResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Orders.class).get(0);
        }
        em.persist(order);
        em.flush();
        orderUserAssign.setOrder(order);
        orderUserAssignRepository.saveAndFlush(orderUserAssign);
        Long orderId = order.getId();
        // Get all the orderUserAssignList where order equals to orderId
        defaultOrderUserAssignShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderUserAssignList where order equals to (orderId + 1)
        defaultOrderUserAssignShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderUserAssignsByUserIsEqualToSomething() throws Exception {
        Users user;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            orderUserAssignRepository.saveAndFlush(orderUserAssign);
            user = UsersResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(user);
        em.flush();
        orderUserAssign.setUser(user);
        orderUserAssignRepository.saveAndFlush(orderUserAssign);
        Long userId = user.getId();
        // Get all the orderUserAssignList where user equals to userId
        defaultOrderUserAssignShouldBeFound("userId.equals=" + userId);

        // Get all the orderUserAssignList where user equals to (userId + 1)
        defaultOrderUserAssignShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderUserAssignShouldBeFound(String filter) throws Exception {
        restOrderUserAssignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderUserAssign.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restOrderUserAssignMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderUserAssignShouldNotBeFound(String filter) throws Exception {
        restOrderUserAssignMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderUserAssignMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderUserAssign() throws Exception {
        // Get the orderUserAssign
        restOrderUserAssignMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderUserAssign() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();

        // Update the orderUserAssign
        OrderUserAssign updatedOrderUserAssign = orderUserAssignRepository.findById(orderUserAssign.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderUserAssign are not directly saved in db
        em.detach(updatedOrderUserAssign);
        updatedOrderUserAssign.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(updatedOrderUserAssign);

        restOrderUserAssignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderUserAssignDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
        OrderUserAssign testOrderUserAssign = orderUserAssignList.get(orderUserAssignList.size() - 1);
        assertThat(testOrderUserAssign.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrderUserAssign.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingOrderUserAssign() throws Exception {
        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();
        orderUserAssign.setId(longCount.incrementAndGet());

        // Create the OrderUserAssign
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(orderUserAssign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderUserAssignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderUserAssignDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderUserAssign() throws Exception {
        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();
        orderUserAssign.setId(longCount.incrementAndGet());

        // Create the OrderUserAssign
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(orderUserAssign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderUserAssignMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderUserAssign() throws Exception {
        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();
        orderUserAssign.setId(longCount.incrementAndGet());

        // Create the OrderUserAssign
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(orderUserAssign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderUserAssignMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderUserAssignWithPatch() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();

        // Update the orderUserAssign using partial update
        OrderUserAssign partialUpdatedOrderUserAssign = new OrderUserAssign();
        partialUpdatedOrderUserAssign.setId(orderUserAssign.getId());

        partialUpdatedOrderUserAssign.createdAt(UPDATED_CREATED_AT);

        restOrderUserAssignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderUserAssign.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderUserAssign))
            )
            .andExpect(status().isOk());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
        OrderUserAssign testOrderUserAssign = orderUserAssignList.get(orderUserAssignList.size() - 1);
        assertThat(testOrderUserAssign.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrderUserAssign.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateOrderUserAssignWithPatch() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();

        // Update the orderUserAssign using partial update
        OrderUserAssign partialUpdatedOrderUserAssign = new OrderUserAssign();
        partialUpdatedOrderUserAssign.setId(orderUserAssign.getId());

        partialUpdatedOrderUserAssign.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restOrderUserAssignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderUserAssign.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderUserAssign))
            )
            .andExpect(status().isOk());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
        OrderUserAssign testOrderUserAssign = orderUserAssignList.get(orderUserAssignList.size() - 1);
        assertThat(testOrderUserAssign.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOrderUserAssign.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingOrderUserAssign() throws Exception {
        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();
        orderUserAssign.setId(longCount.incrementAndGet());

        // Create the OrderUserAssign
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(orderUserAssign);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderUserAssignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderUserAssignDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderUserAssign() throws Exception {
        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();
        orderUserAssign.setId(longCount.incrementAndGet());

        // Create the OrderUserAssign
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(orderUserAssign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderUserAssignMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderUserAssign() throws Exception {
        int databaseSizeBeforeUpdate = orderUserAssignRepository.findAll().size();
        orderUserAssign.setId(longCount.incrementAndGet());

        // Create the OrderUserAssign
        OrderUserAssignDTO orderUserAssignDTO = orderUserAssignMapper.toDto(orderUserAssign);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderUserAssignMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderUserAssignDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderUserAssign in the database
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderUserAssign() throws Exception {
        // Initialize the database
        orderUserAssignRepository.saveAndFlush(orderUserAssign);

        int databaseSizeBeforeDelete = orderUserAssignRepository.findAll().size();

        // Delete the orderUserAssign
        restOrderUserAssignMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderUserAssign.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderUserAssign> orderUserAssignList = orderUserAssignRepository.findAll();
        assertThat(orderUserAssignList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
