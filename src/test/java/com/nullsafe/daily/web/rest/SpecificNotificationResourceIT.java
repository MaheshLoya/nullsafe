package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.SpecificNotification;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.SpecificNotificationRepository;
import com.nullsafe.daily.service.SpecificNotificationService;
import com.nullsafe.daily.service.dto.SpecificNotificationDTO;
import com.nullsafe.daily.service.mapper.SpecificNotificationMapper;
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
 * Integration tests for the {@link SpecificNotificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SpecificNotificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/specific-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecificNotificationRepository specificNotificationRepository;

    @Mock
    private SpecificNotificationRepository specificNotificationRepositoryMock;

    @Autowired
    private SpecificNotificationMapper specificNotificationMapper;

    @Mock
    private SpecificNotificationService specificNotificationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecificNotificationMockMvc;

    private SpecificNotification specificNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecificNotification createEntity(EntityManager em) {
        SpecificNotification specificNotification = new SpecificNotification()
            .title(DEFAULT_TITLE)
            .body(DEFAULT_BODY)
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
        specificNotification.setUser(users);
        return specificNotification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecificNotification createUpdatedEntity(EntityManager em) {
        SpecificNotification specificNotification = new SpecificNotification()
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
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
        specificNotification.setUser(users);
        return specificNotification;
    }

    @BeforeEach
    public void initTest() {
        specificNotification = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecificNotification() throws Exception {
        int databaseSizeBeforeCreate = specificNotificationRepository.findAll().size();
        // Create the SpecificNotification
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);
        restSpecificNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeCreate + 1);
        SpecificNotification testSpecificNotification = specificNotificationList.get(specificNotificationList.size() - 1);
        assertThat(testSpecificNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSpecificNotification.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testSpecificNotification.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSpecificNotification.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSpecificNotificationWithExistingId() throws Exception {
        // Create the SpecificNotification with an existing ID
        specificNotification.setId(1L);
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        int databaseSizeBeforeCreate = specificNotificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecificNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = specificNotificationRepository.findAll().size();
        // set the field null
        specificNotification.setTitle(null);

        // Create the SpecificNotification, which fails.
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        restSpecificNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = specificNotificationRepository.findAll().size();
        // set the field null
        specificNotification.setBody(null);

        // Create the SpecificNotification, which fails.
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        restSpecificNotificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecificNotifications() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList
        restSpecificNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specificNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecificNotificationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(specificNotificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecificNotificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(specificNotificationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpecificNotificationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(specificNotificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpecificNotificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(specificNotificationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSpecificNotification() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get the specificNotification
        restSpecificNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, specificNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specificNotification.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getSpecificNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        Long id = specificNotification.getId();

        defaultSpecificNotificationShouldBeFound("id.equals=" + id);
        defaultSpecificNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultSpecificNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSpecificNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultSpecificNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSpecificNotificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where title equals to DEFAULT_TITLE
        defaultSpecificNotificationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the specificNotificationList where title equals to UPDATED_TITLE
        defaultSpecificNotificationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSpecificNotificationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the specificNotificationList where title equals to UPDATED_TITLE
        defaultSpecificNotificationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where title is not null
        defaultSpecificNotificationShouldBeFound("title.specified=true");

        // Get all the specificNotificationList where title is null
        defaultSpecificNotificationShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where title contains DEFAULT_TITLE
        defaultSpecificNotificationShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the specificNotificationList where title contains UPDATED_TITLE
        defaultSpecificNotificationShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where title does not contain DEFAULT_TITLE
        defaultSpecificNotificationShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the specificNotificationList where title does not contain UPDATED_TITLE
        defaultSpecificNotificationShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where body equals to DEFAULT_BODY
        defaultSpecificNotificationShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the specificNotificationList where body equals to UPDATED_BODY
        defaultSpecificNotificationShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where body in DEFAULT_BODY or UPDATED_BODY
        defaultSpecificNotificationShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the specificNotificationList where body equals to UPDATED_BODY
        defaultSpecificNotificationShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where body is not null
        defaultSpecificNotificationShouldBeFound("body.specified=true");

        // Get all the specificNotificationList where body is null
        defaultSpecificNotificationShouldNotBeFound("body.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByBodyContainsSomething() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where body contains DEFAULT_BODY
        defaultSpecificNotificationShouldBeFound("body.contains=" + DEFAULT_BODY);

        // Get all the specificNotificationList where body contains UPDATED_BODY
        defaultSpecificNotificationShouldNotBeFound("body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where body does not contain DEFAULT_BODY
        defaultSpecificNotificationShouldNotBeFound("body.doesNotContain=" + DEFAULT_BODY);

        // Get all the specificNotificationList where body does not contain UPDATED_BODY
        defaultSpecificNotificationShouldBeFound("body.doesNotContain=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where createdAt equals to DEFAULT_CREATED_AT
        defaultSpecificNotificationShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the specificNotificationList where createdAt equals to UPDATED_CREATED_AT
        defaultSpecificNotificationShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSpecificNotificationShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the specificNotificationList where createdAt equals to UPDATED_CREATED_AT
        defaultSpecificNotificationShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where createdAt is not null
        defaultSpecificNotificationShouldBeFound("createdAt.specified=true");

        // Get all the specificNotificationList where createdAt is null
        defaultSpecificNotificationShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSpecificNotificationShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the specificNotificationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSpecificNotificationShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSpecificNotificationShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the specificNotificationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSpecificNotificationShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        // Get all the specificNotificationList where updatedAt is not null
        defaultSpecificNotificationShouldBeFound("updatedAt.specified=true");

        // Get all the specificNotificationList where updatedAt is null
        defaultSpecificNotificationShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecificNotificationsByUserIsEqualToSomething() throws Exception {
        Users user;
        if (TestUtil.findAll(em, Users.class).isEmpty()) {
            specificNotificationRepository.saveAndFlush(specificNotification);
            user = UsersResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, Users.class).get(0);
        }
        em.persist(user);
        em.flush();
        specificNotification.setUser(user);
        specificNotificationRepository.saveAndFlush(specificNotification);
        Long userId = user.getId();
        // Get all the specificNotificationList where user equals to userId
        defaultSpecificNotificationShouldBeFound("userId.equals=" + userId);

        // Get all the specificNotificationList where user equals to (userId + 1)
        defaultSpecificNotificationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpecificNotificationShouldBeFound(String filter) throws Exception {
        restSpecificNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specificNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restSpecificNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpecificNotificationShouldNotBeFound(String filter) throws Exception {
        restSpecificNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpecificNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSpecificNotification() throws Exception {
        // Get the specificNotification
        restSpecificNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecificNotification() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();

        // Update the specificNotification
        SpecificNotification updatedSpecificNotification = specificNotificationRepository
            .findById(specificNotification.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSpecificNotification are not directly saved in db
        em.detach(updatedSpecificNotification);
        updatedSpecificNotification.title(UPDATED_TITLE).body(UPDATED_BODY).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(updatedSpecificNotification);

        restSpecificNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specificNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
        SpecificNotification testSpecificNotification = specificNotificationList.get(specificNotificationList.size() - 1);
        assertThat(testSpecificNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpecificNotification.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testSpecificNotification.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSpecificNotification.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSpecificNotification() throws Exception {
        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();
        specificNotification.setId(longCount.incrementAndGet());

        // Create the SpecificNotification
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecificNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specificNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecificNotification() throws Exception {
        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();
        specificNotification.setId(longCount.incrementAndGet());

        // Create the SpecificNotification
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecificNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecificNotification() throws Exception {
        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();
        specificNotification.setId(longCount.incrementAndGet());

        // Create the SpecificNotification
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecificNotificationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecificNotificationWithPatch() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();

        // Update the specificNotification using partial update
        SpecificNotification partialUpdatedSpecificNotification = new SpecificNotification();
        partialUpdatedSpecificNotification.setId(specificNotification.getId());

        partialUpdatedSpecificNotification.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT);

        restSpecificNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecificNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecificNotification))
            )
            .andExpect(status().isOk());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
        SpecificNotification testSpecificNotification = specificNotificationList.get(specificNotificationList.size() - 1);
        assertThat(testSpecificNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpecificNotification.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testSpecificNotification.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSpecificNotification.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSpecificNotificationWithPatch() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();

        // Update the specificNotification using partial update
        SpecificNotification partialUpdatedSpecificNotification = new SpecificNotification();
        partialUpdatedSpecificNotification.setId(specificNotification.getId());

        partialUpdatedSpecificNotification
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSpecificNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecificNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecificNotification))
            )
            .andExpect(status().isOk());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
        SpecificNotification testSpecificNotification = specificNotificationList.get(specificNotificationList.size() - 1);
        assertThat(testSpecificNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpecificNotification.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testSpecificNotification.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSpecificNotification.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSpecificNotification() throws Exception {
        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();
        specificNotification.setId(longCount.incrementAndGet());

        // Create the SpecificNotification
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecificNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specificNotificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecificNotification() throws Exception {
        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();
        specificNotification.setId(longCount.incrementAndGet());

        // Create the SpecificNotification
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecificNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecificNotification() throws Exception {
        int databaseSizeBeforeUpdate = specificNotificationRepository.findAll().size();
        specificNotification.setId(longCount.incrementAndGet());

        // Create the SpecificNotification
        SpecificNotificationDTO specificNotificationDTO = specificNotificationMapper.toDto(specificNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecificNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specificNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecificNotification in the database
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecificNotification() throws Exception {
        // Initialize the database
        specificNotificationRepository.saveAndFlush(specificNotification);

        int databaseSizeBeforeDelete = specificNotificationRepository.findAll().size();

        // Delete the specificNotification
        restSpecificNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, specificNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecificNotification> specificNotificationList = specificNotificationRepository.findAll();
        assertThat(specificNotificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
