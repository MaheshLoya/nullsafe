package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.PersonalAccessTokens;
import com.nullsafe.daily.repository.PersonalAccessTokensRepository;
import com.nullsafe.daily.service.dto.PersonalAccessTokensDTO;
import com.nullsafe.daily.service.mapper.PersonalAccessTokensMapper;
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
 * Integration tests for the {@link PersonalAccessTokensResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalAccessTokensResourceIT {

    private static final String DEFAULT_TOKENABLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TOKENABLE_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_TOKENABLE_ID = 1L;
    private static final Long UPDATED_TOKENABLE_ID = 2L;
    private static final Long SMALLER_TOKENABLE_ID = 1L - 1L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_ABILITIES = "AAAAAAAAAA";
    private static final String UPDATED_ABILITIES = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_USED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_USED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/personal-access-tokens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalAccessTokensRepository personalAccessTokensRepository;

    @Autowired
    private PersonalAccessTokensMapper personalAccessTokensMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalAccessTokensMockMvc;

    private PersonalAccessTokens personalAccessTokens;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalAccessTokens createEntity(EntityManager em) {
        PersonalAccessTokens personalAccessTokens = new PersonalAccessTokens()
            .tokenableType(DEFAULT_TOKENABLE_TYPE)
            .tokenableId(DEFAULT_TOKENABLE_ID)
            .name(DEFAULT_NAME)
            .token(DEFAULT_TOKEN)
            .abilities(DEFAULT_ABILITIES)
            .lastUsedAt(DEFAULT_LAST_USED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return personalAccessTokens;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalAccessTokens createUpdatedEntity(EntityManager em) {
        PersonalAccessTokens personalAccessTokens = new PersonalAccessTokens()
            .tokenableType(UPDATED_TOKENABLE_TYPE)
            .tokenableId(UPDATED_TOKENABLE_ID)
            .name(UPDATED_NAME)
            .token(UPDATED_TOKEN)
            .abilities(UPDATED_ABILITIES)
            .lastUsedAt(UPDATED_LAST_USED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return personalAccessTokens;
    }

    @BeforeEach
    public void initTest() {
        personalAccessTokens = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalAccessTokens() throws Exception {
        int databaseSizeBeforeCreate = personalAccessTokensRepository.findAll().size();
        // Create the PersonalAccessTokens
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);
        restPersonalAccessTokensMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalAccessTokens testPersonalAccessTokens = personalAccessTokensList.get(personalAccessTokensList.size() - 1);
        assertThat(testPersonalAccessTokens.getTokenableType()).isEqualTo(DEFAULT_TOKENABLE_TYPE);
        assertThat(testPersonalAccessTokens.getTokenableId()).isEqualTo(DEFAULT_TOKENABLE_ID);
        assertThat(testPersonalAccessTokens.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPersonalAccessTokens.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testPersonalAccessTokens.getAbilities()).isEqualTo(DEFAULT_ABILITIES);
        assertThat(testPersonalAccessTokens.getLastUsedAt()).isEqualTo(DEFAULT_LAST_USED_AT);
        assertThat(testPersonalAccessTokens.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPersonalAccessTokens.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createPersonalAccessTokensWithExistingId() throws Exception {
        // Create the PersonalAccessTokens with an existing ID
        personalAccessTokens.setId(1L);
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        int databaseSizeBeforeCreate = personalAccessTokensRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalAccessTokensMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTokenableTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalAccessTokensRepository.findAll().size();
        // set the field null
        personalAccessTokens.setTokenableType(null);

        // Create the PersonalAccessTokens, which fails.
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        restPersonalAccessTokensMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenableIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalAccessTokensRepository.findAll().size();
        // set the field null
        personalAccessTokens.setTokenableId(null);

        // Create the PersonalAccessTokens, which fails.
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        restPersonalAccessTokensMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalAccessTokensRepository.findAll().size();
        // set the field null
        personalAccessTokens.setName(null);

        // Create the PersonalAccessTokens, which fails.
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        restPersonalAccessTokensMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalAccessTokensRepository.findAll().size();
        // set the field null
        personalAccessTokens.setToken(null);

        // Create the PersonalAccessTokens, which fails.
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        restPersonalAccessTokensMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokens() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList
        restPersonalAccessTokensMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalAccessTokens.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenableType").value(hasItem(DEFAULT_TOKENABLE_TYPE)))
            .andExpect(jsonPath("$.[*].tokenableId").value(hasItem(DEFAULT_TOKENABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].abilities").value(hasItem(DEFAULT_ABILITIES)))
            .andExpect(jsonPath("$.[*].lastUsedAt").value(hasItem(DEFAULT_LAST_USED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getPersonalAccessTokens() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get the personalAccessTokens
        restPersonalAccessTokensMockMvc
            .perform(get(ENTITY_API_URL_ID, personalAccessTokens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalAccessTokens.getId().intValue()))
            .andExpect(jsonPath("$.tokenableType").value(DEFAULT_TOKENABLE_TYPE))
            .andExpect(jsonPath("$.tokenableId").value(DEFAULT_TOKENABLE_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.abilities").value(DEFAULT_ABILITIES))
            .andExpect(jsonPath("$.lastUsedAt").value(DEFAULT_LAST_USED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getPersonalAccessTokensByIdFiltering() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        Long id = personalAccessTokens.getId();

        defaultPersonalAccessTokensShouldBeFound("id.equals=" + id);
        defaultPersonalAccessTokensShouldNotBeFound("id.notEquals=" + id);

        defaultPersonalAccessTokensShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonalAccessTokensShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonalAccessTokensShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonalAccessTokensShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableType equals to DEFAULT_TOKENABLE_TYPE
        defaultPersonalAccessTokensShouldBeFound("tokenableType.equals=" + DEFAULT_TOKENABLE_TYPE);

        // Get all the personalAccessTokensList where tokenableType equals to UPDATED_TOKENABLE_TYPE
        defaultPersonalAccessTokensShouldNotBeFound("tokenableType.equals=" + UPDATED_TOKENABLE_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableTypeIsInShouldWork() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableType in DEFAULT_TOKENABLE_TYPE or UPDATED_TOKENABLE_TYPE
        defaultPersonalAccessTokensShouldBeFound("tokenableType.in=" + DEFAULT_TOKENABLE_TYPE + "," + UPDATED_TOKENABLE_TYPE);

        // Get all the personalAccessTokensList where tokenableType equals to UPDATED_TOKENABLE_TYPE
        defaultPersonalAccessTokensShouldNotBeFound("tokenableType.in=" + UPDATED_TOKENABLE_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableType is not null
        defaultPersonalAccessTokensShouldBeFound("tokenableType.specified=true");

        // Get all the personalAccessTokensList where tokenableType is null
        defaultPersonalAccessTokensShouldNotBeFound("tokenableType.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableTypeContainsSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableType contains DEFAULT_TOKENABLE_TYPE
        defaultPersonalAccessTokensShouldBeFound("tokenableType.contains=" + DEFAULT_TOKENABLE_TYPE);

        // Get all the personalAccessTokensList where tokenableType contains UPDATED_TOKENABLE_TYPE
        defaultPersonalAccessTokensShouldNotBeFound("tokenableType.contains=" + UPDATED_TOKENABLE_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableTypeNotContainsSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableType does not contain DEFAULT_TOKENABLE_TYPE
        defaultPersonalAccessTokensShouldNotBeFound("tokenableType.doesNotContain=" + DEFAULT_TOKENABLE_TYPE);

        // Get all the personalAccessTokensList where tokenableType does not contain UPDATED_TOKENABLE_TYPE
        defaultPersonalAccessTokensShouldBeFound("tokenableType.doesNotContain=" + UPDATED_TOKENABLE_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableIdIsEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableId equals to DEFAULT_TOKENABLE_ID
        defaultPersonalAccessTokensShouldBeFound("tokenableId.equals=" + DEFAULT_TOKENABLE_ID);

        // Get all the personalAccessTokensList where tokenableId equals to UPDATED_TOKENABLE_ID
        defaultPersonalAccessTokensShouldNotBeFound("tokenableId.equals=" + UPDATED_TOKENABLE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableIdIsInShouldWork() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableId in DEFAULT_TOKENABLE_ID or UPDATED_TOKENABLE_ID
        defaultPersonalAccessTokensShouldBeFound("tokenableId.in=" + DEFAULT_TOKENABLE_ID + "," + UPDATED_TOKENABLE_ID);

        // Get all the personalAccessTokensList where tokenableId equals to UPDATED_TOKENABLE_ID
        defaultPersonalAccessTokensShouldNotBeFound("tokenableId.in=" + UPDATED_TOKENABLE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableId is not null
        defaultPersonalAccessTokensShouldBeFound("tokenableId.specified=true");

        // Get all the personalAccessTokensList where tokenableId is null
        defaultPersonalAccessTokensShouldNotBeFound("tokenableId.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableId is greater than or equal to DEFAULT_TOKENABLE_ID
        defaultPersonalAccessTokensShouldBeFound("tokenableId.greaterThanOrEqual=" + DEFAULT_TOKENABLE_ID);

        // Get all the personalAccessTokensList where tokenableId is greater than or equal to UPDATED_TOKENABLE_ID
        defaultPersonalAccessTokensShouldNotBeFound("tokenableId.greaterThanOrEqual=" + UPDATED_TOKENABLE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableId is less than or equal to DEFAULT_TOKENABLE_ID
        defaultPersonalAccessTokensShouldBeFound("tokenableId.lessThanOrEqual=" + DEFAULT_TOKENABLE_ID);

        // Get all the personalAccessTokensList where tokenableId is less than or equal to SMALLER_TOKENABLE_ID
        defaultPersonalAccessTokensShouldNotBeFound("tokenableId.lessThanOrEqual=" + SMALLER_TOKENABLE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableIdIsLessThanSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableId is less than DEFAULT_TOKENABLE_ID
        defaultPersonalAccessTokensShouldNotBeFound("tokenableId.lessThan=" + DEFAULT_TOKENABLE_ID);

        // Get all the personalAccessTokensList where tokenableId is less than UPDATED_TOKENABLE_ID
        defaultPersonalAccessTokensShouldBeFound("tokenableId.lessThan=" + UPDATED_TOKENABLE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenableIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where tokenableId is greater than DEFAULT_TOKENABLE_ID
        defaultPersonalAccessTokensShouldNotBeFound("tokenableId.greaterThan=" + DEFAULT_TOKENABLE_ID);

        // Get all the personalAccessTokensList where tokenableId is greater than SMALLER_TOKENABLE_ID
        defaultPersonalAccessTokensShouldBeFound("tokenableId.greaterThan=" + SMALLER_TOKENABLE_ID);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where name equals to DEFAULT_NAME
        defaultPersonalAccessTokensShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the personalAccessTokensList where name equals to UPDATED_NAME
        defaultPersonalAccessTokensShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByNameIsInShouldWork() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPersonalAccessTokensShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the personalAccessTokensList where name equals to UPDATED_NAME
        defaultPersonalAccessTokensShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where name is not null
        defaultPersonalAccessTokensShouldBeFound("name.specified=true");

        // Get all the personalAccessTokensList where name is null
        defaultPersonalAccessTokensShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByNameContainsSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where name contains DEFAULT_NAME
        defaultPersonalAccessTokensShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the personalAccessTokensList where name contains UPDATED_NAME
        defaultPersonalAccessTokensShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByNameNotContainsSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where name does not contain DEFAULT_NAME
        defaultPersonalAccessTokensShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the personalAccessTokensList where name does not contain UPDATED_NAME
        defaultPersonalAccessTokensShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where token equals to DEFAULT_TOKEN
        defaultPersonalAccessTokensShouldBeFound("token.equals=" + DEFAULT_TOKEN);

        // Get all the personalAccessTokensList where token equals to UPDATED_TOKEN
        defaultPersonalAccessTokensShouldNotBeFound("token.equals=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenIsInShouldWork() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where token in DEFAULT_TOKEN or UPDATED_TOKEN
        defaultPersonalAccessTokensShouldBeFound("token.in=" + DEFAULT_TOKEN + "," + UPDATED_TOKEN);

        // Get all the personalAccessTokensList where token equals to UPDATED_TOKEN
        defaultPersonalAccessTokensShouldNotBeFound("token.in=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where token is not null
        defaultPersonalAccessTokensShouldBeFound("token.specified=true");

        // Get all the personalAccessTokensList where token is null
        defaultPersonalAccessTokensShouldNotBeFound("token.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenContainsSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where token contains DEFAULT_TOKEN
        defaultPersonalAccessTokensShouldBeFound("token.contains=" + DEFAULT_TOKEN);

        // Get all the personalAccessTokensList where token contains UPDATED_TOKEN
        defaultPersonalAccessTokensShouldNotBeFound("token.contains=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByTokenNotContainsSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where token does not contain DEFAULT_TOKEN
        defaultPersonalAccessTokensShouldNotBeFound("token.doesNotContain=" + DEFAULT_TOKEN);

        // Get all the personalAccessTokensList where token does not contain UPDATED_TOKEN
        defaultPersonalAccessTokensShouldBeFound("token.doesNotContain=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByAbilitiesIsEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where abilities equals to DEFAULT_ABILITIES
        defaultPersonalAccessTokensShouldBeFound("abilities.equals=" + DEFAULT_ABILITIES);

        // Get all the personalAccessTokensList where abilities equals to UPDATED_ABILITIES
        defaultPersonalAccessTokensShouldNotBeFound("abilities.equals=" + UPDATED_ABILITIES);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByAbilitiesIsInShouldWork() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where abilities in DEFAULT_ABILITIES or UPDATED_ABILITIES
        defaultPersonalAccessTokensShouldBeFound("abilities.in=" + DEFAULT_ABILITIES + "," + UPDATED_ABILITIES);

        // Get all the personalAccessTokensList where abilities equals to UPDATED_ABILITIES
        defaultPersonalAccessTokensShouldNotBeFound("abilities.in=" + UPDATED_ABILITIES);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByAbilitiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where abilities is not null
        defaultPersonalAccessTokensShouldBeFound("abilities.specified=true");

        // Get all the personalAccessTokensList where abilities is null
        defaultPersonalAccessTokensShouldNotBeFound("abilities.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByAbilitiesContainsSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where abilities contains DEFAULT_ABILITIES
        defaultPersonalAccessTokensShouldBeFound("abilities.contains=" + DEFAULT_ABILITIES);

        // Get all the personalAccessTokensList where abilities contains UPDATED_ABILITIES
        defaultPersonalAccessTokensShouldNotBeFound("abilities.contains=" + UPDATED_ABILITIES);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByAbilitiesNotContainsSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where abilities does not contain DEFAULT_ABILITIES
        defaultPersonalAccessTokensShouldNotBeFound("abilities.doesNotContain=" + DEFAULT_ABILITIES);

        // Get all the personalAccessTokensList where abilities does not contain UPDATED_ABILITIES
        defaultPersonalAccessTokensShouldBeFound("abilities.doesNotContain=" + UPDATED_ABILITIES);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByLastUsedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where lastUsedAt equals to DEFAULT_LAST_USED_AT
        defaultPersonalAccessTokensShouldBeFound("lastUsedAt.equals=" + DEFAULT_LAST_USED_AT);

        // Get all the personalAccessTokensList where lastUsedAt equals to UPDATED_LAST_USED_AT
        defaultPersonalAccessTokensShouldNotBeFound("lastUsedAt.equals=" + UPDATED_LAST_USED_AT);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByLastUsedAtIsInShouldWork() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where lastUsedAt in DEFAULT_LAST_USED_AT or UPDATED_LAST_USED_AT
        defaultPersonalAccessTokensShouldBeFound("lastUsedAt.in=" + DEFAULT_LAST_USED_AT + "," + UPDATED_LAST_USED_AT);

        // Get all the personalAccessTokensList where lastUsedAt equals to UPDATED_LAST_USED_AT
        defaultPersonalAccessTokensShouldNotBeFound("lastUsedAt.in=" + UPDATED_LAST_USED_AT);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByLastUsedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where lastUsedAt is not null
        defaultPersonalAccessTokensShouldBeFound("lastUsedAt.specified=true");

        // Get all the personalAccessTokensList where lastUsedAt is null
        defaultPersonalAccessTokensShouldNotBeFound("lastUsedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where createdAt equals to DEFAULT_CREATED_AT
        defaultPersonalAccessTokensShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the personalAccessTokensList where createdAt equals to UPDATED_CREATED_AT
        defaultPersonalAccessTokensShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPersonalAccessTokensShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the personalAccessTokensList where createdAt equals to UPDATED_CREATED_AT
        defaultPersonalAccessTokensShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where createdAt is not null
        defaultPersonalAccessTokensShouldBeFound("createdAt.specified=true");

        // Get all the personalAccessTokensList where createdAt is null
        defaultPersonalAccessTokensShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultPersonalAccessTokensShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the personalAccessTokensList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPersonalAccessTokensShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultPersonalAccessTokensShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the personalAccessTokensList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPersonalAccessTokensShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllPersonalAccessTokensByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        // Get all the personalAccessTokensList where updatedAt is not null
        defaultPersonalAccessTokensShouldBeFound("updatedAt.specified=true");

        // Get all the personalAccessTokensList where updatedAt is null
        defaultPersonalAccessTokensShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonalAccessTokensShouldBeFound(String filter) throws Exception {
        restPersonalAccessTokensMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalAccessTokens.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenableType").value(hasItem(DEFAULT_TOKENABLE_TYPE)))
            .andExpect(jsonPath("$.[*].tokenableId").value(hasItem(DEFAULT_TOKENABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].abilities").value(hasItem(DEFAULT_ABILITIES)))
            .andExpect(jsonPath("$.[*].lastUsedAt").value(hasItem(DEFAULT_LAST_USED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restPersonalAccessTokensMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonalAccessTokensShouldNotBeFound(String filter) throws Exception {
        restPersonalAccessTokensMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonalAccessTokensMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonalAccessTokens() throws Exception {
        // Get the personalAccessTokens
        restPersonalAccessTokensMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalAccessTokens() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();

        // Update the personalAccessTokens
        PersonalAccessTokens updatedPersonalAccessTokens = personalAccessTokensRepository
            .findById(personalAccessTokens.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPersonalAccessTokens are not directly saved in db
        em.detach(updatedPersonalAccessTokens);
        updatedPersonalAccessTokens
            .tokenableType(UPDATED_TOKENABLE_TYPE)
            .tokenableId(UPDATED_TOKENABLE_ID)
            .name(UPDATED_NAME)
            .token(UPDATED_TOKEN)
            .abilities(UPDATED_ABILITIES)
            .lastUsedAt(UPDATED_LAST_USED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(updatedPersonalAccessTokens);

        restPersonalAccessTokensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalAccessTokensDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isOk());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
        PersonalAccessTokens testPersonalAccessTokens = personalAccessTokensList.get(personalAccessTokensList.size() - 1);
        assertThat(testPersonalAccessTokens.getTokenableType()).isEqualTo(UPDATED_TOKENABLE_TYPE);
        assertThat(testPersonalAccessTokens.getTokenableId()).isEqualTo(UPDATED_TOKENABLE_ID);
        assertThat(testPersonalAccessTokens.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonalAccessTokens.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testPersonalAccessTokens.getAbilities()).isEqualTo(UPDATED_ABILITIES);
        assertThat(testPersonalAccessTokens.getLastUsedAt()).isEqualTo(UPDATED_LAST_USED_AT);
        assertThat(testPersonalAccessTokens.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPersonalAccessTokens.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingPersonalAccessTokens() throws Exception {
        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();
        personalAccessTokens.setId(longCount.incrementAndGet());

        // Create the PersonalAccessTokens
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalAccessTokensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalAccessTokensDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalAccessTokens() throws Exception {
        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();
        personalAccessTokens.setId(longCount.incrementAndGet());

        // Create the PersonalAccessTokens
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalAccessTokensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalAccessTokens() throws Exception {
        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();
        personalAccessTokens.setId(longCount.incrementAndGet());

        // Create the PersonalAccessTokens
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalAccessTokensMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalAccessTokensWithPatch() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();

        // Update the personalAccessTokens using partial update
        PersonalAccessTokens partialUpdatedPersonalAccessTokens = new PersonalAccessTokens();
        partialUpdatedPersonalAccessTokens.setId(personalAccessTokens.getId());

        partialUpdatedPersonalAccessTokens
            .tokenableId(UPDATED_TOKENABLE_ID)
            .name(UPDATED_NAME)
            .abilities(UPDATED_ABILITIES)
            .lastUsedAt(UPDATED_LAST_USED_AT);

        restPersonalAccessTokensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalAccessTokens.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalAccessTokens))
            )
            .andExpect(status().isOk());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
        PersonalAccessTokens testPersonalAccessTokens = personalAccessTokensList.get(personalAccessTokensList.size() - 1);
        assertThat(testPersonalAccessTokens.getTokenableType()).isEqualTo(DEFAULT_TOKENABLE_TYPE);
        assertThat(testPersonalAccessTokens.getTokenableId()).isEqualTo(UPDATED_TOKENABLE_ID);
        assertThat(testPersonalAccessTokens.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonalAccessTokens.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testPersonalAccessTokens.getAbilities()).isEqualTo(UPDATED_ABILITIES);
        assertThat(testPersonalAccessTokens.getLastUsedAt()).isEqualTo(UPDATED_LAST_USED_AT);
        assertThat(testPersonalAccessTokens.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPersonalAccessTokens.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdatePersonalAccessTokensWithPatch() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();

        // Update the personalAccessTokens using partial update
        PersonalAccessTokens partialUpdatedPersonalAccessTokens = new PersonalAccessTokens();
        partialUpdatedPersonalAccessTokens.setId(personalAccessTokens.getId());

        partialUpdatedPersonalAccessTokens
            .tokenableType(UPDATED_TOKENABLE_TYPE)
            .tokenableId(UPDATED_TOKENABLE_ID)
            .name(UPDATED_NAME)
            .token(UPDATED_TOKEN)
            .abilities(UPDATED_ABILITIES)
            .lastUsedAt(UPDATED_LAST_USED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restPersonalAccessTokensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalAccessTokens.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalAccessTokens))
            )
            .andExpect(status().isOk());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
        PersonalAccessTokens testPersonalAccessTokens = personalAccessTokensList.get(personalAccessTokensList.size() - 1);
        assertThat(testPersonalAccessTokens.getTokenableType()).isEqualTo(UPDATED_TOKENABLE_TYPE);
        assertThat(testPersonalAccessTokens.getTokenableId()).isEqualTo(UPDATED_TOKENABLE_ID);
        assertThat(testPersonalAccessTokens.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonalAccessTokens.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testPersonalAccessTokens.getAbilities()).isEqualTo(UPDATED_ABILITIES);
        assertThat(testPersonalAccessTokens.getLastUsedAt()).isEqualTo(UPDATED_LAST_USED_AT);
        assertThat(testPersonalAccessTokens.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPersonalAccessTokens.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalAccessTokens() throws Exception {
        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();
        personalAccessTokens.setId(longCount.incrementAndGet());

        // Create the PersonalAccessTokens
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalAccessTokensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalAccessTokensDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalAccessTokens() throws Exception {
        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();
        personalAccessTokens.setId(longCount.incrementAndGet());

        // Create the PersonalAccessTokens
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalAccessTokensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalAccessTokens() throws Exception {
        int databaseSizeBeforeUpdate = personalAccessTokensRepository.findAll().size();
        personalAccessTokens.setId(longCount.incrementAndGet());

        // Create the PersonalAccessTokens
        PersonalAccessTokensDTO personalAccessTokensDTO = personalAccessTokensMapper.toDto(personalAccessTokens);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalAccessTokensMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalAccessTokensDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalAccessTokens in the database
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalAccessTokens() throws Exception {
        // Initialize the database
        personalAccessTokensRepository.saveAndFlush(personalAccessTokens);

        int databaseSizeBeforeDelete = personalAccessTokensRepository.findAll().size();

        // Delete the personalAccessTokens
        restPersonalAccessTokensMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalAccessTokens.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalAccessTokens> personalAccessTokensList = personalAccessTokensRepository.findAll();
        assertThat(personalAccessTokensList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
