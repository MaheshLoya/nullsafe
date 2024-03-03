package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.UserNotification;
import com.nullsafe.daily.repository.UserNotificationRepository;
import com.nullsafe.daily.service.dto.UserNotificationDTO;
import com.nullsafe.daily.service.mapper.UserNotificationMapper;
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
 * Integration tests for the {@link UserNotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserNotificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private UserNotificationMapper userNotificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserNotificationMockMvc;

    private UserNotification userNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserNotification createEntity(EntityManager em) {
        UserNotification userNotification = new UserNotification()
            .title(DEFAULT_TITLE)
            .body(DEFAULT_BODY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return userNotification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserNotification createUpdatedEntity(EntityManager em) {
        UserNotification userNotification = new UserNotification()
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return userNotification;
    }

    @BeforeEach
    public void initTest() {
        userNotification = createEntity(em);
    }

    @Test
    @Transactional
    void createUserNotification() throws Exception {
        int databaseSizeBeforeCreate = userNotificationRepository.findAll().size();
        // Create the UserNotification
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);
        restUserNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeCreate + 1);
        UserNotification testUserNotification = userNotificationList.get(userNotificationList.size() - 1);
        assertThat(testUserNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testUserNotification.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testUserNotification.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserNotification.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createUserNotificationWithExistingId() throws Exception {
        // Create the UserNotification with an existing ID
        userNotification.setId(1L);
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        int databaseSizeBeforeCreate = userNotificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userNotificationRepository.findAll().size();
        // set the field null
        userNotification.setTitle(null);

        // Create the UserNotification, which fails.
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        restUserNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = userNotificationRepository.findAll().size();
        // set the field null
        userNotification.setBody(null);

        // Create the UserNotification, which fails.
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        restUserNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserNotifications() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList
        restUserNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getUserNotification() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get the userNotification
        restUserNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, userNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userNotification.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getUserNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        Long id = userNotification.getId();

        defaultUserNotificationShouldBeFound("id.equals=" + id);
        defaultUserNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultUserNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultUserNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserNotificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where title equals to DEFAULT_TITLE
        defaultUserNotificationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the userNotificationList where title equals to UPDATED_TITLE
        defaultUserNotificationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultUserNotificationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the userNotificationList where title equals to UPDATED_TITLE
        defaultUserNotificationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where title is not null
        defaultUserNotificationShouldBeFound("title.specified=true");

        // Get all the userNotificationList where title is null
        defaultUserNotificationShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllUserNotificationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where title contains DEFAULT_TITLE
        defaultUserNotificationShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the userNotificationList where title contains UPDATED_TITLE
        defaultUserNotificationShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where title does not contain DEFAULT_TITLE
        defaultUserNotificationShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the userNotificationList where title does not contain UPDATED_TITLE
        defaultUserNotificationShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where body equals to DEFAULT_BODY
        defaultUserNotificationShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the userNotificationList where body equals to UPDATED_BODY
        defaultUserNotificationShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where body in DEFAULT_BODY or UPDATED_BODY
        defaultUserNotificationShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the userNotificationList where body equals to UPDATED_BODY
        defaultUserNotificationShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where body is not null
        defaultUserNotificationShouldBeFound("body.specified=true");

        // Get all the userNotificationList where body is null
        defaultUserNotificationShouldNotBeFound("body.specified=false");
    }

    @Test
    @Transactional
    void getAllUserNotificationsByBodyContainsSomething() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where body contains DEFAULT_BODY
        defaultUserNotificationShouldBeFound("body.contains=" + DEFAULT_BODY);

        // Get all the userNotificationList where body contains UPDATED_BODY
        defaultUserNotificationShouldNotBeFound("body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where body does not contain DEFAULT_BODY
        defaultUserNotificationShouldNotBeFound("body.doesNotContain=" + DEFAULT_BODY);

        // Get all the userNotificationList where body does not contain UPDATED_BODY
        defaultUserNotificationShouldBeFound("body.doesNotContain=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where createdAt equals to DEFAULT_CREATED_AT
        defaultUserNotificationShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the userNotificationList where createdAt equals to UPDATED_CREATED_AT
        defaultUserNotificationShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUserNotificationShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the userNotificationList where createdAt equals to UPDATED_CREATED_AT
        defaultUserNotificationShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where createdAt is not null
        defaultUserNotificationShouldBeFound("createdAt.specified=true");

        // Get all the userNotificationList where createdAt is null
        defaultUserNotificationShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserNotificationsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUserNotificationShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the userNotificationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserNotificationShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUserNotificationShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the userNotificationList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserNotificationShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserNotificationsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        // Get all the userNotificationList where updatedAt is not null
        defaultUserNotificationShouldBeFound("updatedAt.specified=true");

        // Get all the userNotificationList where updatedAt is null
        defaultUserNotificationShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserNotificationShouldBeFound(String filter) throws Exception {
        restUserNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restUserNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserNotificationShouldNotBeFound(String filter) throws Exception {
        restUserNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserNotification() throws Exception {
        // Get the userNotification
        restUserNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserNotification() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();

        // Update the userNotification
        UserNotification updatedUserNotification = userNotificationRepository.findById(userNotification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserNotification are not directly saved in db
        em.detach(updatedUserNotification);
        updatedUserNotification.title(UPDATED_TITLE).body(UPDATED_BODY).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(updatedUserNotification);

        restUserNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
        UserNotification testUserNotification = userNotificationList.get(userNotificationList.size() - 1);
        assertThat(testUserNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserNotification.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testUserNotification.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserNotification.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUserNotification() throws Exception {
        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();
        userNotification.setId(longCount.incrementAndGet());

        // Create the UserNotification
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserNotification() throws Exception {
        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();
        userNotification.setId(longCount.incrementAndGet());

        // Create the UserNotification
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserNotification() throws Exception {
        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();
        userNotification.setId(longCount.incrementAndGet());

        // Create the UserNotification
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserNotificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserNotificationWithPatch() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();

        // Update the userNotification using partial update
        UserNotification partialUpdatedUserNotification = new UserNotification();
        partialUpdatedUserNotification.setId(userNotification.getId());

        partialUpdatedUserNotification.body(UPDATED_BODY);

        restUserNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserNotification))
            )
            .andExpect(status().isOk());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
        UserNotification testUserNotification = userNotificationList.get(userNotificationList.size() - 1);
        assertThat(testUserNotification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testUserNotification.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testUserNotification.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserNotification.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUserNotificationWithPatch() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();

        // Update the userNotification using partial update
        UserNotification partialUpdatedUserNotification = new UserNotification();
        partialUpdatedUserNotification.setId(userNotification.getId());

        partialUpdatedUserNotification.title(UPDATED_TITLE).body(UPDATED_BODY).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restUserNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserNotification))
            )
            .andExpect(status().isOk());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
        UserNotification testUserNotification = userNotificationList.get(userNotificationList.size() - 1);
        assertThat(testUserNotification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserNotification.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testUserNotification.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserNotification.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUserNotification() throws Exception {
        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();
        userNotification.setId(longCount.incrementAndGet());

        // Create the UserNotification
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userNotificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserNotification() throws Exception {
        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();
        userNotification.setId(longCount.incrementAndGet());

        // Create the UserNotification
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserNotification() throws Exception {
        int databaseSizeBeforeUpdate = userNotificationRepository.findAll().size();
        userNotification.setId(longCount.incrementAndGet());

        // Create the UserNotification
        UserNotificationDTO userNotificationDTO = userNotificationMapper.toDto(userNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserNotification in the database
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserNotification() throws Exception {
        // Initialize the database
        userNotificationRepository.saveAndFlush(userNotification);

        int databaseSizeBeforeDelete = userNotificationRepository.findAll().size();

        // Delete the userNotification
        restUserNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserNotification> userNotificationList = userNotificationRepository.findAll();
        assertThat(userNotificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
