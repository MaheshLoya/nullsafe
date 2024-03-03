package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.PasswordResets;
import com.nullsafe.daily.repository.PasswordResetsRepository;
import com.nullsafe.daily.service.dto.PasswordResetsDTO;
import com.nullsafe.daily.service.mapper.PasswordResetsMapper;
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
 * Integration tests for the {@link PasswordResetsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PasswordResetsResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/password-resets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PasswordResetsRepository passwordResetsRepository;

    @Autowired
    private PasswordResetsMapper passwordResetsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPasswordResetsMockMvc;

    private PasswordResets passwordResets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasswordResets createEntity(EntityManager em) {
        PasswordResets passwordResets = new PasswordResets().email(DEFAULT_EMAIL).token(DEFAULT_TOKEN).createdAt(DEFAULT_CREATED_AT);
        return passwordResets;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PasswordResets createUpdatedEntity(EntityManager em) {
        PasswordResets passwordResets = new PasswordResets().email(UPDATED_EMAIL).token(UPDATED_TOKEN).createdAt(UPDATED_CREATED_AT);
        return passwordResets;
    }

    @BeforeEach
    public void initTest() {
        passwordResets = createEntity(em);
    }

    @Test
    @Transactional
    void createPasswordResets() throws Exception {
        int databaseSizeBeforeCreate = passwordResetsRepository.findAll().size();
        // Create the PasswordResets
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);
        restPasswordResetsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeCreate + 1);
        PasswordResets testPasswordResets = passwordResetsList.get(passwordResetsList.size() - 1);
        assertThat(testPasswordResets.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPasswordResets.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testPasswordResets.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createPasswordResetsWithExistingId() throws Exception {
        // Create the PasswordResets with an existing ID
        passwordResets.setId(1L);
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        int databaseSizeBeforeCreate = passwordResetsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPasswordResetsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = passwordResetsRepository.findAll().size();
        // set the field null
        passwordResets.setEmail(null);

        // Create the PasswordResets, which fails.
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        restPasswordResetsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isBadRequest());

        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = passwordResetsRepository.findAll().size();
        // set the field null
        passwordResets.setToken(null);

        // Create the PasswordResets, which fails.
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        restPasswordResetsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isBadRequest());

        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPasswordResets() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList
        restPasswordResetsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passwordResets.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getPasswordResets() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get the passwordResets
        restPasswordResetsMockMvc
            .perform(get(ENTITY_API_URL_ID, passwordResets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passwordResets.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getPasswordResetsByIdFiltering() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        Long id = passwordResets.getId();

        defaultPasswordResetsShouldBeFound("id.equals=" + id);
        defaultPasswordResetsShouldNotBeFound("id.notEquals=" + id);

        defaultPasswordResetsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPasswordResetsShouldNotBeFound("id.greaterThan=" + id);

        defaultPasswordResetsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPasswordResetsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where email equals to DEFAULT_EMAIL
        defaultPasswordResetsShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the passwordResetsList where email equals to UPDATED_EMAIL
        defaultPasswordResetsShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPasswordResetsShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the passwordResetsList where email equals to UPDATED_EMAIL
        defaultPasswordResetsShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where email is not null
        defaultPasswordResetsShouldBeFound("email.specified=true");

        // Get all the passwordResetsList where email is null
        defaultPasswordResetsShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPasswordResetsByEmailContainsSomething() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where email contains DEFAULT_EMAIL
        defaultPasswordResetsShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the passwordResetsList where email contains UPDATED_EMAIL
        defaultPasswordResetsShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where email does not contain DEFAULT_EMAIL
        defaultPasswordResetsShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the passwordResetsList where email does not contain UPDATED_EMAIL
        defaultPasswordResetsShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where token equals to DEFAULT_TOKEN
        defaultPasswordResetsShouldBeFound("token.equals=" + DEFAULT_TOKEN);

        // Get all the passwordResetsList where token equals to UPDATED_TOKEN
        defaultPasswordResetsShouldNotBeFound("token.equals=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByTokenIsInShouldWork() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where token in DEFAULT_TOKEN or UPDATED_TOKEN
        defaultPasswordResetsShouldBeFound("token.in=" + DEFAULT_TOKEN + "," + UPDATED_TOKEN);

        // Get all the passwordResetsList where token equals to UPDATED_TOKEN
        defaultPasswordResetsShouldNotBeFound("token.in=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where token is not null
        defaultPasswordResetsShouldBeFound("token.specified=true");

        // Get all the passwordResetsList where token is null
        defaultPasswordResetsShouldNotBeFound("token.specified=false");
    }

    @Test
    @Transactional
    void getAllPasswordResetsByTokenContainsSomething() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where token contains DEFAULT_TOKEN
        defaultPasswordResetsShouldBeFound("token.contains=" + DEFAULT_TOKEN);

        // Get all the passwordResetsList where token contains UPDATED_TOKEN
        defaultPasswordResetsShouldNotBeFound("token.contains=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByTokenNotContainsSomething() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where token does not contain DEFAULT_TOKEN
        defaultPasswordResetsShouldNotBeFound("token.doesNotContain=" + DEFAULT_TOKEN);

        // Get all the passwordResetsList where token does not contain UPDATED_TOKEN
        defaultPasswordResetsShouldBeFound("token.doesNotContain=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where createdAt equals to DEFAULT_CREATED_AT
        defaultPasswordResetsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the passwordResetsList where createdAt equals to UPDATED_CREATED_AT
        defaultPasswordResetsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPasswordResetsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the passwordResetsList where createdAt equals to UPDATED_CREATED_AT
        defaultPasswordResetsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllPasswordResetsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        // Get all the passwordResetsList where createdAt is not null
        defaultPasswordResetsShouldBeFound("createdAt.specified=true");

        // Get all the passwordResetsList where createdAt is null
        defaultPasswordResetsShouldNotBeFound("createdAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPasswordResetsShouldBeFound(String filter) throws Exception {
        restPasswordResetsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passwordResets.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restPasswordResetsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPasswordResetsShouldNotBeFound(String filter) throws Exception {
        restPasswordResetsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPasswordResetsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPasswordResets() throws Exception {
        // Get the passwordResets
        restPasswordResetsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPasswordResets() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();

        // Update the passwordResets
        PasswordResets updatedPasswordResets = passwordResetsRepository.findById(passwordResets.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPasswordResets are not directly saved in db
        em.detach(updatedPasswordResets);
        updatedPasswordResets.email(UPDATED_EMAIL).token(UPDATED_TOKEN).createdAt(UPDATED_CREATED_AT);
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(updatedPasswordResets);

        restPasswordResetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passwordResetsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isOk());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
        PasswordResets testPasswordResets = passwordResetsList.get(passwordResetsList.size() - 1);
        assertThat(testPasswordResets.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPasswordResets.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testPasswordResets.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingPasswordResets() throws Exception {
        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();
        passwordResets.setId(longCount.incrementAndGet());

        // Create the PasswordResets
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasswordResetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passwordResetsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPasswordResets() throws Exception {
        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();
        passwordResets.setId(longCount.incrementAndGet());

        // Create the PasswordResets
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasswordResetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPasswordResets() throws Exception {
        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();
        passwordResets.setId(longCount.incrementAndGet());

        // Create the PasswordResets
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasswordResetsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePasswordResetsWithPatch() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();

        // Update the passwordResets using partial update
        PasswordResets partialUpdatedPasswordResets = new PasswordResets();
        partialUpdatedPasswordResets.setId(passwordResets.getId());

        partialUpdatedPasswordResets.token(UPDATED_TOKEN).createdAt(UPDATED_CREATED_AT);

        restPasswordResetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasswordResets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPasswordResets))
            )
            .andExpect(status().isOk());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
        PasswordResets testPasswordResets = passwordResetsList.get(passwordResetsList.size() - 1);
        assertThat(testPasswordResets.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPasswordResets.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testPasswordResets.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdatePasswordResetsWithPatch() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();

        // Update the passwordResets using partial update
        PasswordResets partialUpdatedPasswordResets = new PasswordResets();
        partialUpdatedPasswordResets.setId(passwordResets.getId());

        partialUpdatedPasswordResets.email(UPDATED_EMAIL).token(UPDATED_TOKEN).createdAt(UPDATED_CREATED_AT);

        restPasswordResetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasswordResets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPasswordResets))
            )
            .andExpect(status().isOk());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
        PasswordResets testPasswordResets = passwordResetsList.get(passwordResetsList.size() - 1);
        assertThat(testPasswordResets.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPasswordResets.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testPasswordResets.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingPasswordResets() throws Exception {
        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();
        passwordResets.setId(longCount.incrementAndGet());

        // Create the PasswordResets
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasswordResetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, passwordResetsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPasswordResets() throws Exception {
        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();
        passwordResets.setId(longCount.incrementAndGet());

        // Create the PasswordResets
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasswordResetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPasswordResets() throws Exception {
        int databaseSizeBeforeUpdate = passwordResetsRepository.findAll().size();
        passwordResets.setId(longCount.incrementAndGet());

        // Create the PasswordResets
        PasswordResetsDTO passwordResetsDTO = passwordResetsMapper.toDto(passwordResets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasswordResetsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passwordResetsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PasswordResets in the database
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePasswordResets() throws Exception {
        // Initialize the database
        passwordResetsRepository.saveAndFlush(passwordResets);

        int databaseSizeBeforeDelete = passwordResetsRepository.findAll().size();

        // Delete the passwordResets
        restPasswordResetsMockMvc
            .perform(delete(ENTITY_API_URL_ID, passwordResets.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PasswordResets> passwordResetsList = passwordResetsRepository.findAll();
        assertThat(passwordResetsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
