package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionRenewalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscriptionRenewal getSubscriptionRenewalSample1() {
        return new SubscriptionRenewal().id(1L).userId(1).orderId(1L).transactionId(1L);
    }

    public static SubscriptionRenewal getSubscriptionRenewalSample2() {
        return new SubscriptionRenewal().id(2L).userId(2).orderId(2L).transactionId(2L);
    }

    public static SubscriptionRenewal getSubscriptionRenewalRandomSampleGenerator() {
        return new SubscriptionRenewal()
            .id(longCount.incrementAndGet())
            .userId(intCount.incrementAndGet())
            .orderId(longCount.incrementAndGet())
            .transactionId(longCount.incrementAndGet());
    }
}
