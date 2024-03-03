package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.SubCatRepository;
import com.nullsafe.daily.service.SubCatQueryService;
import com.nullsafe.daily.service.SubCatService;
import com.nullsafe.daily.service.criteria.SubCatCriteria;
import com.nullsafe.daily.service.dto.SubCatDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.SubCat}.
 */
@RestController
@RequestMapping("/api/sub-cats")
public class SubCatResource {

    private final Logger log = LoggerFactory.getLogger(SubCatResource.class);

    private static final String ENTITY_NAME = "subCat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubCatService subCatService;

    private final SubCatRepository subCatRepository;

    private final SubCatQueryService subCatQueryService;

    public SubCatResource(SubCatService subCatService, SubCatRepository subCatRepository, SubCatQueryService subCatQueryService) {
        this.subCatService = subCatService;
        this.subCatRepository = subCatRepository;
        this.subCatQueryService = subCatQueryService;
    }

    /**
     * {@code POST  /sub-cats} : Create a new subCat.
     *
     * @param subCatDTO the subCatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subCatDTO, or with status {@code 400 (Bad Request)} if the subCat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubCatDTO> createSubCat(@Valid @RequestBody SubCatDTO subCatDTO) throws URISyntaxException {
        log.debug("REST request to save SubCat : {}", subCatDTO);
        if (subCatDTO.getId() != null) {
            throw new BadRequestAlertException("A new subCat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubCatDTO result = subCatService.save(subCatDTO);
        return ResponseEntity
            .created(new URI("/api/sub-cats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-cats/:id} : Updates an existing subCat.
     *
     * @param id the id of the subCatDTO to save.
     * @param subCatDTO the subCatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCatDTO,
     * or with status {@code 400 (Bad Request)} if the subCatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subCatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubCatDTO> updateSubCat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubCatDTO subCatDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubCat : {}, {}", id, subCatDTO);
        if (subCatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubCatDTO result = subCatService.update(subCatDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subCatDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-cats/:id} : Partial updates given fields of an existing subCat, field will ignore if it is null
     *
     * @param id the id of the subCatDTO to save.
     * @param subCatDTO the subCatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCatDTO,
     * or with status {@code 400 (Bad Request)} if the subCatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subCatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subCatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubCatDTO> partialUpdateSubCat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubCatDTO subCatDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubCat partially : {}, {}", id, subCatDTO);
        if (subCatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubCatDTO> result = subCatService.partialUpdate(subCatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subCatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-cats} : get all the subCats.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubCatDTO>> getAllSubCats(
        SubCatCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SubCats by criteria: {}", criteria);

        Page<SubCatDTO> page = subCatQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-cats/count} : count all the subCats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubCats(SubCatCriteria criteria) {
        log.debug("REST request to count SubCats by criteria: {}", criteria);
        return ResponseEntity.ok().body(subCatQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-cats/:id} : get the "id" subCat.
     *
     * @param id the id of the subCatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubCatDTO> getSubCat(@PathVariable("id") Long id) {
        log.debug("REST request to get SubCat : {}", id);
        Optional<SubCatDTO> subCatDTO = subCatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subCatDTO);
    }

    /**
     * {@code DELETE  /sub-cats/:id} : delete the "id" subCat.
     *
     * @param id the id of the subCatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubCat(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubCat : {}", id);
        subCatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
