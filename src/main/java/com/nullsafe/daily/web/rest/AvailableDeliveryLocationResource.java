package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.AvailableDeliveryLocationRepository;
import com.nullsafe.daily.service.AvailableDeliveryLocationQueryService;
import com.nullsafe.daily.service.AvailableDeliveryLocationService;
import com.nullsafe.daily.service.criteria.AvailableDeliveryLocationCriteria;
import com.nullsafe.daily.service.dto.AvailableDeliveryLocationDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.AvailableDeliveryLocation}.
 */
@RestController
@RequestMapping("/api/available-delivery-locations")
public class AvailableDeliveryLocationResource {

    private final Logger log = LoggerFactory.getLogger(AvailableDeliveryLocationResource.class);

    private static final String ENTITY_NAME = "availableDeliveryLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvailableDeliveryLocationService availableDeliveryLocationService;

    private final AvailableDeliveryLocationRepository availableDeliveryLocationRepository;

    private final AvailableDeliveryLocationQueryService availableDeliveryLocationQueryService;

    public AvailableDeliveryLocationResource(
        AvailableDeliveryLocationService availableDeliveryLocationService,
        AvailableDeliveryLocationRepository availableDeliveryLocationRepository,
        AvailableDeliveryLocationQueryService availableDeliveryLocationQueryService
    ) {
        this.availableDeliveryLocationService = availableDeliveryLocationService;
        this.availableDeliveryLocationRepository = availableDeliveryLocationRepository;
        this.availableDeliveryLocationQueryService = availableDeliveryLocationQueryService;
    }

    /**
     * {@code POST  /available-delivery-locations} : Create a new availableDeliveryLocation.
     *
     * @param availableDeliveryLocationDTO the availableDeliveryLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new availableDeliveryLocationDTO, or with status {@code 400 (Bad Request)} if the availableDeliveryLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AvailableDeliveryLocationDTO> createAvailableDeliveryLocation(
        @Valid @RequestBody AvailableDeliveryLocationDTO availableDeliveryLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AvailableDeliveryLocation : {}", availableDeliveryLocationDTO);
        if (availableDeliveryLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new availableDeliveryLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AvailableDeliveryLocationDTO result = availableDeliveryLocationService.save(availableDeliveryLocationDTO);
        return ResponseEntity
            .created(new URI("/api/available-delivery-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /available-delivery-locations/:id} : Updates an existing availableDeliveryLocation.
     *
     * @param id the id of the availableDeliveryLocationDTO to save.
     * @param availableDeliveryLocationDTO the availableDeliveryLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated availableDeliveryLocationDTO,
     * or with status {@code 400 (Bad Request)} if the availableDeliveryLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the availableDeliveryLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AvailableDeliveryLocationDTO> updateAvailableDeliveryLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AvailableDeliveryLocationDTO availableDeliveryLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AvailableDeliveryLocation : {}, {}", id, availableDeliveryLocationDTO);
        if (availableDeliveryLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, availableDeliveryLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!availableDeliveryLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AvailableDeliveryLocationDTO result = availableDeliveryLocationService.update(availableDeliveryLocationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, availableDeliveryLocationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /available-delivery-locations/:id} : Partial updates given fields of an existing availableDeliveryLocation, field will ignore if it is null
     *
     * @param id the id of the availableDeliveryLocationDTO to save.
     * @param availableDeliveryLocationDTO the availableDeliveryLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated availableDeliveryLocationDTO,
     * or with status {@code 400 (Bad Request)} if the availableDeliveryLocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the availableDeliveryLocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the availableDeliveryLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AvailableDeliveryLocationDTO> partialUpdateAvailableDeliveryLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AvailableDeliveryLocationDTO availableDeliveryLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AvailableDeliveryLocation partially : {}, {}", id, availableDeliveryLocationDTO);
        if (availableDeliveryLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, availableDeliveryLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!availableDeliveryLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AvailableDeliveryLocationDTO> result = availableDeliveryLocationService.partialUpdate(availableDeliveryLocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, availableDeliveryLocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /available-delivery-locations} : get all the availableDeliveryLocations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of availableDeliveryLocations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AvailableDeliveryLocationDTO>> getAllAvailableDeliveryLocations(
        AvailableDeliveryLocationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AvailableDeliveryLocations by criteria: {}", criteria);

        Page<AvailableDeliveryLocationDTO> page = availableDeliveryLocationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /available-delivery-locations/count} : count all the availableDeliveryLocations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAvailableDeliveryLocations(AvailableDeliveryLocationCriteria criteria) {
        log.debug("REST request to count AvailableDeliveryLocations by criteria: {}", criteria);
        return ResponseEntity.ok().body(availableDeliveryLocationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /available-delivery-locations/:id} : get the "id" availableDeliveryLocation.
     *
     * @param id the id of the availableDeliveryLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the availableDeliveryLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AvailableDeliveryLocationDTO> getAvailableDeliveryLocation(@PathVariable("id") Long id) {
        log.debug("REST request to get AvailableDeliveryLocation : {}", id);
        Optional<AvailableDeliveryLocationDTO> availableDeliveryLocationDTO = availableDeliveryLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(availableDeliveryLocationDTO);
    }

    /**
     * {@code DELETE  /available-delivery-locations/:id} : delete the "id" availableDeliveryLocation.
     *
     * @param id the id of the availableDeliveryLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailableDeliveryLocation(@PathVariable("id") Long id) {
        log.debug("REST request to delete AvailableDeliveryLocation : {}", id);
        availableDeliveryLocationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
