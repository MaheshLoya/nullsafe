package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.SubCat;
import com.nullsafe.daily.repository.SubCatRepository;
import com.nullsafe.daily.service.criteria.SubCatCriteria;
import com.nullsafe.daily.service.dto.SubCatDTO;
import com.nullsafe.daily.service.mapper.SubCatMapper;
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
 * Service for executing complex queries for {@link SubCat} entities in the database.
 * The main input is a {@link SubCatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubCatDTO} or a {@link Page} of {@link SubCatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubCatQueryService extends QueryService<SubCat> {

    private final Logger log = LoggerFactory.getLogger(SubCatQueryService.class);

    private final SubCatRepository subCatRepository;

    private final SubCatMapper subCatMapper;

    public SubCatQueryService(SubCatRepository subCatRepository, SubCatMapper subCatMapper) {
        this.subCatRepository = subCatRepository;
        this.subCatMapper = subCatMapper;
    }

    /**
     * Return a {@link List} of {@link SubCatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubCatDTO> findByCriteria(SubCatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubCat> specification = createSpecification(criteria);
        return subCatMapper.toDto(subCatRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubCatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubCatDTO> findByCriteria(SubCatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubCat> specification = createSpecification(criteria);
        return subCatRepository.findAll(specification, page).map(subCatMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubCatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubCat> specification = createSpecification(criteria);
        return subCatRepository.count(specification);
    }

    /**
     * Function to convert {@link SubCatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubCat> createSpecification(SubCatCriteria criteria) {
        Specification<SubCat> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubCat_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), SubCat_.title));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), SubCat_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), SubCat_.updatedAt));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), SubCat_.isActive));
            }
            if (criteria.getCatId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCatId(), root -> root.join(SubCat_.cat, JoinType.LEFT).get(Cat_.id)));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(SubCat_.products, JoinType.LEFT).get(Product_.id))
                    );
            }
        }
        return specification;
    }
}
