package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrdersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Orders getOrdersSample1() {
        return new Orders()
            .id(1L)
            .orderType(1)
            .qty(1)
            .selectedDaysForWeekly("selectedDaysForWeekly1")
            .subscriptionType(1)
            .status(1)
            .deliveryStatus(1);
    }

    public static Orders getOrdersSample2() {
        return new Orders()
            .id(2L)
            .orderType(2)
            .qty(2)
            .selectedDaysForWeekly("selectedDaysForWeekly2")
            .subscriptionType(2)
            .status(2)
            .deliveryStatus(2);
    }

    public static Orders getOrdersRandomSampleGenerator() {
        return new Orders()
            .id(longCount.incrementAndGet())
            .orderType(intCount.incrementAndGet())
            .qty(intCount.incrementAndGet())
            .selectedDaysForWeekly(UUID.randomUUID().toString())
            .subscriptionType(intCount.incrementAndGet())
            .status(intCount.incrementAndGet())
            .deliveryStatus(intCount.incrementAndGet());
    }
}
