package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Transactions;
import com.nullsafe.daily.repository.TransactionsRepository;
import com.nullsafe.daily.service.criteria.TransactionsCriteria;
import com.nullsafe.daily.service.dto.TransactionsDTO;
import com.nullsafe.daily.service.mapper.TransactionsMapper;
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
 * Service for executing complex queries for {@link Transactions} entities in the database.
 * The main input is a {@link TransactionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionsDTO} or a {@link Page} of {@link TransactionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionsQueryService extends QueryService<Transactions> {

    private final Logger log = LoggerFactory.getLogger(TransactionsQueryService.class);

    private final TransactionsRepository transactionsRepository;

    private final TransactionsMapper transactionsMapper;

    public TransactionsQueryService(TransactionsRepository transactionsRepository, TransactionsMapper transactionsMapper) {
        this.transactionsRepository = transactionsRepository;
        this.transactionsMapper = transactionsMapper;
    }

    /**
     * Return a {@link List} of {@link TransactionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionsDTO> findByCriteria(TransactionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transactions> specification = createSpecification(criteria);
        return transactionsMapper.toDto(transactionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionsDTO> findByCriteria(TransactionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transactions> specification = createSpecification(criteria);
        return transactionsRepository.findAll(specification, page).map(transactionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transactions> specification = createSpecification(criteria);
        return transactionsRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transactions> createSpecification(TransactionsCriteria criteria) {
        Specification<Transactions> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Transactions_.id));
            }
            if (criteria.getPaymentId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentId(), Transactions_.paymentId));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Transactions_.amount));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Transactions_.description));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), Transactions_.type));
            }
            if (criteria.getPaymentMode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentMode(), Transactions_.paymentMode));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Transactions_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Transactions_.updatedAt));
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderId(), root -> root.join(Transactions_.order, JoinType.LEFT).get(Orders_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Transactions_.user, JoinType.LEFT).get(Users_.id))
                    );
            }
            if (criteria.getOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrdersId(), root -> root.join(Transactions_.orders, JoinType.LEFT).get(Orders_.id))
                    );
            }
            if (criteria.getSubscribedOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubscribedOrdersId(),
                            root -> root.join(Transactions_.subscribedOrders, JoinType.LEFT).get(SubscribedOrders_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
