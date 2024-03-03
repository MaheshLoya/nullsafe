package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.AssignRole;
import com.nullsafe.daily.repository.AssignRoleRepository;
import com.nullsafe.daily.service.criteria.AssignRoleCriteria;
import com.nullsafe.daily.service.dto.AssignRoleDTO;
import com.nullsafe.daily.service.mapper.AssignRoleMapper;
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
 * Service for executing complex queries for {@link AssignRole} entities in the database.
 * The main input is a {@link AssignRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssignRoleDTO} or a {@link Page} of {@link AssignRoleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssignRoleQueryService extends QueryService<AssignRole> {

    private final Logger log = LoggerFactory.getLogger(AssignRoleQueryService.class);

    private final AssignRoleRepository assignRoleRepository;

    private final AssignRoleMapper assignRoleMapper;

    public AssignRoleQueryService(AssignRoleRepository assignRoleRepository, AssignRoleMapper assignRoleMapper) {
        this.assignRoleRepository = assignRoleRepository;
        this.assignRoleMapper = assignRoleMapper;
    }

    /**
     * Return a {@link List} of {@link AssignRoleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssignRoleDTO> findByCriteria(AssignRoleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssignRole> specification = createSpecification(criteria);
        return assignRoleMapper.toDto(assignRoleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssignRoleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignRoleDTO> findByCriteria(AssignRoleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssignRole> specification = createSpecification(criteria);
        return assignRoleRepository.findAll(specification, page).map(assignRoleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssignRoleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssignRole> specification = createSpecification(criteria);
        return assignRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link AssignRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssignRole> createSpecification(AssignRoleCriteria criteria) {
        Specification<AssignRole> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssignRole_.id));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), AssignRole_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), AssignRole_.updatedAt));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(AssignRole_.user, JoinType.LEFT).get(Users_.id))
                    );
            }
            if (criteria.getRoleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRoleId(), root -> root.join(AssignRole_.role, JoinType.LEFT).get(Role_.id))
                    );
            }
        }
        return specification;
    }
}
