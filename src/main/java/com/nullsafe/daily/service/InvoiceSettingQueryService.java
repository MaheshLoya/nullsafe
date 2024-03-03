package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.InvoiceSetting;
import com.nullsafe.daily.repository.InvoiceSettingRepository;
import com.nullsafe.daily.service.criteria.InvoiceSettingCriteria;
import com.nullsafe.daily.service.dto.InvoiceSettingDTO;
import com.nullsafe.daily.service.mapper.InvoiceSettingMapper;
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
 * Service for executing complex queries for {@link InvoiceSetting} entities in the database.
 * The main input is a {@link InvoiceSettingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceSettingDTO} or a {@link Page} of {@link InvoiceSettingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceSettingQueryService extends QueryService<InvoiceSetting> {

    private final Logger log = LoggerFactory.getLogger(InvoiceSettingQueryService.class);

    private final InvoiceSettingRepository invoiceSettingRepository;

    private final InvoiceSettingMapper invoiceSettingMapper;

    public InvoiceSettingQueryService(InvoiceSettingRepository invoiceSettingRepository, InvoiceSettingMapper invoiceSettingMapper) {
        this.invoiceSettingRepository = invoiceSettingRepository;
        this.invoiceSettingMapper = invoiceSettingMapper;
    }

    /**
     * Return a {@link List} of {@link InvoiceSettingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceSettingDTO> findByCriteria(InvoiceSettingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InvoiceSetting> specification = createSpecification(criteria);
        return invoiceSettingMapper.toDto(invoiceSettingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InvoiceSettingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceSettingDTO> findByCriteria(InvoiceSettingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvoiceSetting> specification = createSpecification(criteria);
        return invoiceSettingRepository.findAll(specification, page).map(invoiceSettingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvoiceSettingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InvoiceSetting> specification = createSpecification(criteria);
        return invoiceSettingRepository.count(specification);
    }

    /**
     * Function to convert {@link InvoiceSettingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InvoiceSetting> createSpecification(InvoiceSettingCriteria criteria) {
        Specification<InvoiceSetting> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InvoiceSetting_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), InvoiceSetting_.title));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), InvoiceSetting_.value));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), InvoiceSetting_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), InvoiceSetting_.updatedAt));
            }
        }
        return specification;
    }
}
