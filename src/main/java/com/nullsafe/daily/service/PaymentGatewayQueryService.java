package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.PaymentGateway;
import com.nullsafe.daily.repository.PaymentGatewayRepository;
import com.nullsafe.daily.service.criteria.PaymentGatewayCriteria;
import com.nullsafe.daily.service.dto.PaymentGatewayDTO;
import com.nullsafe.daily.service.mapper.PaymentGatewayMapper;
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
 * Service for executing complex queries for {@link PaymentGateway} entities in the database.
 * The main input is a {@link PaymentGatewayCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentGatewayDTO} or a {@link Page} of {@link PaymentGatewayDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentGatewayQueryService extends QueryService<PaymentGateway> {

    private final Logger log = LoggerFactory.getLogger(PaymentGatewayQueryService.class);

    private final PaymentGatewayRepository paymentGatewayRepository;

    private final PaymentGatewayMapper paymentGatewayMapper;

    public PaymentGatewayQueryService(PaymentGatewayRepository paymentGatewayRepository, PaymentGatewayMapper paymentGatewayMapper) {
        this.paymentGatewayRepository = paymentGatewayRepository;
        this.paymentGatewayMapper = paymentGatewayMapper;
    }

    /**
     * Return a {@link List} of {@link PaymentGatewayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentGatewayDTO> findByCriteria(PaymentGatewayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentGateway> specification = createSpecification(criteria);
        return paymentGatewayMapper.toDto(paymentGatewayRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentGatewayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentGatewayDTO> findByCriteria(PaymentGatewayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentGateway> specification = createSpecification(criteria);
        return paymentGatewayRepository.findAll(specification, page).map(paymentGatewayMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentGatewayCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentGateway> specification = createSpecification(criteria);
        return paymentGatewayRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentGatewayCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentGateway> createSpecification(PaymentGatewayCriteria criteria) {
        Specification<PaymentGateway> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentGateway_.id));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), PaymentGateway_.active));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), PaymentGateway_.title));
            }
            if (criteria.getKeyId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKeyId(), PaymentGateway_.keyId));
            }
            if (criteria.getSecretId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSecretId(), PaymentGateway_.secretId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), PaymentGateway_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), PaymentGateway_.updatedAt));
            }
        }
        return specification;
    }
}
