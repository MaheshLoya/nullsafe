package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.SubscriptionRenewalRepository;
import com.nullsafe.daily.service.SubscriptionRenewalQueryService;
import com.nullsafe.daily.service.SubscriptionRenewalService;
import com.nullsafe.daily.service.criteria.SubscriptionRenewalCriteria;
import com.nullsafe.daily.service.dto.SubscriptionRenewalDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.SubscriptionRenewal}.
 */
@RestController
@RequestMapping("/api/subscription-renewals")
public class SubscriptionRenewalResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionRenewalResource.class);

    private static final String ENTITY_NAME = "subscriptionRenewal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionRenewalService subscriptionRenewalService;

    private final SubscriptionRenewalRepository subscriptionRenewalRepository;

    private final SubscriptionRenewalQueryService subscriptionRenewalQueryService;

    public SubscriptionRenewalResource(
        SubscriptionRenewalService subscriptionRenewalService,
        SubscriptionRenewalRepository subscriptionRenewalRepository,
        SubscriptionRenewalQueryService subscriptionRenewalQueryService
    ) {
        this.subscriptionRenewalService = subscriptionRenewalService;
        this.subscriptionRenewalRepository = subscriptionRenewalRepository;
        this.subscriptionRenewalQueryService = subscriptionRenewalQueryService;
    }

    /**
     * {@code POST  /subscription-renewals} : Create a new subscriptionRenewal.
     *
     * @param subscriptionRenewalDTO the subscriptionRenewalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionRenewalDTO, or with status {@code 400 (Bad Request)} if the subscriptionRenewal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscriptionRenewalDTO> createSubscriptionRenewal(
        @Valid @RequestBody SubscriptionRenewalDTO subscriptionRenewalDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SubscriptionRenewal : {}", subscriptionRenewalDTO);
        if (subscriptionRenewalDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionRenewal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionRenewalDTO result = subscriptionRenewalService.save(subscriptionRenewalDTO);
        return ResponseEntity
            .created(new URI("/api/subscription-renewals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-renewals/:id} : Updates an existing subscriptionRenewal.
     *
     * @param id the id of the subscriptionRenewalDTO to save.
     * @param subscriptionRenewalDTO the subscriptionRenewalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionRenewalDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionRenewalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionRenewalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionRenewalDTO> updateSubscriptionRenewal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionRenewalDTO subscriptionRenewalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionRenewal : {}, {}", id, subscriptionRenewalDTO);
        if (subscriptionRenewalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionRenewalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionRenewalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubscriptionRenewalDTO result = subscriptionRenewalService.update(subscriptionRenewalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionRenewalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subscription-renewals/:id} : Partial updates given fields of an existing subscriptionRenewal, field will ignore if it is null
     *
     * @param id the id of the subscriptionRenewalDTO to save.
     * @param subscriptionRenewalDTO the subscriptionRenewalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionRenewalDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionRenewalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionRenewalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionRenewalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionRenewalDTO> partialUpdateSubscriptionRenewal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionRenewalDTO subscriptionRenewalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionRenewal partially : {}, {}", id, subscriptionRenewalDTO);
        if (subscriptionRenewalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionRenewalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionRenewalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionRenewalDTO> result = subscriptionRenewalService.partialUpdate(subscriptionRenewalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionRenewalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-renewals} : get all the subscriptionRenewals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionRenewals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubscriptionRenewalDTO>> getAllSubscriptionRenewals(
        SubscriptionRenewalCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SubscriptionRenewals by criteria: {}", criteria);

        Page<SubscriptionRenewalDTO> page = subscriptionRenewalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscription-renewals/count} : count all the subscriptionRenewals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubscriptionRenewals(SubscriptionRenewalCriteria criteria) {
        log.debug("REST request to count SubscriptionRenewals by criteria: {}", criteria);
        return ResponseEntity.ok().body(subscriptionRenewalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /subscription-renewals/:id} : get the "id" subscriptionRenewal.
     *
     * @param id the id of the subscriptionRenewalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionRenewalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionRenewalDTO> getSubscriptionRenewal(@PathVariable("id") Long id) {
        log.debug("REST request to get SubscriptionRenewal : {}", id);
        Optional<SubscriptionRenewalDTO> subscriptionRenewalDTO = subscriptionRenewalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionRenewalDTO);
    }

    /**
     * {@code DELETE  /subscription-renewals/:id} : delete the "id" subscriptionRenewal.
     *
     * @param id the id of the subscriptionRenewalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionRenewal(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubscriptionRenewal : {}", id);
        subscriptionRenewalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
