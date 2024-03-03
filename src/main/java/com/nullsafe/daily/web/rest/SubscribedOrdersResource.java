package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.SubscribedOrdersRepository;
import com.nullsafe.daily.service.SubscribedOrdersQueryService;
import com.nullsafe.daily.service.SubscribedOrdersService;
import com.nullsafe.daily.service.criteria.SubscribedOrdersCriteria;
import com.nullsafe.daily.service.dto.SubscribedOrdersDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.SubscribedOrders}.
 */
@RestController
@RequestMapping("/api/subscribed-orders")
public class SubscribedOrdersResource {

    private final Logger log = LoggerFactory.getLogger(SubscribedOrdersResource.class);

    private static final String ENTITY_NAME = "subscribedOrders";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscribedOrdersService subscribedOrdersService;

    private final SubscribedOrdersRepository subscribedOrdersRepository;

    private final SubscribedOrdersQueryService subscribedOrdersQueryService;

    public SubscribedOrdersResource(
        SubscribedOrdersService subscribedOrdersService,
        SubscribedOrdersRepository subscribedOrdersRepository,
        SubscribedOrdersQueryService subscribedOrdersQueryService
    ) {
        this.subscribedOrdersService = subscribedOrdersService;
        this.subscribedOrdersRepository = subscribedOrdersRepository;
        this.subscribedOrdersQueryService = subscribedOrdersQueryService;
    }

    /**
     * {@code POST  /subscribed-orders} : Create a new subscribedOrders.
     *
     * @param subscribedOrdersDTO the subscribedOrdersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscribedOrdersDTO, or with status {@code 400 (Bad Request)} if the subscribedOrders has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscribedOrdersDTO> createSubscribedOrders(@Valid @RequestBody SubscribedOrdersDTO subscribedOrdersDTO)
        throws URISyntaxException {
        log.debug("REST request to save SubscribedOrders : {}", subscribedOrdersDTO);
        if (subscribedOrdersDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscribedOrders cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscribedOrdersDTO result = subscribedOrdersService.save(subscribedOrdersDTO);
        return ResponseEntity
            .created(new URI("/api/subscribed-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscribed-orders/:id} : Updates an existing subscribedOrders.
     *
     * @param id the id of the subscribedOrdersDTO to save.
     * @param subscribedOrdersDTO the subscribedOrdersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscribedOrdersDTO,
     * or with status {@code 400 (Bad Request)} if the subscribedOrdersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscribedOrdersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscribedOrdersDTO> updateSubscribedOrders(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscribedOrdersDTO subscribedOrdersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubscribedOrders : {}, {}", id, subscribedOrdersDTO);
        if (subscribedOrdersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscribedOrdersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscribedOrdersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubscribedOrdersDTO result = subscribedOrdersService.update(subscribedOrdersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscribedOrdersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subscribed-orders/:id} : Partial updates given fields of an existing subscribedOrders, field will ignore if it is null
     *
     * @param id the id of the subscribedOrdersDTO to save.
     * @param subscribedOrdersDTO the subscribedOrdersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscribedOrdersDTO,
     * or with status {@code 400 (Bad Request)} if the subscribedOrdersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscribedOrdersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscribedOrdersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscribedOrdersDTO> partialUpdateSubscribedOrders(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscribedOrdersDTO subscribedOrdersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscribedOrders partially : {}, {}", id, subscribedOrdersDTO);
        if (subscribedOrdersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscribedOrdersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscribedOrdersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscribedOrdersDTO> result = subscribedOrdersService.partialUpdate(subscribedOrdersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscribedOrdersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscribed-orders} : get all the subscribedOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscribedOrders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubscribedOrdersDTO>> getAllSubscribedOrders(
        SubscribedOrdersCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SubscribedOrders by criteria: {}", criteria);

        Page<SubscribedOrdersDTO> page = subscribedOrdersQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscribed-orders/count} : count all the subscribedOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubscribedOrders(SubscribedOrdersCriteria criteria) {
        log.debug("REST request to count SubscribedOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(subscribedOrdersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /subscribed-orders/:id} : get the "id" subscribedOrders.
     *
     * @param id the id of the subscribedOrdersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscribedOrdersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscribedOrdersDTO> getSubscribedOrders(@PathVariable("id") Long id) {
        log.debug("REST request to get SubscribedOrders : {}", id);
        Optional<SubscribedOrdersDTO> subscribedOrdersDTO = subscribedOrdersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscribedOrdersDTO);
    }

    /**
     * {@code DELETE  /subscribed-orders/:id} : delete the "id" subscribedOrders.
     *
     * @param id the id of the subscribedOrdersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscribedOrders(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubscribedOrders : {}", id);
        subscribedOrdersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
