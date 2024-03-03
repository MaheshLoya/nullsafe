package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.SubscribedOrderDelivery;
import com.nullsafe.daily.repository.SubscribedOrderDeliveryRepository;
import com.nullsafe.daily.service.criteria.SubscribedOrderDeliveryCriteria;
import com.nullsafe.daily.service.dto.SubscribedOrderDeliveryDTO;
import com.nullsafe.daily.service.mapper.SubscribedOrderDeliveryMapper;
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
 * Service for executing complex queries for {@link SubscribedOrderDelivery} entities in the database.
 * The main input is a {@link SubscribedOrderDeliveryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubscribedOrderDeliveryDTO} or a {@link Page} of {@link SubscribedOrderDeliveryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubscribedOrderDeliveryQueryService extends QueryService<SubscribedOrderDelivery> {

    private final Logger log = LoggerFactory.getLogger(SubscribedOrderDeliveryQueryService.class);

    private final SubscribedOrderDeliveryRepository subscribedOrderDeliveryRepository;

    private final SubscribedOrderDeliveryMapper subscribedOrderDeliveryMapper;

    public SubscribedOrderDeliveryQueryService(
        SubscribedOrderDeliveryRepository subscribedOrderDeliveryRepository,
        SubscribedOrderDeliveryMapper subscribedOrderDeliveryMapper
    ) {
        this.subscribedOrderDeliveryRepository = subscribedOrderDeliveryRepository;
        this.subscribedOrderDeliveryMapper = subscribedOrderDeliveryMapper;
    }

    /**
     * Return a {@link List} of {@link SubscribedOrderDeliveryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubscribedOrderDeliveryDTO> findByCriteria(SubscribedOrderDeliveryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubscribedOrderDelivery> specification = createSpecification(criteria);
        return subscribedOrderDeliveryMapper.toDto(subscribedOrderDeliveryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubscribedOrderDeliveryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscribedOrderDeliveryDTO> findByCriteria(SubscribedOrderDeliveryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubscribedOrderDelivery> specification = createSpecification(criteria);
        return subscribedOrderDeliveryRepository.findAll(specification, page).map(subscribedOrderDeliveryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubscribedOrderDeliveryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubscribedOrderDelivery> specification = createSpecification(criteria);
        return subscribedOrderDeliveryRepository.count(specification);
    }

    /**
     * Function to convert {@link SubscribedOrderDeliveryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubscribedOrderDelivery> createSpecification(SubscribedOrderDeliveryCriteria criteria) {
        Specification<SubscribedOrderDelivery> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubscribedOrderDelivery_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), SubscribedOrderDelivery_.date));
            }
            if (criteria.getPaymentMode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentMode(), SubscribedOrderDelivery_.paymentMode));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), SubscribedOrderDelivery_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), SubscribedOrderDelivery_.updatedAt));
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderId(),
                            root -> root.join(SubscribedOrderDelivery_.order, JoinType.LEFT).get(Orders_.id)
                        )
                    );
            }
            if (criteria.getEntryUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEntryUserId(),
                            root -> root.join(SubscribedOrderDelivery_.entryUser, JoinType.LEFT).get(Users_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
