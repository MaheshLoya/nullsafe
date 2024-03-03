package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Images;
import com.nullsafe.daily.repository.ImagesRepository;
import com.nullsafe.daily.service.criteria.ImagesCriteria;
import com.nullsafe.daily.service.dto.ImagesDTO;
import com.nullsafe.daily.service.mapper.ImagesMapper;
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
 * Service for executing complex queries for {@link Images} entities in the database.
 * The main input is a {@link ImagesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ImagesDTO} or a {@link Page} of {@link ImagesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImagesQueryService extends QueryService<Images> {

    private final Logger log = LoggerFactory.getLogger(ImagesQueryService.class);

    private final ImagesRepository imagesRepository;

    private final ImagesMapper imagesMapper;

    public ImagesQueryService(ImagesRepository imagesRepository, ImagesMapper imagesMapper) {
        this.imagesRepository = imagesRepository;
        this.imagesMapper = imagesMapper;
    }

    /**
     * Return a {@link List} of {@link ImagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ImagesDTO> findByCriteria(ImagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesMapper.toDto(imagesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ImagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImagesDTO> findByCriteria(ImagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesRepository.findAll(specification, page).map(imagesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImagesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesRepository.count(specification);
    }

    /**
     * Function to convert {@link ImagesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Images> createSpecification(ImagesCriteria criteria) {
        Specification<Images> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Images_.id));
            }
            if (criteria.getTableName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTableName(), Images_.tableName));
            }
            if (criteria.getTableId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTableId(), Images_.tableId));
            }
            if (criteria.getImageType() != null) {
                specification = specification.and(buildSpecification(criteria.getImageType(), Images_.imageType));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), Images_.image));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Images_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Images_.updatedAt));
            }
        }
        return specification;
    }
}
