package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BannerImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BannerImage getBannerImageSample1() {
        return new BannerImage().id(1L).image("image1");
    }

    public static BannerImage getBannerImageSample2() {
        return new BannerImage().id(2L).image("image2");
    }

    public static BannerImage getBannerImageRandomSampleGenerator() {
        return new BannerImage().id(longCount.incrementAndGet()).image(UUID.randomUUID().toString());
    }
}
