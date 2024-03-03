package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.TestimonialsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestimonialsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Testimonials.class);
        Testimonials testimonials1 = getTestimonialsSample1();
        Testimonials testimonials2 = new Testimonials();
        assertThat(testimonials1).isNotEqualTo(testimonials2);

        testimonials2.setId(testimonials1.getId());
        assertThat(testimonials1).isEqualTo(testimonials2);

        testimonials2 = getTestimonialsSample2();
        assertThat(testimonials1).isNotEqualTo(testimonials2);
    }
}
