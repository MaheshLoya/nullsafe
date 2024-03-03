package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Testimonials;
import com.nullsafe.daily.repository.TestimonialsRepository;
import com.nullsafe.daily.service.dto.TestimonialsDTO;
import com.nullsafe.daily.service.mapper.TestimonialsMapper;
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
 * Integration tests for the {@link TestimonialsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestimonialsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/testimonials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestimonialsRepository testimonialsRepository;

    @Autowired
    private TestimonialsMapper testimonialsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestimonialsMockMvc;

    private Testimonials testimonials;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Testimonials createEntity(EntityManager em) {
        Testimonials testimonials = new Testimonials()
            .title(DEFAULT_TITLE)
            .subTitle(DEFAULT_SUB_TITLE)
            .rating(DEFAULT_RATING)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return testimonials;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Testimonials createUpdatedEntity(EntityManager em) {
        Testimonials testimonials = new Testimonials()
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .rating(UPDATED_RATING)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return testimonials;
    }

    @BeforeEach
    public void initTest() {
        testimonials = createEntity(em);
    }

    @Test
    @Transactional
    void createTestimonials() throws Exception {
        int databaseSizeBeforeCreate = testimonialsRepository.findAll().size();
        // Create the Testimonials
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);
        restTestimonialsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeCreate + 1);
        Testimonials testTestimonials = testimonialsList.get(testimonialsList.size() - 1);
        assertThat(testTestimonials.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTestimonials.getSubTitle()).isEqualTo(DEFAULT_SUB_TITLE);
        assertThat(testTestimonials.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testTestimonials.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestimonials.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestimonials.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTestimonialsWithExistingId() throws Exception {
        // Create the Testimonials with an existing ID
        testimonials.setId(1L);
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        int databaseSizeBeforeCreate = testimonialsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestimonialsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = testimonialsRepository.findAll().size();
        // set the field null
        testimonials.setTitle(null);

        // Create the Testimonials, which fails.
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        restTestimonialsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = testimonialsRepository.findAll().size();
        // set the field null
        testimonials.setSubTitle(null);

        // Create the Testimonials, which fails.
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        restTestimonialsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = testimonialsRepository.findAll().size();
        // set the field null
        testimonials.setRating(null);

        // Create the Testimonials, which fails.
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        restTestimonialsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = testimonialsRepository.findAll().size();
        // set the field null
        testimonials.setDescription(null);

        // Create the Testimonials, which fails.
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        restTestimonialsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTestimonials() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList
        restTestimonialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testimonials.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTestimonials() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get the testimonials
        restTestimonialsMockMvc
            .perform(get(ENTITY_API_URL_ID, testimonials.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testimonials.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subTitle").value(DEFAULT_SUB_TITLE))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getTestimonialsByIdFiltering() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        Long id = testimonials.getId();

        defaultTestimonialsShouldBeFound("id.equals=" + id);
        defaultTestimonialsShouldNotBeFound("id.notEquals=" + id);

        defaultTestimonialsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTestimonialsShouldNotBeFound("id.greaterThan=" + id);

        defaultTestimonialsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTestimonialsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTestimonialsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where title equals to DEFAULT_TITLE
        defaultTestimonialsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the testimonialsList where title equals to UPDATED_TITLE
        defaultTestimonialsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTestimonialsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the testimonialsList where title equals to UPDATED_TITLE
        defaultTestimonialsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where title is not null
        defaultTestimonialsShouldBeFound("title.specified=true");

        // Get all the testimonialsList where title is null
        defaultTestimonialsShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsByTitleContainsSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where title contains DEFAULT_TITLE
        defaultTestimonialsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the testimonialsList where title contains UPDATED_TITLE
        defaultTestimonialsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where title does not contain DEFAULT_TITLE
        defaultTestimonialsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the testimonialsList where title does not contain UPDATED_TITLE
        defaultTestimonialsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTestimonialsBySubTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where subTitle equals to DEFAULT_SUB_TITLE
        defaultTestimonialsShouldBeFound("subTitle.equals=" + DEFAULT_SUB_TITLE);

        // Get all the testimonialsList where subTitle equals to UPDATED_SUB_TITLE
        defaultTestimonialsShouldNotBeFound("subTitle.equals=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllTestimonialsBySubTitleIsInShouldWork() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where subTitle in DEFAULT_SUB_TITLE or UPDATED_SUB_TITLE
        defaultTestimonialsShouldBeFound("subTitle.in=" + DEFAULT_SUB_TITLE + "," + UPDATED_SUB_TITLE);

        // Get all the testimonialsList where subTitle equals to UPDATED_SUB_TITLE
        defaultTestimonialsShouldNotBeFound("subTitle.in=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllTestimonialsBySubTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where subTitle is not null
        defaultTestimonialsShouldBeFound("subTitle.specified=true");

        // Get all the testimonialsList where subTitle is null
        defaultTestimonialsShouldNotBeFound("subTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsBySubTitleContainsSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where subTitle contains DEFAULT_SUB_TITLE
        defaultTestimonialsShouldBeFound("subTitle.contains=" + DEFAULT_SUB_TITLE);

        // Get all the testimonialsList where subTitle contains UPDATED_SUB_TITLE
        defaultTestimonialsShouldNotBeFound("subTitle.contains=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllTestimonialsBySubTitleNotContainsSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where subTitle does not contain DEFAULT_SUB_TITLE
        defaultTestimonialsShouldNotBeFound("subTitle.doesNotContain=" + DEFAULT_SUB_TITLE);

        // Get all the testimonialsList where subTitle does not contain UPDATED_SUB_TITLE
        defaultTestimonialsShouldBeFound("subTitle.doesNotContain=" + UPDATED_SUB_TITLE);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where rating equals to DEFAULT_RATING
        defaultTestimonialsShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the testimonialsList where rating equals to UPDATED_RATING
        defaultTestimonialsShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultTestimonialsShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the testimonialsList where rating equals to UPDATED_RATING
        defaultTestimonialsShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where rating is not null
        defaultTestimonialsShouldBeFound("rating.specified=true");

        // Get all the testimonialsList where rating is null
        defaultTestimonialsShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where rating is greater than or equal to DEFAULT_RATING
        defaultTestimonialsShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the testimonialsList where rating is greater than or equal to UPDATED_RATING
        defaultTestimonialsShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where rating is less than or equal to DEFAULT_RATING
        defaultTestimonialsShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the testimonialsList where rating is less than or equal to SMALLER_RATING
        defaultTestimonialsShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where rating is less than DEFAULT_RATING
        defaultTestimonialsShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the testimonialsList where rating is less than UPDATED_RATING
        defaultTestimonialsShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where rating is greater than DEFAULT_RATING
        defaultTestimonialsShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the testimonialsList where rating is greater than SMALLER_RATING
        defaultTestimonialsShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where description equals to DEFAULT_DESCRIPTION
        defaultTestimonialsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the testimonialsList where description equals to UPDATED_DESCRIPTION
        defaultTestimonialsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTestimonialsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the testimonialsList where description equals to UPDATED_DESCRIPTION
        defaultTestimonialsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where description is not null
        defaultTestimonialsShouldBeFound("description.specified=true");

        // Get all the testimonialsList where description is null
        defaultTestimonialsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where description contains DEFAULT_DESCRIPTION
        defaultTestimonialsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the testimonialsList where description contains UPDATED_DESCRIPTION
        defaultTestimonialsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestimonialsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where description does not contain DEFAULT_DESCRIPTION
        defaultTestimonialsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the testimonialsList where description does not contain UPDATED_DESCRIPTION
        defaultTestimonialsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTestimonialsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where createdAt equals to DEFAULT_CREATED_AT
        defaultTestimonialsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the testimonialsList where createdAt equals to UPDATED_CREATED_AT
        defaultTestimonialsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestimonialsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTestimonialsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the testimonialsList where createdAt equals to UPDATED_CREATED_AT
        defaultTestimonialsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTestimonialsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where createdAt is not null
        defaultTestimonialsShouldBeFound("createdAt.specified=true");

        // Get all the testimonialsList where createdAt is null
        defaultTestimonialsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTestimonialsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTestimonialsShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the testimonialsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestimonialsShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestimonialsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTestimonialsShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the testimonialsList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTestimonialsShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTestimonialsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        // Get all the testimonialsList where updatedAt is not null
        defaultTestimonialsShouldBeFound("updatedAt.specified=true");

        // Get all the testimonialsList where updatedAt is null
        defaultTestimonialsShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTestimonialsShouldBeFound(String filter) throws Exception {
        restTestimonialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testimonials.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subTitle").value(hasItem(DEFAULT_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restTestimonialsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTestimonialsShouldNotBeFound(String filter) throws Exception {
        restTestimonialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTestimonialsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTestimonials() throws Exception {
        // Get the testimonials
        restTestimonialsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestimonials() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();

        // Update the testimonials
        Testimonials updatedTestimonials = testimonialsRepository.findById(testimonials.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestimonials are not directly saved in db
        em.detach(updatedTestimonials);
        updatedTestimonials
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .rating(UPDATED_RATING)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(updatedTestimonials);

        restTestimonialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testimonialsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
        Testimonials testTestimonials = testimonialsList.get(testimonialsList.size() - 1);
        assertThat(testTestimonials.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestimonials.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testTestimonials.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testTestimonials.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestimonials.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestimonials.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTestimonials() throws Exception {
        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();
        testimonials.setId(longCount.incrementAndGet());

        // Create the Testimonials
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestimonialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testimonialsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestimonials() throws Exception {
        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();
        testimonials.setId(longCount.incrementAndGet());

        // Create the Testimonials
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestimonialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestimonials() throws Exception {
        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();
        testimonials.setId(longCount.incrementAndGet());

        // Create the Testimonials
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestimonialsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestimonialsWithPatch() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();

        // Update the testimonials using partial update
        Testimonials partialUpdatedTestimonials = new Testimonials();
        partialUpdatedTestimonials.setId(testimonials.getId());

        partialUpdatedTestimonials.subTitle(UPDATED_SUB_TITLE).rating(UPDATED_RATING).createdAt(UPDATED_CREATED_AT);

        restTestimonialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestimonials.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestimonials))
            )
            .andExpect(status().isOk());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
        Testimonials testTestimonials = testimonialsList.get(testimonialsList.size() - 1);
        assertThat(testTestimonials.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTestimonials.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testTestimonials.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testTestimonials.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTestimonials.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestimonials.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTestimonialsWithPatch() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();

        // Update the testimonials using partial update
        Testimonials partialUpdatedTestimonials = new Testimonials();
        partialUpdatedTestimonials.setId(testimonials.getId());

        partialUpdatedTestimonials
            .title(UPDATED_TITLE)
            .subTitle(UPDATED_SUB_TITLE)
            .rating(UPDATED_RATING)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTestimonialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestimonials.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestimonials))
            )
            .andExpect(status().isOk());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
        Testimonials testTestimonials = testimonialsList.get(testimonialsList.size() - 1);
        assertThat(testTestimonials.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTestimonials.getSubTitle()).isEqualTo(UPDATED_SUB_TITLE);
        assertThat(testTestimonials.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testTestimonials.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTestimonials.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestimonials.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTestimonials() throws Exception {
        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();
        testimonials.setId(longCount.incrementAndGet());

        // Create the Testimonials
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestimonialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testimonialsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestimonials() throws Exception {
        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();
        testimonials.setId(longCount.incrementAndGet());

        // Create the Testimonials
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestimonialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestimonials() throws Exception {
        int databaseSizeBeforeUpdate = testimonialsRepository.findAll().size();
        testimonials.setId(longCount.incrementAndGet());

        // Create the Testimonials
        TestimonialsDTO testimonialsDTO = testimonialsMapper.toDto(testimonials);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestimonialsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testimonialsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Testimonials in the database
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestimonials() throws Exception {
        // Initialize the database
        testimonialsRepository.saveAndFlush(testimonials);

        int databaseSizeBeforeDelete = testimonialsRepository.findAll().size();

        // Delete the testimonials
        restTestimonialsMockMvc
            .perform(delete(ENTITY_API_URL_ID, testimonials.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Testimonials> testimonialsList = testimonialsRepository.findAll();
        assertThat(testimonialsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
