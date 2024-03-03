package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Cat;
import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.domain.SubCat;
import com.nullsafe.daily.repository.SubCatRepository;
import com.nullsafe.daily.service.dto.SubCatDTO;
import com.nullsafe.daily.service.mapper.SubCatMapper;
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
 * Integration tests for the {@link SubCatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubCatResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/sub-cats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubCatRepository subCatRepository;

    @Autowired
    private SubCatMapper subCatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCatMockMvc;

    private SubCat subCat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCat createEntity(EntityManager em) {
        SubCat subCat = new SubCat()
            .title(DEFAULT_TITLE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .isActive(DEFAULT_IS_ACTIVE);
        // Add required entity
        Cat cat;
        if (TestUtil.findAll(em, Cat.class).isEmpty()) {
            cat = CatResourceIT.createEntity(em);
            em.persist(cat);
            em.flush();
        } else {
            cat = TestUtil.findAll(em, Cat.class).get(0);
        }
        subCat.setCat(cat);
        return subCat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCat createUpdatedEntity(EntityManager em) {
        SubCat subCat = new SubCat()
            .title(UPDATED_TITLE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isActive(UPDATED_IS_ACTIVE);
        // Add required entity
        Cat cat;
        if (TestUtil.findAll(em, Cat.class).isEmpty()) {
            cat = CatResourceIT.createUpdatedEntity(em);
            em.persist(cat);
            em.flush();
        } else {
            cat = TestUtil.findAll(em, Cat.class).get(0);
        }
        subCat.setCat(cat);
        return subCat;
    }

    @BeforeEach
    public void initTest() {
        subCat = createEntity(em);
    }

    @Test
    @Transactional
    void createSubCat() throws Exception {
        int databaseSizeBeforeCreate = subCatRepository.findAll().size();
        // Create the SubCat
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);
        restSubCatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCatDTO)))
            .andExpect(status().isCreated());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeCreate + 1);
        SubCat testSubCat = subCatList.get(subCatList.size() - 1);
        assertThat(testSubCat.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSubCat.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubCat.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSubCat.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createSubCatWithExistingId() throws Exception {
        // Create the SubCat with an existing ID
        subCat.setId(1L);
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        int databaseSizeBeforeCreate = subCatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubCatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCatRepository.findAll().size();
        // set the field null
        subCat.setTitle(null);

        // Create the SubCat, which fails.
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        restSubCatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCatDTO)))
            .andExpect(status().isBadRequest());

        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCatRepository.findAll().size();
        // set the field null
        subCat.setIsActive(null);

        // Create the SubCat, which fails.
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        restSubCatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCatDTO)))
            .andExpect(status().isBadRequest());

        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubCats() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList
        restSubCatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCat.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSubCat() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get the subCat
        restSubCatMockMvc
            .perform(get(ENTITY_API_URL_ID, subCat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subCat.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getSubCatsByIdFiltering() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        Long id = subCat.getId();

        defaultSubCatShouldBeFound("id.equals=" + id);
        defaultSubCatShouldNotBeFound("id.notEquals=" + id);

        defaultSubCatShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubCatShouldNotBeFound("id.greaterThan=" + id);

        defaultSubCatShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubCatShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubCatsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where title equals to DEFAULT_TITLE
        defaultSubCatShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the subCatList where title equals to UPDATED_TITLE
        defaultSubCatShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSubCatsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSubCatShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the subCatList where title equals to UPDATED_TITLE
        defaultSubCatShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSubCatsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where title is not null
        defaultSubCatShouldBeFound("title.specified=true");

        // Get all the subCatList where title is null
        defaultSubCatShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCatsByTitleContainsSomething() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where title contains DEFAULT_TITLE
        defaultSubCatShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the subCatList where title contains UPDATED_TITLE
        defaultSubCatShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSubCatsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where title does not contain DEFAULT_TITLE
        defaultSubCatShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the subCatList where title does not contain UPDATED_TITLE
        defaultSubCatShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSubCatsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where createdAt equals to DEFAULT_CREATED_AT
        defaultSubCatShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the subCatList where createdAt equals to UPDATED_CREATED_AT
        defaultSubCatShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubCatsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSubCatShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the subCatList where createdAt equals to UPDATED_CREATED_AT
        defaultSubCatShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubCatsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where createdAt is not null
        defaultSubCatShouldBeFound("createdAt.specified=true");

        // Get all the subCatList where createdAt is null
        defaultSubCatShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCatsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSubCatShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the subCatList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSubCatShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSubCatsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSubCatShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the subCatList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSubCatShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllSubCatsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where updatedAt is not null
        defaultSubCatShouldBeFound("updatedAt.specified=true");

        // Get all the subCatList where updatedAt is null
        defaultSubCatShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCatsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where isActive equals to DEFAULT_IS_ACTIVE
        defaultSubCatShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the subCatList where isActive equals to UPDATED_IS_ACTIVE
        defaultSubCatShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSubCatsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultSubCatShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the subCatList where isActive equals to UPDATED_IS_ACTIVE
        defaultSubCatShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSubCatsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        // Get all the subCatList where isActive is not null
        defaultSubCatShouldBeFound("isActive.specified=true");

        // Get all the subCatList where isActive is null
        defaultSubCatShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCatsByCatIsEqualToSomething() throws Exception {
        Cat cat;
        if (TestUtil.findAll(em, Cat.class).isEmpty()) {
            subCatRepository.saveAndFlush(subCat);
            cat = CatResourceIT.createEntity(em);
        } else {
            cat = TestUtil.findAll(em, Cat.class).get(0);
        }
        em.persist(cat);
        em.flush();
        subCat.setCat(cat);
        subCatRepository.saveAndFlush(subCat);
        Long catId = cat.getId();
        // Get all the subCatList where cat equals to catId
        defaultSubCatShouldBeFound("catId.equals=" + catId);

        // Get all the subCatList where cat equals to (catId + 1)
        defaultSubCatShouldNotBeFound("catId.equals=" + (catId + 1));
    }

    @Test
    @Transactional
    void getAllSubCatsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            subCatRepository.saveAndFlush(subCat);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        subCat.addProduct(product);
        subCatRepository.saveAndFlush(subCat);
        Long productId = product.getId();
        // Get all the subCatList where product equals to productId
        defaultSubCatShouldBeFound("productId.equals=" + productId);

        // Get all the subCatList where product equals to (productId + 1)
        defaultSubCatShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubCatShouldBeFound(String filter) throws Exception {
        restSubCatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCat.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restSubCatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubCatShouldNotBeFound(String filter) throws Exception {
        restSubCatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubCatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubCat() throws Exception {
        // Get the subCat
        restSubCatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubCat() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();

        // Update the subCat
        SubCat updatedSubCat = subCatRepository.findById(subCat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubCat are not directly saved in db
        em.detach(updatedSubCat);
        updatedSubCat.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).isActive(UPDATED_IS_ACTIVE);
        SubCatDTO subCatDTO = subCatMapper.toDto(updatedSubCat);

        restSubCatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subCatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCatDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
        SubCat testSubCat = subCatList.get(subCatList.size() - 1);
        assertThat(testSubCat.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSubCat.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubCat.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSubCat.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSubCat() throws Exception {
        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();
        subCat.setId(longCount.incrementAndGet());

        // Create the SubCat
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subCatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubCat() throws Exception {
        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();
        subCat.setId(longCount.incrementAndGet());

        // Create the SubCat
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubCat() throws Exception {
        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();
        subCat.setId(longCount.incrementAndGet());

        // Create the SubCat
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubCatWithPatch() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();

        // Update the subCat using partial update
        SubCat partialUpdatedSubCat = new SubCat();
        partialUpdatedSubCat.setId(subCat.getId());

        restSubCatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubCat))
            )
            .andExpect(status().isOk());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
        SubCat testSubCat = subCatList.get(subCatList.size() - 1);
        assertThat(testSubCat.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSubCat.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubCat.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSubCat.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSubCatWithPatch() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();

        // Update the subCat using partial update
        SubCat partialUpdatedSubCat = new SubCat();
        partialUpdatedSubCat.setId(subCat.getId());

        partialUpdatedSubCat.title(UPDATED_TITLE).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).isActive(UPDATED_IS_ACTIVE);

        restSubCatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubCat))
            )
            .andExpect(status().isOk());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
        SubCat testSubCat = subCatList.get(subCatList.size() - 1);
        assertThat(testSubCat.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSubCat.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubCat.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSubCat.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSubCat() throws Exception {
        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();
        subCat.setId(longCount.incrementAndGet());

        // Create the SubCat
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subCatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubCat() throws Exception {
        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();
        subCat.setId(longCount.incrementAndGet());

        // Create the SubCat
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubCat() throws Exception {
        int databaseSizeBeforeUpdate = subCatRepository.findAll().size();
        subCat.setId(longCount.incrementAndGet());

        // Create the SubCat
        SubCatDTO subCatDTO = subCatMapper.toDto(subCat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subCatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCat in the database
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubCat() throws Exception {
        // Initialize the database
        subCatRepository.saveAndFlush(subCat);

        int databaseSizeBeforeDelete = subCatRepository.findAll().size();

        // Delete the subCat
        restSubCatMockMvc
            .perform(delete(ENTITY_API_URL_ID, subCat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubCat> subCatList = subCatRepository.findAll();
        assertThat(subCatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
