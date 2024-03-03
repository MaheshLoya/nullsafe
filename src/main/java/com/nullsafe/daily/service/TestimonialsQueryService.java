package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Testimonials;
import com.nullsafe.daily.repository.TestimonialsRepository;
import com.nullsafe.daily.service.criteria.TestimonialsCriteria;
import com.nullsafe.daily.service.dto.TestimonialsDTO;
import com.nullsafe.daily.service.mapper.TestimonialsMapper;
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
 * Service for executing complex queries for {@link Testimonials} entities in the database.
 * The main input is a {@link TestimonialsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TestimonialsDTO} or a {@link Page} of {@link TestimonialsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TestimonialsQueryService extends QueryService<Testimonials> {

    private final Logger log = LoggerFactory.getLogger(TestimonialsQueryService.class);

    private final TestimonialsRepository testimonialsRepository;

    private final TestimonialsMapper testimonialsMapper;

    public TestimonialsQueryService(TestimonialsRepository testimonialsRepository, TestimonialsMapper testimonialsMapper) {
        this.testimonialsRepository = testimonialsRepository;
        this.testimonialsMapper = testimonialsMapper;
    }

    /**
     * Return a {@link List} of {@link TestimonialsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TestimonialsDTO> findByCriteria(TestimonialsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Testimonials> specification = createSpecification(criteria);
        return testimonialsMapper.toDto(testimonialsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TestimonialsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TestimonialsDTO> findByCriteria(TestimonialsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Testimonials> specification = createSpecification(criteria);
        return testimonialsRepository.findAll(specification, page).map(testimonialsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TestimonialsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Testimonials> specification = createSpecification(criteria);
        return testimonialsRepository.count(specification);
    }

    /**
     * Function to convert {@link TestimonialsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Testimonials> createSpecification(TestimonialsCriteria criteria) {
        Specification<Testimonials> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Testimonials_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Testimonials_.title));
            }
            if (criteria.getSubTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubTitle(), Testimonials_.subTitle));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Testimonials_.rating));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Testimonials_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Testimonials_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Testimonials_.updatedAt));
            }
        }
        return specification;
    }
}
