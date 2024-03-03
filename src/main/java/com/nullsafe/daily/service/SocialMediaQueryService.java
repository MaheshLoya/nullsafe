package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.SocialMedia;
import com.nullsafe.daily.repository.SocialMediaRepository;
import com.nullsafe.daily.service.criteria.SocialMediaCriteria;
import com.nullsafe.daily.service.dto.SocialMediaDTO;
import com.nullsafe.daily.service.mapper.SocialMediaMapper;
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
 * Service for executing complex queries for {@link SocialMedia} entities in the database.
 * The main input is a {@link SocialMediaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SocialMediaDTO} or a {@link Page} of {@link SocialMediaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SocialMediaQueryService extends QueryService<SocialMedia> {

    private final Logger log = LoggerFactory.getLogger(SocialMediaQueryService.class);

    private final SocialMediaRepository socialMediaRepository;

    private final SocialMediaMapper socialMediaMapper;

    public SocialMediaQueryService(SocialMediaRepository socialMediaRepository, SocialMediaMapper socialMediaMapper) {
        this.socialMediaRepository = socialMediaRepository;
        this.socialMediaMapper = socialMediaMapper;
    }

    /**
     * Return a {@link List} of {@link SocialMediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SocialMediaDTO> findByCriteria(SocialMediaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SocialMedia> specification = createSpecification(criteria);
        return socialMediaMapper.toDto(socialMediaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SocialMediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialMediaDTO> findByCriteria(SocialMediaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SocialMedia> specification = createSpecification(criteria);
        return socialMediaRepository.findAll(specification, page).map(socialMediaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SocialMediaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SocialMedia> specification = createSpecification(criteria);
        return socialMediaRepository.count(specification);
    }

    /**
     * Function to convert {@link SocialMediaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SocialMedia> createSpecification(SocialMediaCriteria criteria) {
        Specification<SocialMedia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SocialMedia_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), SocialMedia_.title));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), SocialMedia_.image));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), SocialMedia_.url));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), SocialMedia_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), SocialMedia_.updatedAt));
            }
        }
        return specification;
    }
}
