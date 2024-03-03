package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.OrderUserAssign;
import com.nullsafe.daily.repository.OrderUserAssignRepository;
import com.nullsafe.daily.service.criteria.OrderUserAssignCriteria;
import com.nullsafe.daily.service.dto.OrderUserAssignDTO;
import com.nullsafe.daily.service.mapper.OrderUserAssignMapper;
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
 * Service for executing complex queries for {@link OrderUserAssign} entities in the database.
 * The main input is a {@link OrderUserAssignCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderUserAssignDTO} or a {@link Page} of {@link OrderUserAssignDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderUserAssignQueryService extends QueryService<OrderUserAssign> {

    private final Logger log = LoggerFactory.getLogger(OrderUserAssignQueryService.class);

    private final OrderUserAssignRepository orderUserAssignRepository;

    private final OrderUserAssignMapper orderUserAssignMapper;

    public OrderUserAssignQueryService(OrderUserAssignRepository orderUserAssignRepository, OrderUserAssignMapper orderUserAssignMapper) {
        this.orderUserAssignRepository = orderUserAssignRepository;
        this.orderUserAssignMapper = orderUserAssignMapper;
    }

    /**
     * Return a {@link List} of {@link OrderUserAssignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderUserAssignDTO> findByCriteria(OrderUserAssignCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderUserAssign> specification = createSpecification(criteria);
        return orderUserAssignMapper.toDto(orderUserAssignRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrderUserAssignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderUserAssignDTO> findByCriteria(OrderUserAssignCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderUserAssign> specification = createSpecification(criteria);
        return orderUserAssignRepository.findAll(specification, page).map(orderUserAssignMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderUserAssignCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderUserAssign> specification = createSpecification(criteria);
        return orderUserAssignRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderUserAssignCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderUserAssign> createSpecification(OrderUserAssignCriteria criteria) {
        Specification<OrderUserAssign> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderUserAssign_.id));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), OrderUserAssign_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), OrderUserAssign_.updatedAt));
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrderId(), root -> root.join(OrderUserAssign_.order, JoinType.LEFT).get(Orders_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(OrderUserAssign_.user, JoinType.LEFT).get(Users_.id))
                    );
            }
        }
        return specification;
    }
}
