package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestimonialsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestimonialsDTO.class);
        TestimonialsDTO testimonialsDTO1 = new TestimonialsDTO();
        testimonialsDTO1.setId(1L);
        TestimonialsDTO testimonialsDTO2 = new TestimonialsDTO();
        assertThat(testimonialsDTO1).isNotEqualTo(testimonialsDTO2);
        testimonialsDTO2.setId(testimonialsDTO1.getId());
        assertThat(testimonialsDTO1).isEqualTo(testimonialsDTO2);
        testimonialsDTO2.setId(2L);
        assertThat(testimonialsDTO1).isNotEqualTo(testimonialsDTO2);
        testimonialsDTO1.setId(null);
        assertThat(testimonialsDTO1).isNotEqualTo(testimonialsDTO2);
    }
}
