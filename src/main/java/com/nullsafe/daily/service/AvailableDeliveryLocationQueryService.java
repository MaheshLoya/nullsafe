package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.AvailableDeliveryLocation;
import com.nullsafe.daily.repository.AvailableDeliveryLocationRepository;
import com.nullsafe.daily.service.criteria.AvailableDeliveryLocationCriteria;
import com.nullsafe.daily.service.dto.AvailableDeliveryLocationDTO;
import com.nullsafe.daily.service.mapper.AvailableDeliveryLocationMapper;
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
 * Service for executing complex queries for {@link AvailableDeliveryLocation} entities in the database.
 * The main input is a {@link AvailableDeliveryLocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AvailableDeliveryLocationDTO} or a {@link Page} of {@link AvailableDeliveryLocationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AvailableDeliveryLocationQueryService extends QueryService<AvailableDeliveryLocation> {

    private final Logger log = LoggerFactory.getLogger(AvailableDeliveryLocationQueryService.class);

    private final AvailableDeliveryLocationRepository availableDeliveryLocationRepository;

    private final AvailableDeliveryLocationMapper availableDeliveryLocationMapper;

    public AvailableDeliveryLocationQueryService(
        AvailableDeliveryLocationRepository availableDeliveryLocationRepository,
        AvailableDeliveryLocationMapper availableDeliveryLocationMapper
    ) {
        this.availableDeliveryLocationRepository = availableDeliveryLocationRepository;
        this.availableDeliveryLocationMapper = availableDeliveryLocationMapper;
    }

    /**
     * Return a {@link List} of {@link AvailableDeliveryLocationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AvailableDeliveryLocationDTO> findByCriteria(AvailableDeliveryLocationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AvailableDeliveryLocation> specification = createSpecification(criteria);
        return availableDeliveryLocationMapper.toDto(availableDeliveryLocationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AvailableDeliveryLocationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AvailableDeliveryLocationDTO> findByCriteria(AvailableDeliveryLocationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AvailableDeliveryLocation> specification = createSpecification(criteria);
        return availableDeliveryLocationRepository.findAll(specification, page).map(availableDeliveryLocationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AvailableDeliveryLocationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AvailableDeliveryLocation> specification = createSpecification(criteria);
        return availableDeliveryLocationRepository.count(specification);
    }

    /**
     * Function to convert {@link AvailableDeliveryLocationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AvailableDeliveryLocation> createSpecification(AvailableDeliveryLocationCriteria criteria) {
        Specification<AvailableDeliveryLocation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AvailableDeliveryLocation_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AvailableDeliveryLocation_.title));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), AvailableDeliveryLocation_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), AvailableDeliveryLocation_.updatedAt));
            }
        }
        return specification;
    }
}
