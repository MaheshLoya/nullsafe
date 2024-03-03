package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.CatRepository;
import com.nullsafe.daily.service.CatQueryService;
import com.nullsafe.daily.service.CatService;
import com.nullsafe.daily.service.criteria.CatCriteria;
import com.nullsafe.daily.service.dto.CatDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.Cat}.
 */
@RestController
@RequestMapping("/api/cats")
public class CatResource {

    private final Logger log = LoggerFactory.getLogger(CatResource.class);

    private static final String ENTITY_NAME = "cat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatService catService;

    private final CatRepository catRepository;

    private final CatQueryService catQueryService;

    public CatResource(CatService catService, CatRepository catRepository, CatQueryService catQueryService) {
        this.catService = catService;
        this.catRepository = catRepository;
        this.catQueryService = catQueryService;
    }

    /**
     * {@code POST  /cats} : Create a new cat.
     *
     * @param catDTO the catDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catDTO, or with status {@code 400 (Bad Request)} if the cat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CatDTO> createCat(@Valid @RequestBody CatDTO catDTO) throws URISyntaxException {
        log.debug("REST request to save Cat : {}", catDTO);
        if (catDTO.getId() != null) {
            throw new BadRequestAlertException("A new cat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatDTO result = catService.save(catDTO);
        return ResponseEntity
            .created(new URI("/api/cats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cats/:id} : Updates an existing cat.
     *
     * @param id the id of the catDTO to save.
     * @param catDTO the catDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catDTO,
     * or with status {@code 400 (Bad Request)} if the catDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CatDTO> updateCat(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody CatDTO catDTO)
        throws URISyntaxException {
        log.debug("REST request to update Cat : {}, {}", id, catDTO);
        if (catDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatDTO result = catService.update(catDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cats/:id} : Partial updates given fields of an existing cat, field will ignore if it is null
     *
     * @param id the id of the catDTO to save.
     * @param catDTO the catDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catDTO,
     * or with status {@code 400 (Bad Request)} if the catDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatDTO> partialUpdateCat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatDTO catDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cat partially : {}, {}", id, catDTO);
        if (catDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatDTO> result = catService.partialUpdate(catDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cats} : get all the cats.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CatDTO>> getAllCats(
        CatCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Cats by criteria: {}", criteria);

        Page<CatDTO> page = catQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cats/count} : count all the cats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCats(CatCriteria criteria) {
        log.debug("REST request to count Cats by criteria: {}", criteria);
        return ResponseEntity.ok().body(catQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cats/:id} : get the "id" cat.
     *
     * @param id the id of the catDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatDTO> getCat(@PathVariable("id") Long id) {
        log.debug("REST request to get Cat : {}", id);
        Optional<CatDTO> catDTO = catService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catDTO);
    }

    /**
     * {@code DELETE  /cats/:id} : delete the "id" cat.
     *
     * @param id the id of the catDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCat(@PathVariable("id") Long id) {
        log.debug("REST request to delete Cat : {}", id);
        catService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
