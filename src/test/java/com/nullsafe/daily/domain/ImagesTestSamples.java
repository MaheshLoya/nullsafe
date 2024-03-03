package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ImagesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Images getImagesSample1() {
        return new Images().id(1L).tableName("tableName1").tableId(1L).image("image1");
    }

    public static Images getImagesSample2() {
        return new Images().id(2L).tableName("tableName2").tableId(2L).image("image2");
    }

    public static Images getImagesRandomSampleGenerator() {
        return new Images()
            .id(longCount.incrementAndGet())
            .tableName(UUID.randomUUID().toString())
            .tableId(longCount.incrementAndGet())
            .image(UUID.randomUUID().toString());
    }
}
