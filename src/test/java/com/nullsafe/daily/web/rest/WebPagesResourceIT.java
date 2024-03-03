package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.WebPages;
import com.nullsafe.daily.repository.WebPagesRepository;
import com.nullsafe.daily.service.dto.WebPagesDTO;
import com.nullsafe.daily.service.mapper.WebPagesMapper;
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
 * Integration tests for the {@link WebPagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WebPagesResourceIT {

    private static final Integer DEFAULT_PAGE_ID = 1;
    private static final Integer UPDATED_PAGE_ID = 2;
    private static final Integer SMALLER_PAGE_ID = 1 - 1;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/web-pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WebPagesRepository webPagesRepository;

    @Autowired
    private WebPagesMapper webPagesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWebPagesMockMvc;

    private WebPages webPages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebPages createEntity(EntityManager em) {
        WebPages webPages = new WebPages()
            .pageId(DEFAULT_PAGE_ID)
            .title(DEFAULT_TITLE)
            .body(DEFAULT_BODY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return webPages;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebPages createUpdatedEntity(EntityManager em) {
        WebPages webPages = new WebPages()
            .pageId(UPDATED_PAGE_ID)
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return webPages;
    }

    @BeforeEach
    public void initTest() {
        webPages = createEntity(em);
    }

    @Test
    @Transactional
    void createWebPages() throws Exception {
        int databaseSizeBeforeCreate = webPagesRepository.findAll().size();
        // Create the WebPages
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);
        restWebPagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webPagesDTO)))
            .andExpect(status().isCreated());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeCreate + 1);
        WebPages testWebPages = webPagesList.get(webPagesList.size() - 1);
        assertThat(testWebPages.getPageId()).isEqualTo(DEFAULT_PAGE_ID);
        assertThat(testWebPages.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWebPages.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testWebPages.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testWebPages.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createWebPagesWithExistingId() throws Exception {
        // Create the WebPages with an existing ID
        webPages.setId(1L);
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        int databaseSizeBeforeCreate = webPagesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebPagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webPagesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPageIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = webPagesRepository.findAll().size();
        // set the field null
        webPages.setPageId(null);

        // Create the WebPages, which fails.
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        restWebPagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webPagesDTO)))
            .andExpect(status().isBadRequest());

        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = webPagesRepository.findAll().size();
        // set the field null
        webPages.setTitle(null);

        // Create the WebPages, which fails.
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        restWebPagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webPagesDTO)))
            .andExpect(status().isBadRequest());

        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = webPagesRepository.findAll().size();
        // set the field null
        webPages.setBody(null);

        // Create the WebPages, which fails.
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        restWebPagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webPagesDTO)))
            .andExpect(status().isBadRequest());

        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWebPages() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList
        restWebPagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webPages.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageId").value(hasItem(DEFAULT_PAGE_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getWebPages() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get the webPages
        restWebPagesMockMvc
            .perform(get(ENTITY_API_URL_ID, webPages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(webPages.getId().intValue()))
            .andExpect(jsonPath("$.pageId").value(DEFAULT_PAGE_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getWebPagesByIdFiltering() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        Long id = webPages.getId();

        defaultWebPagesShouldBeFound("id.equals=" + id);
        defaultWebPagesShouldNotBeFound("id.notEquals=" + id);

        defaultWebPagesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWebPagesShouldNotBeFound("id.greaterThan=" + id);

        defaultWebPagesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWebPagesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWebPagesByPageIdIsEqualToSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where pageId equals to DEFAULT_PAGE_ID
        defaultWebPagesShouldBeFound("pageId.equals=" + DEFAULT_PAGE_ID);

        // Get all the webPagesList where pageId equals to UPDATED_PAGE_ID
        defaultWebPagesShouldNotBeFound("pageId.equals=" + UPDATED_PAGE_ID);
    }

    @Test
    @Transactional
    void getAllWebPagesByPageIdIsInShouldWork() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where pageId in DEFAULT_PAGE_ID or UPDATED_PAGE_ID
        defaultWebPagesShouldBeFound("pageId.in=" + DEFAULT_PAGE_ID + "," + UPDATED_PAGE_ID);

        // Get all the webPagesList where pageId equals to UPDATED_PAGE_ID
        defaultWebPagesShouldNotBeFound("pageId.in=" + UPDATED_PAGE_ID);
    }

    @Test
    @Transactional
    void getAllWebPagesByPageIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where pageId is not null
        defaultWebPagesShouldBeFound("pageId.specified=true");

        // Get all the webPagesList where pageId is null
        defaultWebPagesShouldNotBeFound("pageId.specified=false");
    }

    @Test
    @Transactional
    void getAllWebPagesByPageIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where pageId is greater than or equal to DEFAULT_PAGE_ID
        defaultWebPagesShouldBeFound("pageId.greaterThanOrEqual=" + DEFAULT_PAGE_ID);

        // Get all the webPagesList where pageId is greater than or equal to UPDATED_PAGE_ID
        defaultWebPagesShouldNotBeFound("pageId.greaterThanOrEqual=" + UPDATED_PAGE_ID);
    }

    @Test
    @Transactional
    void getAllWebPagesByPageIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where pageId is less than or equal to DEFAULT_PAGE_ID
        defaultWebPagesShouldBeFound("pageId.lessThanOrEqual=" + DEFAULT_PAGE_ID);

        // Get all the webPagesList where pageId is less than or equal to SMALLER_PAGE_ID
        defaultWebPagesShouldNotBeFound("pageId.lessThanOrEqual=" + SMALLER_PAGE_ID);
    }

    @Test
    @Transactional
    void getAllWebPagesByPageIdIsLessThanSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where pageId is less than DEFAULT_PAGE_ID
        defaultWebPagesShouldNotBeFound("pageId.lessThan=" + DEFAULT_PAGE_ID);

        // Get all the webPagesList where pageId is less than UPDATED_PAGE_ID
        defaultWebPagesShouldBeFound("pageId.lessThan=" + UPDATED_PAGE_ID);
    }

    @Test
    @Transactional
    void getAllWebPagesByPageIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where pageId is greater than DEFAULT_PAGE_ID
        defaultWebPagesShouldNotBeFound("pageId.greaterThan=" + DEFAULT_PAGE_ID);

        // Get all the webPagesList where pageId is greater than SMALLER_PAGE_ID
        defaultWebPagesShouldBeFound("pageId.greaterThan=" + SMALLER_PAGE_ID);
    }

    @Test
    @Transactional
    void getAllWebPagesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where title equals to DEFAULT_TITLE
        defaultWebPagesShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the webPagesList where title equals to UPDATED_TITLE
        defaultWebPagesShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebPagesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultWebPagesShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the webPagesList where title equals to UPDATED_TITLE
        defaultWebPagesShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebPagesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where title is not null
        defaultWebPagesShouldBeFound("title.specified=true");

        // Get all the webPagesList where title is null
        defaultWebPagesShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllWebPagesByTitleContainsSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where title contains DEFAULT_TITLE
        defaultWebPagesShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the webPagesList where title contains UPDATED_TITLE
        defaultWebPagesShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebPagesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where title does not contain DEFAULT_TITLE
        defaultWebPagesShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the webPagesList where title does not contain UPDATED_TITLE
        defaultWebPagesShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllWebPagesByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where body equals to DEFAULT_BODY
        defaultWebPagesShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the webPagesList where body equals to UPDATED_BODY
        defaultWebPagesShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllWebPagesByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where body in DEFAULT_BODY or UPDATED_BODY
        defaultWebPagesShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the webPagesList where body equals to UPDATED_BODY
        defaultWebPagesShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllWebPagesByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where body is not null
        defaultWebPagesShouldBeFound("body.specified=true");

        // Get all the webPagesList where body is null
        defaultWebPagesShouldNotBeFound("body.specified=false");
    }

    @Test
    @Transactional
    void getAllWebPagesByBodyContainsSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where body contains DEFAULT_BODY
        defaultWebPagesShouldBeFound("body.contains=" + DEFAULT_BODY);

        // Get all the webPagesList where body contains UPDATED_BODY
        defaultWebPagesShouldNotBeFound("body.contains=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllWebPagesByBodyNotContainsSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where body does not contain DEFAULT_BODY
        defaultWebPagesShouldNotBeFound("body.doesNotContain=" + DEFAULT_BODY);

        // Get all the webPagesList where body does not contain UPDATED_BODY
        defaultWebPagesShouldBeFound("body.doesNotContain=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    void getAllWebPagesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where createdAt equals to DEFAULT_CREATED_AT
        defaultWebPagesShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the webPagesList where createdAt equals to UPDATED_CREATED_AT
        defaultWebPagesShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllWebPagesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultWebPagesShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the webPagesList where createdAt equals to UPDATED_CREATED_AT
        defaultWebPagesShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllWebPagesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where createdAt is not null
        defaultWebPagesShouldBeFound("createdAt.specified=true");

        // Get all the webPagesList where createdAt is null
        defaultWebPagesShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllWebPagesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultWebPagesShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the webPagesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultWebPagesShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllWebPagesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultWebPagesShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the webPagesList where updatedAt equals to UPDATED_UPDATED_AT
        defaultWebPagesShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllWebPagesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        // Get all the webPagesList where updatedAt is not null
        defaultWebPagesShouldBeFound("updatedAt.specified=true");

        // Get all the webPagesList where updatedAt is null
        defaultWebPagesShouldNotBeFound("updatedAt.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWebPagesShouldBeFound(String filter) throws Exception {
        restWebPagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webPages.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageId").value(hasItem(DEFAULT_PAGE_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restWebPagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWebPagesShouldNotBeFound(String filter) throws Exception {
        restWebPagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWebPagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWebPages() throws Exception {
        // Get the webPages
        restWebPagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWebPages() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();

        // Update the webPages
        WebPages updatedWebPages = webPagesRepository.findById(webPages.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWebPages are not directly saved in db
        em.detach(updatedWebPages);
        updatedWebPages
            .pageId(UPDATED_PAGE_ID)
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(updatedWebPages);

        restWebPagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webPagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webPagesDTO))
            )
            .andExpect(status().isOk());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
        WebPages testWebPages = webPagesList.get(webPagesList.size() - 1);
        assertThat(testWebPages.getPageId()).isEqualTo(UPDATED_PAGE_ID);
        assertThat(testWebPages.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWebPages.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testWebPages.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWebPages.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingWebPages() throws Exception {
        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();
        webPages.setId(longCount.incrementAndGet());

        // Create the WebPages
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebPagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webPagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webPagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWebPages() throws Exception {
        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();
        webPages.setId(longCount.incrementAndGet());

        // Create the WebPages
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebPagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(webPagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWebPages() throws Exception {
        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();
        webPages.setId(longCount.incrementAndGet());

        // Create the WebPages
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebPagesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(webPagesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWebPagesWithPatch() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();

        // Update the webPages using partial update
        WebPages partialUpdatedWebPages = new WebPages();
        partialUpdatedWebPages.setId(webPages.getId());

        partialUpdatedWebPages.body(UPDATED_BODY).updatedAt(UPDATED_UPDATED_AT);

        restWebPagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebPages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWebPages))
            )
            .andExpect(status().isOk());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
        WebPages testWebPages = webPagesList.get(webPagesList.size() - 1);
        assertThat(testWebPages.getPageId()).isEqualTo(DEFAULT_PAGE_ID);
        assertThat(testWebPages.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWebPages.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testWebPages.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testWebPages.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateWebPagesWithPatch() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();

        // Update the webPages using partial update
        WebPages partialUpdatedWebPages = new WebPages();
        partialUpdatedWebPages.setId(webPages.getId());

        partialUpdatedWebPages
            .pageId(UPDATED_PAGE_ID)
            .title(UPDATED_TITLE)
            .body(UPDATED_BODY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restWebPagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebPages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWebPages))
            )
            .andExpect(status().isOk());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
        WebPages testWebPages = webPagesList.get(webPagesList.size() - 1);
        assertThat(testWebPages.getPageId()).isEqualTo(UPDATED_PAGE_ID);
        assertThat(testWebPages.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWebPages.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testWebPages.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWebPages.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingWebPages() throws Exception {
        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();
        webPages.setId(longCount.incrementAndGet());

        // Create the WebPages
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebPagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, webPagesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(webPagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWebPages() throws Exception {
        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();
        webPages.setId(longCount.incrementAndGet());

        // Create the WebPages
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebPagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(webPagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWebPages() throws Exception {
        int databaseSizeBeforeUpdate = webPagesRepository.findAll().size();
        webPages.setId(longCount.incrementAndGet());

        // Create the WebPages
        WebPagesDTO webPagesDTO = webPagesMapper.toDto(webPages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebPagesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(webPagesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebPages in the database
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWebPages() throws Exception {
        // Initialize the database
        webPagesRepository.saveAndFlush(webPages);

        int databaseSizeBeforeDelete = webPagesRepository.findAll().size();

        // Delete the webPages
        restWebPagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, webPages.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WebPages> webPagesList = webPagesRepository.findAll();
        assertThat(webPagesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
