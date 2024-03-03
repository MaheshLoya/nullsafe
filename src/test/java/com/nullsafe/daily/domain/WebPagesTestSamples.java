package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WebPagesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WebPages getWebPagesSample1() {
        return new WebPages().id(1L).pageId(1).title("title1").body("body1");
    }

    public static WebPages getWebPagesSample2() {
        return new WebPages().id(2L).pageId(2).title("title2").body("body2");
    }

    public static WebPages getWebPagesRandomSampleGenerator() {
        return new WebPages()
            .id(longCount.incrementAndGet())
            .pageId(intCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .body(UUID.randomUUID().toString());
    }
}
