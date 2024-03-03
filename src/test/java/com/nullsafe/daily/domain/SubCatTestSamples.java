package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubCatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubCat getSubCatSample1() {
        return new SubCat().id(1L).title("title1");
    }

    public static SubCat getSubCatSample2() {
        return new SubCat().id(2L).title("title2");
    }

    public static SubCat getSubCatRandomSampleGenerator() {
        return new SubCat().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
