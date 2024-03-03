package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.SocialMediaRepository;
import com.nullsafe.daily.service.SocialMediaQueryService;
import com.nullsafe.daily.service.SocialMediaService;
import com.nullsafe.daily.service.criteria.SocialMediaCriteria;
import com.nullsafe.daily.service.dto.SocialMediaDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.SocialMedia}.
 */
@RestController
@RequestMapping("/api/social-medias")
public class SocialMediaResource {

    private final Logger log = LoggerFactory.getLogger(SocialMediaResource.class);

    private static final String ENTITY_NAME = "socialMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialMediaService socialMediaService;

    private final SocialMediaRepository socialMediaRepository;

    private final SocialMediaQueryService socialMediaQueryService;

    public SocialMediaResource(
        SocialMediaService socialMediaService,
        SocialMediaRepository socialMediaRepository,
        SocialMediaQueryService socialMediaQueryService
    ) {
        this.socialMediaService = socialMediaService;
        this.socialMediaRepository = socialMediaRepository;
        this.socialMediaQueryService = socialMediaQueryService;
    }

    /**
     * {@code POST  /social-medias} : Create a new socialMedia.
     *
     * @param socialMediaDTO the socialMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialMediaDTO, or with status {@code 400 (Bad Request)} if the socialMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SocialMediaDTO> createSocialMedia(@Valid @RequestBody SocialMediaDTO socialMediaDTO) throws URISyntaxException {
        log.debug("REST request to save SocialMedia : {}", socialMediaDTO);
        if (socialMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new socialMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialMediaDTO result = socialMediaService.save(socialMediaDTO);
        return ResponseEntity
            .created(new URI("/api/social-medias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-medias/:id} : Updates an existing socialMedia.
     *
     * @param id the id of the socialMediaDTO to save.
     * @param socialMediaDTO the socialMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialMediaDTO,
     * or with status {@code 400 (Bad Request)} if the socialMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SocialMediaDTO> updateSocialMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocialMediaDTO socialMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SocialMedia : {}, {}", id, socialMediaDTO);
        if (socialMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SocialMediaDTO result = socialMediaService.update(socialMediaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /social-medias/:id} : Partial updates given fields of an existing socialMedia, field will ignore if it is null
     *
     * @param id the id of the socialMediaDTO to save.
     * @param socialMediaDTO the socialMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialMediaDTO,
     * or with status {@code 400 (Bad Request)} if the socialMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the socialMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocialMediaDTO> partialUpdateSocialMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocialMediaDTO socialMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialMedia partially : {}, {}", id, socialMediaDTO);
        if (socialMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocialMediaDTO> result = socialMediaService.partialUpdate(socialMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /social-medias} : get all the socialMedias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SocialMediaDTO>> getAllSocialMedias(
        SocialMediaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SocialMedias by criteria: {}", criteria);

        Page<SocialMediaDTO> page = socialMediaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /social-medias/count} : count all the socialMedias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSocialMedias(SocialMediaCriteria criteria) {
        log.debug("REST request to count SocialMedias by criteria: {}", criteria);
        return ResponseEntity.ok().body(socialMediaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /social-medias/:id} : get the "id" socialMedia.
     *
     * @param id the id of the socialMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SocialMediaDTO> getSocialMedia(@PathVariable("id") Long id) {
        log.debug("REST request to get SocialMedia : {}", id);
        Optional<SocialMediaDTO> socialMediaDTO = socialMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialMediaDTO);
    }

    /**
     * {@code DELETE  /social-medias/:id} : delete the "id" socialMedia.
     *
     * @param id the id of the socialMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocialMedia(@PathVariable("id") Long id) {
        log.debug("REST request to delete SocialMedia : {}", id);
        socialMediaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
