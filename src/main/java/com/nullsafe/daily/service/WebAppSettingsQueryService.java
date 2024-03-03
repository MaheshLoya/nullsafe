package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.WebAppSettings;
import com.nullsafe.daily.repository.WebAppSettingsRepository;
import com.nullsafe.daily.service.criteria.WebAppSettingsCriteria;
import com.nullsafe.daily.service.dto.WebAppSettingsDTO;
import com.nullsafe.daily.service.mapper.WebAppSettingsMapper;
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
 * Service for executing complex queries for {@link WebAppSettings} entities in the database.
 * The main input is a {@link WebAppSettingsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WebAppSettingsDTO} or a {@link Page} of {@link WebAppSettingsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WebAppSettingsQueryService extends QueryService<WebAppSettings> {

    private final Logger log = LoggerFactory.getLogger(WebAppSettingsQueryService.class);

    private final WebAppSettingsRepository webAppSettingsRepository;

    private final WebAppSettingsMapper webAppSettingsMapper;

    public WebAppSettingsQueryService(WebAppSettingsRepository webAppSettingsRepository, WebAppSettingsMapper webAppSettingsMapper) {
        this.webAppSettingsRepository = webAppSettingsRepository;
        this.webAppSettingsMapper = webAppSettingsMapper;
    }

    /**
     * Return a {@link List} of {@link WebAppSettingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WebAppSettingsDTO> findByCriteria(WebAppSettingsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WebAppSettings> specification = createSpecification(criteria);
        return webAppSettingsMapper.toDto(webAppSettingsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WebAppSettingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WebAppSettingsDTO> findByCriteria(WebAppSettingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WebAppSettings> specification = createSpecification(criteria);
        return webAppSettingsRepository.findAll(specification, page).map(webAppSettingsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WebAppSettingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WebAppSettings> specification = createSpecification(criteria);
        return webAppSettingsRepository.count(specification);
    }

    /**
     * Function to convert {@link WebAppSettingsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WebAppSettings> createSpecification(WebAppSettingsCriteria criteria) {
        Specification<WebAppSettings> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WebAppSettings_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), WebAppSettings_.title));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), WebAppSettings_.value));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), WebAppSettings_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), WebAppSettings_.updatedAt));
            }
        }
        return specification;
    }
}
