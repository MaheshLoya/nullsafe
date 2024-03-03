package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.UserHoliday;
import com.nullsafe.daily.repository.UserHolidayRepository;
import com.nullsafe.daily.service.criteria.UserHolidayCriteria;
import com.nullsafe.daily.service.dto.UserHolidayDTO;
import com.nullsafe.daily.service.mapper.UserHolidayMapper;
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
 * Service for executing complex queries for {@link UserHoliday} entities in the database.
 * The main input is a {@link UserHolidayCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserHolidayDTO} or a {@link Page} of {@link UserHolidayDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserHolidayQueryService extends QueryService<UserHoliday> {

    private final Logger log = LoggerFactory.getLogger(UserHolidayQueryService.class);

    private final UserHolidayRepository userHolidayRepository;

    private final UserHolidayMapper userHolidayMapper;

    public UserHolidayQueryService(UserHolidayRepository userHolidayRepository, UserHolidayMapper userHolidayMapper) {
        this.userHolidayRepository = userHolidayRepository;
        this.userHolidayMapper = userHolidayMapper;
    }

    /**
     * Return a {@link List} of {@link UserHolidayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserHolidayDTO> findByCriteria(UserHolidayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserHoliday> specification = createSpecification(criteria);
        return userHolidayMapper.toDto(userHolidayRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserHolidayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserHolidayDTO> findByCriteria(UserHolidayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserHoliday> specification = createSpecification(criteria);
        return userHolidayRepository.findAll(specification, page).map(userHolidayMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserHolidayCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserHoliday> specification = createSpecification(criteria);
        return userHolidayRepository.count(specification);
    }

    /**
     * Function to convert {@link UserHolidayCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserHoliday> createSpecification(UserHolidayCriteria criteria) {
        Specification<UserHoliday> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserHoliday_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), UserHoliday_.userId));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), UserHoliday_.date));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), UserHoliday_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), UserHoliday_.updatedAt));
            }
        }
        return specification;
    }
}
