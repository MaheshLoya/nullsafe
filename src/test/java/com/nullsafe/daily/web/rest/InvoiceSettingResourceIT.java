package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.InvoiceSetting;
import com.nullsafe.daily.repository.InvoiceSettingRepository;
import com.nullsafe.daily.service.dto.InvoiceSettingDTO;
import com.nullsafe.daily.service.mapper.InvoiceSettingMapper;
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
 * Integration tests for the {@link InvoiceSettingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceSettingResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/invoice-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceSettingRepository invoiceSettingRepository;

    @Autowired
    private InvoiceSettingMapper invoiceSettingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceSettingMockMvc;

    private InvoiceSetting invoiceSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceSetting createEntity(EntityManager em) {
        InvoiceSetting invoiceSetting = new InvoiceSetting()
            .title(DEFAULT_TITLE)
            .value(DEFAULT_VALUE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return invoiceSetting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceSetting createUpdatedEntity(EntityManager em) {
        InvoiceSetting invoiceSetting = new InvoiceSetting()
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return invoiceSetting;
    }

    @BeforeEach
    public void initTest() {
        invoiceSetting = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceSetting() throws Exception {
        int databaseSizeBeforeCreate = invoiceSettingRepository.findAll().size();
        // Create the InvoiceSetting
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);
        restInvoiceSettingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceSetting testInvoiceSetting = invoiceSettingList.get(invoiceSettingList.size() - 1);
        assertThat(testInvoiceSetting.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInvoiceSetting.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testInvoiceSetting.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testInvoiceSetting.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createInvoiceSettingWithExistingId() throws Exception {
        // Create the InvoiceSetting with an existing ID
        invoiceSetting.setId(1L);
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        int databaseSizeBeforeCreate = invoiceSettingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceSettingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceSettingRepository.findAll().size();
        // set the field null
        invoiceSetting.setTitle(null);

        // Create the InvoiceSetting, which fails.
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        restInvoiceSettingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isBadRequest());

        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceSettingRepository.findAll().size();
        // set the field null
        invoiceSetting.setValue(null);

        // Create the InvoiceSetting, which fails.
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        restInvoiceSettingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isBadRequest());

        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceSettings() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList
        restInvoiceSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getInvoiceSetting() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get the invoiceSetting
        restInvoiceSettingMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceSetting.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getInvoiceSettingsByIdFiltering() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        Long id = invoiceSetting.getId();

        defaultInvoiceSettingShouldBeFound("id.equals=" + id);
        defaultInvoiceSettingShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceSettingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceSettingShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceSettingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceSettingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where title equals to DEFAULT_TITLE
        defaultInvoiceSettingShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the invoiceSettingList where title equals to UPDATED_TITLE
        defaultInvoiceSettingShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultInvoiceSettingShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the invoiceSettingList where title equals to UPDATED_TITLE
        defaultInvoiceSettingShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where title is not null
        defaultInvoiceSettingShouldBeFound("title.specified=true");

        // Get all the invoiceSettingList where title is null
        defaultInvoiceSettingShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByTitleContainsSomething() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where title contains DEFAULT_TITLE
        defaultInvoiceSettingShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the invoiceSettingList where title contains UPDATED_TITLE
        defaultInvoiceSettingShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where title does not contain DEFAULT_TITLE
        defaultInvoiceSettingShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the invoiceSettingList where title does not contain UPDATED_TITLE
        defaultInvoiceSettingShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where value equals to DEFAULT_VALUE
        defaultInvoiceSettingShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the invoiceSettingList where value equals to UPDATED_VALUE
        defaultInvoiceSettingShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultInvoiceSettingShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the invoiceSettingList where value equals to UPDATED_VALUE
        defaultInvoiceSettingShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where value is not null
        defaultInvoiceSettingShouldBeFound("value.specified=true");

        // Get all the invoiceSettingList where value is null
        defaultInvoiceSettingShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByValueContainsSomething() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where value contains DEFAULT_VALUE
        defaultInvoiceSettingShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the invoiceSettingList where value contains UPDATED_VALUE
        defaultInvoiceSettingShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where value does not contain DEFAULT_VALUE
        defaultInvoiceSettingShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the invoiceSettingList where value does not contain UPDATED_VALUE
        defaultInvoiceSettingShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where createdAt equals to DEFAULT_CREATED_AT
        defaultInvoiceSettingShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the invoiceSettingList where createdAt equals to UPDATED_CREATED_AT
        defaultInvoiceSettingShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultInvoiceSettingShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the invoiceSettingList where createdAt equals to UPDATED_CREATED_AT
        defaultInvoiceSettingShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where createdAt is not null
        defaultInvoiceSettingShouldBeFound("createdAt.specified=true");

        // Get all the invoiceSettingList where createdAt is null
        defaultInvoiceSettingShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultInvoiceSettingShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the invoiceSettingList where updatedAt equals to UPDATED_UPDATED_AT
        defaultInvoiceSettingShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultInvoiceSettingShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the invoiceSettingList where updatedAt equals to UPDATED_UPDATED_AT
        defaultInvoiceSettingShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllInvoiceSettingsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        // Get all the invoiceSettingList where updatedAt is not null
        defaultInvoiceSettingShouldBeFound("updatedAt.specified=true");

        // Get all the invoiceSettingList where updatedAt is null
        defaultInvoiceSettingShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceSettingShouldBeFound(String filter) throws Exception {
        restInvoiceSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restInvoiceSettingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceSettingShouldNotBeFound(String filter) throws Exception {
        restInvoiceSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceSettingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceSetting() throws Exception {
        // Get the invoiceSetting
        restInvoiceSettingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceSetting() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();

        // Update the invoiceSetting
        InvoiceSetting updatedInvoiceSetting = invoiceSettingRepository.findById(invoiceSetting.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceSetting are not directly saved in db
        em.detach(updatedInvoiceSetting);
        updatedInvoiceSetting.title(UPDATED_TITLE).value(UPDATED_VALUE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(updatedInvoiceSetting);

        restInvoiceSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceSettingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
        InvoiceSetting testInvoiceSetting = invoiceSettingList.get(invoiceSettingList.size() - 1);
        assertThat(testInvoiceSetting.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInvoiceSetting.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testInvoiceSetting.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoiceSetting.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceSetting() throws Exception {
        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();
        invoiceSetting.setId(longCount.incrementAndGet());

        // Create the InvoiceSetting
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceSettingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceSetting() throws Exception {
        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();
        invoiceSetting.setId(longCount.incrementAndGet());

        // Create the InvoiceSetting
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceSetting() throws Exception {
        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();
        invoiceSetting.setId(longCount.incrementAndGet());

        // Create the InvoiceSetting
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSettingMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceSettingWithPatch() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();

        // Update the invoiceSetting using partial update
        InvoiceSetting partialUpdatedInvoiceSetting = new InvoiceSetting();
        partialUpdatedInvoiceSetting.setId(invoiceSetting.getId());

        partialUpdatedInvoiceSetting.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restInvoiceSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceSetting))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
        InvoiceSetting testInvoiceSetting = invoiceSettingList.get(invoiceSettingList.size() - 1);
        assertThat(testInvoiceSetting.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInvoiceSetting.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testInvoiceSetting.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoiceSetting.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceSettingWithPatch() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();

        // Update the invoiceSetting using partial update
        InvoiceSetting partialUpdatedInvoiceSetting = new InvoiceSetting();
        partialUpdatedInvoiceSetting.setId(invoiceSetting.getId());

        partialUpdatedInvoiceSetting.title(UPDATED_TITLE).value(UPDATED_VALUE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restInvoiceSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceSetting))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
        InvoiceSetting testInvoiceSetting = invoiceSettingList.get(invoiceSettingList.size() - 1);
        assertThat(testInvoiceSetting.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInvoiceSetting.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testInvoiceSetting.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInvoiceSetting.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceSetting() throws Exception {
        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();
        invoiceSetting.setId(longCount.incrementAndGet());

        // Create the InvoiceSetting
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceSettingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceSetting() throws Exception {
        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();
        invoiceSetting.setId(longCount.incrementAndGet());

        // Create the InvoiceSetting
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceSetting() throws Exception {
        int databaseSizeBeforeUpdate = invoiceSettingRepository.findAll().size();
        invoiceSetting.setId(longCount.incrementAndGet());

        // Create the InvoiceSetting
        InvoiceSettingDTO invoiceSettingDTO = invoiceSettingMapper.toDto(invoiceSetting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceSettingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceSettingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceSetting in the database
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceSetting() throws Exception {
        // Initialize the database
        invoiceSettingRepository.saveAndFlush(invoiceSetting);

        int databaseSizeBeforeDelete = invoiceSettingRepository.findAll().size();

        // Delete the invoiceSetting
        restInvoiceSettingMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceSetting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceSetting> invoiceSettingList = invoiceSettingRepository.findAll();
        assertThat(invoiceSettingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
