package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserNotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserNotification getUserNotificationSample1() {
        return new UserNotification().id(1L).title("title1").body("body1");
    }

    public static UserNotification getUserNotificationSample2() {
        return new UserNotification().id(2L).title("title2").body("body2");
    }

    public static UserNotification getUserNotificationRandomSampleGenerator() {
        return new UserNotification()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .body(UUID.randomUUID().toString());
    }
}
