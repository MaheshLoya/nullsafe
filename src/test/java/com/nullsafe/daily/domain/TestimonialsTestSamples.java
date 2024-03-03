package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestimonialsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Testimonials getTestimonialsSample1() {
        return new Testimonials().id(1L).title("title1").subTitle("subTitle1").rating(1).description("description1");
    }

    public static Testimonials getTestimonialsSample2() {
        return new Testimonials().id(2L).title("title2").subTitle("subTitle2").rating(2).description("description2");
    }

    public static Testimonials getTestimonialsRandomSampleGenerator() {
        return new Testimonials()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .subTitle(UUID.randomUUID().toString())
            .rating(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
