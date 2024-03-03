package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.SubscribedOrderDeliveryRepository;
import com.nullsafe.daily.service.SubscribedOrderDeliveryQueryService;
import com.nullsafe.daily.service.SubscribedOrderDeliveryService;
import com.nullsafe.daily.service.criteria.SubscribedOrderDeliveryCriteria;
import com.nullsafe.daily.service.dto.SubscribedOrderDeliveryDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.SubscribedOrderDelivery}.
 */
@RestController
@RequestMapping("/api/subscribed-order-deliveries")
public class SubscribedOrderDeliveryResource {

    private final Logger log = LoggerFactory.getLogger(SubscribedOrderDeliveryResource.class);

    private static final String ENTITY_NAME = "subscribedOrderDelivery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscribedOrderDeliveryService subscribedOrderDeliveryService;

    private final SubscribedOrderDeliveryRepository subscribedOrderDeliveryRepository;

    private final SubscribedOrderDeliveryQueryService subscribedOrderDeliveryQueryService;

    public SubscribedOrderDeliveryResource(
        SubscribedOrderDeliveryService subscribedOrderDeliveryService,
        SubscribedOrderDeliveryRepository subscribedOrderDeliveryRepository,
        SubscribedOrderDeliveryQueryService subscribedOrderDeliveryQueryService
    ) {
        this.subscribedOrderDeliveryService = subscribedOrderDeliveryService;
        this.subscribedOrderDeliveryRepository = subscribedOrderDeliveryRepository;
        this.subscribedOrderDeliveryQueryService = subscribedOrderDeliveryQueryService;
    }

    /**
     * {@code POST  /subscribed-order-deliveries} : Create a new subscribedOrderDelivery.
     *
     * @param subscribedOrderDeliveryDTO the subscribedOrderDeliveryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscribedOrderDeliveryDTO, or with status {@code 400 (Bad Request)} if the subscribedOrderDelivery has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscribedOrderDeliveryDTO> createSubscribedOrderDelivery(
        @Valid @RequestBody SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SubscribedOrderDelivery : {}", subscribedOrderDeliveryDTO);
        if (subscribedOrderDeliveryDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscribedOrderDelivery cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscribedOrderDeliveryDTO result = subscribedOrderDeliveryService.save(subscribedOrderDeliveryDTO);
        return ResponseEntity
            .created(new URI("/api/subscribed-order-deliveries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscribed-order-deliveries/:id} : Updates an existing subscribedOrderDelivery.
     *
     * @param id the id of the subscribedOrderDeliveryDTO to save.
     * @param subscribedOrderDeliveryDTO the subscribedOrderDeliveryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscribedOrderDeliveryDTO,
     * or with status {@code 400 (Bad Request)} if the subscribedOrderDeliveryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscribedOrderDeliveryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscribedOrderDeliveryDTO> updateSubscribedOrderDelivery(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubscribedOrderDelivery : {}, {}", id, subscribedOrderDeliveryDTO);
        if (subscribedOrderDeliveryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscribedOrderDeliveryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscribedOrderDeliveryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubscribedOrderDeliveryDTO result = subscribedOrderDeliveryService.update(subscribedOrderDeliveryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscribedOrderDeliveryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subscribed-order-deliveries/:id} : Partial updates given fields of an existing subscribedOrderDelivery, field will ignore if it is null
     *
     * @param id the id of the subscribedOrderDeliveryDTO to save.
     * @param subscribedOrderDeliveryDTO the subscribedOrderDeliveryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscribedOrderDeliveryDTO,
     * or with status {@code 400 (Bad Request)} if the subscribedOrderDeliveryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscribedOrderDeliveryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscribedOrderDeliveryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscribedOrderDeliveryDTO> partialUpdateSubscribedOrderDelivery(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscribedOrderDelivery partially : {}, {}", id, subscribedOrderDeliveryDTO);
        if (subscribedOrderDeliveryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscribedOrderDeliveryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscribedOrderDeliveryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscribedOrderDeliveryDTO> result = subscribedOrderDeliveryService.partialUpdate(subscribedOrderDeliveryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscribedOrderDeliveryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscribed-order-deliveries} : get all the subscribedOrderDeliveries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscribedOrderDeliveries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubscribedOrderDeliveryDTO>> getAllSubscribedOrderDeliveries(
        SubscribedOrderDeliveryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SubscribedOrderDeliveries by criteria: {}", criteria);

        Page<SubscribedOrderDeliveryDTO> page = subscribedOrderDeliveryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscribed-order-deliveries/count} : count all the subscribedOrderDeliveries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubscribedOrderDeliveries(SubscribedOrderDeliveryCriteria criteria) {
        log.debug("REST request to count SubscribedOrderDeliveries by criteria: {}", criteria);
        return ResponseEntity.ok().body(subscribedOrderDeliveryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /subscribed-order-deliveries/:id} : get the "id" subscribedOrderDelivery.
     *
     * @param id the id of the subscribedOrderDeliveryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscribedOrderDeliveryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscribedOrderDeliveryDTO> getSubscribedOrderDelivery(@PathVariable("id") Long id) {
        log.debug("REST request to get SubscribedOrderDelivery : {}", id);
        Optional<SubscribedOrderDeliveryDTO> subscribedOrderDeliveryDTO = subscribedOrderDeliveryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscribedOrderDeliveryDTO);
    }

    /**
     * {@code DELETE  /subscribed-order-deliveries/:id} : delete the "id" subscribedOrderDelivery.
     *
     * @param id the id of the subscribedOrderDeliveryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscribedOrderDelivery(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubscribedOrderDelivery : {}", id);
        subscribedOrderDeliveryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
