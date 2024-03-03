package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderUserAssignTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OrderUserAssign getOrderUserAssignSample1() {
        return new OrderUserAssign().id(1L);
    }

    public static OrderUserAssign getOrderUserAssignSample2() {
        return new OrderUserAssign().id(2L);
    }

    public static OrderUserAssign getOrderUserAssignRandomSampleGenerator() {
        return new OrderUserAssign().id(longCount.incrementAndGet());
    }
}
