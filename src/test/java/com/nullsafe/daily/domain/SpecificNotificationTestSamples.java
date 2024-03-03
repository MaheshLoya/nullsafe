package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpecificNotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SpecificNotification getSpecificNotificationSample1() {
        return new SpecificNotification().id(1L).title("title1").body("body1");
    }

    public static SpecificNotification getSpecificNotificationSample2() {
        return new SpecificNotification().id(2L).title("title2").body("body2");
    }

    public static SpecificNotification getSpecificNotificationRandomSampleGenerator() {
        return new SpecificNotification()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .body(UUID.randomUUID().toString());
    }
}
