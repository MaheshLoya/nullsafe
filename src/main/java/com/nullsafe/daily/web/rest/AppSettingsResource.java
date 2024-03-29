package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.AppSettingsRepository;
import com.nullsafe.daily.service.AppSettingsQueryService;
import com.nullsafe.daily.service.AppSettingsService;
import com.nullsafe.daily.service.criteria.AppSettingsCriteria;
import com.nullsafe.daily.service.dto.AppSettingsDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.AppSettings}.
 */
@RestController
@RequestMapping("/api/app-settings")
public class AppSettingsResource {

    private final Logger log = LoggerFactory.getLogger(AppSettingsResource.class);

    private static final String ENTITY_NAME = "appSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppSettingsService appSettingsService;

    private final AppSettingsRepository appSettingsRepository;

    private final AppSettingsQueryService appSettingsQueryService;

    public AppSettingsResource(
        AppSettingsService appSettingsService,
        AppSettingsRepository appSettingsRepository,
        AppSettingsQueryService appSettingsQueryService
    ) {
        this.appSettingsService = appSettingsService;
        this.appSettingsRepository = appSettingsRepository;
        this.appSettingsQueryService = appSettingsQueryService;
    }

    /**
     * {@code POST  /app-settings} : Create a new appSettings.
     *
     * @param appSettingsDTO the appSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appSettingsDTO, or with status {@code 400 (Bad Request)} if the appSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppSettingsDTO> createAppSettings(@Valid @RequestBody AppSettingsDTO appSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save AppSettings : {}", appSettingsDTO);
        if (appSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new appSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppSettingsDTO result = appSettingsService.save(appSettingsDTO);
        return ResponseEntity
            .created(new URI("/api/app-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-settings/:id} : Updates an existing appSettings.
     *
     * @param id the id of the appSettingsDTO to save.
     * @param appSettingsDTO the appSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the appSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppSettingsDTO> updateAppSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppSettingsDTO appSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppSettings : {}, {}", id, appSettingsDTO);
        if (appSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppSettingsDTO result = appSettingsService.update(appSettingsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /app-settings/:id} : Partial updates given fields of an existing appSettings, field will ignore if it is null
     *
     * @param id the id of the appSettingsDTO to save.
     * @param appSettingsDTO the appSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the appSettingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appSettingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppSettingsDTO> partialUpdateAppSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppSettingsDTO appSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppSettings partially : {}, {}", id, appSettingsDTO);
        if (appSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppSettingsDTO> result = appSettingsService.partialUpdate(appSettingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appSettingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /app-settings} : get all the appSettings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appSettings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AppSettingsDTO>> getAllAppSettings(
        AppSettingsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AppSettings by criteria: {}", criteria);

        Page<AppSettingsDTO> page = appSettingsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-settings/count} : count all the appSettings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAppSettings(AppSettingsCriteria criteria) {
        log.debug("REST request to count AppSettings by criteria: {}", criteria);
        return ResponseEntity.ok().body(appSettingsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /app-settings/:id} : get the "id" appSettings.
     *
     * @param id the id of the appSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppSettingsDTO> getAppSettings(@PathVariable("id") Long id) {
        log.debug("REST request to get AppSettings : {}", id);
        Optional<AppSettingsDTO> appSettingsDTO = appSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appSettingsDTO);
    }

    /**
     * {@code DELETE  /app-settings/:id} : delete the "id" appSettings.
     *
     * @param id the id of the appSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppSettings(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppSettings : {}", id);
        appSettingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
