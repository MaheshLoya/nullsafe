package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.FailedJobs;
import com.nullsafe.daily.repository.FailedJobsRepository;
import com.nullsafe.daily.service.criteria.FailedJobsCriteria;
import com.nullsafe.daily.service.dto.FailedJobsDTO;
import com.nullsafe.daily.service.mapper.FailedJobsMapper;
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
 * Service for executing complex queries for {@link FailedJobs} entities in the database.
 * The main input is a {@link FailedJobsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FailedJobsDTO} or a {@link Page} of {@link FailedJobsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FailedJobsQueryService extends QueryService<FailedJobs> {

    private final Logger log = LoggerFactory.getLogger(FailedJobsQueryService.class);

    private final FailedJobsRepository failedJobsRepository;

    private final FailedJobsMapper failedJobsMapper;

    public FailedJobsQueryService(FailedJobsRepository failedJobsRepository, FailedJobsMapper failedJobsMapper) {
        this.failedJobsRepository = failedJobsRepository;
        this.failedJobsMapper = failedJobsMapper;
    }

    /**
     * Return a {@link List} of {@link FailedJobsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FailedJobsDTO> findByCriteria(FailedJobsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FailedJobs> specification = createSpecification(criteria);
        return failedJobsMapper.toDto(failedJobsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FailedJobsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FailedJobsDTO> findByCriteria(FailedJobsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FailedJobs> specification = createSpecification(criteria);
        return failedJobsRepository.findAll(specification, page).map(failedJobsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FailedJobsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FailedJobs> specification = createSpecification(criteria);
        return failedJobsRepository.count(specification);
    }

    /**
     * Function to convert {@link FailedJobsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FailedJobs> createSpecification(FailedJobsCriteria criteria) {
        Specification<FailedJobs> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FailedJobs_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUuid(), FailedJobs_.uuid));
            }
            if (criteria.getConnection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConnection(), FailedJobs_.connection));
            }
            if (criteria.getQueue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQueue(), FailedJobs_.queue));
            }
            if (criteria.getFailedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFailedAt(), FailedJobs_.failedAt));
            }
        }
        return specification;
    }
}
