package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.SpecificNotification;
import com.nullsafe.daily.repository.SpecificNotificationRepository;
import com.nullsafe.daily.service.criteria.SpecificNotificationCriteria;
import com.nullsafe.daily.service.dto.SpecificNotificationDTO;
import com.nullsafe.daily.service.mapper.SpecificNotificationMapper;
import jakarta.persistence.criteria.JoinType;
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
 * Service for executing complex queries for {@link SpecificNotification} entities in the database.
 * The main input is a {@link SpecificNotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SpecificNotificationDTO} or a {@link Page} of {@link SpecificNotificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpecificNotificationQueryService extends QueryService<SpecificNotification> {

    private final Logger log = LoggerFactory.getLogger(SpecificNotificationQueryService.class);

    private final SpecificNotificationRepository specificNotificationRepository;

    private final SpecificNotificationMapper specificNotificationMapper;

    public SpecificNotificationQueryService(
        SpecificNotificationRepository specificNotificationRepository,
        SpecificNotificationMapper specificNotificationMapper
    ) {
        this.specificNotificationRepository = specificNotificationRepository;
        this.specificNotificationMapper = specificNotificationMapper;
    }

    /**
     * Return a {@link List} of {@link SpecificNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SpecificNotificationDTO> findByCriteria(SpecificNotificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SpecificNotification> specification = createSpecification(criteria);
        return specificNotificationMapper.toDto(specificNotificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SpecificNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpecificNotificationDTO> findByCriteria(SpecificNotificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SpecificNotification> specification = createSpecification(criteria);
        return specificNotificationRepository.findAll(specification, page).map(specificNotificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpecificNotificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SpecificNotification> specification = createSpecification(criteria);
        return specificNotificationRepository.count(specification);
    }

    /**
     * Function to convert {@link SpecificNotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SpecificNotification> createSpecification(SpecificNotificationCriteria criteria) {
        Specification<SpecificNotification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SpecificNotification_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), SpecificNotification_.title));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), SpecificNotification_.body));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), SpecificNotification_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), SpecificNotification_.updatedAt));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserId(),
                            root -> root.join(SpecificNotification_.user, JoinType.LEFT).get(Users_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
