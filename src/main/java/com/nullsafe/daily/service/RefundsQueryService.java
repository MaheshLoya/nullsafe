package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Refunds;
import com.nullsafe.daily.repository.RefundsRepository;
import com.nullsafe.daily.service.criteria.RefundsCriteria;
import com.nullsafe.daily.service.dto.RefundsDTO;
import com.nullsafe.daily.service.mapper.RefundsMapper;
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
 * Service for executing complex queries for {@link Refunds} entities in the database.
 * The main input is a {@link RefundsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RefundsDTO} or a {@link Page} of {@link RefundsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RefundsQueryService extends QueryService<Refunds> {

    private final Logger log = LoggerFactory.getLogger(RefundsQueryService.class);

    private final RefundsRepository refundsRepository;

    private final RefundsMapper refundsMapper;

    public RefundsQueryService(RefundsRepository refundsRepository, RefundsMapper refundsMapper) {
        this.refundsRepository = refundsRepository;
        this.refundsMapper = refundsMapper;
    }

    /**
     * Return a {@link List} of {@link RefundsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RefundsDTO> findByCriteria(RefundsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Refunds> specification = createSpecification(criteria);
        return refundsMapper.toDto(refundsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RefundsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RefundsDTO> findByCriteria(RefundsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Refunds> specification = createSpecification(criteria);
        return refundsRepository.findAll(specification, page).map(refundsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RefundsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Refunds> specification = createSpecification(criteria);
        return refundsRepository.count(specification);
    }

    /**
     * Function to convert {@link RefundsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Refunds> createSpecification(RefundsCriteria criteria) {
        Specification<Refunds> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Refunds_.id));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderId(), Refunds_.orderId));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionId(), Refunds_.transactionId));
            }
            if (criteria.getRazorpayRefundId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRazorpayRefundId(), Refunds_.razorpayRefundId));
            }
            if (criteria.getRazorpayPaymentId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRazorpayPaymentId(), Refunds_.razorpayPaymentId));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Refunds_.amount));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), Refunds_.currency));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Refunds_.status));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Refunds_.createdBy));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Refunds_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Refunds_.updatedAt));
            }
        }
        return specification;
    }
}
