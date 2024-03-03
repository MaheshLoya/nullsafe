package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.WebPagesRepository;
import com.nullsafe.daily.service.WebPagesQueryService;
import com.nullsafe.daily.service.WebPagesService;
import com.nullsafe.daily.service.criteria.WebPagesCriteria;
import com.nullsafe.daily.service.dto.WebPagesDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.WebPages}.
 */
@RestController
@RequestMapping("/api/web-pages")
public class WebPagesResource {

    private final Logger log = LoggerFactory.getLogger(WebPagesResource.class);

    private static final String ENTITY_NAME = "webPages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebPagesService webPagesService;

    private final WebPagesRepository webPagesRepository;

    private final WebPagesQueryService webPagesQueryService;

    public WebPagesResource(
        WebPagesService webPagesService,
        WebPagesRepository webPagesRepository,
        WebPagesQueryService webPagesQueryService
    ) {
        this.webPagesService = webPagesService;
        this.webPagesRepository = webPagesRepository;
        this.webPagesQueryService = webPagesQueryService;
    }

    /**
     * {@code POST  /web-pages} : Create a new webPages.
     *
     * @param webPagesDTO the webPagesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new webPagesDTO, or with status {@code 400 (Bad Request)} if the webPages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WebPagesDTO> createWebPages(@Valid @RequestBody WebPagesDTO webPagesDTO) throws URISyntaxException {
        log.debug("REST request to save WebPages : {}", webPagesDTO);
        if (webPagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new webPages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WebPagesDTO result = webPagesService.save(webPagesDTO);
        return ResponseEntity
            .created(new URI("/api/web-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /web-pages/:id} : Updates an existing webPages.
     *
     * @param id the id of the webPagesDTO to save.
     * @param webPagesDTO the webPagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webPagesDTO,
     * or with status {@code 400 (Bad Request)} if the webPagesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the webPagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WebPagesDTO> updateWebPages(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WebPagesDTO webPagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WebPages : {}, {}", id, webPagesDTO);
        if (webPagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, webPagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!webPagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WebPagesDTO result = webPagesService.update(webPagesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webPagesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /web-pages/:id} : Partial updates given fields of an existing webPages, field will ignore if it is null
     *
     * @param id the id of the webPagesDTO to save.
     * @param webPagesDTO the webPagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webPagesDTO,
     * or with status {@code 400 (Bad Request)} if the webPagesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the webPagesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the webPagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WebPagesDTO> partialUpdateWebPages(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WebPagesDTO webPagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WebPages partially : {}, {}", id, webPagesDTO);
        if (webPagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, webPagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!webPagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WebPagesDTO> result = webPagesService.partialUpdate(webPagesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webPagesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /web-pages} : get all the webPages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of webPages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WebPagesDTO>> getAllWebPages(
        WebPagesCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get WebPages by criteria: {}", criteria);

        Page<WebPagesDTO> page = webPagesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /web-pages/count} : count all the webPages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWebPages(WebPagesCriteria criteria) {
        log.debug("REST request to count WebPages by criteria: {}", criteria);
        return ResponseEntity.ok().body(webPagesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /web-pages/:id} : get the "id" webPages.
     *
     * @param id the id of the webPagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the webPagesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WebPagesDTO> getWebPages(@PathVariable("id") Long id) {
        log.debug("REST request to get WebPages : {}", id);
        Optional<WebPagesDTO> webPagesDTO = webPagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(webPagesDTO);
    }

    /**
     * {@code DELETE  /web-pages/:id} : delete the "id" webPages.
     *
     * @param id the id of the webPagesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebPages(@PathVariable("id") Long id) {
        log.debug("REST request to delete WebPages : {}", id);
        webPagesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
