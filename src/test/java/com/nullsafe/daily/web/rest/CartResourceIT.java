package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Cart;
import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.CartRepository;
import com.nullsafe.daily.service.CartService;
import com.nullsafe.daily.service.dto.CartDTO;
import com.nullsafe.daily.service.mapper.CartMapper;
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
 * Integration tests for the {@link CartResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CartResourceIT {

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;
    private static final Integer SMALLER_QTY = 1 - 1;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;
    private static final Double SMALLER_TOTAL_PRICE = 1D - 1D;

    private static final Double DEFAULT_MRP = 1D;
    private static final Double UPDATED_MRP = 2D;
    private static final Double SMALLER_MRP = 1D - 1D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;
    private static final Double SMALLER_TAX = 1D - 1D;

    private static final String DEFAULT_QTY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QTY_TEXT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CartRepository cartRepository;

    @Mock
    private CartRepository cartRepositoryMock;

    @Autowired
    private CartMapper cartMapper;

    @Mock
    private CartService cartServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartMockMvc;

    private Cart cart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createEntity(EntityManager em) {
        Cart cart = new Cart()
            .qty(DEFAULT_QTY)
            .price(DEFAULT_PRICE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .mrp(DEFAULT_MRP)
            .tax(DEFAULT_TAX)
            .qtyText(DEFAULT_QTY_TEXT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        cart.setProduct(product);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        cart.setUser(users);
        return cart;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createUpdatedEntity(EntityManager em) {
        Cart cart = new Cart()
            .qty(UPDATED_QTY)
            .price(UPDATED_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qtyText(UPDATED_QTY_TEXT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        cart.setProduct(product);
        // Add required entity
        Users users;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            users = UsersResourceIT.createUpdatedEntity(em);
            em.persist(users);
            em.flush();
        } else {
            users = TestUtil.findAll(em, Users.class).get(0);
        }
        cart.setUser(users);
        return cart;
    }

    @BeforeEach
    public void initTest() {
        cart = createEntity(em);
    }

    @Test
    @Transactional
    void createCart() throws Exception {
        int databaseSizeBeforeCreate = cartRepository.findAll().size();
        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);
        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isCreated());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate + 1);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testCart.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCart.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testCart.getMrp()).isEqualTo(DEFAULT_MRP);
        assertThat(testCart.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testCart.getQtyText()).isEqualTo(DEFAULT_QTY_TEXT);
        assertThat(testCart.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCart.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createCartWithExistingId() throws Exception {
        // Create the Cart with an existing ID
        cart.setId(1L);
        CartDTO cartDTO = cartMapper.toDto(cart);

        int databaseSizeBeforeCreate = cartRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRepository.findAll().size();
        // set the field null
        cart.setQty(null);

        // Create the Cart, which fails.
        CartDTO cartDTO = cartMapper.toDto(cart);

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isBadRequest());

        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRepository.findAll().size();
        // set the field null
        cart.setPrice(null);

        // Create the Cart, which fails.
        CartDTO cartDTO = cartMapper.toDto(cart);

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isBadRequest());

        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRepository.findAll().size();
        // set the field null
        cart.setTotalPrice(null);

        // Create the Cart, which fails.
        CartDTO cartDTO = cartMapper.toDto(cart);

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isBadRequest());

        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMrpIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRepository.findAll().size();
        // set the field null
        cart.setMrp(null);

        // Create the Cart, which fails.
        CartDTO cartDTO = cartMapper.toDto(cart);

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isBadRequest());

        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRepository.findAll().size();
        // set the field null
        cart.setTax(null);

        // Create the Cart, which fails.
        CartDTO cartDTO = cartMapper.toDto(cart);

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isBadRequest());

        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQtyTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRepository.findAll().size();
        // set the field null
        cart.setQtyText(null);

        // Create the Cart, which fails.
        CartDTO cartDTO = cartMapper.toDto(cart);

        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isBadRequest());

        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarts() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qtyText").value(hasItem(DEFAULT_QTY_TEXT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCartsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cartServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCartMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cartServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCartsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cartServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCartMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cartRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get the cart
        restCartMockMvc
            .perform(get(ENTITY_API_URL_ID, cart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cart.getId().intValue()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.mrp").value(DEFAULT_MRP.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.qtyText").value(DEFAULT_QTY_TEXT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getCartsByIdFiltering() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        Long id = cart.getId();

        defaultCartShouldBeFound("id.equals=" + id);
        defaultCartShouldNotBeFound("id.notEquals=" + id);

        defaultCartShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCartShouldNotBeFound("id.greaterThan=" + id);

        defaultCartShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCartShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCartsByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qty equals to DEFAULT_QTY
        defaultCartShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the cartList where qty equals to UPDATED_QTY
        defaultCartShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllCartsByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultCartShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the cartList where qty equals to UPDATED_QTY
        defaultCartShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllCartsByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qty is not null
        defaultCartShouldBeFound("qty.specified=true");

        // Get all the cartList where qty is null
        defaultCartShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qty is greater than or equal to DEFAULT_QTY
        defaultCartShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the cartList where qty is greater than or equal to UPDATED_QTY
        defaultCartShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllCartsByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qty is less than or equal to DEFAULT_QTY
        defaultCartShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the cartList where qty is less than or equal to SMALLER_QTY
        defaultCartShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllCartsByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qty is less than DEFAULT_QTY
        defaultCartShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the cartList where qty is less than UPDATED_QTY
        defaultCartShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllCartsByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qty is greater than DEFAULT_QTY
        defaultCartShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the cartList where qty is greater than SMALLER_QTY
        defaultCartShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllCartsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where price equals to DEFAULT_PRICE
        defaultCartShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the cartList where price equals to UPDATED_PRICE
        defaultCartShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultCartShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the cartList where price equals to UPDATED_PRICE
        defaultCartShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where price is not null
        defaultCartShouldBeFound("price.specified=true");

        // Get all the cartList where price is null
        defaultCartShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where price is greater than or equal to DEFAULT_PRICE
        defaultCartShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the cartList where price is greater than or equal to UPDATED_PRICE
        defaultCartShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where price is less than or equal to DEFAULT_PRICE
        defaultCartShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the cartList where price is less than or equal to SMALLER_PRICE
        defaultCartShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where price is less than DEFAULT_PRICE
        defaultCartShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the cartList where price is less than UPDATED_PRICE
        defaultCartShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where price is greater than DEFAULT_PRICE
        defaultCartShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the cartList where price is greater than SMALLER_PRICE
        defaultCartShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the cartList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is not null
        defaultCartShouldBeFound("totalPrice.specified=true");

        // Get all the cartList where totalPrice is null
        defaultCartShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByMrpIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where mrp equals to DEFAULT_MRP
        defaultCartShouldBeFound("mrp.equals=" + DEFAULT_MRP);

        // Get all the cartList where mrp equals to UPDATED_MRP
        defaultCartShouldNotBeFound("mrp.equals=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllCartsByMrpIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where mrp in DEFAULT_MRP or UPDATED_MRP
        defaultCartShouldBeFound("mrp.in=" + DEFAULT_MRP + "," + UPDATED_MRP);

        // Get all the cartList where mrp equals to UPDATED_MRP
        defaultCartShouldNotBeFound("mrp.in=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllCartsByMrpIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where mrp is not null
        defaultCartShouldBeFound("mrp.specified=true");

        // Get all the cartList where mrp is null
        defaultCartShouldNotBeFound("mrp.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByMrpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where mrp is greater than or equal to DEFAULT_MRP
        defaultCartShouldBeFound("mrp.greaterThanOrEqual=" + DEFAULT_MRP);

        // Get all the cartList where mrp is greater than or equal to UPDATED_MRP
        defaultCartShouldNotBeFound("mrp.greaterThanOrEqual=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllCartsByMrpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where mrp is less than or equal to DEFAULT_MRP
        defaultCartShouldBeFound("mrp.lessThanOrEqual=" + DEFAULT_MRP);

        // Get all the cartList where mrp is less than or equal to SMALLER_MRP
        defaultCartShouldNotBeFound("mrp.lessThanOrEqual=" + SMALLER_MRP);
    }

    @Test
    @Transactional
    void getAllCartsByMrpIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where mrp is less than DEFAULT_MRP
        defaultCartShouldNotBeFound("mrp.lessThan=" + DEFAULT_MRP);

        // Get all the cartList where mrp is less than UPDATED_MRP
        defaultCartShouldBeFound("mrp.lessThan=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllCartsByMrpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where mrp is greater than DEFAULT_MRP
        defaultCartShouldNotBeFound("mrp.greaterThan=" + DEFAULT_MRP);

        // Get all the cartList where mrp is greater than SMALLER_MRP
        defaultCartShouldBeFound("mrp.greaterThan=" + SMALLER_MRP);
    }

    @Test
    @Transactional
    void getAllCartsByTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where tax equals to DEFAULT_TAX
        defaultCartShouldBeFound("tax.equals=" + DEFAULT_TAX);

        // Get all the cartList where tax equals to UPDATED_TAX
        defaultCartShouldNotBeFound("tax.equals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllCartsByTaxIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where tax in DEFAULT_TAX or UPDATED_TAX
        defaultCartShouldBeFound("tax.in=" + DEFAULT_TAX + "," + UPDATED_TAX);

        // Get all the cartList where tax equals to UPDATED_TAX
        defaultCartShouldNotBeFound("tax.in=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllCartsByTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where tax is not null
        defaultCartShouldBeFound("tax.specified=true");

        // Get all the cartList where tax is null
        defaultCartShouldNotBeFound("tax.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where tax is greater than or equal to DEFAULT_TAX
        defaultCartShouldBeFound("tax.greaterThanOrEqual=" + DEFAULT_TAX);

        // Get all the cartList where tax is greater than or equal to UPDATED_TAX
        defaultCartShouldNotBeFound("tax.greaterThanOrEqual=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllCartsByTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where tax is less than or equal to DEFAULT_TAX
        defaultCartShouldBeFound("tax.lessThanOrEqual=" + DEFAULT_TAX);

        // Get all the cartList where tax is less than or equal to SMALLER_TAX
        defaultCartShouldNotBeFound("tax.lessThanOrEqual=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllCartsByTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where tax is less than DEFAULT_TAX
        defaultCartShouldNotBeFound("tax.lessThan=" + DEFAULT_TAX);

        // Get all the cartList where tax is less than UPDATED_TAX
        defaultCartShouldBeFound("tax.lessThan=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllCartsByTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where tax is greater than DEFAULT_TAX
        defaultCartShouldNotBeFound("tax.greaterThan=" + DEFAULT_TAX);

        // Get all the cartList where tax is greater than SMALLER_TAX
        defaultCartShouldBeFound("tax.greaterThan=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllCartsByQtyTextIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qtyText equals to DEFAULT_QTY_TEXT
        defaultCartShouldBeFound("qtyText.equals=" + DEFAULT_QTY_TEXT);

        // Get all the cartList where qtyText equals to UPDATED_QTY_TEXT
        defaultCartShouldNotBeFound("qtyText.equals=" + UPDATED_QTY_TEXT);
    }

    @Test
    @Transactional
    void getAllCartsByQtyTextIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qtyText in DEFAULT_QTY_TEXT or UPDATED_QTY_TEXT
        defaultCartShouldBeFound("qtyText.in=" + DEFAULT_QTY_TEXT + "," + UPDATED_QTY_TEXT);

        // Get all the cartList where qtyText equals to UPDATED_QTY_TEXT
        defaultCartShouldNotBeFound("qtyText.in=" + UPDATED_QTY_TEXT);
    }

    @Test
    @Transactional
    void getAllCartsByQtyTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qtyText is not null
        defaultCartShouldBeFound("qtyText.specified=true");

        // Get all the cartList where qtyText is null
        defaultCartShouldNotBeFound("qtyText.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByQtyTextContainsSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qtyText contains DEFAULT_QTY_TEXT
        defaultCartShouldBeFound("qtyText.contains=" + DEFAULT_QTY_TEXT);

        // Get all the cartList where qtyText contains UPDATED_QTY_TEXT
        defaultCartShouldNotBeFound("qtyText.contains=" + UPDATED_QTY_TEXT);
    }

    @Test
    @Transactional
    void getAllCartsByQtyTextNotContainsSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where qtyText does not contain DEFAULT_QTY_TEXT
        defaultCartShouldNotBeFound("qtyText.doesNotContain=" + DEFAULT_QTY_TEXT);

        // Get all the cartList where qtyText does not contain UPDATED_QTY_TEXT
        defaultCartShouldBeFound("qtyText.doesNotContain=" + UPDATED_QTY_TEXT);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdAt equals to DEFAULT_CREATED_AT
        defaultCartShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the cartList where createdAt equals to UPDATED_CREATED_AT
        defaultCartShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCartShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the cartList where createdAt equals to UPDATED_CREATED_AT
        defaultCartShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCartsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where createdAt is not null
        defaultCartShouldBeFound("createdAt.specified=true");

        // Get all the cartList where createdAt is null
        defaultCartShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultCartShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the cartList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCartShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCartsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultCartShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the cartList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCartShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCartsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where updatedAt is not null
        defaultCartShouldBeFound("updatedAt.specified=true");

        // Get all the cartList where updatedAt is null
        defaultCartShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            cartRepository.saveAndFlush(cart);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        cart.setProduct(product);
        cartRepository.saveAndFlush(cart);
        Long productId = product.getId();
        // Get all the cartList where product equals to productId
        defaultCartShouldBeFound("productId.equals=" + productId);

        // Get all the cartList where product equals to (productId + 1)
        defaultCartShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllCartsByUserIsEqualToSomething() throws Exception {
        Users user;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            cartRepository.saveAndFlush(cart);
            user = UsersResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(user);
        em.flush();
        cart.setUser(user);
        cartRepository.saveAndFlush(cart);
        Long userId = user.getId();
        // Get all the cartList where user equals to userId
        defaultCartShouldBeFound("userId.equals=" + userId);

        // Get all the cartList where user equals to (userId + 1)
        defaultCartShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCartShouldBeFound(String filter) throws Exception {
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].qtyText").value(hasItem(DEFAULT_QTY_TEXT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCartShouldNotBeFound(String filter) throws Exception {
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCart() throws Exception {
        // Get the cart
        restCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart
        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCart are not directly saved in db
        em.detach(updatedCart);
        updatedCart
            .qty(UPDATED_QTY)
            .price(UPDATED_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qtyText(UPDATED_QTY_TEXT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CartDTO cartDTO = cartMapper.toDto(updatedCart);

        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testCart.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCart.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCart.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testCart.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testCart.getQtyText()).isEqualTo(UPDATED_QTY_TEXT);
        assertThat(testCart.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCart.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartWithPatch() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        partialUpdatedCart.price(UPDATED_PRICE).totalPrice(UPDATED_TOTAL_PRICE).tax(UPDATED_TAX).qtyText(UPDATED_QTY_TEXT);

        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testCart.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCart.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCart.getMrp()).isEqualTo(DEFAULT_MRP);
        assertThat(testCart.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testCart.getQtyText()).isEqualTo(UPDATED_QTY_TEXT);
        assertThat(testCart.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCart.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateCartWithPatch() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        partialUpdatedCart
            .qty(UPDATED_QTY)
            .price(UPDATED_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .mrp(UPDATED_MRP)
            .tax(UPDATED_TAX)
            .qtyText(UPDATED_QTY_TEXT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testCart.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCart.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testCart.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testCart.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testCart.getQtyText()).isEqualTo(UPDATED_QTY_TEXT);
        assertThat(testCart.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCart.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cartDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeDelete = cartRepository.findAll().size();

        // Delete the cart
        restCartMockMvc
            .perform(delete(ENTITY_API_URL_ID, cart.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
