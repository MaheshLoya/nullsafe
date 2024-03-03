package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Testimonials;
import com.nullsafe.daily.service.dto.TestimonialsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Testimonials} and its DTO {@link TestimonialsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestimonialsMapper extends EntityMapper<TestimonialsDTO, Testimonials> {}
