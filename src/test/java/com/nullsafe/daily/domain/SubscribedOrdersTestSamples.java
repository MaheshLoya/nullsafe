package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubscribedOrdersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscribedOrders getSubscribedOrdersSample1() {
        return new SubscribedOrders()
            .id(1L)
            .paymentType(1)
            .qty(1)
            .offerId(1)
            .selectedDaysForWeekly("selectedDaysForWeekly1")
            .subscriptionType(1)
            .approvalStatus(1)
            .createdBy("createdBy1")
            .updatedBy("updatedBy1");
    }

    public static SubscribedOrders getSubscribedOrdersSample2() {
        return new SubscribedOrders()
            .id(2L)
            .paymentType(2)
            .qty(2)
            .offerId(2)
            .selectedDaysForWeekly("selectedDaysForWeekly2")
            .subscriptionType(2)
            .approvalStatus(2)
            .createdBy("createdBy2")
            .updatedBy("updatedBy2");
    }

    public static SubscribedOrders getSubscribedOrdersRandomSampleGenerator() {
        return new SubscribedOrders()
            .id(longCount.incrementAndGet())
            .paymentType(intCount.incrementAndGet())
            .qty(intCount.incrementAndGet())
            .offerId(intCount.incrementAndGet())
            .selectedDaysForWeekly(UUID.randomUUID().toString())
            .subscriptionType(intCount.incrementAndGet())
            .approvalStatus(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .updatedBy(UUID.randomUUID().toString());
    }
}
