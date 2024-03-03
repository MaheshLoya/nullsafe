package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.MigrationsRepository;
import com.nullsafe.daily.service.MigrationsQueryService;
import com.nullsafe.daily.service.MigrationsService;
import com.nullsafe.daily.service.criteria.MigrationsCriteria;
import com.nullsafe.daily.service.dto.MigrationsDTO;
import com.nullsafe.daily.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nullsafe.daily.domain.Migrations}.
 */
@RestController
@RequestMapping("/api/migrations")
public class MigrationsResource {

    private final Logger log = LoggerFactory.getLogger(MigrationsResource.class);

    private static final String ENTITY_NAME = "migrations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MigrationsService migrationsService;

    private final MigrationsRepository migrationsRepository;

    private final MigrationsQueryService migrationsQueryService;

    public MigrationsResource(
        MigrationsService migrationsService,
        MigrationsRepository migrationsRepository,
        MigrationsQueryService migrationsQueryService
    ) {
        this.migrationsService = migrationsService;
        this.migrationsRepository = migrationsRepository;
        this.migrationsQueryService = migrationsQueryService;
    }

    /**
     * {@code POST  /migrations} : Create a new migrations.
     *
     * @param migrationsDTO the migrationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new migrationsDTO, or with status {@code 400 (Bad Request)} if the migrations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MigrationsDTO> createMigrations(@Valid @RequestBody MigrationsDTO migrationsDTO) throws URISyntaxException {
        log.debug("REST request to save Migrations : {}", migrationsDTO);
        if (migrationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new migrations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MigrationsDTO result = migrationsService.save(migrationsDTO);
        return ResponseEntity
            .created(new URI("/api/migrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /migrations/:id} : Updates an existing migrations.
     *
     * @param id the id of the migrationsDTO to save.
     * @param migrationsDTO the migrationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated migrationsDTO,
     * or with status {@code 400 (Bad Request)} if the migrationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the migrationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MigrationsDTO> updateMigrations(
        @PathVariable(value = "id", required = false) final Integer id,
        @Valid @RequestBody MigrationsDTO migrationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Migrations : {}, {}", id, migrationsDTO);
        if (migrationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, migrationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!migrationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MigrationsDTO result = migrationsService.update(migrationsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, migrationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /migrations/:id} : Partial updates given fields of an existing migrations, field will ignore if it is null
     *
     * @param id the id of the migrationsDTO to save.
     * @param migrationsDTO the migrationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated migrationsDTO,
     * or with status {@code 400 (Bad Request)} if the migrationsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the migrationsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the migrationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MigrationsDTO> partialUpdateMigrations(
        @PathVariable(value = "id", required = false) final Integer id,
        @NotNull @RequestBody MigrationsDTO migrationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Migrations partially : {}, {}", id, migrationsDTO);
        if (migrationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, migrationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!migrationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MigrationsDTO> result = migrationsService.partialUpdate(migrationsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, migrationsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /migrations} : get all the migrations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of migrations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MigrationsDTO>> getAllMigrations(
        MigrationsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Migrations by criteria: {}", criteria);

        Page<MigrationsDTO> page = migrationsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /migrations/count} : count all the migrations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMigrations(MigrationsCriteria criteria) {
        log.debug("REST request to count Migrations by criteria: {}", criteria);
        return ResponseEntity.ok().body(migrationsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /migrations/:id} : get the "id" migrations.
     *
     * @param id the id of the migrationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the migrationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MigrationsDTO> getMigrations(@PathVariable("id") Integer id) {
        log.debug("REST request to get Migrations : {}", id);
        Optional<MigrationsDTO> migrationsDTO = migrationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(migrationsDTO);
    }

    /**
     * {@code DELETE  /migrations/:id} : delete the "id" migrations.
     *
     * @param id the id of the migrationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMigrations(@PathVariable("id") Integer id) {
        log.debug("REST request to delete Migrations : {}", id);
        migrationsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
