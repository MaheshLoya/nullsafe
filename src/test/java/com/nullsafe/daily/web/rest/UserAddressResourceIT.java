package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.SubscribedOrders;
import com.nullsafe.daily.domain.UserAddress;
import com.nullsafe.daily.repository.UserAddressRepository;
import com.nullsafe.daily.service.dto.UserAddressDTO;
import com.nullsafe.daily.service.mapper.UserAddressMapper;
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
 * Integration tests for the {@link UserAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAddressResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_S_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_S_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FLAT_NO = "AAAAAAAAAA";
    private static final String UPDATED_FLAT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_APARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APARTMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AREA = "AAAAAAAAAA";
    private static final String UPDATED_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_LANDMARK = "AAAAAAAAAA";
    private static final String UPDATED_LANDMARK = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_PINCODE = 1;
    private static final Integer UPDATED_PINCODE = 2;
    private static final Integer SMALLER_PINCODE = 1 - 1;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;
    private static final Double SMALLER_LNG = 1D - 1D;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/user-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAddressMockMvc;

    private UserAddress userAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAddress createEntity(EntityManager em) {
        UserAddress userAddress = new UserAddress()
            .userId(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .sPhone(DEFAULT_S_PHONE)
            .flatNo(DEFAULT_FLAT_NO)
            .apartmentName(DEFAULT_APARTMENT_NAME)
            .area(DEFAULT_AREA)
            .landmark(DEFAULT_LANDMARK)
            .city(DEFAULT_CITY)
            .pincode(DEFAULT_PINCODE)
            .lat(DEFAULT_LAT)
            .lng(DEFAULT_LNG)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .isActive(DEFAULT_IS_ACTIVE);
        return userAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAddress createUpdatedEntity(EntityManager em) {
        UserAddress userAddress = new UserAddress()
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .sPhone(UPDATED_S_PHONE)
            .flatNo(UPDATED_FLAT_NO)
            .apartmentName(UPDATED_APARTMENT_NAME)
            .area(UPDATED_AREA)
            .landmark(UPDATED_LANDMARK)
            .city(UPDATED_CITY)
            .pincode(UPDATED_PINCODE)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE);
        return userAddress;
    }

    @BeforeEach
    public void initTest() {
        userAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAddress() throws Exception {
        int databaseSizeBeforeCreate = userAddressRepository.findAll().size();
        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);
        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeCreate + 1);
        UserAddress testUserAddress = userAddressList.get(userAddressList.size() - 1);
        assertThat(testUserAddress.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserAddress.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserAddress.getsPhone()).isEqualTo(DEFAULT_S_PHONE);
        assertThat(testUserAddress.getFlatNo()).isEqualTo(DEFAULT_FLAT_NO);
        assertThat(testUserAddress.getApartmentName()).isEqualTo(DEFAULT_APARTMENT_NAME);
        assertThat(testUserAddress.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testUserAddress.getLandmark()).isEqualTo(DEFAULT_LANDMARK);
        assertThat(testUserAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserAddress.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testUserAddress.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testUserAddress.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testUserAddress.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserAddress.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testUserAddress.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createUserAddressWithExistingId() throws Exception {
        // Create the UserAddress with an existing ID
        userAddress.setId(1L);
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        int databaseSizeBeforeCreate = userAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setUserId(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setName(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checksPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setsPhone(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAreaIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setArea(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLandmarkIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setLandmark(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setCity(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPincodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAddressRepository.findAll().size();
        // set the field null
        userAddress.setPincode(null);

        // Create the UserAddress, which fails.
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        restUserAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAddresses() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sPhone").value(hasItem(DEFAULT_S_PHONE)))
            .andExpect(jsonPath("$.[*].flatNo").value(hasItem(DEFAULT_FLAT_NO)))
            .andExpect(jsonPath("$.[*].apartmentName").value(hasItem(DEFAULT_APARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get the userAddress
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, userAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAddress.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sPhone").value(DEFAULT_S_PHONE))
            .andExpect(jsonPath("$.flatNo").value(DEFAULT_FLAT_NO))
            .andExpect(jsonPath("$.apartmentName").value(DEFAULT_APARTMENT_NAME))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA))
            .andExpect(jsonPath("$.landmark").value(DEFAULT_LANDMARK))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getUserAddressesByIdFiltering() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        Long id = userAddress.getId();

        defaultUserAddressShouldBeFound("id.equals=" + id);
        defaultUserAddressShouldNotBeFound("id.notEquals=" + id);

        defaultUserAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultUserAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserAddressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserAddressesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where userId equals to DEFAULT_USER_ID
        defaultUserAddressShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the userAddressList where userId equals to UPDATED_USER_ID
        defaultUserAddressShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserAddressesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultUserAddressShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the userAddressList where userId equals to UPDATED_USER_ID
        defaultUserAddressShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserAddressesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where userId is not null
        defaultUserAddressShouldBeFound("userId.specified=true");

        // Get all the userAddressList where userId is null
        defaultUserAddressShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where userId is greater than or equal to DEFAULT_USER_ID
        defaultUserAddressShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the userAddressList where userId is greater than or equal to UPDATED_USER_ID
        defaultUserAddressShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserAddressesByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where userId is less than or equal to DEFAULT_USER_ID
        defaultUserAddressShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the userAddressList where userId is less than or equal to SMALLER_USER_ID
        defaultUserAddressShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserAddressesByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where userId is less than DEFAULT_USER_ID
        defaultUserAddressShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the userAddressList where userId is less than UPDATED_USER_ID
        defaultUserAddressShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserAddressesByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where userId is greater than DEFAULT_USER_ID
        defaultUserAddressShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the userAddressList where userId is greater than SMALLER_USER_ID
        defaultUserAddressShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserAddressesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where name equals to DEFAULT_NAME
        defaultUserAddressShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the userAddressList where name equals to UPDATED_NAME
        defaultUserAddressShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUserAddressShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the userAddressList where name equals to UPDATED_NAME
        defaultUserAddressShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where name is not null
        defaultUserAddressShouldBeFound("name.specified=true");

        // Get all the userAddressList where name is null
        defaultUserAddressShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByNameContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where name contains DEFAULT_NAME
        defaultUserAddressShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the userAddressList where name contains UPDATED_NAME
        defaultUserAddressShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where name does not contain DEFAULT_NAME
        defaultUserAddressShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the userAddressList where name does not contain UPDATED_NAME
        defaultUserAddressShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesBysPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where sPhone equals to DEFAULT_S_PHONE
        defaultUserAddressShouldBeFound("sPhone.equals=" + DEFAULT_S_PHONE);

        // Get all the userAddressList where sPhone equals to UPDATED_S_PHONE
        defaultUserAddressShouldNotBeFound("sPhone.equals=" + UPDATED_S_PHONE);
    }

    @Test
    @Transactional
    void getAllUserAddressesBysPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where sPhone in DEFAULT_S_PHONE or UPDATED_S_PHONE
        defaultUserAddressShouldBeFound("sPhone.in=" + DEFAULT_S_PHONE + "," + UPDATED_S_PHONE);

        // Get all the userAddressList where sPhone equals to UPDATED_S_PHONE
        defaultUserAddressShouldNotBeFound("sPhone.in=" + UPDATED_S_PHONE);
    }

    @Test
    @Transactional
    void getAllUserAddressesBysPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where sPhone is not null
        defaultUserAddressShouldBeFound("sPhone.specified=true");

        // Get all the userAddressList where sPhone is null
        defaultUserAddressShouldNotBeFound("sPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesBysPhoneContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where sPhone contains DEFAULT_S_PHONE
        defaultUserAddressShouldBeFound("sPhone.contains=" + DEFAULT_S_PHONE);

        // Get all the userAddressList where sPhone contains UPDATED_S_PHONE
        defaultUserAddressShouldNotBeFound("sPhone.contains=" + UPDATED_S_PHONE);
    }

    @Test
    @Transactional
    void getAllUserAddressesBysPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where sPhone does not contain DEFAULT_S_PHONE
        defaultUserAddressShouldNotBeFound("sPhone.doesNotContain=" + DEFAULT_S_PHONE);

        // Get all the userAddressList where sPhone does not contain UPDATED_S_PHONE
        defaultUserAddressShouldBeFound("sPhone.doesNotContain=" + UPDATED_S_PHONE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByFlatNoIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where flatNo equals to DEFAULT_FLAT_NO
        defaultUserAddressShouldBeFound("flatNo.equals=" + DEFAULT_FLAT_NO);

        // Get all the userAddressList where flatNo equals to UPDATED_FLAT_NO
        defaultUserAddressShouldNotBeFound("flatNo.equals=" + UPDATED_FLAT_NO);
    }

    @Test
    @Transactional
    void getAllUserAddressesByFlatNoIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where flatNo in DEFAULT_FLAT_NO or UPDATED_FLAT_NO
        defaultUserAddressShouldBeFound("flatNo.in=" + DEFAULT_FLAT_NO + "," + UPDATED_FLAT_NO);

        // Get all the userAddressList where flatNo equals to UPDATED_FLAT_NO
        defaultUserAddressShouldNotBeFound("flatNo.in=" + UPDATED_FLAT_NO);
    }

    @Test
    @Transactional
    void getAllUserAddressesByFlatNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where flatNo is not null
        defaultUserAddressShouldBeFound("flatNo.specified=true");

        // Get all the userAddressList where flatNo is null
        defaultUserAddressShouldNotBeFound("flatNo.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByFlatNoContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where flatNo contains DEFAULT_FLAT_NO
        defaultUserAddressShouldBeFound("flatNo.contains=" + DEFAULT_FLAT_NO);

        // Get all the userAddressList where flatNo contains UPDATED_FLAT_NO
        defaultUserAddressShouldNotBeFound("flatNo.contains=" + UPDATED_FLAT_NO);
    }

    @Test
    @Transactional
    void getAllUserAddressesByFlatNoNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where flatNo does not contain DEFAULT_FLAT_NO
        defaultUserAddressShouldNotBeFound("flatNo.doesNotContain=" + DEFAULT_FLAT_NO);

        // Get all the userAddressList where flatNo does not contain UPDATED_FLAT_NO
        defaultUserAddressShouldBeFound("flatNo.doesNotContain=" + UPDATED_FLAT_NO);
    }

    @Test
    @Transactional
    void getAllUserAddressesByApartmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where apartmentName equals to DEFAULT_APARTMENT_NAME
        defaultUserAddressShouldBeFound("apartmentName.equals=" + DEFAULT_APARTMENT_NAME);

        // Get all the userAddressList where apartmentName equals to UPDATED_APARTMENT_NAME
        defaultUserAddressShouldNotBeFound("apartmentName.equals=" + UPDATED_APARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByApartmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where apartmentName in DEFAULT_APARTMENT_NAME or UPDATED_APARTMENT_NAME
        defaultUserAddressShouldBeFound("apartmentName.in=" + DEFAULT_APARTMENT_NAME + "," + UPDATED_APARTMENT_NAME);

        // Get all the userAddressList where apartmentName equals to UPDATED_APARTMENT_NAME
        defaultUserAddressShouldNotBeFound("apartmentName.in=" + UPDATED_APARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByApartmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where apartmentName is not null
        defaultUserAddressShouldBeFound("apartmentName.specified=true");

        // Get all the userAddressList where apartmentName is null
        defaultUserAddressShouldNotBeFound("apartmentName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByApartmentNameContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where apartmentName contains DEFAULT_APARTMENT_NAME
        defaultUserAddressShouldBeFound("apartmentName.contains=" + DEFAULT_APARTMENT_NAME);

        // Get all the userAddressList where apartmentName contains UPDATED_APARTMENT_NAME
        defaultUserAddressShouldNotBeFound("apartmentName.contains=" + UPDATED_APARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByApartmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where apartmentName does not contain DEFAULT_APARTMENT_NAME
        defaultUserAddressShouldNotBeFound("apartmentName.doesNotContain=" + DEFAULT_APARTMENT_NAME);

        // Get all the userAddressList where apartmentName does not contain UPDATED_APARTMENT_NAME
        defaultUserAddressShouldBeFound("apartmentName.doesNotContain=" + UPDATED_APARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllUserAddressesByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where area equals to DEFAULT_AREA
        defaultUserAddressShouldBeFound("area.equals=" + DEFAULT_AREA);

        // Get all the userAddressList where area equals to UPDATED_AREA
        defaultUserAddressShouldNotBeFound("area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllUserAddressesByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where area in DEFAULT_AREA or UPDATED_AREA
        defaultUserAddressShouldBeFound("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA);

        // Get all the userAddressList where area equals to UPDATED_AREA
        defaultUserAddressShouldNotBeFound("area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllUserAddressesByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where area is not null
        defaultUserAddressShouldBeFound("area.specified=true");

        // Get all the userAddressList where area is null
        defaultUserAddressShouldNotBeFound("area.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByAreaContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where area contains DEFAULT_AREA
        defaultUserAddressShouldBeFound("area.contains=" + DEFAULT_AREA);

        // Get all the userAddressList where area contains UPDATED_AREA
        defaultUserAddressShouldNotBeFound("area.contains=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllUserAddressesByAreaNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where area does not contain DEFAULT_AREA
        defaultUserAddressShouldNotBeFound("area.doesNotContain=" + DEFAULT_AREA);

        // Get all the userAddressList where area does not contain UPDATED_AREA
        defaultUserAddressShouldBeFound("area.doesNotContain=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark equals to DEFAULT_LANDMARK
        defaultUserAddressShouldBeFound("landmark.equals=" + DEFAULT_LANDMARK);

        // Get all the userAddressList where landmark equals to UPDATED_LANDMARK
        defaultUserAddressShouldNotBeFound("landmark.equals=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark in DEFAULT_LANDMARK or UPDATED_LANDMARK
        defaultUserAddressShouldBeFound("landmark.in=" + DEFAULT_LANDMARK + "," + UPDATED_LANDMARK);

        // Get all the userAddressList where landmark equals to UPDATED_LANDMARK
        defaultUserAddressShouldNotBeFound("landmark.in=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark is not null
        defaultUserAddressShouldBeFound("landmark.specified=true");

        // Get all the userAddressList where landmark is null
        defaultUserAddressShouldNotBeFound("landmark.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark contains DEFAULT_LANDMARK
        defaultUserAddressShouldBeFound("landmark.contains=" + DEFAULT_LANDMARK);

        // Get all the userAddressList where landmark contains UPDATED_LANDMARK
        defaultUserAddressShouldNotBeFound("landmark.contains=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLandmarkNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where landmark does not contain DEFAULT_LANDMARK
        defaultUserAddressShouldNotBeFound("landmark.doesNotContain=" + DEFAULT_LANDMARK);

        // Get all the userAddressList where landmark does not contain UPDATED_LANDMARK
        defaultUserAddressShouldBeFound("landmark.doesNotContain=" + UPDATED_LANDMARK);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city equals to DEFAULT_CITY
        defaultUserAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the userAddressList where city equals to UPDATED_CITY
        defaultUserAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultUserAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the userAddressList where city equals to UPDATED_CITY
        defaultUserAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city is not null
        defaultUserAddressShouldBeFound("city.specified=true");

        // Get all the userAddressList where city is null
        defaultUserAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city contains DEFAULT_CITY
        defaultUserAddressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the userAddressList where city contains UPDATED_CITY
        defaultUserAddressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where city does not contain DEFAULT_CITY
        defaultUserAddressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the userAddressList where city does not contain UPDATED_CITY
        defaultUserAddressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPincodeIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where pincode equals to DEFAULT_PINCODE
        defaultUserAddressShouldBeFound("pincode.equals=" + DEFAULT_PINCODE);

        // Get all the userAddressList where pincode equals to UPDATED_PINCODE
        defaultUserAddressShouldNotBeFound("pincode.equals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPincodeIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where pincode in DEFAULT_PINCODE or UPDATED_PINCODE
        defaultUserAddressShouldBeFound("pincode.in=" + DEFAULT_PINCODE + "," + UPDATED_PINCODE);

        // Get all the userAddressList where pincode equals to UPDATED_PINCODE
        defaultUserAddressShouldNotBeFound("pincode.in=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPincodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where pincode is not null
        defaultUserAddressShouldBeFound("pincode.specified=true");

        // Get all the userAddressList where pincode is null
        defaultUserAddressShouldNotBeFound("pincode.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByPincodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where pincode is greater than or equal to DEFAULT_PINCODE
        defaultUserAddressShouldBeFound("pincode.greaterThanOrEqual=" + DEFAULT_PINCODE);

        // Get all the userAddressList where pincode is greater than or equal to UPDATED_PINCODE
        defaultUserAddressShouldNotBeFound("pincode.greaterThanOrEqual=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPincodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where pincode is less than or equal to DEFAULT_PINCODE
        defaultUserAddressShouldBeFound("pincode.lessThanOrEqual=" + DEFAULT_PINCODE);

        // Get all the userAddressList where pincode is less than or equal to SMALLER_PINCODE
        defaultUserAddressShouldNotBeFound("pincode.lessThanOrEqual=" + SMALLER_PINCODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPincodeIsLessThanSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where pincode is less than DEFAULT_PINCODE
        defaultUserAddressShouldNotBeFound("pincode.lessThan=" + DEFAULT_PINCODE);

        // Get all the userAddressList where pincode is less than UPDATED_PINCODE
        defaultUserAddressShouldBeFound("pincode.lessThan=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByPincodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where pincode is greater than DEFAULT_PINCODE
        defaultUserAddressShouldNotBeFound("pincode.greaterThan=" + DEFAULT_PINCODE);

        // Get all the userAddressList where pincode is greater than SMALLER_PINCODE
        defaultUserAddressShouldBeFound("pincode.greaterThan=" + SMALLER_PINCODE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lat equals to DEFAULT_LAT
        defaultUserAddressShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the userAddressList where lat equals to UPDATED_LAT
        defaultUserAddressShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLatIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultUserAddressShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the userAddressList where lat equals to UPDATED_LAT
        defaultUserAddressShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lat is not null
        defaultUserAddressShouldBeFound("lat.specified=true");

        // Get all the userAddressList where lat is null
        defaultUserAddressShouldNotBeFound("lat.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lat is greater than or equal to DEFAULT_LAT
        defaultUserAddressShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the userAddressList where lat is greater than or equal to UPDATED_LAT
        defaultUserAddressShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lat is less than or equal to DEFAULT_LAT
        defaultUserAddressShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the userAddressList where lat is less than or equal to SMALLER_LAT
        defaultUserAddressShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lat is less than DEFAULT_LAT
        defaultUserAddressShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the userAddressList where lat is less than UPDATED_LAT
        defaultUserAddressShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lat is greater than DEFAULT_LAT
        defaultUserAddressShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the userAddressList where lat is greater than SMALLER_LAT
        defaultUserAddressShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLngIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lng equals to DEFAULT_LNG
        defaultUserAddressShouldBeFound("lng.equals=" + DEFAULT_LNG);

        // Get all the userAddressList where lng equals to UPDATED_LNG
        defaultUserAddressShouldNotBeFound("lng.equals=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLngIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lng in DEFAULT_LNG or UPDATED_LNG
        defaultUserAddressShouldBeFound("lng.in=" + DEFAULT_LNG + "," + UPDATED_LNG);

        // Get all the userAddressList where lng equals to UPDATED_LNG
        defaultUserAddressShouldNotBeFound("lng.in=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLngIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lng is not null
        defaultUserAddressShouldBeFound("lng.specified=true");

        // Get all the userAddressList where lng is null
        defaultUserAddressShouldNotBeFound("lng.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByLngIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lng is greater than or equal to DEFAULT_LNG
        defaultUserAddressShouldBeFound("lng.greaterThanOrEqual=" + DEFAULT_LNG);

        // Get all the userAddressList where lng is greater than or equal to UPDATED_LNG
        defaultUserAddressShouldNotBeFound("lng.greaterThanOrEqual=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLngIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lng is less than or equal to DEFAULT_LNG
        defaultUserAddressShouldBeFound("lng.lessThanOrEqual=" + DEFAULT_LNG);

        // Get all the userAddressList where lng is less than or equal to SMALLER_LNG
        defaultUserAddressShouldNotBeFound("lng.lessThanOrEqual=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLngIsLessThanSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lng is less than DEFAULT_LNG
        defaultUserAddressShouldNotBeFound("lng.lessThan=" + DEFAULT_LNG);

        // Get all the userAddressList where lng is less than UPDATED_LNG
        defaultUserAddressShouldBeFound("lng.lessThan=" + UPDATED_LNG);
    }

    @Test
    @Transactional
    void getAllUserAddressesByLngIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where lng is greater than DEFAULT_LNG
        defaultUserAddressShouldNotBeFound("lng.greaterThan=" + DEFAULT_LNG);

        // Get all the userAddressList where lng is greater than SMALLER_LNG
        defaultUserAddressShouldBeFound("lng.greaterThan=" + SMALLER_LNG);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdAt equals to DEFAULT_CREATED_AT
        defaultUserAddressShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the userAddressList where createdAt equals to UPDATED_CREATED_AT
        defaultUserAddressShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUserAddressShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the userAddressList where createdAt equals to UPDATED_CREATED_AT
        defaultUserAddressShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where createdAt is not null
        defaultUserAddressShouldBeFound("createdAt.specified=true");

        // Get all the userAddressList where createdAt is null
        defaultUserAddressShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUserAddressShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the userAddressList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserAddressShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUserAddressShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the userAddressList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserAddressShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserAddressesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where updatedAt is not null
        defaultUserAddressShouldBeFound("updatedAt.specified=true");

        // Get all the userAddressList where updatedAt is null
        defaultUserAddressShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where isActive equals to DEFAULT_IS_ACTIVE
        defaultUserAddressShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the userAddressList where isActive equals to UPDATED_IS_ACTIVE
        defaultUserAddressShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultUserAddressShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the userAddressList where isActive equals to UPDATED_IS_ACTIVE
        defaultUserAddressShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllUserAddressesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        // Get all the userAddressList where isActive is not null
        defaultUserAddressShouldBeFound("isActive.specified=true");

        // Get all the userAddressList where isActive is null
        defaultUserAddressShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllUserAddressesByOrdersIsEqualToSomething() throws Exception {
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            userAddressRepository.saveAndFlush(userAddress);
            orders = OrdersResourceIT.createEntity(em);
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        em.persist(orders);
        em.flush();
        userAddress.addOrders(orders);
        userAddressRepository.saveAndFlush(userAddress);
        Long ordersId = orders.getId();
        // Get all the userAddressList where orders equals to ordersId
        defaultUserAddressShouldBeFound("ordersId.equals=" + ordersId);

        // Get all the userAddressList where orders equals to (ordersId + 1)
        defaultUserAddressShouldNotBeFound("ordersId.equals=" + (ordersId + 1));
    }

    @Test
    @Transactional
    void getAllUserAddressesBySubscribedOrdersIsEqualToSomething() throws Exception {
        SubscribedOrders subscribedOrders;
        if (TestUtil.findAll(em, SubscribedOrders.class).isEmpty()) {
            userAddressRepository.saveAndFlush(userAddress);
            subscribedOrders = SubscribedOrdersResourceIT.createEntity(em);
        } else {
            subscribedOrders = TestUtil.findAll(em, SubscribedOrders.class).get(0);
        }
        em.persist(subscribedOrders);
        em.flush();
        userAddress.addSubscribedOrders(subscribedOrders);
        userAddressRepository.saveAndFlush(userAddress);
        Long subscribedOrdersId = subscribedOrders.getId();
        // Get all the userAddressList where subscribedOrders equals to subscribedOrdersId
        defaultUserAddressShouldBeFound("subscribedOrdersId.equals=" + subscribedOrdersId);

        // Get all the userAddressList where subscribedOrders equals to (subscribedOrdersId + 1)
        defaultUserAddressShouldNotBeFound("subscribedOrdersId.equals=" + (subscribedOrdersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserAddressShouldBeFound(String filter) throws Exception {
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sPhone").value(hasItem(DEFAULT_S_PHONE)))
            .andExpect(jsonPath("$.[*].flatNo").value(hasItem(DEFAULT_FLAT_NO)))
            .andExpect(jsonPath("$.[*].apartmentName").value(hasItem(DEFAULT_APARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserAddressShouldNotBeFound(String filter) throws Exception {
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserAddress() throws Exception {
        // Get the userAddress
        restUserAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();

        // Update the userAddress
        UserAddress updatedUserAddress = userAddressRepository.findById(userAddress.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAddress are not directly saved in db
        em.detach(updatedUserAddress);
        updatedUserAddress
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .sPhone(UPDATED_S_PHONE)
            .flatNo(UPDATED_FLAT_NO)
            .apartmentName(UPDATED_APARTMENT_NAME)
            .area(UPDATED_AREA)
            .landmark(UPDATED_LANDMARK)
            .city(UPDATED_CITY)
            .pincode(UPDATED_PINCODE)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE);
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(updatedUserAddress);

        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
        UserAddress testUserAddress = userAddressList.get(userAddressList.size() - 1);
        assertThat(testUserAddress.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserAddress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserAddress.getsPhone()).isEqualTo(UPDATED_S_PHONE);
        assertThat(testUserAddress.getFlatNo()).isEqualTo(UPDATED_FLAT_NO);
        assertThat(testUserAddress.getApartmentName()).isEqualTo(UPDATED_APARTMENT_NAME);
        assertThat(testUserAddress.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testUserAddress.getLandmark()).isEqualTo(UPDATED_LANDMARK);
        assertThat(testUserAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testUserAddress.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testUserAddress.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testUserAddress.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserAddress.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testUserAddress.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAddressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAddressWithPatch() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();

        // Update the userAddress using partial update
        UserAddress partialUpdatedUserAddress = new UserAddress();
        partialUpdatedUserAddress.setId(userAddress.getId());

        partialUpdatedUserAddress
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .sPhone(UPDATED_S_PHONE)
            .apartmentName(UPDATED_APARTMENT_NAME)
            .area(UPDATED_AREA)
            .lat(UPDATED_LAT)
            .isActive(UPDATED_IS_ACTIVE);

        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAddress))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
        UserAddress testUserAddress = userAddressList.get(userAddressList.size() - 1);
        assertThat(testUserAddress.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserAddress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserAddress.getsPhone()).isEqualTo(UPDATED_S_PHONE);
        assertThat(testUserAddress.getFlatNo()).isEqualTo(DEFAULT_FLAT_NO);
        assertThat(testUserAddress.getApartmentName()).isEqualTo(UPDATED_APARTMENT_NAME);
        assertThat(testUserAddress.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testUserAddress.getLandmark()).isEqualTo(DEFAULT_LANDMARK);
        assertThat(testUserAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserAddress.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testUserAddress.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testUserAddress.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testUserAddress.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserAddress.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testUserAddress.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateUserAddressWithPatch() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();

        // Update the userAddress using partial update
        UserAddress partialUpdatedUserAddress = new UserAddress();
        partialUpdatedUserAddress.setId(userAddress.getId());

        partialUpdatedUserAddress
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .sPhone(UPDATED_S_PHONE)
            .flatNo(UPDATED_FLAT_NO)
            .apartmentName(UPDATED_APARTMENT_NAME)
            .area(UPDATED_AREA)
            .landmark(UPDATED_LANDMARK)
            .city(UPDATED_CITY)
            .pincode(UPDATED_PINCODE)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE);

        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAddress))
            )
            .andExpect(status().isOk());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
        UserAddress testUserAddress = userAddressList.get(userAddressList.size() - 1);
        assertThat(testUserAddress.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserAddress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserAddress.getsPhone()).isEqualTo(UPDATED_S_PHONE);
        assertThat(testUserAddress.getFlatNo()).isEqualTo(UPDATED_FLAT_NO);
        assertThat(testUserAddress.getApartmentName()).isEqualTo(UPDATED_APARTMENT_NAME);
        assertThat(testUserAddress.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testUserAddress.getLandmark()).isEqualTo(UPDATED_LANDMARK);
        assertThat(testUserAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testUserAddress.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testUserAddress.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testUserAddress.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserAddress.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testUserAddress.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAddressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAddress() throws Exception {
        int databaseSizeBeforeUpdate = userAddressRepository.findAll().size();
        userAddress.setId(longCount.incrementAndGet());

        // Create the UserAddress
        UserAddressDTO userAddressDTO = userAddressMapper.toDto(userAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAddress in the database
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAddress() throws Exception {
        // Initialize the database
        userAddressRepository.saveAndFlush(userAddress);

        int databaseSizeBeforeDelete = userAddressRepository.findAll().size();

        // Delete the userAddress
        restUserAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAddress> userAddressList = userAddressRepository.findAll();
        assertThat(userAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
