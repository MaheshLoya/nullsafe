package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.SubscribedOrders;
import com.nullsafe.daily.repository.SubscribedOrdersRepository;
import com.nullsafe.daily.service.criteria.SubscribedOrdersCriteria;
import com.nullsafe.daily.service.dto.SubscribedOrdersDTO;
import com.nullsafe.daily.service.mapper.SubscribedOrdersMapper;
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
 * Service for executing complex queries for {@link SubscribedOrders} entities in the database.
 * The main input is a {@link SubscribedOrdersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubscribedOrdersDTO} or a {@link Page} of {@link SubscribedOrdersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubscribedOrdersQueryService extends QueryService<SubscribedOrders> {

    private final Logger log = LoggerFactory.getLogger(SubscribedOrdersQueryService.class);

    private final SubscribedOrdersRepository subscribedOrdersRepository;

    private final SubscribedOrdersMapper subscribedOrdersMapper;

    public SubscribedOrdersQueryService(
        SubscribedOrdersRepository subscribedOrdersRepository,
        SubscribedOrdersMapper subscribedOrdersMapper
    ) {
        this.subscribedOrdersRepository = subscribedOrdersRepository;
        this.subscribedOrdersMapper = subscribedOrdersMapper;
    }

    /**
     * Return a {@link List} of {@link SubscribedOrdersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubscribedOrdersDTO> findByCriteria(SubscribedOrdersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubscribedOrders> specification = createSpecification(criteria);
        return subscribedOrdersMapper.toDto(subscribedOrdersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubscribedOrdersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscribedOrdersDTO> findByCriteria(SubscribedOrdersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubscribedOrders> specification = createSpecification(criteria);
        return subscribedOrdersRepository.findAll(specification, page).map(subscribedOrdersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubscribedOrdersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubscribedOrders> specification = createSpecification(criteria);
        return subscribedOrdersRepository.count(specification);
    }

    /**
     * Function to convert {@link SubscribedOrdersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubscribedOrders> createSpecification(SubscribedOrdersCriteria criteria) {
        Specification<SubscribedOrders> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubscribedOrders_.id));
            }
            if (criteria.getPaymentType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentType(), SubscribedOrders_.paymentType));
            }
            if (criteria.getOrderAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderAmount(), SubscribedOrders_.orderAmount));
            }
            if (criteria.getSubscriptionBalanceAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSubscriptionBalanceAmount(), SubscribedOrders_.subscriptionBalanceAmount)
                    );
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), SubscribedOrders_.price));
            }
            if (criteria.getMrp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMrp(), SubscribedOrders_.mrp));
            }
            if (criteria.getTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTax(), SubscribedOrders_.tax));
            }
            if (criteria.getQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty(), SubscribedOrders_.qty));
            }
            if (criteria.getOfferId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOfferId(), SubscribedOrders_.offerId));
            }
            if (criteria.getSelectedDaysForWeekly() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getSelectedDaysForWeekly(), SubscribedOrders_.selectedDaysForWeekly)
                    );
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), SubscribedOrders_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), SubscribedOrders_.endDate));
            }
            if (criteria.getLastRenewalDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastRenewalDate(), SubscribedOrders_.lastRenewalDate));
            }
            if (criteria.getSubscriptionType() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSubscriptionType(), SubscribedOrders_.subscriptionType));
            }
            if (criteria.getApprovalStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovalStatus(), SubscribedOrders_.approvalStatus));
            }
            if (criteria.getOrderStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderStatus(), SubscribedOrders_.orderStatus));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), SubscribedOrders_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), SubscribedOrders_.updatedAt));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), SubscribedOrders_.createdBy));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdatedBy(), SubscribedOrders_.updatedBy));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(SubscribedOrders_.user, JoinType.LEFT).get(Users_.id))
                    );
            }
            if (criteria.getTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionId(),
                            root -> root.join(SubscribedOrders_.transaction, JoinType.LEFT).get(Transactions_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(SubscribedOrders_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
            if (criteria.getAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAddressId(),
                            root -> root.join(SubscribedOrders_.address, JoinType.LEFT).get(UserAddress_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
