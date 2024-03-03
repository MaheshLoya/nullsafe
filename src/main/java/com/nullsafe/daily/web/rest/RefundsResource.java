package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.RefundsRepository;
import com.nullsafe.daily.service.RefundsQueryService;
import com.nullsafe.daily.service.RefundsService;
import com.nullsafe.daily.service.criteria.RefundsCriteria;
import com.nullsafe.daily.service.dto.RefundsDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.Refunds}.
 */
@RestController
@RequestMapping("/api/refunds")
public class RefundsResource {

    private final Logger log = LoggerFactory.getLogger(RefundsResource.class);

    private static final String ENTITY_NAME = "refunds";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RefundsService refundsService;

    private final RefundsRepository refundsRepository;

    private final RefundsQueryService refundsQueryService;

    public RefundsResource(RefundsService refundsService, RefundsRepository refundsRepository, RefundsQueryService refundsQueryService) {
        this.refundsService = refundsService;
        this.refundsRepository = refundsRepository;
        this.refundsQueryService = refundsQueryService;
    }

    /**
     * {@code POST  /refunds} : Create a new refunds.
     *
     * @param refundsDTO the refundsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new refundsDTO, or with status {@code 400 (Bad Request)} if the refunds has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RefundsDTO> createRefunds(@Valid @RequestBody RefundsDTO refundsDTO) throws URISyntaxException {
        log.debug("REST request to save Refunds : {}", refundsDTO);
        if (refundsDTO.getId() != null) {
            throw new BadRequestAlertException("A new refunds cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RefundsDTO result = refundsService.save(refundsDTO);
        return ResponseEntity
            .created(new URI("/api/refunds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /refunds/:id} : Updates an existing refunds.
     *
     * @param id the id of the refundsDTO to save.
     * @param refundsDTO the refundsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refundsDTO,
     * or with status {@code 400 (Bad Request)} if the refundsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the refundsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RefundsDTO> updateRefunds(
        @PathVariable(value = "id", required = false) final Integer id,
        @Valid @RequestBody RefundsDTO refundsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Refunds : {}, {}", id, refundsDTO);
        if (refundsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refundsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!refundsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RefundsDTO result = refundsService.update(refundsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, refundsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /refunds/:id} : Partial updates given fields of an existing refunds, field will ignore if it is null
     *
     * @param id the id of the refundsDTO to save.
     * @param refundsDTO the refundsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refundsDTO,
     * or with status {@code 400 (Bad Request)} if the refundsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the refundsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the refundsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RefundsDTO> partialUpdateRefunds(
        @PathVariable(value = "id", required = false) final Integer id,
        @NotNull @RequestBody RefundsDTO refundsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Refunds partially : {}, {}", id, refundsDTO);
        if (refundsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refundsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!refundsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RefundsDTO> result = refundsService.partialUpdate(refundsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, refundsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /refunds} : get all the refunds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of refunds in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RefundsDTO>> getAllRefunds(
        RefundsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Refunds by criteria: {}", criteria);

        Page<RefundsDTO> page = refundsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /refunds/count} : count all the refunds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRefunds(RefundsCriteria criteria) {
        log.debug("REST request to count Refunds by criteria: {}", criteria);
        return ResponseEntity.ok().body(refundsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /refunds/:id} : get the "id" refunds.
     *
     * @param id the id of the refundsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the refundsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RefundsDTO> getRefunds(@PathVariable("id") Integer id) {
        log.debug("REST request to get Refunds : {}", id);
        Optional<RefundsDTO> refundsDTO = refundsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(refundsDTO);
    }

    /**
     * {@code DELETE  /refunds/:id} : delete the "id" refunds.
     *
     * @param id the id of the refundsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefunds(@PathVariable("id") Integer id) {
        log.debug("REST request to delete Refunds : {}", id);
        refundsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
