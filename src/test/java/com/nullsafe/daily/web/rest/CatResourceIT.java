package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Cat;
import com.nullsafe.daily.domain.SubCat;
import com.nullsafe.daily.repository.CatRepository;
import com.nullsafe.daily.service.dto.CatDTO;
import com.nullsafe.daily.service.mapper.CatMapper;
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
 * Integration tests for the {@link CatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/cats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private CatMapper catMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatMockMvc;

    private Cat cat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cat createEntity(EntityManager em) {
        Cat cat = new Cat().title(DEFAULT_TITLE).createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT).isActive(DEFAULT_IS_ACTIVE);
        return cat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cat createUpdatedEntity(EntityManager em) {
        Cat cat = new Cat().title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).isActive(UPDATED_IS_ACTIVE);
        return cat;
    }

    @BeforeEach
    public void initTest() {
        cat = createEntity(em);
    }

    @Test
    @Transactional
    void createCat() throws Exception {
        int databaseSizeBeforeCreate = catRepository.findAll().size();
        // Create the Cat
        CatDTO catDTO = catMapper.toDto(cat);
        restCatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catDTO)))
            .andExpect(status().isCreated());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeCreate + 1);
        Cat testCat = catList.get(catList.size() - 1);
        assertThat(testCat.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCat.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCat.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testCat.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createCatWithExistingId() throws Exception {
        // Create the Cat with an existing ID
        cat.setId(1L);
        CatDTO catDTO = catMapper.toDto(cat);

        int databaseSizeBeforeCreate = catRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = catRepository.findAll().size();
        // set the field null
        cat.setTitle(null);

        // Create the Cat, which fails.
        CatDTO catDTO = catMapper.toDto(cat);

        restCatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catDTO)))
            .andExpect(status().isBadRequest());

        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = catRepository.findAll().size();
        // set the field null
        cat.setIsActive(null);

        // Create the Cat, which fails.
        CatDTO catDTO = catMapper.toDto(cat);

        restCatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catDTO)))
            .andExpect(status().isBadRequest());

        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCats() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList
        restCatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cat.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getCat() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get the cat
        restCatMockMvc
            .perform(get(ENTITY_API_URL_ID, cat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cat.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getCatsByIdFiltering() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        Long id = cat.getId();

        defaultCatShouldBeFound("id.equals=" + id);
        defaultCatShouldNotBeFound("id.notEquals=" + id);

        defaultCatShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCatShouldNotBeFound("id.greaterThan=" + id);

        defaultCatShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCatShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCatsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where title equals to DEFAULT_TITLE
        defaultCatShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the catList where title equals to UPDATED_TITLE
        defaultCatShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCatsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCatShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the catList where title equals to UPDATED_TITLE
        defaultCatShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCatsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where title is not null
        defaultCatShouldBeFound("title.specified=true");

        // Get all the catList where title is null
        defaultCatShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllCatsByTitleContainsSomething() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where title contains DEFAULT_TITLE
        defaultCatShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the catList where title contains UPDATED_TITLE
        defaultCatShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCatsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where title does not contain DEFAULT_TITLE
        defaultCatShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the catList where title does not contain UPDATED_TITLE
        defaultCatShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCatsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where createdAt equals to DEFAULT_CREATED_AT
        defaultCatShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the catList where createdAt equals to UPDATED_CREATED_AT
        defaultCatShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCatsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCatShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the catList where createdAt equals to UPDATED_CREATED_AT
        defaultCatShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllCatsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where createdAt is not null
        defaultCatShouldBeFound("createdAt.specified=true");

        // Get all the catList where createdAt is null
        defaultCatShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCatsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultCatShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the catList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCatShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCatsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultCatShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the catList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCatShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllCatsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where updatedAt is not null
        defaultCatShouldBeFound("updatedAt.specified=true");

        // Get all the catList where updatedAt is null
        defaultCatShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllCatsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where isActive equals to DEFAULT_IS_ACTIVE
        defaultCatShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the catList where isActive equals to UPDATED_IS_ACTIVE
        defaultCatShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCatsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultCatShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the catList where isActive equals to UPDATED_IS_ACTIVE
        defaultCatShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCatsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        // Get all the catList where isActive is not null
        defaultCatShouldBeFound("isActive.specified=true");

        // Get all the catList where isActive is null
        defaultCatShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllCatsBySubCatIsEqualToSomething() throws Exception {
        SubCat subCat;
        if (TestUtil.findAll(em, SubCat.class).isEmpty()) {
            catRepository.saveAndFlush(cat);
            subCat = SubCatResourceIT.createEntity(em);
        } else {
            subCat = TestUtil.findAll(em, SubCat.class).get(0);
        }
        em.persist(subCat);
        em.flush();
        cat.addSubCat(subCat);
        catRepository.saveAndFlush(cat);
        Long subCatId = subCat.getId();
        // Get all the catList where subCat equals to subCatId
        defaultCatShouldBeFound("subCatId.equals=" + subCatId);

        // Get all the catList where subCat equals to (subCatId + 1)
        defaultCatShouldNotBeFound("subCatId.equals=" + (subCatId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatShouldBeFound(String filter) throws Exception {
        restCatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cat.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatShouldNotBeFound(String filter) throws Exception {
        restCatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCat() throws Exception {
        // Get the cat
        restCatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCat() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        int databaseSizeBeforeUpdate = catRepository.findAll().size();

        // Update the cat
        Cat updatedCat = catRepository.findById(cat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCat are not directly saved in db
        em.detach(updatedCat);
        updatedCat.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).isActive(UPDATED_IS_ACTIVE);
        CatDTO catDTO = catMapper.toDto(updatedCat);

        restCatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
        Cat testCat = catList.get(catList.size() - 1);
        assertThat(testCat.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCat.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCat.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testCat.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingCat() throws Exception {
        int databaseSizeBeforeUpdate = catRepository.findAll().size();
        cat.setId(longCount.incrementAndGet());

        // Create the Cat
        CatDTO catDTO = catMapper.toDto(cat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCat() throws Exception {
        int databaseSizeBeforeUpdate = catRepository.findAll().size();
        cat.setId(longCount.incrementAndGet());

        // Create the Cat
        CatDTO catDTO = catMapper.toDto(cat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCat() throws Exception {
        int databaseSizeBeforeUpdate = catRepository.findAll().size();
        cat.setId(longCount.incrementAndGet());

        // Create the Cat
        CatDTO catDTO = catMapper.toDto(cat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatWithPatch() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        int databaseSizeBeforeUpdate = catRepository.findAll().size();

        // Update the cat using partial update
        Cat partialUpdatedCat = new Cat();
        partialUpdatedCat.setId(cat.getId());

        partialUpdatedCat.updatedAt(UPDATED_UPDATED_AT).isActive(UPDATED_IS_ACTIVE);

        restCatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCat))
            )
            .andExpect(status().isOk());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
        Cat testCat = catList.get(catList.size() - 1);
        assertThat(testCat.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCat.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCat.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testCat.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateCatWithPatch() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        int databaseSizeBeforeUpdate = catRepository.findAll().size();

        // Update the cat using partial update
        Cat partialUpdatedCat = new Cat();
        partialUpdatedCat.setId(cat.getId());

        partialUpdatedCat.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).isActive(UPDATED_IS_ACTIVE);

        restCatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCat))
            )
            .andExpect(status().isOk());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
        Cat testCat = catList.get(catList.size() - 1);
        assertThat(testCat.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCat.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCat.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testCat.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingCat() throws Exception {
        int databaseSizeBeforeUpdate = catRepository.findAll().size();
        cat.setId(longCount.incrementAndGet());

        // Create the Cat
        CatDTO catDTO = catMapper.toDto(cat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCat() throws Exception {
        int databaseSizeBeforeUpdate = catRepository.findAll().size();
        cat.setId(longCount.incrementAndGet());

        // Create the Cat
        CatDTO catDTO = catMapper.toDto(cat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCat() throws Exception {
        int databaseSizeBeforeUpdate = catRepository.findAll().size();
        cat.setId(longCount.incrementAndGet());

        // Create the Cat
        CatDTO catDTO = catMapper.toDto(cat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(catDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cat in the database
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCat() throws Exception {
        // Initialize the database
        catRepository.saveAndFlush(cat);

        int databaseSizeBeforeDelete = catRepository.findAll().size();

        // Delete the cat
        restCatMockMvc.perform(delete(ENTITY_API_URL_ID, cat.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cat> catList = catRepository.findAll();
        assertThat(catList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
