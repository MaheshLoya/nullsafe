package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AvailableDeliveryLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AvailableDeliveryLocation getAvailableDeliveryLocationSample1() {
        return new AvailableDeliveryLocation().id(1L).title("title1");
    }

    public static AvailableDeliveryLocation getAvailableDeliveryLocationSample2() {
        return new AvailableDeliveryLocation().id(2L).title("title2");
    }

    public static AvailableDeliveryLocation getAvailableDeliveryLocationRandomSampleGenerator() {
        return new AvailableDeliveryLocation().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
