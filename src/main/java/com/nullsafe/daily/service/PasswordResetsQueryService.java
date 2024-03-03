package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.PasswordResets;
import com.nullsafe.daily.repository.PasswordResetsRepository;
import com.nullsafe.daily.service.criteria.PasswordResetsCriteria;
import com.nullsafe.daily.service.dto.PasswordResetsDTO;
import com.nullsafe.daily.service.mapper.PasswordResetsMapper;
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
 * Service for executing complex queries for {@link PasswordResets} entities in the database.
 * The main input is a {@link PasswordResetsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PasswordResetsDTO} or a {@link Page} of {@link PasswordResetsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PasswordResetsQueryService extends QueryService<PasswordResets> {

    private final Logger log = LoggerFactory.getLogger(PasswordResetsQueryService.class);

    private final PasswordResetsRepository passwordResetsRepository;

    private final PasswordResetsMapper passwordResetsMapper;

    public PasswordResetsQueryService(PasswordResetsRepository passwordResetsRepository, PasswordResetsMapper passwordResetsMapper) {
        this.passwordResetsRepository = passwordResetsRepository;
        this.passwordResetsMapper = passwordResetsMapper;
    }

    /**
     * Return a {@link List} of {@link PasswordResetsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PasswordResetsDTO> findByCriteria(PasswordResetsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PasswordResets> specification = createSpecification(criteria);
        return passwordResetsMapper.toDto(passwordResetsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PasswordResetsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PasswordResetsDTO> findByCriteria(PasswordResetsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PasswordResets> specification = createSpecification(criteria);
        return passwordResetsRepository.findAll(specification, page).map(passwordResetsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PasswordResetsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PasswordResets> specification = createSpecification(criteria);
        return passwordResetsRepository.count(specification);
    }

    /**
     * Function to convert {@link PasswordResetsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PasswordResets> createSpecification(PasswordResetsCriteria criteria) {
        Specification<PasswordResets> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PasswordResets_.id));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), PasswordResets_.email));
            }
            if (criteria.getToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getToken(), PasswordResets_.token));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), PasswordResets_.createdAt));
            }
        }
        return specification;
    }
}
