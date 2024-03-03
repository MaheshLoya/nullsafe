package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.BannerImageRepository;
import com.nullsafe.daily.service.BannerImageQueryService;
import com.nullsafe.daily.service.BannerImageService;
import com.nullsafe.daily.service.criteria.BannerImageCriteria;
import com.nullsafe.daily.service.dto.BannerImageDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.BannerImage}.
 */
@RestController
@RequestMapping("/api/banner-images")
public class BannerImageResource {

    private final Logger log = LoggerFactory.getLogger(BannerImageResource.class);

    private static final String ENTITY_NAME = "bannerImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BannerImageService bannerImageService;

    private final BannerImageRepository bannerImageRepository;

    private final BannerImageQueryService bannerImageQueryService;

    public BannerImageResource(
        BannerImageService bannerImageService,
        BannerImageRepository bannerImageRepository,
        BannerImageQueryService bannerImageQueryService
    ) {
        this.bannerImageService = bannerImageService;
        this.bannerImageRepository = bannerImageRepository;
        this.bannerImageQueryService = bannerImageQueryService;
    }

    /**
     * {@code POST  /banner-images} : Create a new bannerImage.
     *
     * @param bannerImageDTO the bannerImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bannerImageDTO, or with status {@code 400 (Bad Request)} if the bannerImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BannerImageDTO> createBannerImage(@Valid @RequestBody BannerImageDTO bannerImageDTO) throws URISyntaxException {
        log.debug("REST request to save BannerImage : {}", bannerImageDTO);
        if (bannerImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new bannerImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BannerImageDTO result = bannerImageService.save(bannerImageDTO);
        return ResponseEntity
            .created(new URI("/api/banner-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /banner-images/:id} : Updates an existing bannerImage.
     *
     * @param id the id of the bannerImageDTO to save.
     * @param bannerImageDTO the bannerImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bannerImageDTO,
     * or with status {@code 400 (Bad Request)} if the bannerImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bannerImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BannerImageDTO> updateBannerImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BannerImageDTO bannerImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BannerImage : {}, {}", id, bannerImageDTO);
        if (bannerImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bannerImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bannerImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BannerImageDTO result = bannerImageService.update(bannerImageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bannerImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /banner-images/:id} : Partial updates given fields of an existing bannerImage, field will ignore if it is null
     *
     * @param id the id of the bannerImageDTO to save.
     * @param bannerImageDTO the bannerImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bannerImageDTO,
     * or with status {@code 400 (Bad Request)} if the bannerImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bannerImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bannerImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BannerImageDTO> partialUpdateBannerImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BannerImageDTO bannerImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BannerImage partially : {}, {}", id, bannerImageDTO);
        if (bannerImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bannerImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bannerImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BannerImageDTO> result = bannerImageService.partialUpdate(bannerImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bannerImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /banner-images} : get all the bannerImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bannerImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BannerImageDTO>> getAllBannerImages(
        BannerImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get BannerImages by criteria: {}", criteria);

        Page<BannerImageDTO> page = bannerImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /banner-images/count} : count all the bannerImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBannerImages(BannerImageCriteria criteria) {
        log.debug("REST request to count BannerImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(bannerImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /banner-images/:id} : get the "id" bannerImage.
     *
     * @param id the id of the bannerImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bannerImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BannerImageDTO> getBannerImage(@PathVariable("id") Long id) {
        log.debug("REST request to get BannerImage : {}", id);
        Optional<BannerImageDTO> bannerImageDTO = bannerImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bannerImageDTO);
    }

    /**
     * {@code DELETE  /banner-images/:id} : delete the "id" bannerImage.
     *
     * @param id the id of the bannerImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBannerImage(@PathVariable("id") Long id) {
        log.debug("REST request to delete BannerImage : {}", id);
        bannerImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
