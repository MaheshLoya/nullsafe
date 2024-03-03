package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.FilesRepository;
import com.nullsafe.daily.service.FilesQueryService;
import com.nullsafe.daily.service.FilesService;
import com.nullsafe.daily.service.criteria.FilesCriteria;
import com.nullsafe.daily.service.dto.FilesDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.Files}.
 */
@RestController
@RequestMapping("/api/files")
public class FilesResource {

    private final Logger log = LoggerFactory.getLogger(FilesResource.class);

    private static final String ENTITY_NAME = "files";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilesService filesService;

    private final FilesRepository filesRepository;

    private final FilesQueryService filesQueryService;

    public FilesResource(FilesService filesService, FilesRepository filesRepository, FilesQueryService filesQueryService) {
        this.filesService = filesService;
        this.filesRepository = filesRepository;
        this.filesQueryService = filesQueryService;
    }

    /**
     * {@code POST  /files} : Create a new files.
     *
     * @param filesDTO the filesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filesDTO, or with status {@code 400 (Bad Request)} if the files has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FilesDTO> createFiles(@Valid @RequestBody FilesDTO filesDTO) throws URISyntaxException {
        log.debug("REST request to save Files : {}", filesDTO);
        if (filesDTO.getId() != null) {
            throw new BadRequestAlertException("A new files cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilesDTO result = filesService.save(filesDTO);
        return ResponseEntity
            .created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /files/:id} : Updates an existing files.
     *
     * @param id the id of the filesDTO to save.
     * @param filesDTO the filesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filesDTO,
     * or with status {@code 400 (Bad Request)} if the filesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FilesDTO> updateFiles(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FilesDTO filesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Files : {}, {}", id, filesDTO);
        if (filesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FilesDTO result = filesService.update(filesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /files/:id} : Partial updates given fields of an existing files, field will ignore if it is null
     *
     * @param id the id of the filesDTO to save.
     * @param filesDTO the filesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filesDTO,
     * or with status {@code 400 (Bad Request)} if the filesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the filesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the filesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FilesDTO> partialUpdateFiles(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FilesDTO filesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Files partially : {}, {}", id, filesDTO);
        if (filesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FilesDTO> result = filesService.partialUpdate(filesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /files} : get all the files.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of files in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FilesDTO>> getAllFiles(
        FilesCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Files by criteria: {}", criteria);

        Page<FilesDTO> page = filesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /files/count} : count all the files.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFiles(FilesCriteria criteria) {
        log.debug("REST request to count Files by criteria: {}", criteria);
        return ResponseEntity.ok().body(filesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /files/:id} : get the "id" files.
     *
     * @param id the id of the filesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FilesDTO> getFiles(@PathVariable("id") Long id) {
        log.debug("REST request to get Files : {}", id);
        Optional<FilesDTO> filesDTO = filesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filesDTO);
    }

    /**
     * {@code DELETE  /files/:id} : delete the "id" files.
     *
     * @param id the id of the filesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiles(@PathVariable("id") Long id) {
        log.debug("REST request to delete Files : {}", id);
        filesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
