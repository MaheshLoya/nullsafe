package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.PasswordResetsRepository;
import com.nullsafe.daily.service.PasswordResetsQueryService;
import com.nullsafe.daily.service.PasswordResetsService;
import com.nullsafe.daily.service.criteria.PasswordResetsCriteria;
import com.nullsafe.daily.service.dto.PasswordResetsDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.PasswordResets}.
 */
@RestController
@RequestMapping("/api/password-resets")
public class PasswordResetsResource {

    private final Logger log = LoggerFactory.getLogger(PasswordResetsResource.class);

    private static final String ENTITY_NAME = "passwordResets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PasswordResetsService passwordResetsService;

    private final PasswordResetsRepository passwordResetsRepository;

    private final PasswordResetsQueryService passwordResetsQueryService;

    public PasswordResetsResource(
        PasswordResetsService passwordResetsService,
        PasswordResetsRepository passwordResetsRepository,
        PasswordResetsQueryService passwordResetsQueryService
    ) {
        this.passwordResetsService = passwordResetsService;
        this.passwordResetsRepository = passwordResetsRepository;
        this.passwordResetsQueryService = passwordResetsQueryService;
    }

    /**
     * {@code POST  /password-resets} : Create a new passwordResets.
     *
     * @param passwordResetsDTO the passwordResetsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passwordResetsDTO, or with status {@code 400 (Bad Request)} if the passwordResets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PasswordResetsDTO> createPasswordResets(@Valid @RequestBody PasswordResetsDTO passwordResetsDTO)
        throws URISyntaxException {
        log.debug("REST request to save PasswordResets : {}", passwordResetsDTO);
        if (passwordResetsDTO.getId() != null) {
            throw new BadRequestAlertException("A new passwordResets cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PasswordResetsDTO result = passwordResetsService.save(passwordResetsDTO);
        return ResponseEntity
            .created(new URI("/api/password-resets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /password-resets/:id} : Updates an existing passwordResets.
     *
     * @param id the id of the passwordResetsDTO to save.
     * @param passwordResetsDTO the passwordResetsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passwordResetsDTO,
     * or with status {@code 400 (Bad Request)} if the passwordResetsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passwordResetsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PasswordResetsDTO> updatePasswordResets(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PasswordResetsDTO passwordResetsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PasswordResets : {}, {}", id, passwordResetsDTO);
        if (passwordResetsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passwordResetsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passwordResetsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PasswordResetsDTO result = passwordResetsService.update(passwordResetsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passwordResetsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /password-resets/:id} : Partial updates given fields of an existing passwordResets, field will ignore if it is null
     *
     * @param id the id of the passwordResetsDTO to save.
     * @param passwordResetsDTO the passwordResetsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passwordResetsDTO,
     * or with status {@code 400 (Bad Request)} if the passwordResetsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the passwordResetsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the passwordResetsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PasswordResetsDTO> partialUpdatePasswordResets(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PasswordResetsDTO passwordResetsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PasswordResets partially : {}, {}", id, passwordResetsDTO);
        if (passwordResetsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passwordResetsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passwordResetsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PasswordResetsDTO> result = passwordResetsService.partialUpdate(passwordResetsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passwordResetsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /password-resets} : get all the passwordResets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passwordResets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PasswordResetsDTO>> getAllPasswordResets(
        PasswordResetsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PasswordResets by criteria: {}", criteria);

        Page<PasswordResetsDTO> page = passwordResetsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /password-resets/count} : count all the passwordResets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPasswordResets(PasswordResetsCriteria criteria) {
        log.debug("REST request to count PasswordResets by criteria: {}", criteria);
        return ResponseEntity.ok().body(passwordResetsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /password-resets/:id} : get the "id" passwordResets.
     *
     * @param id the id of the passwordResetsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passwordResetsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PasswordResetsDTO> getPasswordResets(@PathVariable("id") Long id) {
        log.debug("REST request to get PasswordResets : {}", id);
        Optional<PasswordResetsDTO> passwordResetsDTO = passwordResetsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passwordResetsDTO);
    }

    /**
     * {@code DELETE  /password-resets/:id} : delete the "id" passwordResets.
     *
     * @param id the id of the passwordResetsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasswordResets(@PathVariable("id") Long id) {
        log.debug("REST request to delete PasswordResets : {}", id);
        passwordResetsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
