package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cat getCatSample1() {
        return new Cat().id(1L).title("title1");
    }

    public static Cat getCatSample2() {
        return new Cat().id(2L).title("title2");
    }

    public static Cat getCatRandomSampleGenerator() {
        return new Cat().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
