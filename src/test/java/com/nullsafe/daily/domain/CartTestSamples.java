package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CartTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cart getCartSample1() {
        return new Cart().id(1L).qty(1).qtyText("qtyText1");
    }

    public static Cart getCartSample2() {
        return new Cart().id(2L).qty(2).qtyText("qtyText2");
    }

    public static Cart getCartRandomSampleGenerator() {
        return new Cart().id(longCount.incrementAndGet()).qty(intCount.incrementAndGet()).qtyText(UUID.randomUUID().toString());
    }
}
