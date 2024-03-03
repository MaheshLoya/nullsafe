package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.SubscriptionRenewal;
import com.nullsafe.daily.repository.SubscriptionRenewalRepository;
import com.nullsafe.daily.service.criteria.SubscriptionRenewalCriteria;
import com.nullsafe.daily.service.dto.SubscriptionRenewalDTO;
import com.nullsafe.daily.service.mapper.SubscriptionRenewalMapper;
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
 * Service for executing complex queries for {@link SubscriptionRenewal} entities in the database.
 * The main input is a {@link SubscriptionRenewalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubscriptionRenewalDTO} or a {@link Page} of {@link SubscriptionRenewalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubscriptionRenewalQueryService extends QueryService<SubscriptionRenewal> {

    private final Logger log = LoggerFactory.getLogger(SubscriptionRenewalQueryService.class);

    private final SubscriptionRenewalRepository subscriptionRenewalRepository;

    private final SubscriptionRenewalMapper subscriptionRenewalMapper;

    public SubscriptionRenewalQueryService(
        SubscriptionRenewalRepository subscriptionRenewalRepository,
        SubscriptionRenewalMapper subscriptionRenewalMapper
    ) {
        this.subscriptionRenewalRepository = subscriptionRenewalRepository;
        this.subscriptionRenewalMapper = subscriptionRenewalMapper;
    }

    /**
     * Return a {@link List} of {@link SubscriptionRenewalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionRenewalDTO> findByCriteria(SubscriptionRenewalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubscriptionRenewal> specification = createSpecification(criteria);
        return subscriptionRenewalMapper.toDto(subscriptionRenewalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubscriptionRenewalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionRenewalDTO> findByCriteria(SubscriptionRenewalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubscriptionRenewal> specification = createSpecification(criteria);
        return subscriptionRenewalRepository.findAll(specification, page).map(subscriptionRenewalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubscriptionRenewalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubscriptionRenewal> specification = createSpecification(criteria);
        return subscriptionRenewalRepository.count(specification);
    }

    /**
     * Function to convert {@link SubscriptionRenewalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubscriptionRenewal> createSpecification(SubscriptionRenewalCriteria criteria) {
        Specification<SubscriptionRenewal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubscriptionRenewal_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), SubscriptionRenewal_.userId));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderId(), SubscriptionRenewal_.orderId));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionId(), SubscriptionRenewal_.transactionId));
            }
            if (criteria.getRenewalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRenewalDate(), SubscriptionRenewal_.renewalDate));
            }
            if (criteria.getPaidRenewalAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPaidRenewalAmount(), SubscriptionRenewal_.paidRenewalAmount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), SubscriptionRenewal_.status));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), SubscriptionRenewal_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), SubscriptionRenewal_.endDate));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), SubscriptionRenewal_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), SubscriptionRenewal_.updatedAt));
            }
        }
        return specification;
    }
}
