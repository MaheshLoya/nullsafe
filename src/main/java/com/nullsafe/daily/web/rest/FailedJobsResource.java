package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.FailedJobsRepository;
import com.nullsafe.daily.service.FailedJobsQueryService;
import com.nullsafe.daily.service.FailedJobsService;
import com.nullsafe.daily.service.criteria.FailedJobsCriteria;
import com.nullsafe.daily.service.dto.FailedJobsDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.FailedJobs}.
 */
@RestController
@RequestMapping("/api/failed-jobs")
public class FailedJobsResource {

    private final Logger log = LoggerFactory.getLogger(FailedJobsResource.class);

    private static final String ENTITY_NAME = "failedJobs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FailedJobsService failedJobsService;

    private final FailedJobsRepository failedJobsRepository;

    private final FailedJobsQueryService failedJobsQueryService;

    public FailedJobsResource(
        FailedJobsService failedJobsService,
        FailedJobsRepository failedJobsRepository,
        FailedJobsQueryService failedJobsQueryService
    ) {
        this.failedJobsService = failedJobsService;
        this.failedJobsRepository = failedJobsRepository;
        this.failedJobsQueryService = failedJobsQueryService;
    }

    /**
     * {@code POST  /failed-jobs} : Create a new failedJobs.
     *
     * @param failedJobsDTO the failedJobsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new failedJobsDTO, or with status {@code 400 (Bad Request)} if the failedJobs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FailedJobsDTO> createFailedJobs(@Valid @RequestBody FailedJobsDTO failedJobsDTO) throws URISyntaxException {
        log.debug("REST request to save FailedJobs : {}", failedJobsDTO);
        if (failedJobsDTO.getId() != null) {
            throw new BadRequestAlertException("A new failedJobs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FailedJobsDTO result = failedJobsService.save(failedJobsDTO);
        return ResponseEntity
            .created(new URI("/api/failed-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /failed-jobs/:id} : Updates an existing failedJobs.
     *
     * @param id the id of the failedJobsDTO to save.
     * @param failedJobsDTO the failedJobsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated failedJobsDTO,
     * or with status {@code 400 (Bad Request)} if the failedJobsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the failedJobsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FailedJobsDTO> updateFailedJobs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FailedJobsDTO failedJobsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FailedJobs : {}, {}", id, failedJobsDTO);
        if (failedJobsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, failedJobsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!failedJobsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FailedJobsDTO result = failedJobsService.update(failedJobsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, failedJobsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /failed-jobs/:id} : Partial updates given fields of an existing failedJobs, field will ignore if it is null
     *
     * @param id the id of the failedJobsDTO to save.
     * @param failedJobsDTO the failedJobsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated failedJobsDTO,
     * or with status {@code 400 (Bad Request)} if the failedJobsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the failedJobsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the failedJobsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FailedJobsDTO> partialUpdateFailedJobs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FailedJobsDTO failedJobsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FailedJobs partially : {}, {}", id, failedJobsDTO);
        if (failedJobsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, failedJobsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!failedJobsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FailedJobsDTO> result = failedJobsService.partialUpdate(failedJobsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, failedJobsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /failed-jobs} : get all the failedJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of failedJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FailedJobsDTO>> getAllFailedJobs(
        FailedJobsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get FailedJobs by criteria: {}", criteria);

        Page<FailedJobsDTO> page = failedJobsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /failed-jobs/count} : count all the failedJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFailedJobs(FailedJobsCriteria criteria) {
        log.debug("REST request to count FailedJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(failedJobsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /failed-jobs/:id} : get the "id" failedJobs.
     *
     * @param id the id of the failedJobsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the failedJobsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FailedJobsDTO> getFailedJobs(@PathVariable("id") Long id) {
        log.debug("REST request to get FailedJobs : {}", id);
        Optional<FailedJobsDTO> failedJobsDTO = failedJobsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(failedJobsDTO);
    }

    /**
     * {@code DELETE  /failed-jobs/:id} : delete the "id" failedJobs.
     *
     * @param id the id of the failedJobsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFailedJobs(@PathVariable("id") Long id) {
        log.debug("REST request to delete FailedJobs : {}", id);
        failedJobsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
