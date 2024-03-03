package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.InvoiceSettingRepository;
import com.nullsafe.daily.service.InvoiceSettingQueryService;
import com.nullsafe.daily.service.InvoiceSettingService;
import com.nullsafe.daily.service.criteria.InvoiceSettingCriteria;
import com.nullsafe.daily.service.dto.InvoiceSettingDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.InvoiceSetting}.
 */
@RestController
@RequestMapping("/api/invoice-settings")
public class InvoiceSettingResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceSettingResource.class);

    private static final String ENTITY_NAME = "invoiceSetting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceSettingService invoiceSettingService;

    private final InvoiceSettingRepository invoiceSettingRepository;

    private final InvoiceSettingQueryService invoiceSettingQueryService;

    public InvoiceSettingResource(
        InvoiceSettingService invoiceSettingService,
        InvoiceSettingRepository invoiceSettingRepository,
        InvoiceSettingQueryService invoiceSettingQueryService
    ) {
        this.invoiceSettingService = invoiceSettingService;
        this.invoiceSettingRepository = invoiceSettingRepository;
        this.invoiceSettingQueryService = invoiceSettingQueryService;
    }

    /**
     * {@code POST  /invoice-settings} : Create a new invoiceSetting.
     *
     * @param invoiceSettingDTO the invoiceSettingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceSettingDTO, or with status {@code 400 (Bad Request)} if the invoiceSetting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceSettingDTO> createInvoiceSetting(@Valid @RequestBody InvoiceSettingDTO invoiceSettingDTO)
        throws URISyntaxException {
        log.debug("REST request to save InvoiceSetting : {}", invoiceSettingDTO);
        if (invoiceSettingDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceSettingDTO result = invoiceSettingService.save(invoiceSettingDTO);
        return ResponseEntity
            .created(new URI("/api/invoice-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-settings/:id} : Updates an existing invoiceSetting.
     *
     * @param id the id of the invoiceSettingDTO to save.
     * @param invoiceSettingDTO the invoiceSettingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceSettingDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceSettingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceSettingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceSettingDTO> updateInvoiceSetting(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceSettingDTO invoiceSettingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceSetting : {}, {}", id, invoiceSettingDTO);
        if (invoiceSettingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceSettingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceSettingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceSettingDTO result = invoiceSettingService.update(invoiceSettingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceSettingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-settings/:id} : Partial updates given fields of an existing invoiceSetting, field will ignore if it is null
     *
     * @param id the id of the invoiceSettingDTO to save.
     * @param invoiceSettingDTO the invoiceSettingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceSettingDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceSettingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceSettingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceSettingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceSettingDTO> partialUpdateInvoiceSetting(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceSettingDTO invoiceSettingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceSetting partially : {}, {}", id, invoiceSettingDTO);
        if (invoiceSettingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceSettingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceSettingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceSettingDTO> result = invoiceSettingService.partialUpdate(invoiceSettingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceSettingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-settings} : get all the invoiceSettings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceSettings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceSettingDTO>> getAllInvoiceSettings(
        InvoiceSettingCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get InvoiceSettings by criteria: {}", criteria);

        Page<InvoiceSettingDTO> page = invoiceSettingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-settings/count} : count all the invoiceSettings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceSettings(InvoiceSettingCriteria criteria) {
        log.debug("REST request to count InvoiceSettings by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceSettingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-settings/:id} : get the "id" invoiceSetting.
     *
     * @param id the id of the invoiceSettingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceSettingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceSettingDTO> getInvoiceSetting(@PathVariable("id") Long id) {
        log.debug("REST request to get InvoiceSetting : {}", id);
        Optional<InvoiceSettingDTO> invoiceSettingDTO = invoiceSettingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceSettingDTO);
    }

    /**
     * {@code DELETE  /invoice-settings/:id} : delete the "id" invoiceSetting.
     *
     * @param id the id of the invoiceSettingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceSetting(@PathVariable("id") Long id) {
        log.debug("REST request to delete InvoiceSetting : {}", id);
        invoiceSettingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
