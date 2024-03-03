package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.PersonalAccessTokens;
import com.nullsafe.daily.repository.PersonalAccessTokensRepository;
import com.nullsafe.daily.service.criteria.PersonalAccessTokensCriteria;
import com.nullsafe.daily.service.dto.PersonalAccessTokensDTO;
import com.nullsafe.daily.service.mapper.PersonalAccessTokensMapper;
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
 * Service for executing complex queries for {@link PersonalAccessTokens} entities in the database.
 * The main input is a {@link PersonalAccessTokensCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonalAccessTokensDTO} or a {@link Page} of {@link PersonalAccessTokensDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonalAccessTokensQueryService extends QueryService<PersonalAccessTokens> {

    private final Logger log = LoggerFactory.getLogger(PersonalAccessTokensQueryService.class);

    private final PersonalAccessTokensRepository personalAccessTokensRepository;

    private final PersonalAccessTokensMapper personalAccessTokensMapper;

    public PersonalAccessTokensQueryService(
        PersonalAccessTokensRepository personalAccessTokensRepository,
        PersonalAccessTokensMapper personalAccessTokensMapper
    ) {
        this.personalAccessTokensRepository = personalAccessTokensRepository;
        this.personalAccessTokensMapper = personalAccessTokensMapper;
    }

    /**
     * Return a {@link List} of {@link PersonalAccessTokensDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonalAccessTokensDTO> findByCriteria(PersonalAccessTokensCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PersonalAccessTokens> specification = createSpecification(criteria);
        return personalAccessTokensMapper.toDto(personalAccessTokensRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonalAccessTokensDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonalAccessTokensDTO> findByCriteria(PersonalAccessTokensCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonalAccessTokens> specification = createSpecification(criteria);
        return personalAccessTokensRepository.findAll(specification, page).map(personalAccessTokensMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonalAccessTokensCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PersonalAccessTokens> specification = createSpecification(criteria);
        return personalAccessTokensRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonalAccessTokensCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PersonalAccessTokens> createSpecification(PersonalAccessTokensCriteria criteria) {
        Specification<PersonalAccessTokens> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PersonalAccessTokens_.id));
            }
            if (criteria.getTokenableType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTokenableType(), PersonalAccessTokens_.tokenableType));
            }
            if (criteria.getTokenableId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTokenableId(), PersonalAccessTokens_.tokenableId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PersonalAccessTokens_.name));
            }
            if (criteria.getToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getToken(), PersonalAccessTokens_.token));
            }
            if (criteria.getAbilities() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAbilities(), PersonalAccessTokens_.abilities));
            }
            if (criteria.getLastUsedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUsedAt(), PersonalAccessTokens_.lastUsedAt));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), PersonalAccessTokens_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), PersonalAccessTokens_.updatedAt));
            }
        }
        return specification;
    }
}
