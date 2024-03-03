package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.OrderUserAssignRepository;
import com.nullsafe.daily.service.OrderUserAssignQueryService;
import com.nullsafe.daily.service.OrderUserAssignService;
import com.nullsafe.daily.service.criteria.OrderUserAssignCriteria;
import com.nullsafe.daily.service.dto.OrderUserAssignDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.OrderUserAssign}.
 */
@RestController
@RequestMapping("/api/order-user-assigns")
public class OrderUserAssignResource {

    private final Logger log = LoggerFactory.getLogger(OrderUserAssignResource.class);

    private static final String ENTITY_NAME = "orderUserAssign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderUserAssignService orderUserAssignService;

    private final OrderUserAssignRepository orderUserAssignRepository;

    private final OrderUserAssignQueryService orderUserAssignQueryService;

    public OrderUserAssignResource(
        OrderUserAssignService orderUserAssignService,
        OrderUserAssignRepository orderUserAssignRepository,
        OrderUserAssignQueryService orderUserAssignQueryService
    ) {
        this.orderUserAssignService = orderUserAssignService;
        this.orderUserAssignRepository = orderUserAssignRepository;
        this.orderUserAssignQueryService = orderUserAssignQueryService;
    }

    /**
     * {@code POST  /order-user-assigns} : Create a new orderUserAssign.
     *
     * @param orderUserAssignDTO the orderUserAssignDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderUserAssignDTO, or with status {@code 400 (Bad Request)} if the orderUserAssign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderUserAssignDTO> createOrderUserAssign(@Valid @RequestBody OrderUserAssignDTO orderUserAssignDTO)
        throws URISyntaxException {
        log.debug("REST request to save OrderUserAssign : {}", orderUserAssignDTO);
        if (orderUserAssignDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderUserAssign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderUserAssignDTO result = orderUserAssignService.save(orderUserAssignDTO);
        return ResponseEntity
            .created(new URI("/api/order-user-assigns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-user-assigns/:id} : Updates an existing orderUserAssign.
     *
     * @param id the id of the orderUserAssignDTO to save.
     * @param orderUserAssignDTO the orderUserAssignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderUserAssignDTO,
     * or with status {@code 400 (Bad Request)} if the orderUserAssignDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderUserAssignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderUserAssignDTO> updateOrderUserAssign(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderUserAssignDTO orderUserAssignDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrderUserAssign : {}, {}", id, orderUserAssignDTO);
        if (orderUserAssignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderUserAssignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderUserAssignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderUserAssignDTO result = orderUserAssignService.update(orderUserAssignDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderUserAssignDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-user-assigns/:id} : Partial updates given fields of an existing orderUserAssign, field will ignore if it is null
     *
     * @param id the id of the orderUserAssignDTO to save.
     * @param orderUserAssignDTO the orderUserAssignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderUserAssignDTO,
     * or with status {@code 400 (Bad Request)} if the orderUserAssignDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderUserAssignDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderUserAssignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderUserAssignDTO> partialUpdateOrderUserAssign(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderUserAssignDTO orderUserAssignDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderUserAssign partially : {}, {}", id, orderUserAssignDTO);
        if (orderUserAssignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderUserAssignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderUserAssignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderUserAssignDTO> result = orderUserAssignService.partialUpdate(orderUserAssignDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderUserAssignDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-user-assigns} : get all the orderUserAssigns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderUserAssigns in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderUserAssignDTO>> getAllOrderUserAssigns(
        OrderUserAssignCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OrderUserAssigns by criteria: {}", criteria);

        Page<OrderUserAssignDTO> page = orderUserAssignQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-user-assigns/count} : count all the orderUserAssigns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderUserAssigns(OrderUserAssignCriteria criteria) {
        log.debug("REST request to count OrderUserAssigns by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderUserAssignQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-user-assigns/:id} : get the "id" orderUserAssign.
     *
     * @param id the id of the orderUserAssignDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderUserAssignDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderUserAssignDTO> getOrderUserAssign(@PathVariable("id") Long id) {
        log.debug("REST request to get OrderUserAssign : {}", id);
        Optional<OrderUserAssignDTO> orderUserAssignDTO = orderUserAssignService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderUserAssignDTO);
    }

    /**
     * {@code DELETE  /order-user-assigns/:id} : delete the "id" orderUserAssign.
     *
     * @param id the id of the orderUserAssignDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderUserAssign(@PathVariable("id") Long id) {
        log.debug("REST request to delete OrderUserAssign : {}", id);
        orderUserAssignService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
