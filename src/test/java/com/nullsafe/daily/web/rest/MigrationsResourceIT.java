package com.nullsafe.daily.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nullsafe.daily.IntegrationTest;
import com.nullsafe.daily.domain.Migrations;
import com.nullsafe.daily.repository.MigrationsRepository;
import com.nullsafe.daily.service.dto.MigrationsDTO;
import com.nullsafe.daily.service.mapper.MigrationsMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MigrationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MigrationsResourceIT {

    private static final String DEFAULT_MIGRATION = "AAAAAAAAAA";
    private static final String UPDATED_MIGRATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_BATCH = 1;
    private static final Integer UPDATED_BATCH = 2;
    private static final Integer SMALLER_BATCH = 1 - 1;

    private static final String ENTITY_API_URL = "/api/migrations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private MigrationsRepository migrationsRepository;

    @Autowired
    private MigrationsMapper migrationsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMigrationsMockMvc;

    private Migrations migrations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Migrations createEntity(EntityManager em) {
        Migrations migrations = new Migrations().migration(DEFAULT_MIGRATION).batch(DEFAULT_BATCH);
        return migrations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Migrations createUpdatedEntity(EntityManager em) {
        Migrations migrations = new Migrations().migration(UPDATED_MIGRATION).batch(UPDATED_BATCH);
        return migrations;
    }

    @BeforeEach
    public void initTest() {
        migrations = createEntity(em);
    }

    @Test
    @Transactional
    void createMigrations() throws Exception {
        int databaseSizeBeforeCreate = migrationsRepository.findAll().size();
        // Create the Migrations
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);
        restMigrationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(migrationsDTO)))
            .andExpect(status().isCreated());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeCreate + 1);
        Migrations testMigrations = migrationsList.get(migrationsList.size() - 1);
        assertThat(testMigrations.getMigration()).isEqualTo(DEFAULT_MIGRATION);
        assertThat(testMigrations.getBatch()).isEqualTo(DEFAULT_BATCH);
    }

    @Test
    @Transactional
    void createMigrationsWithExistingId() throws Exception {
        // Create the Migrations with an existing ID
        migrations.setId(1);
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        int databaseSizeBeforeCreate = migrationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMigrationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(migrationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMigrationIsRequired() throws Exception {
        int databaseSizeBeforeTest = migrationsRepository.findAll().size();
        // set the field null
        migrations.setMigration(null);

        // Create the Migrations, which fails.
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        restMigrationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(migrationsDTO)))
            .andExpect(status().isBadRequest());

        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBatchIsRequired() throws Exception {
        int databaseSizeBeforeTest = migrationsRepository.findAll().size();
        // set the field null
        migrations.setBatch(null);

        // Create the Migrations, which fails.
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        restMigrationsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(migrationsDTO)))
            .andExpect(status().isBadRequest());

        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMigrations() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList
        restMigrationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(migrations.getId().intValue())))
            .andExpect(jsonPath("$.[*].migration").value(hasItem(DEFAULT_MIGRATION)))
            .andExpect(jsonPath("$.[*].batch").value(hasItem(DEFAULT_BATCH)));
    }

    @Test
    @Transactional
    void getMigrations() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get the migrations
        restMigrationsMockMvc
            .perform(get(ENTITY_API_URL_ID, migrations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(migrations.getId().intValue()))
            .andExpect(jsonPath("$.migration").value(DEFAULT_MIGRATION))
            .andExpect(jsonPath("$.batch").value(DEFAULT_BATCH));
    }

    @Test
    @Transactional
    void getMigrationsByIdFiltering() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        Integer id = migrations.getId();

        defaultMigrationsShouldBeFound("id.equals=" + id);
        defaultMigrationsShouldNotBeFound("id.notEquals=" + id);

        defaultMigrationsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMigrationsShouldNotBeFound("id.greaterThan=" + id);

        defaultMigrationsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMigrationsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMigrationsByMigrationIsEqualToSomething() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where migration equals to DEFAULT_MIGRATION
        defaultMigrationsShouldBeFound("migration.equals=" + DEFAULT_MIGRATION);

        // Get all the migrationsList where migration equals to UPDATED_MIGRATION
        defaultMigrationsShouldNotBeFound("migration.equals=" + UPDATED_MIGRATION);
    }

    @Test
    @Transactional
    void getAllMigrationsByMigrationIsInShouldWork() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where migration in DEFAULT_MIGRATION or UPDATED_MIGRATION
        defaultMigrationsShouldBeFound("migration.in=" + DEFAULT_MIGRATION + "," + UPDATED_MIGRATION);

        // Get all the migrationsList where migration equals to UPDATED_MIGRATION
        defaultMigrationsShouldNotBeFound("migration.in=" + UPDATED_MIGRATION);
    }

    @Test
    @Transactional
    void getAllMigrationsByMigrationIsNullOrNotNull() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where migration is not null
        defaultMigrationsShouldBeFound("migration.specified=true");

        // Get all the migrationsList where migration is null
        defaultMigrationsShouldNotBeFound("migration.specified=false");
    }

    @Test
    @Transactional
    void getAllMigrationsByMigrationContainsSomething() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where migration contains DEFAULT_MIGRATION
        defaultMigrationsShouldBeFound("migration.contains=" + DEFAULT_MIGRATION);

        // Get all the migrationsList where migration contains UPDATED_MIGRATION
        defaultMigrationsShouldNotBeFound("migration.contains=" + UPDATED_MIGRATION);
    }

    @Test
    @Transactional
    void getAllMigrationsByMigrationNotContainsSomething() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where migration does not contain DEFAULT_MIGRATION
        defaultMigrationsShouldNotBeFound("migration.doesNotContain=" + DEFAULT_MIGRATION);

        // Get all the migrationsList where migration does not contain UPDATED_MIGRATION
        defaultMigrationsShouldBeFound("migration.doesNotContain=" + UPDATED_MIGRATION);
    }

    @Test
    @Transactional
    void getAllMigrationsByBatchIsEqualToSomething() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where batch equals to DEFAULT_BATCH
        defaultMigrationsShouldBeFound("batch.equals=" + DEFAULT_BATCH);

        // Get all the migrationsList where batch equals to UPDATED_BATCH
        defaultMigrationsShouldNotBeFound("batch.equals=" + UPDATED_BATCH);
    }

    @Test
    @Transactional
    void getAllMigrationsByBatchIsInShouldWork() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where batch in DEFAULT_BATCH or UPDATED_BATCH
        defaultMigrationsShouldBeFound("batch.in=" + DEFAULT_BATCH + "," + UPDATED_BATCH);

        // Get all the migrationsList where batch equals to UPDATED_BATCH
        defaultMigrationsShouldNotBeFound("batch.in=" + UPDATED_BATCH);
    }

    @Test
    @Transactional
    void getAllMigrationsByBatchIsNullOrNotNull() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where batch is not null
        defaultMigrationsShouldBeFound("batch.specified=true");

        // Get all the migrationsList where batch is null
        defaultMigrationsShouldNotBeFound("batch.specified=false");
    }

    @Test
    @Transactional
    void getAllMigrationsByBatchIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where batch is greater than or equal to DEFAULT_BATCH
        defaultMigrationsShouldBeFound("batch.greaterThanOrEqual=" + DEFAULT_BATCH);

        // Get all the migrationsList where batch is greater than or equal to UPDATED_BATCH
        defaultMigrationsShouldNotBeFound("batch.greaterThanOrEqual=" + UPDATED_BATCH);
    }

    @Test
    @Transactional
    void getAllMigrationsByBatchIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where batch is less than or equal to DEFAULT_BATCH
        defaultMigrationsShouldBeFound("batch.lessThanOrEqual=" + DEFAULT_BATCH);

        // Get all the migrationsList where batch is less than or equal to SMALLER_BATCH
        defaultMigrationsShouldNotBeFound("batch.lessThanOrEqual=" + SMALLER_BATCH);
    }

    @Test
    @Transactional
    void getAllMigrationsByBatchIsLessThanSomething() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where batch is less than DEFAULT_BATCH
        defaultMigrationsShouldNotBeFound("batch.lessThan=" + DEFAULT_BATCH);

        // Get all the migrationsList where batch is less than UPDATED_BATCH
        defaultMigrationsShouldBeFound("batch.lessThan=" + UPDATED_BATCH);
    }

    @Test
    @Transactional
    void getAllMigrationsByBatchIsGreaterThanSomething() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        // Get all the migrationsList where batch is greater than DEFAULT_BATCH
        defaultMigrationsShouldNotBeFound("batch.greaterThan=" + DEFAULT_BATCH);

        // Get all the migrationsList where batch is greater than SMALLER_BATCH
        defaultMigrationsShouldBeFound("batch.greaterThan=" + SMALLER_BATCH);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMigrationsShouldBeFound(String filter) throws Exception {
        restMigrationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(migrations.getId())))
            .andExpect(jsonPath("$.[*].migration").value(hasItem(DEFAULT_MIGRATION)))
            .andExpect(jsonPath("$.[*].batch").value(hasItem(DEFAULT_BATCH)));

        // Check, that the count call also returns 1
        restMigrationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMigrationsShouldNotBeFound(String filter) throws Exception {
        restMigrationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMigrationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMigrations() throws Exception {
        // Get the migrations
        restMigrationsMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMigrations() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();

        // Update the migrations
        Migrations updatedMigrations = migrationsRepository.findById(migrations.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMigrations are not directly saved in db
        em.detach(updatedMigrations);
        updatedMigrations.migration(UPDATED_MIGRATION).batch(UPDATED_BATCH);
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(updatedMigrations);

        restMigrationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, migrationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(migrationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
        Migrations testMigrations = migrationsList.get(migrationsList.size() - 1);
        assertThat(testMigrations.getMigration()).isEqualTo(UPDATED_MIGRATION);
        assertThat(testMigrations.getBatch()).isEqualTo(UPDATED_BATCH);
    }

    @Test
    @Transactional
    void putNonExistingMigrations() throws Exception {
        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();
        migrations.setId(intCount.incrementAndGet());

        // Create the Migrations
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMigrationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, migrationsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(migrationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMigrations() throws Exception {
        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();
        migrations.setId(intCount.incrementAndGet());

        // Create the Migrations
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMigrationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(migrationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMigrations() throws Exception {
        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();
        migrations.setId(intCount.incrementAndGet());

        // Create the Migrations
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMigrationsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(migrationsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMigrationsWithPatch() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();

        // Update the migrations using partial update
        Migrations partialUpdatedMigrations = new Migrations();
        partialUpdatedMigrations.setId(migrations.getId());

        partialUpdatedMigrations.migration(UPDATED_MIGRATION).batch(UPDATED_BATCH);

        restMigrationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMigrations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMigrations))
            )
            .andExpect(status().isOk());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
        Migrations testMigrations = migrationsList.get(migrationsList.size() - 1);
        assertThat(testMigrations.getMigration()).isEqualTo(UPDATED_MIGRATION);
        assertThat(testMigrations.getBatch()).isEqualTo(UPDATED_BATCH);
    }

    @Test
    @Transactional
    void fullUpdateMigrationsWithPatch() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();

        // Update the migrations using partial update
        Migrations partialUpdatedMigrations = new Migrations();
        partialUpdatedMigrations.setId(migrations.getId());

        partialUpdatedMigrations.migration(UPDATED_MIGRATION).batch(UPDATED_BATCH);

        restMigrationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMigrations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMigrations))
            )
            .andExpect(status().isOk());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
        Migrations testMigrations = migrationsList.get(migrationsList.size() - 1);
        assertThat(testMigrations.getMigration()).isEqualTo(UPDATED_MIGRATION);
        assertThat(testMigrations.getBatch()).isEqualTo(UPDATED_BATCH);
    }

    @Test
    @Transactional
    void patchNonExistingMigrations() throws Exception {
        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();
        migrations.setId(intCount.incrementAndGet());

        // Create the Migrations
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMigrationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, migrationsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(migrationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMigrations() throws Exception {
        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();
        migrations.setId(intCount.incrementAndGet());

        // Create the Migrations
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMigrationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(migrationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMigrations() throws Exception {
        int databaseSizeBeforeUpdate = migrationsRepository.findAll().size();
        migrations.setId(intCount.incrementAndGet());

        // Create the Migrations
        MigrationsDTO migrationsDTO = migrationsMapper.toDto(migrations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMigrationsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(migrationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Migrations in the database
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMigrations() throws Exception {
        // Initialize the database
        migrationsRepository.saveAndFlush(migrations);

        int databaseSizeBeforeDelete = migrationsRepository.findAll().size();

        // Delete the migrations
        restMigrationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, migrations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Migrations> migrationsList = migrationsRepository.findAll();
        assertThat(migrationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
