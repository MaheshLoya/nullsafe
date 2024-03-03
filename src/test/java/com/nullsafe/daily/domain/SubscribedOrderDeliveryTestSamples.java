package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SubscribedOrderDeliveryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SubscribedOrderDelivery getSubscribedOrderDeliverySample1() {
        return new SubscribedOrderDelivery().id(1L).paymentMode(1);
    }

    public static SubscribedOrderDelivery getSubscribedOrderDeliverySample2() {
        return new SubscribedOrderDelivery().id(2L).paymentMode(2);
    }

    public static SubscribedOrderDelivery getSubscribedOrderDeliveryRandomSampleGenerator() {
        return new SubscribedOrderDelivery().id(longCount.incrementAndGet()).paymentMode(intCount.incrementAndGet());
    }
}
