package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.repository.OrdersRepository;
import com.nullsafe.daily.service.criteria.OrdersCriteria;
import com.nullsafe.daily.service.dto.OrdersDTO;
import com.nullsafe.daily.service.mapper.OrdersMapper;
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
 * Service for executing complex queries for {@link Orders} entities in the database.
 * The main input is a {@link OrdersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrdersDTO} or a {@link Page} of {@link OrdersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrdersQueryService extends QueryService<Orders> {

    private final Logger log = LoggerFactory.getLogger(OrdersQueryService.class);

    private final OrdersRepository ordersRepository;

    private final OrdersMapper ordersMapper;

    public OrdersQueryService(OrdersRepository ordersRepository, OrdersMapper ordersMapper) {
        this.ordersRepository = ordersRepository;
        this.ordersMapper = ordersMapper;
    }

    /**
     * Return a {@link List} of {@link OrdersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrdersDTO> findByCriteria(OrdersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Orders> specification = createSpecification(criteria);
        return ordersMapper.toDto(ordersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrdersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdersDTO> findByCriteria(OrdersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Orders> specification = createSpecification(criteria);
        return ordersRepository.findAll(specification, page).map(ordersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrdersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Orders> specification = createSpecification(criteria);
        return ordersRepository.count(specification);
    }

    /**
     * Function to convert {@link OrdersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Orders> createSpecification(OrdersCriteria criteria) {
        Specification<Orders> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Orders_.id));
            }
            if (criteria.getOrderType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderType(), Orders_.orderType));
            }
            if (criteria.getOrderAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderAmount(), Orders_.orderAmount));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Orders_.price));
            }
            if (criteria.getMrp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMrp(), Orders_.mrp));
            }
            if (criteria.getTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTax(), Orders_.tax));
            }
            if (criteria.getQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty(), Orders_.qty));
            }
            if (criteria.getSelectedDaysForWeekly() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSelectedDaysForWeekly(), Orders_.selectedDaysForWeekly));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Orders_.startDate));
            }
            if (criteria.getSubscriptionType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubscriptionType(), Orders_.subscriptionType));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Orders_.status));
            }
            if (criteria.getDeliveryStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryStatus(), Orders_.deliveryStatus));
            }
            if (criteria.getOrderStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderStatus(), Orders_.orderStatus));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Orders_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Orders_.updatedAt));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Orders_.user, JoinType.LEFT).get(Users_.id))
                    );
            }
            if (criteria.getTrasationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTrasationId(),
                            root -> root.join(Orders_.trasation, JoinType.LEFT).get(Transactions_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(Orders_.product, JoinType.LEFT).get(Product_.id))
                    );
            }
            if (criteria.getAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAddressId(), root -> root.join(Orders_.address, JoinType.LEFT).get(UserAddress_.id))
                    );
            }
            if (criteria.getOrderUserAssignId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderUserAssignId(),
                            root -> root.join(Orders_.orderUserAssigns, JoinType.LEFT).get(OrderUserAssign_.id)
                        )
                    );
            }
            if (criteria.getSubscribedOrderDeliveryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubscribedOrderDeliveryId(),
                            root -> root.join(Orders_.subscribedOrderDeliveries, JoinType.LEFT).get(SubscribedOrderDelivery_.id)
                        )
                    );
            }
            if (criteria.getTransactionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionsId(),
                            root -> root.join(Orders_.transactions, JoinType.LEFT).get(Transactions_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
