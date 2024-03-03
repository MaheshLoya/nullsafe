package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.SpecificNotificationRepository;
import com.nullsafe.daily.service.SpecificNotificationQueryService;
import com.nullsafe.daily.service.SpecificNotificationService;
import com.nullsafe.daily.service.criteria.SpecificNotificationCriteria;
import com.nullsafe.daily.service.dto.SpecificNotificationDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.SpecificNotification}.
 */
@RestController
@RequestMapping("/api/specific-notifications")
public class SpecificNotificationResource {

    private final Logger log = LoggerFactory.getLogger(SpecificNotificationResource.class);

    private static final String ENTITY_NAME = "specificNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecificNotificationService specificNotificationService;

    private final SpecificNotificationRepository specificNotificationRepository;

    private final SpecificNotificationQueryService specificNotificationQueryService;

    public SpecificNotificationResource(
        SpecificNotificationService specificNotificationService,
        SpecificNotificationRepository specificNotificationRepository,
        SpecificNotificationQueryService specificNotificationQueryService
    ) {
        this.specificNotificationService = specificNotificationService;
        this.specificNotificationRepository = specificNotificationRepository;
        this.specificNotificationQueryService = specificNotificationQueryService;
    }

    /**
     * {@code POST  /specific-notifications} : Create a new specificNotification.
     *
     * @param specificNotificationDTO the specificNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specificNotificationDTO, or with status {@code 400 (Bad Request)} if the specificNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecificNotificationDTO> createSpecificNotification(
        @Valid @RequestBody SpecificNotificationDTO specificNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SpecificNotification : {}", specificNotificationDTO);
        if (specificNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new specificNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecificNotificationDTO result = specificNotificationService.save(specificNotificationDTO);
        return ResponseEntity
            .created(new URI("/api/specific-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specific-notifications/:id} : Updates an existing specificNotification.
     *
     * @param id the id of the specificNotificationDTO to save.
     * @param specificNotificationDTO the specificNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specificNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the specificNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specificNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecificNotificationDTO> updateSpecificNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecificNotificationDTO specificNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SpecificNotification : {}, {}", id, specificNotificationDTO);
        if (specificNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specificNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specificNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecificNotificationDTO result = specificNotificationService.update(specificNotificationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specificNotificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specific-notifications/:id} : Partial updates given fields of an existing specificNotification, field will ignore if it is null
     *
     * @param id the id of the specificNotificationDTO to save.
     * @param specificNotificationDTO the specificNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specificNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the specificNotificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specificNotificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specificNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecificNotificationDTO> partialUpdateSpecificNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecificNotificationDTO specificNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpecificNotification partially : {}, {}", id, specificNotificationDTO);
        if (specificNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specificNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specificNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecificNotificationDTO> result = specificNotificationService.partialUpdate(specificNotificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specificNotificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /specific-notifications} : get all the specificNotifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specificNotifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpecificNotificationDTO>> getAllSpecificNotifications(
        SpecificNotificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SpecificNotifications by criteria: {}", criteria);

        Page<SpecificNotificationDTO> page = specificNotificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specific-notifications/count} : count all the specificNotifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSpecificNotifications(SpecificNotificationCriteria criteria) {
        log.debug("REST request to count SpecificNotifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(specificNotificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /specific-notifications/:id} : get the "id" specificNotification.
     *
     * @param id the id of the specificNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specificNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecificNotificationDTO> getSpecificNotification(@PathVariable("id") Long id) {
        log.debug("REST request to get SpecificNotification : {}", id);
        Optional<SpecificNotificationDTO> specificNotificationDTO = specificNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specificNotificationDTO);
    }

    /**
     * {@code DELETE  /specific-notifications/:id} : delete the "id" specificNotification.
     *
     * @param id the id of the specificNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecificNotification(@PathVariable("id") Long id) {
        log.debug("REST request to delete SpecificNotification : {}", id);
        specificNotificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
