package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.UserNotification;
import com.nullsafe.daily.repository.UserNotificationRepository;
import com.nullsafe.daily.service.criteria.UserNotificationCriteria;
import com.nullsafe.daily.service.dto.UserNotificationDTO;
import com.nullsafe.daily.service.mapper.UserNotificationMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserNotification} entities in the database.
 * The main input is a {@link UserNotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserNotificationDTO} or a {@link Page} of {@link UserNotificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserNotificationQueryService extends QueryService<UserNotification> {

    private final Logger log = LoggerFactory.getLogger(UserNotificationQueryService.class);

    private final UserNotificationRepository userNotificationRepository;

    private final UserNotificationMapper userNotificationMapper;

    public UserNotificationQueryService(
        UserNotificationRepository userNotificationRepository,
        UserNotificationMapper userNotificationMapper
    ) {
        this.userNotificationRepository = userNotificationRepository;
        this.userNotificationMapper = userNotificationMapper;
    }

    /**
     * Return a {@link List} of {@link UserNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserNotificationDTO> findByCriteria(UserNotificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserNotification> specification = createSpecification(criteria);
        return userNotificationMapper.toDto(userNotificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserNotificationDTO> findByCriteria(UserNotificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserNotification> specification = createSpecification(criteria);
        return userNotificationRepository.findAll(specification, page).map(userNotificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserNotificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserNotification> specification = createSpecification(criteria);
        return userNotificationRepository.count(specification);
    }

    /**
     * Function to convert {@link UserNotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserNotification> createSpecification(UserNotificationCriteria criteria) {
        Specification<UserNotification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserNotification_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), UserNotification_.title));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), UserNotification_.body));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), UserNotification_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), UserNotification_.updatedAt));
            }
        }
        return specification;
    }
}
