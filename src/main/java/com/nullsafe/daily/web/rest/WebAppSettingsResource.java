package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.WebAppSettingsRepository;
import com.nullsafe.daily.service.WebAppSettingsQueryService;
import com.nullsafe.daily.service.WebAppSettingsService;
import com.nullsafe.daily.service.criteria.WebAppSettingsCriteria;
import com.nullsafe.daily.service.dto.WebAppSettingsDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.WebAppSettings}.
 */
@RestController
@RequestMapping("/api/web-app-settings")
public class WebAppSettingsResource {

    private final Logger log = LoggerFactory.getLogger(WebAppSettingsResource.class);

    private static final String ENTITY_NAME = "webAppSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebAppSettingsService webAppSettingsService;

    private final WebAppSettingsRepository webAppSettingsRepository;

    private final WebAppSettingsQueryService webAppSettingsQueryService;

    public WebAppSettingsResource(
        WebAppSettingsService webAppSettingsService,
        WebAppSettingsRepository webAppSettingsRepository,
        WebAppSettingsQueryService webAppSettingsQueryService
    ) {
        this.webAppSettingsService = webAppSettingsService;
        this.webAppSettingsRepository = webAppSettingsRepository;
        this.webAppSettingsQueryService = webAppSettingsQueryService;
    }

    /**
     * {@code POST  /web-app-settings} : Create a new webAppSettings.
     *
     * @param webAppSettingsDTO the webAppSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new webAppSettingsDTO, or with status {@code 400 (Bad Request)} if the webAppSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WebAppSettingsDTO> createWebAppSettings(@Valid @RequestBody WebAppSettingsDTO webAppSettingsDTO)
        throws URISyntaxException {
        log.debug("REST request to save WebAppSettings : {}", webAppSettingsDTO);
        if (webAppSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new webAppSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WebAppSettingsDTO result = webAppSettingsService.save(webAppSettingsDTO);
        return ResponseEntity
            .created(new URI("/api/web-app-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /web-app-settings/:id} : Updates an existing webAppSettings.
     *
     * @param id the id of the webAppSettingsDTO to save.
     * @param webAppSettingsDTO the webAppSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webAppSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the webAppSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the webAppSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WebAppSettingsDTO> updateWebAppSettings(
        @PathVariable(value = "id", required = false) final Integer id,
        @Valid @RequestBody WebAppSettingsDTO webAppSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WebAppSettings : {}, {}", id, webAppSettingsDTO);
        if (webAppSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, webAppSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!webAppSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WebAppSettingsDTO result = webAppSettingsService.update(webAppSettingsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webAppSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /web-app-settings/:id} : Partial updates given fields of an existing webAppSettings, field will ignore if it is null
     *
     * @param id the id of the webAppSettingsDTO to save.
     * @param webAppSettingsDTO the webAppSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webAppSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the webAppSettingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the webAppSettingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the webAppSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WebAppSettingsDTO> partialUpdateWebAppSettings(
        @PathVariable(value = "id", required = false) final Integer id,
        @NotNull @RequestBody WebAppSettingsDTO webAppSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WebAppSettings partially : {}, {}", id, webAppSettingsDTO);
        if (webAppSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, webAppSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!webAppSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WebAppSettingsDTO> result = webAppSettingsService.partialUpdate(webAppSettingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webAppSettingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /web-app-settings} : get all the webAppSettings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of webAppSettings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WebAppSettingsDTO>> getAllWebAppSettings(
        WebAppSettingsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get WebAppSettings by criteria: {}", criteria);

        Page<WebAppSettingsDTO> page = webAppSettingsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /web-app-settings/count} : count all the webAppSettings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWebAppSettings(WebAppSettingsCriteria criteria) {
        log.debug("REST request to count WebAppSettings by criteria: {}", criteria);
        return ResponseEntity.ok().body(webAppSettingsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /web-app-settings/:id} : get the "id" webAppSettings.
     *
     * @param id the id of the webAppSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the webAppSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WebAppSettingsDTO> getWebAppSettings(@PathVariable("id") Integer id) {
        log.debug("REST request to get WebAppSettings : {}", id);
        Optional<WebAppSettingsDTO> webAppSettingsDTO = webAppSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(webAppSettingsDTO);
    }

    /**
     * {@code DELETE  /web-app-settings/:id} : delete the "id" webAppSettings.
     *
     * @param id the id of the webAppSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebAppSettings(@PathVariable("id") Integer id) {
        log.debug("REST request to delete WebAppSettings : {}", id);
        webAppSettingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
