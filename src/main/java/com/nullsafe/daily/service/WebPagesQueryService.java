package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.WebPages;
import com.nullsafe.daily.repository.WebPagesRepository;
import com.nullsafe.daily.service.criteria.WebPagesCriteria;
import com.nullsafe.daily.service.dto.WebPagesDTO;
import com.nullsafe.daily.service.mapper.WebPagesMapper;
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
 * Service for executing complex queries for {@link WebPages} entities in the database.
 * The main input is a {@link WebPagesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WebPagesDTO} or a {@link Page} of {@link WebPagesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WebPagesQueryService extends QueryService<WebPages> {

    private final Logger log = LoggerFactory.getLogger(WebPagesQueryService.class);

    private final WebPagesRepository webPagesRepository;

    private final WebPagesMapper webPagesMapper;

    public WebPagesQueryService(WebPagesRepository webPagesRepository, WebPagesMapper webPagesMapper) {
        this.webPagesRepository = webPagesRepository;
        this.webPagesMapper = webPagesMapper;
    }

    /**
     * Return a {@link List} of {@link WebPagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WebPagesDTO> findByCriteria(WebPagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WebPages> specification = createSpecification(criteria);
        return webPagesMapper.toDto(webPagesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WebPagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WebPagesDTO> findByCriteria(WebPagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WebPages> specification = createSpecification(criteria);
        return webPagesRepository.findAll(specification, page).map(webPagesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WebPagesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WebPages> specification = createSpecification(criteria);
        return webPagesRepository.count(specification);
    }

    /**
     * Function to convert {@link WebPagesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WebPages> createSpecification(WebPagesCriteria criteria) {
        Specification<WebPages> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WebPages_.id));
            }
            if (criteria.getPageId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageId(), WebPages_.pageId));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), WebPages_.title));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), WebPages_.body));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), WebPages_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), WebPages_.updatedAt));
            }
        }
        return specification;
    }
}
