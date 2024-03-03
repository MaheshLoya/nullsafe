package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.AssignRole;
import com.nullsafe.daily.domain.Cart;
import com.nullsafe.daily.domain.OrderUserAssign;
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.SpecificNotification;
import com.nullsafe.daily.domain.SubscribedOrderDelivery;
import com.nullsafe.daily.domain.SubscribedOrders;
import com.nullsafe.daily.domain.Transactions;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.UsersRepository;
import com.nullsafe.daily.service.dto.UsersDTO;
import com.nullsafe.daily.service.mapper.UsersMapper;
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
 * Integration tests for the {@link UsersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsersResourceIT {

    private static final Double DEFAULT_WALLET_AMOUNT = 1D;
    private static final Double UPDATED_WALLET_AMOUNT = 2D;
    private static final Double SMALLER_WALLET_AMOUNT = 1D - 1D;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Instant DEFAULT_EMAIL_VERIFIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EMAIL_VERIFIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_REMEMBER_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_REMEMBER_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FCM = "AAAAAAAAAA";
    private static final String UPDATED_FCM = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUBSCRIPTION_AMOUNT = 1;
    private static final Integer UPDATED_SUBSCRIPTION_AMOUNT = 2;
    private static final Integer SMALLER_SUBSCRIPTION_AMOUNT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsersMockMvc;

    private Users users;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createEntity(EntityManager em) {
        Users users = new Users()
            .walletAmount(DEFAULT_WALLET_AMOUNT)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .emailVerifiedAt(DEFAULT_EMAIL_VERIFIED_AT)
            .password(DEFAULT_PASSWORD)
            .rememberToken(DEFAULT_REMEMBER_TOKEN)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .name(DEFAULT_NAME)
            .fcm(DEFAULT_FCM)
            .subscriptionAmount(DEFAULT_SUBSCRIPTION_AMOUNT);
        return users;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createUpdatedEntity(EntityManager em) {
        Users users = new Users()
            .walletAmount(UPDATED_WALLET_AMOUNT)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .emailVerifiedAt(UPDATED_EMAIL_VERIFIED_AT)
            .password(UPDATED_PASSWORD)
            .rememberToken(UPDATED_REMEMBER_TOKEN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .name(UPDATED_NAME)
            .fcm(UPDATED_FCM)
            .subscriptionAmount(UPDATED_SUBSCRIPTION_AMOUNT);
        return users;
    }

    @BeforeEach
    public void initTest() {
        users = createEntity(em);
    }

    @Test
    @Transactional
    void createUsers() throws Exception {
        int databaseSizeBeforeCreate = usersRepository.findAll().size();
        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isCreated());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate + 1);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getWalletAmount()).isEqualTo(DEFAULT_WALLET_AMOUNT);
        assertThat(testUsers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsers.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUsers.getEmailVerifiedAt()).isEqualTo(DEFAULT_EMAIL_VERIFIED_AT);
        assertThat(testUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUsers.getRememberToken()).isEqualTo(DEFAULT_REMEMBER_TOKEN);
        assertThat(testUsers.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUsers.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testUsers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUsers.getFcm()).isEqualTo(DEFAULT_FCM);
        assertThat(testUsers.getSubscriptionAmount()).isEqualTo(DEFAULT_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void createUsersWithExistingId() throws Exception {
        // Create the Users with an existing ID
        users.setId(1L);
        UsersDTO usersDTO = usersMapper.toDto(users);

        int databaseSizeBeforeCreate = usersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setName(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setSubscriptionAmount(null);

        // Create the Users, which fails.
        UsersDTO usersDTO = usersMapper.toDto(users);

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isBadRequest());

        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].walletAmount").value(hasItem(DEFAULT_WALLET_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].emailVerifiedAt").value(hasItem(DEFAULT_EMAIL_VERIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].rememberToken").value(hasItem(DEFAULT_REMEMBER_TOKEN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fcm").value(hasItem(DEFAULT_FCM)))
            .andExpect(jsonPath("$.[*].subscriptionAmount").value(hasItem(DEFAULT_SUBSCRIPTION_AMOUNT)));
    }

    @Test
    @Transactional
    void getUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get the users
        restUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, users.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(users.getId().intValue()))
            .andExpect(jsonPath("$.walletAmount").value(DEFAULT_WALLET_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.emailVerifiedAt").value(DEFAULT_EMAIL_VERIFIED_AT.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.rememberToken").value(DEFAULT_REMEMBER_TOKEN))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fcm").value(DEFAULT_FCM))
            .andExpect(jsonPath("$.subscriptionAmount").value(DEFAULT_SUBSCRIPTION_AMOUNT));
    }

    @Test
    @Transactional
    void getUsersByIdFiltering() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        Long id = users.getId();

        defaultUsersShouldBeFound("id.equals=" + id);
        defaultUsersShouldNotBeFound("id.notEquals=" + id);

        defaultUsersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.greaterThan=" + id);

        defaultUsersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsersByWalletAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where walletAmount equals to DEFAULT_WALLET_AMOUNT
        defaultUsersShouldBeFound("walletAmount.equals=" + DEFAULT_WALLET_AMOUNT);

        // Get all the usersList where walletAmount equals to UPDATED_WALLET_AMOUNT
        defaultUsersShouldNotBeFound("walletAmount.equals=" + UPDATED_WALLET_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersByWalletAmountIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where walletAmount in DEFAULT_WALLET_AMOUNT or UPDATED_WALLET_AMOUNT
        defaultUsersShouldBeFound("walletAmount.in=" + DEFAULT_WALLET_AMOUNT + "," + UPDATED_WALLET_AMOUNT);

        // Get all the usersList where walletAmount equals to UPDATED_WALLET_AMOUNT
        defaultUsersShouldNotBeFound("walletAmount.in=" + UPDATED_WALLET_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersByWalletAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where walletAmount is not null
        defaultUsersShouldBeFound("walletAmount.specified=true");

        // Get all the usersList where walletAmount is null
        defaultUsersShouldNotBeFound("walletAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByWalletAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where walletAmount is greater than or equal to DEFAULT_WALLET_AMOUNT
        defaultUsersShouldBeFound("walletAmount.greaterThanOrEqual=" + DEFAULT_WALLET_AMOUNT);

        // Get all the usersList where walletAmount is greater than or equal to UPDATED_WALLET_AMOUNT
        defaultUsersShouldNotBeFound("walletAmount.greaterThanOrEqual=" + UPDATED_WALLET_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersByWalletAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where walletAmount is less than or equal to DEFAULT_WALLET_AMOUNT
        defaultUsersShouldBeFound("walletAmount.lessThanOrEqual=" + DEFAULT_WALLET_AMOUNT);

        // Get all the usersList where walletAmount is less than or equal to SMALLER_WALLET_AMOUNT
        defaultUsersShouldNotBeFound("walletAmount.lessThanOrEqual=" + SMALLER_WALLET_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersByWalletAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where walletAmount is less than DEFAULT_WALLET_AMOUNT
        defaultUsersShouldNotBeFound("walletAmount.lessThan=" + DEFAULT_WALLET_AMOUNT);

        // Get all the usersList where walletAmount is less than UPDATED_WALLET_AMOUNT
        defaultUsersShouldBeFound("walletAmount.lessThan=" + UPDATED_WALLET_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersByWalletAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where walletAmount is greater than DEFAULT_WALLET_AMOUNT
        defaultUsersShouldNotBeFound("walletAmount.greaterThan=" + DEFAULT_WALLET_AMOUNT);

        // Get all the usersList where walletAmount is greater than SMALLER_WALLET_AMOUNT
        defaultUsersShouldBeFound("walletAmount.greaterThan=" + SMALLER_WALLET_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email equals to DEFAULT_EMAIL
        defaultUsersShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the usersList where email equals to UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUsersShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the usersList where email equals to UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email is not null
        defaultUsersShouldBeFound("email.specified=true");

        // Get all the usersList where email is null
        defaultUsersShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email contains DEFAULT_EMAIL
        defaultUsersShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the usersList where email contains UPDATED_EMAIL
        defaultUsersShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where email does not contain DEFAULT_EMAIL
        defaultUsersShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the usersList where email does not contain UPDATED_EMAIL
        defaultUsersShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phone equals to DEFAULT_PHONE
        defaultUsersShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the usersList where phone equals to UPDATED_PHONE
        defaultUsersShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUsersShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the usersList where phone equals to UPDATED_PHONE
        defaultUsersShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phone is not null
        defaultUsersShouldBeFound("phone.specified=true");

        // Get all the usersList where phone is null
        defaultUsersShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phone contains DEFAULT_PHONE
        defaultUsersShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the usersList where phone contains UPDATED_PHONE
        defaultUsersShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where phone does not contain DEFAULT_PHONE
        defaultUsersShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the usersList where phone does not contain UPDATED_PHONE
        defaultUsersShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUsersByEmailVerifiedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where emailVerifiedAt equals to DEFAULT_EMAIL_VERIFIED_AT
        defaultUsersShouldBeFound("emailVerifiedAt.equals=" + DEFAULT_EMAIL_VERIFIED_AT);

        // Get all the usersList where emailVerifiedAt equals to UPDATED_EMAIL_VERIFIED_AT
        defaultUsersShouldNotBeFound("emailVerifiedAt.equals=" + UPDATED_EMAIL_VERIFIED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByEmailVerifiedAtIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where emailVerifiedAt in DEFAULT_EMAIL_VERIFIED_AT or UPDATED_EMAIL_VERIFIED_AT
        defaultUsersShouldBeFound("emailVerifiedAt.in=" + DEFAULT_EMAIL_VERIFIED_AT + "," + UPDATED_EMAIL_VERIFIED_AT);

        // Get all the usersList where emailVerifiedAt equals to UPDATED_EMAIL_VERIFIED_AT
        defaultUsersShouldNotBeFound("emailVerifiedAt.in=" + UPDATED_EMAIL_VERIFIED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByEmailVerifiedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where emailVerifiedAt is not null
        defaultUsersShouldBeFound("emailVerifiedAt.specified=true");

        // Get all the usersList where emailVerifiedAt is null
        defaultUsersShouldNotBeFound("emailVerifiedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password equals to DEFAULT_PASSWORD
        defaultUsersShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the usersList where password equals to UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the usersList where password equals to UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password is not null
        defaultUsersShouldBeFound("password.specified=true");

        // Get all the usersList where password is null
        defaultUsersShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByPasswordContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password contains DEFAULT_PASSWORD
        defaultUsersShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the usersList where password contains UPDATED_PASSWORD
        defaultUsersShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where password does not contain DEFAULT_PASSWORD
        defaultUsersShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the usersList where password does not contain UPDATED_PASSWORD
        defaultUsersShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUsersByRememberTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where rememberToken equals to DEFAULT_REMEMBER_TOKEN
        defaultUsersShouldBeFound("rememberToken.equals=" + DEFAULT_REMEMBER_TOKEN);

        // Get all the usersList where rememberToken equals to UPDATED_REMEMBER_TOKEN
        defaultUsersShouldNotBeFound("rememberToken.equals=" + UPDATED_REMEMBER_TOKEN);
    }

    @Test
    @Transactional
    void getAllUsersByRememberTokenIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where rememberToken in DEFAULT_REMEMBER_TOKEN or UPDATED_REMEMBER_TOKEN
        defaultUsersShouldBeFound("rememberToken.in=" + DEFAULT_REMEMBER_TOKEN + "," + UPDATED_REMEMBER_TOKEN);

        // Get all the usersList where rememberToken equals to UPDATED_REMEMBER_TOKEN
        defaultUsersShouldNotBeFound("rememberToken.in=" + UPDATED_REMEMBER_TOKEN);
    }

    @Test
    @Transactional
    void getAllUsersByRememberTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where rememberToken is not null
        defaultUsersShouldBeFound("rememberToken.specified=true");

        // Get all the usersList where rememberToken is null
        defaultUsersShouldNotBeFound("rememberToken.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByRememberTokenContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where rememberToken contains DEFAULT_REMEMBER_TOKEN
        defaultUsersShouldBeFound("rememberToken.contains=" + DEFAULT_REMEMBER_TOKEN);

        // Get all the usersList where rememberToken contains UPDATED_REMEMBER_TOKEN
        defaultUsersShouldNotBeFound("rememberToken.contains=" + UPDATED_REMEMBER_TOKEN);
    }

    @Test
    @Transactional
    void getAllUsersByRememberTokenNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where rememberToken does not contain DEFAULT_REMEMBER_TOKEN
        defaultUsersShouldNotBeFound("rememberToken.doesNotContain=" + DEFAULT_REMEMBER_TOKEN);

        // Get all the usersList where rememberToken does not contain UPDATED_REMEMBER_TOKEN
        defaultUsersShouldBeFound("rememberToken.doesNotContain=" + UPDATED_REMEMBER_TOKEN);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdAt equals to DEFAULT_CREATED_AT
        defaultUsersShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the usersList where createdAt equals to UPDATED_CREATED_AT
        defaultUsersShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUsersShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the usersList where createdAt equals to UPDATED_CREATED_AT
        defaultUsersShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where createdAt is not null
        defaultUsersShouldBeFound("createdAt.specified=true");

        // Get all the usersList where createdAt is null
        defaultUsersShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUsersShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the usersList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUsersShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUsersShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the usersList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUsersShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUsersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where updatedAt is not null
        defaultUsersShouldBeFound("updatedAt.specified=true");

        // Get all the usersList where updatedAt is null
        defaultUsersShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where name equals to DEFAULT_NAME
        defaultUsersShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the usersList where name equals to UPDATED_NAME
        defaultUsersShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUsersShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the usersList where name equals to UPDATED_NAME
        defaultUsersShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where name is not null
        defaultUsersShouldBeFound("name.specified=true");

        // Get all the usersList where name is null
        defaultUsersShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByNameContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where name contains DEFAULT_NAME
        defaultUsersShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the usersList where name contains UPDATED_NAME
        defaultUsersShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where name does not contain DEFAULT_NAME
        defaultUsersShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the usersList where name does not contain UPDATED_NAME
        defaultUsersShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUsersByFcmIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where fcm equals to DEFAULT_FCM
        defaultUsersShouldBeFound("fcm.equals=" + DEFAULT_FCM);

        // Get all the usersList where fcm equals to UPDATED_FCM
        defaultUsersShouldNotBeFound("fcm.equals=" + UPDATED_FCM);
    }

    @Test
    @Transactional
    void getAllUsersByFcmIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where fcm in DEFAULT_FCM or UPDATED_FCM
        defaultUsersShouldBeFound("fcm.in=" + DEFAULT_FCM + "," + UPDATED_FCM);

        // Get all the usersList where fcm equals to UPDATED_FCM
        defaultUsersShouldNotBeFound("fcm.in=" + UPDATED_FCM);
    }

    @Test
    @Transactional
    void getAllUsersByFcmIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where fcm is not null
        defaultUsersShouldBeFound("fcm.specified=true");

        // Get all the usersList where fcm is null
        defaultUsersShouldNotBeFound("fcm.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersByFcmContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where fcm contains DEFAULT_FCM
        defaultUsersShouldBeFound("fcm.contains=" + DEFAULT_FCM);

        // Get all the usersList where fcm contains UPDATED_FCM
        defaultUsersShouldNotBeFound("fcm.contains=" + UPDATED_FCM);
    }

    @Test
    @Transactional
    void getAllUsersByFcmNotContainsSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where fcm does not contain DEFAULT_FCM
        defaultUsersShouldNotBeFound("fcm.doesNotContain=" + DEFAULT_FCM);

        // Get all the usersList where fcm does not contain UPDATED_FCM
        defaultUsersShouldBeFound("fcm.doesNotContain=" + UPDATED_FCM);
    }

    @Test
    @Transactional
    void getAllUsersBySubscriptionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where subscriptionAmount equals to DEFAULT_SUBSCRIPTION_AMOUNT
        defaultUsersShouldBeFound("subscriptionAmount.equals=" + DEFAULT_SUBSCRIPTION_AMOUNT);

        // Get all the usersList where subscriptionAmount equals to UPDATED_SUBSCRIPTION_AMOUNT
        defaultUsersShouldNotBeFound("subscriptionAmount.equals=" + UPDATED_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersBySubscriptionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where subscriptionAmount in DEFAULT_SUBSCRIPTION_AMOUNT or UPDATED_SUBSCRIPTION_AMOUNT
        defaultUsersShouldBeFound("subscriptionAmount.in=" + DEFAULT_SUBSCRIPTION_AMOUNT + "," + UPDATED_SUBSCRIPTION_AMOUNT);

        // Get all the usersList where subscriptionAmount equals to UPDATED_SUBSCRIPTION_AMOUNT
        defaultUsersShouldNotBeFound("subscriptionAmount.in=" + UPDATED_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersBySubscriptionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where subscriptionAmount is not null
        defaultUsersShouldBeFound("subscriptionAmount.specified=true");

        // Get all the usersList where subscriptionAmount is null
        defaultUsersShouldNotBeFound("subscriptionAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllUsersBySubscriptionAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where subscriptionAmount is greater than or equal to DEFAULT_SUBSCRIPTION_AMOUNT
        defaultUsersShouldBeFound("subscriptionAmount.greaterThanOrEqual=" + DEFAULT_SUBSCRIPTION_AMOUNT);

        // Get all the usersList where subscriptionAmount is greater than or equal to UPDATED_SUBSCRIPTION_AMOUNT
        defaultUsersShouldNotBeFound("subscriptionAmount.greaterThanOrEqual=" + UPDATED_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersBySubscriptionAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where subscriptionAmount is less than or equal to DEFAULT_SUBSCRIPTION_AMOUNT
        defaultUsersShouldBeFound("subscriptionAmount.lessThanOrEqual=" + DEFAULT_SUBSCRIPTION_AMOUNT);

        // Get all the usersList where subscriptionAmount is less than or equal to SMALLER_SUBSCRIPTION_AMOUNT
        defaultUsersShouldNotBeFound("subscriptionAmount.lessThanOrEqual=" + SMALLER_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersBySubscriptionAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where subscriptionAmount is less than DEFAULT_SUBSCRIPTION_AMOUNT
        defaultUsersShouldNotBeFound("subscriptionAmount.lessThan=" + DEFAULT_SUBSCRIPTION_AMOUNT);

        // Get all the usersList where subscriptionAmount is less than UPDATED_SUBSCRIPTION_AMOUNT
        defaultUsersShouldBeFound("subscriptionAmount.lessThan=" + UPDATED_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersBySubscriptionAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the usersList where subscriptionAmount is greater than DEFAULT_SUBSCRIPTION_AMOUNT
        defaultUsersShouldNotBeFound("subscriptionAmount.greaterThan=" + DEFAULT_SUBSCRIPTION_AMOUNT);

        // Get all the usersList where subscriptionAmount is greater than SMALLER_SUBSCRIPTION_AMOUNT
        defaultUsersShouldBeFound("subscriptionAmount.greaterThan=" + SMALLER_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllUsersByAssignRoleIsEqualToSomething() throws Exception {
        AssignRole assignRole;
        if (TestUtil.findAll(em, AssignRole.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            assignRole = AssignRoleResourceIT.createEntity(em);
        } else {
            assignRole = TestUtil.findAll(em, AssignRole.class).get(0);
        }
        em.persist(assignRole);
        em.flush();
        users.addAssignRole(assignRole);
        usersRepository.saveAndFlush(users);
        Long assignRoleId = assignRole.getId();
        // Get all the usersList where assignRole equals to assignRoleId
        defaultUsersShouldBeFound("assignRoleId.equals=" + assignRoleId);

        // Get all the usersList where assignRole equals to (assignRoleId + 1)
        defaultUsersShouldNotBeFound("assignRoleId.equals=" + (assignRoleId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByCartIsEqualToSomething() throws Exception {
        Cart cart;
        if (TestUtil.findAll(em, Cart.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            cart = CartResourceIT.createEntity(em);
        } else {
            cart = TestUtil.findAll(em, Cart.class).get(0);
        }
        em.persist(cart);
        em.flush();
        users.addCart(cart);
        usersRepository.saveAndFlush(users);
        Long cartId = cart.getId();
        // Get all the usersList where cart equals to cartId
        defaultUsersShouldBeFound("cartId.equals=" + cartId);

        // Get all the usersList where cart equals to (cartId + 1)
        defaultUsersShouldNotBeFound("cartId.equals=" + (cartId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByOrderUserAssignIsEqualToSomething() throws Exception {
        OrderUserAssign orderUserAssign;
        if (TestUtil.findAll(em, OrderUserAssign.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            orderUserAssign = OrderUserAssignResourceIT.createEntity(em);
        } else {
            orderUserAssign = TestUtil.findAll(em, OrderUserAssign.class).get(0);
        }
        em.persist(orderUserAssign);
        em.flush();
        users.addOrderUserAssign(orderUserAssign);
        usersRepository.saveAndFlush(users);
        Long orderUserAssignId = orderUserAssign.getId();
        // Get all the usersList where orderUserAssign equals to orderUserAssignId
        defaultUsersShouldBeFound("orderUserAssignId.equals=" + orderUserAssignId);

        // Get all the usersList where orderUserAssign equals to (orderUserAssignId + 1)
        defaultUsersShouldNotBeFound("orderUserAssignId.equals=" + (orderUserAssignId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByOrdersIsEqualToSomething() throws Exception {
        Orders orders;
        if (TestUtil.findAll(em, Orders.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            orders = OrdersResourceIT.createEntity(em);
        } else {
            orders = TestUtil.findAll(em, Orders.class).get(0);
        }
        em.persist(orders);
        em.flush();
        users.addOrders(orders);
        usersRepository.saveAndFlush(users);
        Long ordersId = orders.getId();
        // Get all the usersList where orders equals to ordersId
        defaultUsersShouldBeFound("ordersId.equals=" + ordersId);

        // Get all the usersList where orders equals to (ordersId + 1)
        defaultUsersShouldNotBeFound("ordersId.equals=" + (ordersId + 1));
    }

    @Test
    @Transactional
    void getAllUsersBySpecificNotificationIsEqualToSomething() throws Exception {
        SpecificNotification specificNotification;
        if (TestUtil.findAll(em, SpecificNotification.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            specificNotification = SpecificNotificationResourceIT.createEntity(em);
        } else {
            specificNotification = TestUtil.findAll(em, SpecificNotification.class).get(0);
        }
        em.persist(specificNotification);
        em.flush();
        users.addSpecificNotification(specificNotification);
        usersRepository.saveAndFlush(users);
        Long specificNotificationId = specificNotification.getId();
        // Get all the usersList where specificNotification equals to specificNotificationId
        defaultUsersShouldBeFound("specificNotificationId.equals=" + specificNotificationId);

        // Get all the usersList where specificNotification equals to (specificNotificationId + 1)
        defaultUsersShouldNotBeFound("specificNotificationId.equals=" + (specificNotificationId + 1));
    }

    @Test
    @Transactional
    void getAllUsersBySubscribedOrderDeliveryIsEqualToSomething() throws Exception {
        SubscribedOrderDelivery subscribedOrderDelivery;
        if (TestUtil.findAll(em, SubscribedOrderDelivery.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            subscribedOrderDelivery = SubscribedOrderDeliveryResourceIT.createEntity(em);
        } else {
            subscribedOrderDelivery = TestUtil.findAll(em, SubscribedOrderDelivery.class).get(0);
        }
        em.persist(subscribedOrderDelivery);
        em.flush();
        users.addSubscribedOrderDelivery(subscribedOrderDelivery);
        usersRepository.saveAndFlush(users);
        Long subscribedOrderDeliveryId = subscribedOrderDelivery.getId();
        // Get all the usersList where subscribedOrderDelivery equals to subscribedOrderDeliveryId
        defaultUsersShouldBeFound("subscribedOrderDeliveryId.equals=" + subscribedOrderDeliveryId);

        // Get all the usersList where subscribedOrderDelivery equals to (subscribedOrderDeliveryId + 1)
        defaultUsersShouldNotBeFound("subscribedOrderDeliveryId.equals=" + (subscribedOrderDeliveryId + 1));
    }

    @Test
    @Transactional
    void getAllUsersBySubscribedOrdersIsEqualToSomething() throws Exception {
        SubscribedOrders subscribedOrders;
        if (TestUtil.findAll(em, SubscribedOrders.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            subscribedOrders = SubscribedOrdersResourceIT.createEntity(em);
        } else {
            subscribedOrders = TestUtil.findAll(em, SubscribedOrders.class).get(0);
        }
        em.persist(subscribedOrders);
        em.flush();
        users.addSubscribedOrders(subscribedOrders);
        usersRepository.saveAndFlush(users);
        Long subscribedOrdersId = subscribedOrders.getId();
        // Get all the usersList where subscribedOrders equals to subscribedOrdersId
        defaultUsersShouldBeFound("subscribedOrdersId.equals=" + subscribedOrdersId);

        // Get all the usersList where subscribedOrders equals to (subscribedOrdersId + 1)
        defaultUsersShouldNotBeFound("subscribedOrdersId.equals=" + (subscribedOrdersId + 1));
    }

    @Test
    @Transactional
    void getAllUsersByTransactionsIsEqualToSomething() throws Exception {
        Transactions transactions;
        if (TestUtil.findAll(em, Transactions.class).isEmpty()) {
            usersRepository.saveAndFlush(users);
            transactions = TransactionsResourceIT.createEntity(em);
        } else {
            transactions = TestUtil.findAll(em, Transactions.class).get(0);
        }
        em.persist(transactions);
        em.flush();
        users.addTransactions(transactions);
        usersRepository.saveAndFlush(users);
        Long transactionsId = transactions.getId();
        // Get all the usersList where transactions equals to transactionsId
        defaultUsersShouldBeFound("transactionsId.equals=" + transactionsId);

        // Get all the usersList where transactions equals to (transactionsId + 1)
        defaultUsersShouldNotBeFound("transactionsId.equals=" + (transactionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsersShouldBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].walletAmount").value(hasItem(DEFAULT_WALLET_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].emailVerifiedAt").value(hasItem(DEFAULT_EMAIL_VERIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].rememberToken").value(hasItem(DEFAULT_REMEMBER_TOKEN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fcm").value(hasItem(DEFAULT_FCM)))
            .andExpect(jsonPath("$.[*].subscriptionAmount").value(hasItem(DEFAULT_SUBSCRIPTION_AMOUNT)));

        // Check, that the count call also returns 1
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsersShouldNotBeFound(String filter) throws Exception {
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsers() throws Exception {
        // Get the users
        restUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users
        Users updatedUsers = usersRepository.findById(users.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUsers are not directly saved in db
        em.detach(updatedUsers);
        updatedUsers
            .walletAmount(UPDATED_WALLET_AMOUNT)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .emailVerifiedAt(UPDATED_EMAIL_VERIFIED_AT)
            .password(UPDATED_PASSWORD)
            .rememberToken(UPDATED_REMEMBER_TOKEN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .name(UPDATED_NAME)
            .fcm(UPDATED_FCM)
            .subscriptionAmount(UPDATED_SUBSCRIPTION_AMOUNT);
        UsersDTO usersDTO = usersMapper.toDto(updatedUsers);

        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getWalletAmount()).isEqualTo(UPDATED_WALLET_AMOUNT);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUsers.getEmailVerifiedAt()).isEqualTo(UPDATED_EMAIL_VERIFIED_AT);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getRememberToken()).isEqualTo(UPDATED_REMEMBER_TOKEN);
        assertThat(testUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUsers.getFcm()).isEqualTo(UPDATED_FCM);
        assertThat(testUsers.getSubscriptionAmount()).isEqualTo(UPDATED_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .walletAmount(UPDATED_WALLET_AMOUNT)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .rememberToken(UPDATED_REMEMBER_TOKEN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .name(UPDATED_NAME)
            .subscriptionAmount(UPDATED_SUBSCRIPTION_AMOUNT);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getWalletAmount()).isEqualTo(UPDATED_WALLET_AMOUNT);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUsers.getEmailVerifiedAt()).isEqualTo(DEFAULT_EMAIL_VERIFIED_AT);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getRememberToken()).isEqualTo(UPDATED_REMEMBER_TOKEN);
        assertThat(testUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUsers.getFcm()).isEqualTo(DEFAULT_FCM);
        assertThat(testUsers.getSubscriptionAmount()).isEqualTo(UPDATED_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .walletAmount(UPDATED_WALLET_AMOUNT)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .emailVerifiedAt(UPDATED_EMAIL_VERIFIED_AT)
            .password(UPDATED_PASSWORD)
            .rememberToken(UPDATED_REMEMBER_TOKEN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .name(UPDATED_NAME)
            .fcm(UPDATED_FCM)
            .subscriptionAmount(UPDATED_SUBSCRIPTION_AMOUNT);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = usersList.get(usersList.size() - 1);
        assertThat(testUsers.getWalletAmount()).isEqualTo(UPDATED_WALLET_AMOUNT);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUsers.getEmailVerifiedAt()).isEqualTo(UPDATED_EMAIL_VERIFIED_AT);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getRememberToken()).isEqualTo(UPDATED_REMEMBER_TOKEN);
        assertThat(testUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUsers.getFcm()).isEqualTo(UPDATED_FCM);
        assertThat(testUsers.getSubscriptionAmount()).isEqualTo(UPDATED_SUBSCRIPTION_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsers() throws Exception {
        int databaseSizeBeforeUpdate = usersRepository.findAll().size();
        users.setId(longCount.incrementAndGet());

        // Create the Users
        UsersDTO usersDTO = usersMapper.toDto(users);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        int databaseSizeBeforeDelete = usersRepository.findAll().size();

        // Delete the users
        restUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, users.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Users> usersList = usersRepository.findAll();
        assertThat(usersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
