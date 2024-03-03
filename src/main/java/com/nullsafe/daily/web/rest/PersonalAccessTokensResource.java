package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.PersonalAccessTokensRepository;
import com.nullsafe.daily.service.PersonalAccessTokensQueryService;
import com.nullsafe.daily.service.PersonalAccessTokensService;
import com.nullsafe.daily.service.criteria.PersonalAccessTokensCriteria;
import com.nullsafe.daily.service.dto.PersonalAccessTokensDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.PersonalAccessTokens}.
 */
@RestController
@RequestMapping("/api/personal-access-tokens")
public class PersonalAccessTokensResource {

    private final Logger log = LoggerFactory.getLogger(PersonalAccessTokensResource.class);

    private static final String ENTITY_NAME = "personalAccessTokens";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalAccessTokensService personalAccessTokensService;

    private final PersonalAccessTokensRepository personalAccessTokensRepository;

    private final PersonalAccessTokensQueryService personalAccessTokensQueryService;

    public PersonalAccessTokensResource(
        PersonalAccessTokensService personalAccessTokensService,
        PersonalAccessTokensRepository personalAccessTokensRepository,
        PersonalAccessTokensQueryService personalAccessTokensQueryService
    ) {
        this.personalAccessTokensService = personalAccessTokensService;
        this.personalAccessTokensRepository = personalAccessTokensRepository;
        this.personalAccessTokensQueryService = personalAccessTokensQueryService;
    }

    /**
     * {@code POST  /personal-access-tokens} : Create a new personalAccessTokens.
     *
     * @param personalAccessTokensDTO the personalAccessTokensDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalAccessTokensDTO, or with status {@code 400 (Bad Request)} if the personalAccessTokens has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PersonalAccessTokensDTO> createPersonalAccessTokens(
        @Valid @RequestBody PersonalAccessTokensDTO personalAccessTokensDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PersonalAccessTokens : {}", personalAccessTokensDTO);
        if (personalAccessTokensDTO.getId() != null) {
            throw new BadRequestAlertException("A new personalAccessTokens cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalAccessTokensDTO result = personalAccessTokensService.save(personalAccessTokensDTO);
        return ResponseEntity
            .created(new URI("/api/personal-access-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personal-access-tokens/:id} : Updates an existing personalAccessTokens.
     *
     * @param id the id of the personalAccessTokensDTO to save.
     * @param personalAccessTokensDTO the personalAccessTokensDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalAccessTokensDTO,
     * or with status {@code 400 (Bad Request)} if the personalAccessTokensDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalAccessTokensDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonalAccessTokensDTO> updatePersonalAccessTokens(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonalAccessTokensDTO personalAccessTokensDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PersonalAccessTokens : {}, {}", id, personalAccessTokensDTO);
        if (personalAccessTokensDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalAccessTokensDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalAccessTokensRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonalAccessTokensDTO result = personalAccessTokensService.update(personalAccessTokensDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalAccessTokensDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personal-access-tokens/:id} : Partial updates given fields of an existing personalAccessTokens, field will ignore if it is null
     *
     * @param id the id of the personalAccessTokensDTO to save.
     * @param personalAccessTokensDTO the personalAccessTokensDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalAccessTokensDTO,
     * or with status {@code 400 (Bad Request)} if the personalAccessTokensDTO is not valid,
     * or with status {@code 404 (Not Found)} if the personalAccessTokensDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalAccessTokensDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalAccessTokensDTO> partialUpdatePersonalAccessTokens(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonalAccessTokensDTO personalAccessTokensDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonalAccessTokens partially : {}, {}", id, personalAccessTokensDTO);
        if (personalAccessTokensDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalAccessTokensDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalAccessTokensRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalAccessTokensDTO> result = personalAccessTokensService.partialUpdate(personalAccessTokensDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalAccessTokensDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /personal-access-tokens} : get all the personalAccessTokens.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalAccessTokens in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PersonalAccessTokensDTO>> getAllPersonalAccessTokens(
        PersonalAccessTokensCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PersonalAccessTokens by criteria: {}", criteria);

        Page<PersonalAccessTokensDTO> page = personalAccessTokensQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /personal-access-tokens/count} : count all the personalAccessTokens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPersonalAccessTokens(PersonalAccessTokensCriteria criteria) {
        log.debug("REST request to count PersonalAccessTokens by criteria: {}", criteria);
        return ResponseEntity.ok().body(personalAccessTokensQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /personal-access-tokens/:id} : get the "id" personalAccessTokens.
     *
     * @param id the id of the personalAccessTokensDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalAccessTokensDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonalAccessTokensDTO> getPersonalAccessTokens(@PathVariable("id") Long id) {
        log.debug("REST request to get PersonalAccessTokens : {}", id);
        Optional<PersonalAccessTokensDTO> personalAccessTokensDTO = personalAccessTokensService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personalAccessTokensDTO);
    }

    /**
     * {@code DELETE  /personal-access-tokens/:id} : delete the "id" personalAccessTokens.
     *
     * @param id the id of the personalAccessTokensDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonalAccessTokens(@PathVariable("id") Long id) {
        log.debug("REST request to delete PersonalAccessTokens : {}", id);
        personalAccessTokensService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
