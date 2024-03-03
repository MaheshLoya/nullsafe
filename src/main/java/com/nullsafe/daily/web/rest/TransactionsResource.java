package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.TransactionsRepository;
import com.nullsafe.daily.service.TransactionsQueryService;
import com.nullsafe.daily.service.TransactionsService;
import com.nullsafe.daily.service.criteria.TransactionsCriteria;
import com.nullsafe.daily.service.dto.TransactionsDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.Transactions}.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionsResource {

    private final Logger log = LoggerFactory.getLogger(TransactionsResource.class);

    private static final String ENTITY_NAME = "transactions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionsService transactionsService;

    private final TransactionsRepository transactionsRepository;

    private final TransactionsQueryService transactionsQueryService;

    public TransactionsResource(
        TransactionsService transactionsService,
        TransactionsRepository transactionsRepository,
        TransactionsQueryService transactionsQueryService
    ) {
        this.transactionsService = transactionsService;
        this.transactionsRepository = transactionsRepository;
        this.transactionsQueryService = transactionsQueryService;
    }

    /**
     * {@code POST  /transactions} : Create a new transactions.
     *
     * @param transactionsDTO the transactionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionsDTO, or with status {@code 400 (Bad Request)} if the transactions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransactionsDTO> createTransactions(@Valid @RequestBody TransactionsDTO transactionsDTO)
        throws URISyntaxException {
        log.debug("REST request to save Transactions : {}", transactionsDTO);
        if (transactionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionsDTO result = transactionsService.save(transactionsDTO);
        return ResponseEntity
            .created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transactions/:id} : Updates an existing transactions.
     *
     * @param id the id of the transactionsDTO to save.
     * @param transactionsDTO the transactionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionsDTO,
     * or with status {@code 400 (Bad Request)} if the transactionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionsDTO> updateTransactions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionsDTO transactionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transactions : {}, {}", id, transactionsDTO);
        if (transactionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionsDTO result = transactionsService.update(transactionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transactions/:id} : Partial updates given fields of an existing transactions, field will ignore if it is null
     *
     * @param id the id of the transactionsDTO to save.
     * @param transactionsDTO the transactionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionsDTO,
     * or with status {@code 400 (Bad Request)} if the transactionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionsDTO> partialUpdateTransactions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionsDTO transactionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transactions partially : {}, {}", id, transactionsDTO);
        if (transactionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionsDTO> result = transactionsService.partialUpdate(transactionsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transactions} : get all the transactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransactionsDTO>> getAllTransactions(
        TransactionsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Transactions by criteria: {}", criteria);

        Page<TransactionsDTO> page = transactionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transactions/count} : count all the transactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransactions(TransactionsCriteria criteria) {
        log.debug("REST request to count Transactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transactions.
     *
     * @param id the id of the transactionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionsDTO> getTransactions(@PathVariable("id") Long id) {
        log.debug("REST request to get Transactions : {}", id);
        Optional<TransactionsDTO> transactionsDTO = transactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionsDTO);
    }

    /**
     * {@code DELETE  /transactions/:id} : delete the "id" transactions.
     *
     * @param id the id of the transactionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactions(@PathVariable("id") Long id) {
        log.debug("REST request to delete Transactions : {}", id);
        transactionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
