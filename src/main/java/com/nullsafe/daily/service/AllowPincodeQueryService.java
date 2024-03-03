package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.AllowPincode;
import com.nullsafe.daily.repository.AllowPincodeRepository;
import com.nullsafe.daily.service.criteria.AllowPincodeCriteria;
import com.nullsafe.daily.service.dto.AllowPincodeDTO;
import com.nullsafe.daily.service.mapper.AllowPincodeMapper;
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
 * Service for executing complex queries for {@link AllowPincode} entities in the database.
 * The main input is a {@link AllowPincodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AllowPincodeDTO} or a {@link Page} of {@link AllowPincodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AllowPincodeQueryService extends QueryService<AllowPincode> {

    private final Logger log = LoggerFactory.getLogger(AllowPincodeQueryService.class);

    private final AllowPincodeRepository allowPincodeRepository;

    private final AllowPincodeMapper allowPincodeMapper;

    public AllowPincodeQueryService(AllowPincodeRepository allowPincodeRepository, AllowPincodeMapper allowPincodeMapper) {
        this.allowPincodeRepository = allowPincodeRepository;
        this.allowPincodeMapper = allowPincodeMapper;
    }

    /**
     * Return a {@link List} of {@link AllowPincodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AllowPincodeDTO> findByCriteria(AllowPincodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AllowPincode> specification = createSpecification(criteria);
        return allowPincodeMapper.toDto(allowPincodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AllowPincodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AllowPincodeDTO> findByCriteria(AllowPincodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AllowPincode> specification = createSpecification(criteria);
        return allowPincodeRepository.findAll(specification, page).map(allowPincodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AllowPincodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AllowPincode> specification = createSpecification(criteria);
        return allowPincodeRepository.count(specification);
    }

    /**
     * Function to convert {@link AllowPincodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AllowPincode> createSpecification(AllowPincodeCriteria criteria) {
        Specification<AllowPincode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AllowPincode_.id));
            }
            if (criteria.getPinCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPinCode(), AllowPincode_.pinCode));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), AllowPincode_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), AllowPincode_.updatedAt));
            }
        }
        return specification;
    }
}
