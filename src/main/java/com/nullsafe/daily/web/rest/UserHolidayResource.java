package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.UserHolidayRepository;
import com.nullsafe.daily.service.UserHolidayQueryService;
import com.nullsafe.daily.service.UserHolidayService;
import com.nullsafe.daily.service.criteria.UserHolidayCriteria;
import com.nullsafe.daily.service.dto.UserHolidayDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.UserHoliday}.
 */
@RestController
@RequestMapping("/api/user-holidays")
public class UserHolidayResource {

    private final Logger log = LoggerFactory.getLogger(UserHolidayResource.class);

    private static final String ENTITY_NAME = "userHoliday";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserHolidayService userHolidayService;

    private final UserHolidayRepository userHolidayRepository;

    private final UserHolidayQueryService userHolidayQueryService;

    public UserHolidayResource(
        UserHolidayService userHolidayService,
        UserHolidayRepository userHolidayRepository,
        UserHolidayQueryService userHolidayQueryService
    ) {
        this.userHolidayService = userHolidayService;
        this.userHolidayRepository = userHolidayRepository;
        this.userHolidayQueryService = userHolidayQueryService;
    }

    /**
     * {@code POST  /user-holidays} : Create a new userHoliday.
     *
     * @param userHolidayDTO the userHolidayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userHolidayDTO, or with status {@code 400 (Bad Request)} if the userHoliday has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserHolidayDTO> createUserHoliday(@Valid @RequestBody UserHolidayDTO userHolidayDTO) throws URISyntaxException {
        log.debug("REST request to save UserHoliday : {}", userHolidayDTO);
        if (userHolidayDTO.getId() != null) {
            throw new BadRequestAlertException("A new userHoliday cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserHolidayDTO result = userHolidayService.save(userHolidayDTO);
        return ResponseEntity
            .created(new URI("/api/user-holidays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-holidays/:id} : Updates an existing userHoliday.
     *
     * @param id the id of the userHolidayDTO to save.
     * @param userHolidayDTO the userHolidayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userHolidayDTO,
     * or with status {@code 400 (Bad Request)} if the userHolidayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userHolidayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserHolidayDTO> updateUserHoliday(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserHolidayDTO userHolidayDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserHoliday : {}, {}", id, userHolidayDTO);
        if (userHolidayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userHolidayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userHolidayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserHolidayDTO result = userHolidayService.update(userHolidayDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userHolidayDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-holidays/:id} : Partial updates given fields of an existing userHoliday, field will ignore if it is null
     *
     * @param id the id of the userHolidayDTO to save.
     * @param userHolidayDTO the userHolidayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userHolidayDTO,
     * or with status {@code 400 (Bad Request)} if the userHolidayDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userHolidayDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userHolidayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserHolidayDTO> partialUpdateUserHoliday(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserHolidayDTO userHolidayDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserHoliday partially : {}, {}", id, userHolidayDTO);
        if (userHolidayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userHolidayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userHolidayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserHolidayDTO> result = userHolidayService.partialUpdate(userHolidayDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userHolidayDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-holidays} : get all the userHolidays.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userHolidays in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserHolidayDTO>> getAllUserHolidays(
        UserHolidayCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserHolidays by criteria: {}", criteria);

        Page<UserHolidayDTO> page = userHolidayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-holidays/count} : count all the userHolidays.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserHolidays(UserHolidayCriteria criteria) {
        log.debug("REST request to count UserHolidays by criteria: {}", criteria);
        return ResponseEntity.ok().body(userHolidayQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-holidays/:id} : get the "id" userHoliday.
     *
     * @param id the id of the userHolidayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userHolidayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserHolidayDTO> getUserHoliday(@PathVariable("id") Long id) {
        log.debug("REST request to get UserHoliday : {}", id);
        Optional<UserHolidayDTO> userHolidayDTO = userHolidayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userHolidayDTO);
    }

    /**
     * {@code DELETE  /user-holidays/:id} : delete the "id" userHoliday.
     *
     * @param id the id of the userHolidayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserHoliday(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserHoliday : {}", id);
        userHolidayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
