package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Migrations;
import com.nullsafe.daily.repository.MigrationsRepository;
import com.nullsafe.daily.service.criteria.MigrationsCriteria;
import com.nullsafe.daily.service.dto.MigrationsDTO;
import com.nullsafe.daily.service.mapper.MigrationsMapper;
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
 * Service for executing complex queries for {@link Migrations} entities in the database.
 * The main input is a {@link MigrationsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MigrationsDTO} or a {@link Page} of {@link MigrationsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MigrationsQueryService extends QueryService<Migrations> {

    private final Logger log = LoggerFactory.getLogger(MigrationsQueryService.class);

    private final MigrationsRepository migrationsRepository;

    private final MigrationsMapper migrationsMapper;

    public MigrationsQueryService(MigrationsRepository migrationsRepository, MigrationsMapper migrationsMapper) {
        this.migrationsRepository = migrationsRepository;
        this.migrationsMapper = migrationsMapper;
    }

    /**
     * Return a {@link List} of {@link MigrationsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MigrationsDTO> findByCriteria(MigrationsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Migrations> specification = createSpecification(criteria);
        return migrationsMapper.toDto(migrationsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MigrationsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MigrationsDTO> findByCriteria(MigrationsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Migrations> specification = createSpecification(criteria);
        return migrationsRepository.findAll(specification, page).map(migrationsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MigrationsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Migrations> specification = createSpecification(criteria);
        return migrationsRepository.count(specification);
    }

    /**
     * Function to convert {@link MigrationsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Migrations> createSpecification(MigrationsCriteria criteria) {
        Specification<Migrations> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Migrations_.id));
            }
            if (criteria.getMigration() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMigration(), Migrations_.migration));
            }
            if (criteria.getBatch() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBatch(), Migrations_.batch));
            }
        }
        return specification;
    }
}
