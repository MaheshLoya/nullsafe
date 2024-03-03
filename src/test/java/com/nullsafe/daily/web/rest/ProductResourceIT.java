package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Cart;
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.domain.SubCat;
import com.nullsafe.daily.domain.SubscribedOrders;
import com.nullsafe.daily.repository.ProductRepository;
import com.nullsafe.daily.service.dto.ProductDTO;
import com.nullsafe.daily.service.mapper.ProductMapper;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_QTY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_QTY_TEXT = "BBBBBBBBBB";

    private static final Long DEFAULT_STOCK_QTY = 1L;
    private static final Long UPDATED_STOCK_QTY = 2L;
    private static final Long SMALLER_STOCK_QTY = 1L - 1L;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;
    private static final Double SMALLER_TAX = 1D - 1D;

    private static final Double DEFAULT_MRP = 1D;
    private static final Double UPDATED_MRP = 2D;
    private static final Double SMALLER_MRP = 1D - 1D;

    private static final String DEFAULT_OFFER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_OFFER_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DISCLAIMER = "AAAAAAAAAA";
    private static final String UPDATED_DISCLAIMER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SUBSCRIPTION = false;
    private static final Boolean UPDATED_SUBSCRIPTION = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .title(DEFAULT_TITLE)
            .qtyText(DEFAULT_QTY_TEXT)
            .stockQty(DEFAULT_STOCK_QTY)
            .price(DEFAULT_PRICE)
            .tax(DEFAULT_TAX)
            .mrp(DEFAULT_MRP)
            .offerText(DEFAULT_OFFER_TEXT)
            .description(DEFAULT_DESCRIPTION)
            .disclaimer(DEFAULT_DISCLAIMER)
            .subscription(DEFAULT_SUBSCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        SubCat subCat;
        if (TestUtil.findAll(em, SubCat.class).isEmpty()) {
            subCat = SubCatResourceIT.createEntity(em);
            em.persist(subCat);
            em.flush();
        } else {
            subCat = TestUtil.findAll(em, SubCat.class).get(0);
        }
        product.setSubCat(subCat);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .title(UPDATED_TITLE)
            .qtyText(UPDATED_QTY_TEXT)
            .stockQty(UPDATED_STOCK_QTY)
            .price(UPDATED_PRICE)
            .tax(UPDATED_TAX)
            .mrp(UPDATED_MRP)
            .offerText(UPDATED_OFFER_TEXT)
            .description(UPDATED_DESCRIPTION)
            .disclaimer(UPDATED_DISCLAIMER)
            .subscription(UPDATED_SUBSCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        SubCat subCat;
        if (TestUtil.findAll(em, SubCat.class).isEmpty()) {
            subCat = SubCatResourceIT.createUpdatedEntity(em);
            em.persist(subCat);
            em.flush();
        } else {
            subCat = TestUtil.findAll(em, SubCat.class).get(0);
        }
        product.setSubCat(subCat);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProduct.getQtyText()).isEqualTo(DEFAULT_QTY_TEXT);
        assertThat(testProduct.getStockQty()).isEqualTo(DEFAULT_STOCK_QTY);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testProduct.getMrp()).isEqualTo(DEFAULT_MRP);
        assertThat(testProduct.getOfferText()).isEqualTo(DEFAULT_OFFER_TEXT);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getDisclaimer()).isEqualTo(DEFAULT_DISCLAIMER);
        assertThat(testProduct.getSubscription()).isEqualTo(DEFAULT_SUBSCRIPTION);
        assertThat(testProduct.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProduct.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testProduct.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setTitle(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQtyTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setQtyText(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setTax(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMrpIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setMrp(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setSubscription(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setIsActive(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].qtyText").value(hasItem(DEFAULT_QTY_TEXT)))
            .andExpect(jsonPath("$.[*].stockQty").value(hasItem(DEFAULT_STOCK_QTY.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].offerText").value(hasItem(DEFAULT_OFFER_TEXT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].disclaimer").value(hasItem(DEFAULT_DISCLAIMER)))
            .andExpect(jsonPath("$.[*].subscription").value(hasItem(DEFAULT_SUBSCRIPTION.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.qtyText").value(DEFAULT_QTY_TEXT))
            .andExpect(jsonPath("$.stockQty").value(DEFAULT_STOCK_QTY.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.mrp").value(DEFAULT_MRP.doubleValue()))
            .andExpect(jsonPath("$.offerText").value(DEFAULT_OFFER_TEXT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.disclaimer").value(DEFAULT_DISCLAIMER))
            .andExpect(jsonPath("$.subscription").value(DEFAULT_SUBSCRIPTION.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where title equals to DEFAULT_TITLE
        defaultProductShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the productList where title equals to UPDATED_TITLE
        defaultProductShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultProductShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the productList where title equals to UPDATED_TITLE
        defaultProductShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where title is not null
        defaultProductShouldBeFound("title.specified=true");

        // Get all the productList where title is null
        defaultProductShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByTitleContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where title contains DEFAULT_TITLE
        defaultProductShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the productList where title contains UPDATED_TITLE
        defaultProductShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where title does not contain DEFAULT_TITLE
        defaultProductShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the productList where title does not contain UPDATED_TITLE
        defaultProductShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllProductsByQtyTextIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where qtyText equals to DEFAULT_QTY_TEXT
        defaultProductShouldBeFound("qtyText.equals=" + DEFAULT_QTY_TEXT);

        // Get all the productList where qtyText equals to UPDATED_QTY_TEXT
        defaultProductShouldNotBeFound("qtyText.equals=" + UPDATED_QTY_TEXT);
    }

    @Test
    @Transactional
    void getAllProductsByQtyTextIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where qtyText in DEFAULT_QTY_TEXT or UPDATED_QTY_TEXT
        defaultProductShouldBeFound("qtyText.in=" + DEFAULT_QTY_TEXT + "," + UPDATED_QTY_TEXT);

        // Get all the productList where qtyText equals to UPDATED_QTY_TEXT
        defaultProductShouldNotBeFound("qtyText.in=" + UPDATED_QTY_TEXT);
    }

    @Test
    @Transactional
    void getAllProductsByQtyTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where qtyText is not null
        defaultProductShouldBeFound("qtyText.specified=true");

        // Get all the productList where qtyText is null
        defaultProductShouldNotBeFound("qtyText.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByQtyTextContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where qtyText contains DEFAULT_QTY_TEXT
        defaultProductShouldBeFound("qtyText.contains=" + DEFAULT_QTY_TEXT);

        // Get all the productList where qtyText contains UPDATED_QTY_TEXT
        defaultProductShouldNotBeFound("qtyText.contains=" + UPDATED_QTY_TEXT);
    }

    @Test
    @Transactional
    void getAllProductsByQtyTextNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where qtyText does not contain DEFAULT_QTY_TEXT
        defaultProductShouldNotBeFound("qtyText.doesNotContain=" + DEFAULT_QTY_TEXT);

        // Get all the productList where qtyText does not contain UPDATED_QTY_TEXT
        defaultProductShouldBeFound("qtyText.doesNotContain=" + UPDATED_QTY_TEXT);
    }

    @Test
    @Transactional
    void getAllProductsByStockQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stockQty equals to DEFAULT_STOCK_QTY
        defaultProductShouldBeFound("stockQty.equals=" + DEFAULT_STOCK_QTY);

        // Get all the productList where stockQty equals to UPDATED_STOCK_QTY
        defaultProductShouldNotBeFound("stockQty.equals=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByStockQtyIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stockQty in DEFAULT_STOCK_QTY or UPDATED_STOCK_QTY
        defaultProductShouldBeFound("stockQty.in=" + DEFAULT_STOCK_QTY + "," + UPDATED_STOCK_QTY);

        // Get all the productList where stockQty equals to UPDATED_STOCK_QTY
        defaultProductShouldNotBeFound("stockQty.in=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByStockQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stockQty is not null
        defaultProductShouldBeFound("stockQty.specified=true");

        // Get all the productList where stockQty is null
        defaultProductShouldNotBeFound("stockQty.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByStockQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stockQty is greater than or equal to DEFAULT_STOCK_QTY
        defaultProductShouldBeFound("stockQty.greaterThanOrEqual=" + DEFAULT_STOCK_QTY);

        // Get all the productList where stockQty is greater than or equal to UPDATED_STOCK_QTY
        defaultProductShouldNotBeFound("stockQty.greaterThanOrEqual=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByStockQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stockQty is less than or equal to DEFAULT_STOCK_QTY
        defaultProductShouldBeFound("stockQty.lessThanOrEqual=" + DEFAULT_STOCK_QTY);

        // Get all the productList where stockQty is less than or equal to SMALLER_STOCK_QTY
        defaultProductShouldNotBeFound("stockQty.lessThanOrEqual=" + SMALLER_STOCK_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByStockQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stockQty is less than DEFAULT_STOCK_QTY
        defaultProductShouldNotBeFound("stockQty.lessThan=" + DEFAULT_STOCK_QTY);

        // Get all the productList where stockQty is less than UPDATED_STOCK_QTY
        defaultProductShouldBeFound("stockQty.lessThan=" + UPDATED_STOCK_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByStockQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stockQty is greater than DEFAULT_STOCK_QTY
        defaultProductShouldNotBeFound("stockQty.greaterThan=" + DEFAULT_STOCK_QTY);

        // Get all the productList where stockQty is greater than SMALLER_STOCK_QTY
        defaultProductShouldBeFound("stockQty.greaterThan=" + SMALLER_STOCK_QTY);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than or equal to DEFAULT_PRICE
        defaultProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productList where price is greater than or equal to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is less than or equal to DEFAULT_PRICE
        defaultProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productList where price is less than or equal to SMALLER_PRICE
        defaultProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is less than DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the productList where price is less than UPDATED_PRICE
        defaultProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the productList where price is greater than SMALLER_PRICE
        defaultProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where tax equals to DEFAULT_TAX
        defaultProductShouldBeFound("tax.equals=" + DEFAULT_TAX);

        // Get all the productList where tax equals to UPDATED_TAX
        defaultProductShouldNotBeFound("tax.equals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllProductsByTaxIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where tax in DEFAULT_TAX or UPDATED_TAX
        defaultProductShouldBeFound("tax.in=" + DEFAULT_TAX + "," + UPDATED_TAX);

        // Get all the productList where tax equals to UPDATED_TAX
        defaultProductShouldNotBeFound("tax.in=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllProductsByTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where tax is not null
        defaultProductShouldBeFound("tax.specified=true");

        // Get all the productList where tax is null
        defaultProductShouldNotBeFound("tax.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where tax is greater than or equal to DEFAULT_TAX
        defaultProductShouldBeFound("tax.greaterThanOrEqual=" + DEFAULT_TAX);

        // Get all the productList where tax is greater than or equal to UPDATED_TAX
        defaultProductShouldNotBeFound("tax.greaterThanOrEqual=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllProductsByTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where tax is less than or equal to DEFAULT_TAX
        defaultProductShouldBeFound("tax.lessThanOrEqual=" + DEFAULT_TAX);

        // Get all the productList where tax is less than or equal to SMALLER_TAX
        defaultProductShouldNotBeFound("tax.lessThanOrEqual=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllProductsByTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where tax is less than DEFAULT_TAX
        defaultProductShouldNotBeFound("tax.lessThan=" + DEFAULT_TAX);

        // Get all the productList where tax is less than UPDATED_TAX
        defaultProductShouldBeFound("tax.lessThan=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllProductsByTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where tax is greater than DEFAULT_TAX
        defaultProductShouldNotBeFound("tax.greaterThan=" + DEFAULT_TAX);

        // Get all the productList where tax is greater than SMALLER_TAX
        defaultProductShouldBeFound("tax.greaterThan=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllProductsByMrpIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where mrp equals to DEFAULT_MRP
        defaultProductShouldBeFound("mrp.equals=" + DEFAULT_MRP);

        // Get all the productList where mrp equals to UPDATED_MRP
        defaultProductShouldNotBeFound("mrp.equals=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllProductsByMrpIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where mrp in DEFAULT_MRP or UPDATED_MRP
        defaultProductShouldBeFound("mrp.in=" + DEFAULT_MRP + "," + UPDATED_MRP);

        // Get all the productList where mrp equals to UPDATED_MRP
        defaultProductShouldNotBeFound("mrp.in=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllProductsByMrpIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where mrp is not null
        defaultProductShouldBeFound("mrp.specified=true");

        // Get all the productList where mrp is null
        defaultProductShouldNotBeFound("mrp.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByMrpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where mrp is greater than or equal to DEFAULT_MRP
        defaultProductShouldBeFound("mrp.greaterThanOrEqual=" + DEFAULT_MRP);

        // Get all the productList where mrp is greater than or equal to UPDATED_MRP
        defaultProductShouldNotBeFound("mrp.greaterThanOrEqual=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllProductsByMrpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where mrp is less than or equal to DEFAULT_MRP
        defaultProductShouldBeFound("mrp.lessThanOrEqual=" + DEFAULT_MRP);

        // Get all the productList where mrp is less than or equal to SMALLER_MRP
        defaultProductShouldNotBeFound("mrp.lessThanOrEqual=" + SMALLER_MRP);
    }

    @Test
    @Transactional
    void getAllProductsByMrpIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where mrp is less than DEFAULT_MRP
        defaultProductShouldNotBeFound("mrp.lessThan=" + DEFAULT_MRP);

        // Get all the productList where mrp is less than UPDATED_MRP
        defaultProductShouldBeFound("mrp.lessThan=" + UPDATED_MRP);
    }

    @Test
    @Transactional
    void getAllProductsByMrpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where mrp is greater than DEFAULT_MRP
        defaultProductShouldNotBeFound("mrp.greaterThan=" + DEFAULT_MRP);

        // Get all the productList where mrp is greater than SMALLER_MRP
        defaultProductShouldBeFound("mrp.greaterThan=" + SMALLER_MRP);
    }

    @Test
    @Transactional
    void getAllProductsByOfferTextIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where offerText equals to DEFAULT_OFFER_TEXT
        defaultProductShouldBeFound("offerText.equals=" + DEFAULT_OFFER_TEXT);

        // Get all the productList where offerText equals to UPDATED_OFFER_TEXT
        defaultProductShouldNotBeFound("offerText.equals=" + UPDATED_OFFER_TEXT);
    }

    @Test
    @Transactional
    void getAllProductsByOfferTextIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where offerText in DEFAULT_OFFER_TEXT or UPDATED_OFFER_TEXT
        defaultProductShouldBeFound("offerText.in=" + DEFAULT_OFFER_TEXT + "," + UPDATED_OFFER_TEXT);

        // Get all the productList where offerText equals to UPDATED_OFFER_TEXT
        defaultProductShouldNotBeFound("offerText.in=" + UPDATED_OFFER_TEXT);
    }

    @Test
    @Transactional
    void getAllProductsByOfferTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where offerText is not null
        defaultProductShouldBeFound("offerText.specified=true");

        // Get all the productList where offerText is null
        defaultProductShouldNotBeFound("offerText.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByOfferTextContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where offerText contains DEFAULT_OFFER_TEXT
        defaultProductShouldBeFound("offerText.contains=" + DEFAULT_OFFER_TEXT);

        // Get all the productList where offerText contains UPDATED_OFFER_TEXT
        defaultProductShouldNotBeFound("offerText.contains=" + UPDATED_OFFER_TEXT);
    }

    @Test
    @Transactional
    void getAllProductsByOfferTextNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where offerText does not contain DEFAULT_OFFER_TEXT
        defaultProductShouldNotBeFound("offerText.doesNotContain=" + DEFAULT_OFFER_TEXT);

        // Get all the productList where offerText does not contain UPDATED_OFFER_TEXT
        defaultProductShouldBeFound("offerText.doesNotContain=" + UPDATED_OFFER_TEXT);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDisclaimerIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where disclaimer equals to DEFAULT_DISCLAIMER
        defaultProductShouldBeFound("disclaimer.equals=" + DEFAULT_DISCLAIMER);

        // Get all the productList where disclaimer equals to UPDATED_DISCLAIMER
        defaultProductShouldNotBeFound("disclaimer.equals=" + UPDATED_DISCLAIMER);
    }

    @Test
    @Transactional
    void getAllProductsByDisclaimerIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where disclaimer in DEFAULT_DISCLAIMER or UPDATED_DISCLAIMER
        defaultProductShouldBeFound("disclaimer.in=" + DEFAULT_DISCLAIMER + "," + UPDATED_DISCLAIMER);

        // Get all the productList where disclaimer equals to UPDATED_DISCLAIMER
        defaultProductShouldNotBeFound("disclaimer.in=" + UPDATED_DISCLAIMER);
    }

    @Test
    @Transactional
    void getAllProductsByDisclaimerIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where disclaimer is not null
        defaultProductShouldBeFound("disclaimer.specified=true");

        // Get all the productList where disclaimer is null
        defaultProductShouldNotBeFound("disclaimer.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDisclaimerContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where disclaimer contains DEFAULT_DISCLAIMER
        defaultProductShouldBeFound("disclaimer.contains=" + DEFAULT_DISCLAIMER);

        // Get all the productList where disclaimer contains UPDATED_DISCLAIMER
        defaultProductShouldNotBeFound("disclaimer.contains=" + UPDATED_DISCLAIMER);
    }

    @Test
    @Transactional
    void getAllProductsByDisclaimerNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where disclaimer does not contain DEFAULT_DISCLAIMER
        defaultProductShouldNotBeFound("disclaimer.doesNotContain=" + DEFAULT_DISCLAIMER);

        // Get all the productList where disclaimer does not contain UPDATED_DISCLAIMER
        defaultProductShouldBeFound("disclaimer.doesNotContain=" + UPDATED_DISCLAIMER);
    }

    @Test
    @Transactional
    void getAllProductsBySubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where subscription equals to DEFAULT_SUBSCRIPTION
        defaultProductShouldBeFound("subscription.equals=" + DEFAULT_SUBSCRIPTION);

        // Get all the productList where subscription equals to UPDATED_SUBSCRIPTION
        defaultProductShouldNotBeFound("subscription.equals=" + UPDATED_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsBySubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where subscription in DEFAULT_SUBSCRIPTION or UPDATED_SUBSCRIPTION
        defaultProductShouldBeFound("subscription.in=" + DEFAULT_SUBSCRIPTION + "," + UPDATED_SUBSCRIPTION);

        // Get all the productList where subscription equals to UPDATED_SUBSCRIPTION
        defaultProductShouldNotBeFound("subscription.in=" + UPDATED_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsBySubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where subscription is not null
        defaultProductShouldBeFound("subscription.specified=true");

        // Get all the productList where subscription is null
        defaultProductShouldNotBeFound("subscription.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdAt equals to DEFAULT_CREATED_AT
        defaultProductShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the productList where createdAt equals to UPDATED_CREATED_AT
        defaultProductShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultProductShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the productList where createdAt equals to UPDATED_CREATED_AT
        defaultProductShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdAt is not null
        defaultProductShouldBeFound("createdAt.specified=true");

        // Get all the productList where createdAt is null
        defaultProductShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultProductShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the productList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProductShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllProductsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultProductShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the productList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProductShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllProductsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where updatedAt is not null
        defaultProductShouldBeFound("updatedAt.specified=true");

        // Get all the productList where updatedAt is null
        defaultProductShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isActive equals to DEFAULT_IS_ACTIVE
        defaultProductShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the productList where isActive equals to UPDATED_IS_ACTIVE
        defaultProductShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllProductsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultProductShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the productList where isActive equals to UPDATED_IS_ACTIVE
        defaultProductShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllProductsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where isActive is not null
        defaultProductShouldBeFound("isActive.specified=true");

        // Get all the productList where isActive is null
        defaultProductShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySubCatIsEqualToSomething() throws Exception {
        SubCat subCat;
        if (TestUtil.findAll(em, SubCat.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            subCat = SubCatResourceIT.createEntity(em);
        } else {
            subCat = TestUtil.findAll(em, SubCat.class).get(0);
        }
        em.persist(subCat);
        em.flush();
        product.setSubCat(subCat);
        productRepository.saveAndFlush(product);
        Long subCatId = subCat.getId();
        // Get all the productList where subCat equals to subCatId
        defaultProductShouldBeFound("subCatId.equals=" + subCatId);

        // Get all the productList where subCat equals to (subCatId + 1)
        defaultProductShouldNotBeFound("subCatId.equals=" + (subCatId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByCartIsEqualToSomething() throws Exception {
        Cart cart;
        if (TestUtil.findAll(em, Cart.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            cart = CartResourceIT.createEntity(em);
        } else {
            cart = TestUtil.findAll(em, Cart.class).get(0);
        }
        em.persist(cart);
        em.flush();
        product.addCart(cart);
        productRepository.saveAndFlush(product);
        Long cartId = cart.getId();
        // Get all the productList where cart equals to cartId
        defaultProductShouldBeFound("cartId.equals=" + cartId);

        // Get all the productList where cart equals to (cartId + 1)
        defaultProductShouldNotBeFound("cartId.equals=" + (cartId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByOrdersIsEqualToSomething() throws Exception {
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            orders = OrdersResourceIT.createEntity(em);
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        em.persist(orders);
        em.flush();
        product.addOrders(orders);
        productRepository.saveAndFlush(product);
        Long ordersId = orders.getId();
        // Get all the productList where orders equals to ordersId
        defaultProductShouldBeFound("ordersId.equals=" + ordersId);

        // Get all the productList where orders equals to (ordersId + 1)
        defaultProductShouldNotBeFound("ordersId.equals=" + (ordersId + 1));
    }

    @Test
    @Transactional
    void getAllProductsBySubscribedOrdersIsEqualToSomething() throws Exception {
        SubscribedOrders subscribedOrders;
        if (TestUtil.findAll(em, SubscribedOrders.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            subscribedOrders = SubscribedOrdersResourceIT.createEntity(em);
        } else {
            subscribedOrders = TestUtil.findAll(em, SubscribedOrders.class).get(0);
        }
        em.persist(subscribedOrders);
        em.flush();
        product.addSubscribedOrders(subscribedOrders);
        productRepository.saveAndFlush(product);
        Long subscribedOrdersId = subscribedOrders.getId();
        // Get all the productList where subscribedOrders equals to subscribedOrdersId
        defaultProductShouldBeFound("subscribedOrdersId.equals=" + subscribedOrdersId);

        // Get all the productList where subscribedOrders equals to (subscribedOrdersId + 1)
        defaultProductShouldNotBeFound("subscribedOrdersId.equals=" + (subscribedOrdersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].qtyText").value(hasItem(DEFAULT_QTY_TEXT)))
            .andExpect(jsonPath("$.[*].stockQty").value(hasItem(DEFAULT_STOCK_QTY.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.doubleValue())))
            .andExpect(jsonPath("$.[*].offerText").value(hasItem(DEFAULT_OFFER_TEXT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].disclaimer").value(hasItem(DEFAULT_DISCLAIMER)))
            .andExpect(jsonPath("$.[*].subscription").value(hasItem(DEFAULT_SUBSCRIPTION.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .title(UPDATED_TITLE)
            .qtyText(UPDATED_QTY_TEXT)
            .stockQty(UPDATED_STOCK_QTY)
            .price(UPDATED_PRICE)
            .tax(UPDATED_TAX)
            .mrp(UPDATED_MRP)
            .offerText(UPDATED_OFFER_TEXT)
            .description(UPDATED_DESCRIPTION)
            .disclaimer(UPDATED_DISCLAIMER)
            .subscription(UPDATED_SUBSCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProduct.getQtyText()).isEqualTo(UPDATED_QTY_TEXT);
        assertThat(testProduct.getStockQty()).isEqualTo(UPDATED_STOCK_QTY);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testProduct.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testProduct.getOfferText()).isEqualTo(UPDATED_OFFER_TEXT);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getDisclaimer()).isEqualTo(UPDATED_DISCLAIMER);
        assertThat(testProduct.getSubscription()).isEqualTo(UPDATED_SUBSCRIPTION);
        assertThat(testProduct.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProduct.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testProduct.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .tax(UPDATED_TAX)
            .mrp(UPDATED_MRP)
            .description(UPDATED_DESCRIPTION)
            .disclaimer(UPDATED_DISCLAIMER)
            .subscription(UPDATED_SUBSCRIPTION)
            .createdAt(UPDATED_CREATED_AT);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProduct.getQtyText()).isEqualTo(DEFAULT_QTY_TEXT);
        assertThat(testProduct.getStockQty()).isEqualTo(DEFAULT_STOCK_QTY);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testProduct.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testProduct.getOfferText()).isEqualTo(DEFAULT_OFFER_TEXT);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getDisclaimer()).isEqualTo(UPDATED_DISCLAIMER);
        assertThat(testProduct.getSubscription()).isEqualTo(UPDATED_SUBSCRIPTION);
        assertThat(testProduct.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProduct.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testProduct.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .title(UPDATED_TITLE)
            .qtyText(UPDATED_QTY_TEXT)
            .stockQty(UPDATED_STOCK_QTY)
            .price(UPDATED_PRICE)
            .tax(UPDATED_TAX)
            .mrp(UPDATED_MRP)
            .offerText(UPDATED_OFFER_TEXT)
            .description(UPDATED_DESCRIPTION)
            .disclaimer(UPDATED_DISCLAIMER)
            .subscription(UPDATED_SUBSCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProduct.getQtyText()).isEqualTo(UPDATED_QTY_TEXT);
        assertThat(testProduct.getStockQty()).isEqualTo(UPDATED_STOCK_QTY);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testProduct.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testProduct.getOfferText()).isEqualTo(UPDATED_OFFER_TEXT);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getDisclaimer()).isEqualTo(UPDATED_DISCLAIMER);
        assertThat(testProduct.getSubscription()).isEqualTo(UPDATED_SUBSCRIPTION);
        assertThat(testProduct.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProduct.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testProduct.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
