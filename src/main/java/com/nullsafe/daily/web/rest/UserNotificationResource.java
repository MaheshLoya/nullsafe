package com.nullsafe.daily.web.rest;

import com.nullsafe.daily.repository.UserNotificationRepository;
import com.nullsafe.daily.service.UserNotificationQueryService;
import com.nullsafe.daily.service.UserNotificationService;
import com.nullsafe.daily.service.criteria.UserNotificationCriteria;
import com.nullsafe.daily.service.dto.UserNotificationDTO;
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
 * REST controller for managing {@link com.nullsafe.daily.domain.UserNotification}.
 */
@RestController
@RequestMapping("/api/user-notifications")
public class UserNotificationResource {

    private final Logger log = LoggerFactory.getLogger(UserNotificationResource.class);

    private static final String ENTITY_NAME = "userNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserNotificationService userNotificationService;

    private final UserNotificationRepository userNotificationRepository;

    private final UserNotificationQueryService userNotificationQueryService;

    public UserNotificationResource(
        UserNotificationService userNotificationService,
        UserNotificationRepository userNotificationRepository,
        UserNotificationQueryService userNotificationQueryService
    ) {
        this.userNotificationService = userNotificationService;
        this.userNotificationRepository = userNotificationRepository;
        this.userNotificationQueryService = userNotificationQueryService;
    }

    /**
     * {@code POST  /user-notifications} : Create a new userNotification.
     *
     * @param userNotificationDTO the userNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userNotificationDTO, or with status {@code 400 (Bad Request)} if the userNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserNotificationDTO> createUserNotification(@Valid @RequestBody UserNotificationDTO userNotificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserNotification : {}", userNotificationDTO);
        if (userNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new userNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserNotificationDTO result = userNotificationService.save(userNotificationDTO);
        return ResponseEntity
            .created(new URI("/api/user-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-notifications/:id} : Updates an existing userNotification.
     *
     * @param id the id of the userNotificationDTO to save.
     * @param userNotificationDTO the userNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the userNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserNotificationDTO> updateUserNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserNotificationDTO userNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserNotification : {}, {}", id, userNotificationDTO);
        if (userNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserNotificationDTO result = userNotificationService.update(userNotificationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userNotificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-notifications/:id} : Partial updates given fields of an existing userNotification, field will ignore if it is null
     *
     * @param id the id of the userNotificationDTO to save.
     * @param userNotificationDTO the userNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the userNotificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userNotificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserNotificationDTO> partialUpdateUserNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserNotificationDTO userNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserNotification partially : {}, {}", id, userNotificationDTO);
        if (userNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserNotificationDTO> result = userNotificationService.partialUpdate(userNotificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userNotificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-notifications} : get all the userNotifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userNotifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserNotificationDTO>> getAllUserNotifications(
        UserNotificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserNotifications by criteria: {}", criteria);

        Page<UserNotificationDTO> page = userNotificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-notifications/count} : count all the userNotifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserNotifications(UserNotificationCriteria criteria) {
        log.debug("REST request to count UserNotifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(userNotificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-notifications/:id} : get the "id" userNotification.
     *
     * @param id the id of the userNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserNotificationDTO> getUserNotification(@PathVariable("id") Long id) {
        log.debug("REST request to get UserNotification : {}", id);
        Optional<UserNotificationDTO> userNotificationDTO = userNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userNotificationDTO);
    }

    /**
     * {@code DELETE  /user-notifications/:id} : delete the "id" userNotification.
     *
     * @param id the id of the userNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserNotification(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserNotification : {}", id);
        userNotificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
