package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.AllowPincodeRepository;
import com.nullsafe.daily.service.AllowPincodeQueryService;
import com.nullsafe.daily.service.AllowPincodeService;
import com.nullsafe.daily.service.criteria.AllowPincodeCriteria;
import com.nullsafe.daily.service.dto.AllowPincodeDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.AllowPincode}.
 */
@RestController
@RequestMapping("/api/allow-pincodes")
public class AllowPincodeResource {

    private final Logger log = LoggerFactory.getLogger(AllowPincodeResource.class);

    private static final String ENTITY_NAME = "allowPincode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllowPincodeService allowPincodeService;

    private final AllowPincodeRepository allowPincodeRepository;

    private final AllowPincodeQueryService allowPincodeQueryService;

    public AllowPincodeResource(
        AllowPincodeService allowPincodeService,
        AllowPincodeRepository allowPincodeRepository,
        AllowPincodeQueryService allowPincodeQueryService
    ) {
        this.allowPincodeService = allowPincodeService;
        this.allowPincodeRepository = allowPincodeRepository;
        this.allowPincodeQueryService = allowPincodeQueryService;
    }

    /**
     * {@code POST  /allow-pincodes} : Create a new allowPincode.
     *
     * @param allowPincodeDTO the allowPincodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allowPincodeDTO, or with status {@code 400 (Bad Request)} if the allowPincode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AllowPincodeDTO> createAllowPincode(@Valid @RequestBody AllowPincodeDTO allowPincodeDTO)
        throws URISyntaxException {
        log.debug("REST request to save AllowPincode : {}", allowPincodeDTO);
        if (allowPincodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new allowPincode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AllowPincodeDTO result = allowPincodeService.save(allowPincodeDTO);
        return ResponseEntity
            .created(new URI("/api/allow-pincodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /allow-pincodes/:id} : Updates an existing allowPincode.
     *
     * @param id the id of the allowPincodeDTO to save.
     * @param allowPincodeDTO the allowPincodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allowPincodeDTO,
     * or with status {@code 400 (Bad Request)} if the allowPincodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allowPincodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AllowPincodeDTO> updateAllowPincode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AllowPincodeDTO allowPincodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AllowPincode : {}, {}", id, allowPincodeDTO);
        if (allowPincodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allowPincodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allowPincodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AllowPincodeDTO result = allowPincodeService.update(allowPincodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allowPincodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /allow-pincodes/:id} : Partial updates given fields of an existing allowPincode, field will ignore if it is null
     *
     * @param id the id of the allowPincodeDTO to save.
     * @param allowPincodeDTO the allowPincodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allowPincodeDTO,
     * or with status {@code 400 (Bad Request)} if the allowPincodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the allowPincodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the allowPincodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AllowPincodeDTO> partialUpdateAllowPincode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AllowPincodeDTO allowPincodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AllowPincode partially : {}, {}", id, allowPincodeDTO);
        if (allowPincodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allowPincodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allowPincodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AllowPincodeDTO> result = allowPincodeService.partialUpdate(allowPincodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allowPincodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /allow-pincodes} : get all the allowPincodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allowPincodes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AllowPincodeDTO>> getAllAllowPincodes(
        AllowPincodeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AllowPincodes by criteria: {}", criteria);

        Page<AllowPincodeDTO> page = allowPincodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /allow-pincodes/count} : count all the allowPincodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAllowPincodes(AllowPincodeCriteria criteria) {
        log.debug("REST request to count AllowPincodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(allowPincodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /allow-pincodes/:id} : get the "id" allowPincode.
     *
     * @param id the id of the allowPincodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allowPincodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AllowPincodeDTO> getAllowPincode(@PathVariable("id") Long id) {
        log.debug("REST request to get AllowPincode : {}", id);
        Optional<AllowPincodeDTO> allowPincodeDTO = allowPincodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(allowPincodeDTO);
    }

    /**
     * {@code DELETE  /allow-pincodes/:id} : delete the "id" allowPincode.
     *
     * @param id the id of the allowPincodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllowPincode(@PathVariable("id") Long id) {
        log.debug("REST request to delete AllowPincode : {}", id);
        allowPincodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
