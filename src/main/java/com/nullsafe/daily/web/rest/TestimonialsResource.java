package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.TestimonialsRepository;
import com.nullsafe.daily.service.TestimonialsQueryService;
import com.nullsafe.daily.service.TestimonialsService;
import com.nullsafe.daily.service.criteria.TestimonialsCriteria;
import com.nullsafe.daily.service.dto.TestimonialsDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.Testimonials}.
 */
@RestController
@RequestMapping("/api/testimonials")
public class TestimonialsResource {

    private final Logger log = LoggerFactory.getLogger(TestimonialsResource.class);

    private static final String ENTITY_NAME = "testimonials";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestimonialsService testimonialsService;

    private final TestimonialsRepository testimonialsRepository;

    private final TestimonialsQueryService testimonialsQueryService;

    public TestimonialsResource(
        TestimonialsService testimonialsService,
        TestimonialsRepository testimonialsRepository,
        TestimonialsQueryService testimonialsQueryService
    ) {
        this.testimonialsService = testimonialsService;
        this.testimonialsRepository = testimonialsRepository;
        this.testimonialsQueryService = testimonialsQueryService;
    }

    /**
     * {@code POST  /testimonials} : Create a new testimonials.
     *
     * @param testimonialsDTO the testimonialsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testimonialsDTO, or with status {@code 400 (Bad Request)} if the testimonials has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestimonialsDTO> createTestimonials(@Valid @RequestBody TestimonialsDTO testimonialsDTO)
        throws URISyntaxException {
        log.debug("REST request to save Testimonials : {}", testimonialsDTO);
        if (testimonialsDTO.getId() != null) {
            throw new BadRequestAlertException("A new testimonials cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestimonialsDTO result = testimonialsService.save(testimonialsDTO);
        return ResponseEntity
            .created(new URI("/api/testimonials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /testimonials/:id} : Updates an existing testimonials.
     *
     * @param id the id of the testimonialsDTO to save.
     * @param testimonialsDTO the testimonialsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testimonialsDTO,
     * or with status {@code 400 (Bad Request)} if the testimonialsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testimonialsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestimonialsDTO> updateTestimonials(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestimonialsDTO testimonialsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Testimonials : {}, {}", id, testimonialsDTO);
        if (testimonialsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testimonialsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testimonialsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestimonialsDTO result = testimonialsService.update(testimonialsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testimonialsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /testimonials/:id} : Partial updates given fields of an existing testimonials, field will ignore if it is null
     *
     * @param id the id of the testimonialsDTO to save.
     * @param testimonialsDTO the testimonialsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testimonialsDTO,
     * or with status {@code 400 (Bad Request)} if the testimonialsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testimonialsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testimonialsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestimonialsDTO> partialUpdateTestimonials(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestimonialsDTO testimonialsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Testimonials partially : {}, {}", id, testimonialsDTO);
        if (testimonialsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testimonialsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testimonialsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestimonialsDTO> result = testimonialsService.partialUpdate(testimonialsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testimonialsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /testimonials} : get all the testimonials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testimonials in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TestimonialsDTO>> getAllTestimonials(
        TestimonialsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Testimonials by criteria: {}", criteria);

        Page<TestimonialsDTO> page = testimonialsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /testimonials/count} : count all the testimonials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTestimonials(TestimonialsCriteria criteria) {
        log.debug("REST request to count Testimonials by criteria: {}", criteria);
        return ResponseEntity.ok().body(testimonialsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /testimonials/:id} : get the "id" testimonials.
     *
     * @param id the id of the testimonialsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testimonialsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestimonialsDTO> getTestimonials(@PathVariable("id") Long id) {
        log.debug("REST request to get Testimonials : {}", id);
        Optional<TestimonialsDTO> testimonialsDTO = testimonialsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testimonialsDTO);
    }

    /**
     * {@code DELETE  /testimonials/:id} : delete the "id" testimonials.
     *
     * @param id the id of the testimonialsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestimonials(@PathVariable("id") Long id) {
        log.debug("REST request to delete Testimonials : {}", id);
        testimonialsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
