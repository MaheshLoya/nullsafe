package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.Testimonials;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Testimonials entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestimonialsRepository extends JpaRepository<Testimonials, Long>, JpaSpecificationExecutor<Testimonials> {}
