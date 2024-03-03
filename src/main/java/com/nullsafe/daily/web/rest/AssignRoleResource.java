package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.AssignRoleRepository;
import com.nullsafe.daily.service.AssignRoleQueryService;
import com.nullsafe.daily.service.AssignRoleService;
import com.nullsafe.daily.service.criteria.AssignRoleCriteria;
import com.nullsafe.daily.service.dto.AssignRoleDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.AssignRole}.
 */
@RestController
@RequestMapping("/api/assign-roles")
public class AssignRoleResource {

    private final Logger log = LoggerFactory.getLogger(AssignRoleResource.class);

    private static final String ENTITY_NAME = "assignRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssignRoleService assignRoleService;

    private final AssignRoleRepository assignRoleRepository;

    private final AssignRoleQueryService assignRoleQueryService;

    public AssignRoleResource(
        AssignRoleService assignRoleService,
        AssignRoleRepository assignRoleRepository,
        AssignRoleQueryService assignRoleQueryService
    ) {
        this.assignRoleService = assignRoleService;
        this.assignRoleRepository = assignRoleRepository;
        this.assignRoleQueryService = assignRoleQueryService;
    }

    /**
     * {@code POST  /assign-roles} : Create a new assignRole.
     *
     * @param assignRoleDTO the assignRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assignRoleDTO, or with status {@code 400 (Bad Request)} if the assignRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AssignRoleDTO> createAssignRole(@Valid @RequestBody AssignRoleDTO assignRoleDTO) throws URISyntaxException {
        log.debug("REST request to save AssignRole : {}", assignRoleDTO);
        if (assignRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new assignRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssignRoleDTO result = assignRoleService.save(assignRoleDTO);
        return ResponseEntity
            .created(new URI("/api/assign-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assign-roles/:id} : Updates an existing assignRole.
     *
     * @param id the id of the assignRoleDTO to save.
     * @param assignRoleDTO the assignRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignRoleDTO,
     * or with status {@code 400 (Bad Request)} if the assignRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assignRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssignRoleDTO> updateAssignRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssignRoleDTO assignRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssignRole : {}, {}", id, assignRoleDTO);
        if (assignRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assignRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssignRoleDTO result = assignRoleService.update(assignRoleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignRoleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assign-roles/:id} : Partial updates given fields of an existing assignRole, field will ignore if it is null
     *
     * @param id the id of the assignRoleDTO to save.
     * @param assignRoleDTO the assignRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignRoleDTO,
     * or with status {@code 400 (Bad Request)} if the assignRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assignRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assignRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssignRoleDTO> partialUpdateAssignRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssignRoleDTO assignRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssignRole partially : {}, {}", id, assignRoleDTO);
        if (assignRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assignRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssignRoleDTO> result = assignRoleService.partialUpdate(assignRoleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignRoleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /assign-roles} : get all the assignRoles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assignRoles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AssignRoleDTO>> getAllAssignRoles(
        AssignRoleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssignRoles by criteria: {}", criteria);

        Page<AssignRoleDTO> page = assignRoleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assign-roles/count} : count all the assignRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAssignRoles(AssignRoleCriteria criteria) {
        log.debug("REST request to count AssignRoles by criteria: {}", criteria);
        return ResponseEntity.ok().body(assignRoleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assign-roles/:id} : get the "id" assignRole.
     *
     * @param id the id of the assignRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assignRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssignRoleDTO> getAssignRole(@PathVariable("id") Long id) {
        log.debug("REST request to get AssignRole : {}", id);
        Optional<AssignRoleDTO> assignRoleDTO = assignRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assignRoleDTO);
    }

    /**
     * {@code DELETE  /assign-roles/:id} : delete the "id" assignRole.
     *
     * @param id the id of the assignRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignRole(@PathVariable("id") Long id) {
        log.debug("REST request to delete AssignRole : {}", id);
        assignRoleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
