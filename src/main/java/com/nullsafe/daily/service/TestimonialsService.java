package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.Testimonials;
import com.nullsafe.daily.repository.TestimonialsRepository;
import com.nullsafe.daily.service.dto.TestimonialsDTO;
import com.nullsafe.daily.service.mapper.TestimonialsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.Testimonials}.
 */
@Service
@Transactional
public class TestimonialsService {

    private final Logger log = LoggerFactory.getLogger(TestimonialsService.class);

    private final TestimonialsRepository testimonialsRepository;

    private final TestimonialsMapper testimonialsMapper;

    public TestimonialsService(TestimonialsRepository testimonialsRepository, TestimonialsMapper testimonialsMapper) {
        this.testimonialsRepository = testimonialsRepository;
        this.testimonialsMapper = testimonialsMapper;
    }

    /**
     * Save a testimonials.
     *
     * @param testimonialsDTO the entity to save.
     * @return the persisted entity.
     */
    public TestimonialsDTO save(TestimonialsDTO testimonialsDTO) {
        log.debug("Request to save Testimonials : {}", testimonialsDTO);
        Testimonials testimonials = testimonialsMapper.toEntity(testimonialsDTO);
        testimonials = testimonialsRepository.save(testimonials);
        return testimonialsMapper.toDto(testimonials);
    }

    /**
     * Update a testimonials.
     *
     * @param testimonialsDTO the entity to save.
     * @return the persisted entity.
     */
    public TestimonialsDTO update(TestimonialsDTO testimonialsDTO) {
        log.debug("Request to update Testimonials : {}", testimonialsDTO);
        Testimonials testimonials = testimonialsMapper.toEntity(testimonialsDTO);
        testimonials = testimonialsRepository.save(testimonials);
        return testimonialsMapper.toDto(testimonials);
    }

    /**
     * Partially update a testimonials.
     *
     * @param testimonialsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestimonialsDTO> partialUpdate(TestimonialsDTO testimonialsDTO) {
        log.debug("Request to partially update Testimonials : {}", testimonialsDTO);

        return testimonialsRepository
            .findById(testimonialsDTO.getId())
            .map(existingTestimonials -> {
                testimonialsMapper.partialUpdate(existingTestimonials, testimonialsDTO);

                return existingTestimonials;
            })
            .map(testimonialsRepository::save)
            .map(testimonialsMapper::toDto);
    }

    /**
     * Get all the testimonials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TestimonialsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Testimonials");
        return testimonialsRepository.findAll(pageable).map(testimonialsMapper::toDto);
    }

    /**
     * Get one testimonials by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestimonialsDTO> findOne(Long id) {
        log.debug("Request to get Testimonials : {}", id);
        return testimonialsRepository.findById(id).map(testimonialsMapper::toDto);
    }

    /**
     * Delete the testimonials by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Testimonials : {}", id);
        testimonialsRepository.deleteById(id);
    }
}
