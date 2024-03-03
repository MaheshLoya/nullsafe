package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.AllowPincode;
import com.nullsafe.daily.repository.AllowPincodeRepository;
import com.nullsafe.daily.service.dto.AllowPincodeDTO;
import com.nullsafe.daily.service.mapper.AllowPincodeMapper;
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
 * Integration tests for the {@link AllowPincodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllowPincodeResourceIT {

    private static final Integer DEFAULT_PIN_CODE = 1;
    private static final Integer UPDATED_PIN_CODE = 2;
    private static final Integer SMALLER_PIN_CODE = 1 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/allow-pincodes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AllowPincodeRepository allowPincodeRepository;

    @Autowired
    private AllowPincodeMapper allowPincodeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllowPincodeMockMvc;

    private AllowPincode allowPincode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllowPincode createEntity(EntityManager em) {
        AllowPincode allowPincode = new AllowPincode()
            .pinCode(DEFAULT_PIN_CODE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return allowPincode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllowPincode createUpdatedEntity(EntityManager em) {
        AllowPincode allowPincode = new AllowPincode()
            .pinCode(UPDATED_PIN_CODE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return allowPincode;
    }

    @BeforeEach
    public void initTest() {
        allowPincode = createEntity(em);
    }

    @Test
    @Transactional
    void createAllowPincode() throws Exception {
        int databaseSizeBeforeCreate = allowPincodeRepository.findAll().size();
        // Create the AllowPincode
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);
        restAllowPincodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeCreate + 1);
        AllowPincode testAllowPincode = allowPincodeList.get(allowPincodeList.size() - 1);
        assertThat(testAllowPincode.getPinCode()).isEqualTo(DEFAULT_PIN_CODE);
        assertThat(testAllowPincode.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAllowPincode.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createAllowPincodeWithExistingId() throws Exception {
        // Create the AllowPincode with an existing ID
        allowPincode.setId(1L);
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);

        int databaseSizeBeforeCreate = allowPincodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllowPincodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPinCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = allowPincodeRepository.findAll().size();
        // set the field null
        allowPincode.setPinCode(null);

        // Create the AllowPincode, which fails.
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);

        restAllowPincodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAllowPincodes() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList
        restAllowPincodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allowPincode.getId().intValue())))
            .andExpect(jsonPath("$.[*].pinCode").value(hasItem(DEFAULT_PIN_CODE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAllowPincode() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get the allowPincode
        restAllowPincodeMockMvc
            .perform(get(ENTITY_API_URL_ID, allowPincode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allowPincode.getId().intValue()))
            .andExpect(jsonPath("$.pinCode").value(DEFAULT_PIN_CODE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getAllowPincodesByIdFiltering() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        Long id = allowPincode.getId();

        defaultAllowPincodeShouldBeFound("id.equals=" + id);
        defaultAllowPincodeShouldNotBeFound("id.notEquals=" + id);

        defaultAllowPincodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAllowPincodeShouldNotBeFound("id.greaterThan=" + id);

        defaultAllowPincodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAllowPincodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByPinCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where pinCode equals to DEFAULT_PIN_CODE
        defaultAllowPincodeShouldBeFound("pinCode.equals=" + DEFAULT_PIN_CODE);

        // Get all the allowPincodeList where pinCode equals to UPDATED_PIN_CODE
        defaultAllowPincodeShouldNotBeFound("pinCode.equals=" + UPDATED_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByPinCodeIsInShouldWork() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where pinCode in DEFAULT_PIN_CODE or UPDATED_PIN_CODE
        defaultAllowPincodeShouldBeFound("pinCode.in=" + DEFAULT_PIN_CODE + "," + UPDATED_PIN_CODE);

        // Get all the allowPincodeList where pinCode equals to UPDATED_PIN_CODE
        defaultAllowPincodeShouldNotBeFound("pinCode.in=" + UPDATED_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByPinCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where pinCode is not null
        defaultAllowPincodeShouldBeFound("pinCode.specified=true");

        // Get all the allowPincodeList where pinCode is null
        defaultAllowPincodeShouldNotBeFound("pinCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAllowPincodesByPinCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where pinCode is greater than or equal to DEFAULT_PIN_CODE
        defaultAllowPincodeShouldBeFound("pinCode.greaterThanOrEqual=" + DEFAULT_PIN_CODE);

        // Get all the allowPincodeList where pinCode is greater than or equal to UPDATED_PIN_CODE
        defaultAllowPincodeShouldNotBeFound("pinCode.greaterThanOrEqual=" + UPDATED_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByPinCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where pinCode is less than or equal to DEFAULT_PIN_CODE
        defaultAllowPincodeShouldBeFound("pinCode.lessThanOrEqual=" + DEFAULT_PIN_CODE);

        // Get all the allowPincodeList where pinCode is less than or equal to SMALLER_PIN_CODE
        defaultAllowPincodeShouldNotBeFound("pinCode.lessThanOrEqual=" + SMALLER_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByPinCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where pinCode is less than DEFAULT_PIN_CODE
        defaultAllowPincodeShouldNotBeFound("pinCode.lessThan=" + DEFAULT_PIN_CODE);

        // Get all the allowPincodeList where pinCode is less than UPDATED_PIN_CODE
        defaultAllowPincodeShouldBeFound("pinCode.lessThan=" + UPDATED_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByPinCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where pinCode is greater than DEFAULT_PIN_CODE
        defaultAllowPincodeShouldNotBeFound("pinCode.greaterThan=" + DEFAULT_PIN_CODE);

        // Get all the allowPincodeList where pinCode is greater than SMALLER_PIN_CODE
        defaultAllowPincodeShouldBeFound("pinCode.greaterThan=" + SMALLER_PIN_CODE);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where createdAt equals to DEFAULT_CREATED_AT
        defaultAllowPincodeShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the allowPincodeList where createdAt equals to UPDATED_CREATED_AT
        defaultAllowPincodeShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAllowPincodeShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the allowPincodeList where createdAt equals to UPDATED_CREATED_AT
        defaultAllowPincodeShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where createdAt is not null
        defaultAllowPincodeShouldBeFound("createdAt.specified=true");

        // Get all the allowPincodeList where createdAt is null
        defaultAllowPincodeShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAllowPincodesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultAllowPincodeShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the allowPincodeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAllowPincodeShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultAllowPincodeShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the allowPincodeList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAllowPincodeShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllAllowPincodesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        // Get all the allowPincodeList where updatedAt is not null
        defaultAllowPincodeShouldBeFound("updatedAt.specified=true");

        // Get all the allowPincodeList where updatedAt is null
        defaultAllowPincodeShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAllowPincodeShouldBeFound(String filter) throws Exception {
        restAllowPincodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allowPincode.getId().intValue())))
            .andExpect(jsonPath("$.[*].pinCode").value(hasItem(DEFAULT_PIN_CODE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restAllowPincodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAllowPincodeShouldNotBeFound(String filter) throws Exception {
        restAllowPincodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAllowPincodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAllowPincode() throws Exception {
        // Get the allowPincode
        restAllowPincodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAllowPincode() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();

        // Update the allowPincode
        AllowPincode updatedAllowPincode = allowPincodeRepository.findById(allowPincode.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAllowPincode are not directly saved in db
        em.detach(updatedAllowPincode);
        updatedAllowPincode.pinCode(UPDATED_PIN_CODE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(updatedAllowPincode);

        restAllowPincodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allowPincodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
        AllowPincode testAllowPincode = allowPincodeList.get(allowPincodeList.size() - 1);
        assertThat(testAllowPincode.getPinCode()).isEqualTo(UPDATED_PIN_CODE);
        assertThat(testAllowPincode.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAllowPincode.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAllowPincode() throws Exception {
        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();
        allowPincode.setId(longCount.incrementAndGet());

        // Create the AllowPincode
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllowPincodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allowPincodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllowPincode() throws Exception {
        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();
        allowPincode.setId(longCount.incrementAndGet());

        // Create the AllowPincode
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllowPincodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllowPincode() throws Exception {
        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();
        allowPincode.setId(longCount.incrementAndGet());

        // Create the AllowPincode
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllowPincodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllowPincodeWithPatch() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();

        // Update the allowPincode using partial update
        AllowPincode partialUpdatedAllowPincode = new AllowPincode();
        partialUpdatedAllowPincode.setId(allowPincode.getId());

        partialUpdatedAllowPincode.pinCode(UPDATED_PIN_CODE).createdAt(UPDATED_CREATED_AT);

        restAllowPincodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllowPincode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAllowPincode))
            )
            .andExpect(status().isOk());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
        AllowPincode testAllowPincode = allowPincodeList.get(allowPincodeList.size() - 1);
        assertThat(testAllowPincode.getPinCode()).isEqualTo(UPDATED_PIN_CODE);
        assertThat(testAllowPincode.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAllowPincode.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAllowPincodeWithPatch() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();

        // Update the allowPincode using partial update
        AllowPincode partialUpdatedAllowPincode = new AllowPincode();
        partialUpdatedAllowPincode.setId(allowPincode.getId());

        partialUpdatedAllowPincode.pinCode(UPDATED_PIN_CODE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restAllowPincodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllowPincode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAllowPincode))
            )
            .andExpect(status().isOk());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
        AllowPincode testAllowPincode = allowPincodeList.get(allowPincodeList.size() - 1);
        assertThat(testAllowPincode.getPinCode()).isEqualTo(UPDATED_PIN_CODE);
        assertThat(testAllowPincode.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAllowPincode.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAllowPincode() throws Exception {
        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();
        allowPincode.setId(longCount.incrementAndGet());

        // Create the AllowPincode
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllowPincodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allowPincodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllowPincode() throws Exception {
        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();
        allowPincode.setId(longCount.incrementAndGet());

        // Create the AllowPincode
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllowPincodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllowPincode() throws Exception {
        int databaseSizeBeforeUpdate = allowPincodeRepository.findAll().size();
        allowPincode.setId(longCount.incrementAndGet());

        // Create the AllowPincode
        AllowPincodeDTO allowPincodeDTO = allowPincodeMapper.toDto(allowPincode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllowPincodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(allowPincodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllowPincode in the database
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllowPincode() throws Exception {
        // Initialize the database
        allowPincodeRepository.saveAndFlush(allowPincode);

        int databaseSizeBeforeDelete = allowPincodeRepository.findAll().size();

        // Delete the allowPincode
        restAllowPincodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, allowPincode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AllowPincode> allowPincodeList = allowPincodeRepository.findAll();
        assertThat(allowPincodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
