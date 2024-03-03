package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Cat;
import com.nullsafe.daily.repository.CatRepository;
import com.nullsafe.daily.service.criteria.CatCriteria;
import com.nullsafe.daily.service.dto.CatDTO;
import com.nullsafe.daily.service.mapper.CatMapper;
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
 * Service for executing complex queries for {@link Cat} entities in the database.
 * The main input is a {@link CatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CatDTO} or a {@link Page} of {@link CatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatQueryService extends QueryService<Cat> {

    private final Logger log = LoggerFactory.getLogger(CatQueryService.class);

    private final CatRepository catRepository;

    private final CatMapper catMapper;

    public CatQueryService(CatRepository catRepository, CatMapper catMapper) {
        this.catRepository = catRepository;
        this.catMapper = catMapper;
    }

    /**
     * Return a {@link List} of {@link CatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CatDTO> findByCriteria(CatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cat> specification = createSpecification(criteria);
        return catMapper.toDto(catRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CatDTO> findByCriteria(CatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cat> specification = createSpecification(criteria);
        return catRepository.findAll(specification, page).map(catMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cat> specification = createSpecification(criteria);
        return catRepository.count(specification);
    }

    /**
     * Function to convert {@link CatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cat> createSpecification(CatCriteria criteria) {
        Specification<Cat> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cat_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Cat_.title));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Cat_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Cat_.updatedAt));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Cat_.isActive));
            }
            if (criteria.getSubCatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSubCatId(), root -> root.join(Cat_.subCats, JoinType.LEFT).get(SubCat_.id))
                    );
            }
        }
        return specification;
    }
}
