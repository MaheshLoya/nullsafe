package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.BannerImage;
import com.nullsafe.daily.repository.BannerImageRepository;
import com.nullsafe.daily.service.criteria.BannerImageCriteria;
import com.nullsafe.daily.service.dto.BannerImageDTO;
import com.nullsafe.daily.service.mapper.BannerImageMapper;
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
 * Service for executing complex queries for {@link BannerImage} entities in the database.
 * The main input is a {@link BannerImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BannerImageDTO} or a {@link Page} of {@link BannerImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BannerImageQueryService extends QueryService<BannerImage> {

    private final Logger log = LoggerFactory.getLogger(BannerImageQueryService.class);

    private final BannerImageRepository bannerImageRepository;

    private final BannerImageMapper bannerImageMapper;

    public BannerImageQueryService(BannerImageRepository bannerImageRepository, BannerImageMapper bannerImageMapper) {
        this.bannerImageRepository = bannerImageRepository;
        this.bannerImageMapper = bannerImageMapper;
    }

    /**
     * Return a {@link List} of {@link BannerImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BannerImageDTO> findByCriteria(BannerImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BannerImage> specification = createSpecification(criteria);
        return bannerImageMapper.toDto(bannerImageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BannerImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BannerImageDTO> findByCriteria(BannerImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BannerImage> specification = createSpecification(criteria);
        return bannerImageRepository.findAll(specification, page).map(bannerImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BannerImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BannerImage> specification = createSpecification(criteria);
        return bannerImageRepository.count(specification);
    }

    /**
     * Function to convert {@link BannerImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BannerImage> createSpecification(BannerImageCriteria criteria) {
        Specification<BannerImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BannerImage_.id));
            }
            if (criteria.getImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage(), BannerImage_.image));
            }
            if (criteria.getImageType() != null) {
                specification = specification.and(buildSpecification(criteria.getImageType(), BannerImage_.imageType));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), BannerImage_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), BannerImage_.updatedAt));
            }
        }
        return specification;
    }
}
