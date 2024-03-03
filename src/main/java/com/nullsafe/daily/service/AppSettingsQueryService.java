package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.AppSettings;
import com.nullsafe.daily.repository.AppSettingsRepository;
import com.nullsafe.daily.service.criteria.AppSettingsCriteria;
import com.nullsafe.daily.service.dto.AppSettingsDTO;
import com.nullsafe.daily.service.mapper.AppSettingsMapper;
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
 * Service for executing complex queries for {@link AppSettings} entities in the database.
 * The main input is a {@link AppSettingsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppSettingsDTO} or a {@link Page} of {@link AppSettingsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppSettingsQueryService extends QueryService<AppSettings> {

    private final Logger log = LoggerFactory.getLogger(AppSettingsQueryService.class);

    private final AppSettingsRepository appSettingsRepository;

    private final AppSettingsMapper appSettingsMapper;

    public AppSettingsQueryService(AppSettingsRepository appSettingsRepository, AppSettingsMapper appSettingsMapper) {
        this.appSettingsRepository = appSettingsRepository;
        this.appSettingsMapper = appSettingsMapper;
    }

    /**
     * Return a {@link List} of {@link AppSettingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppSettingsDTO> findByCriteria(AppSettingsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AppSettings> specification = createSpecification(criteria);
        return appSettingsMapper.toDto(appSettingsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppSettingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppSettingsDTO> findByCriteria(AppSettingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppSettings> specification = createSpecification(criteria);
        return appSettingsRepository.findAll(specification, page).map(appSettingsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppSettingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AppSettings> specification = createSpecification(criteria);
        return appSettingsRepository.count(specification);
    }

    /**
     * Function to convert {@link AppSettingsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppSettings> createSpecification(AppSettingsCriteria criteria) {
        Specification<AppSettings> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppSettings_.id));
            }
            if (criteria.getSettingId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSettingId(), AppSettings_.settingId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AppSettings_.title));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), AppSettings_.value));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), AppSettings_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), AppSettings_.updatedAt));
            }
        }
        return specification;
    }
}
