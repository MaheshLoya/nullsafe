package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AllowPincodeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AllowPincode getAllowPincodeSample1() {
        return new AllowPincode().id(1L).pinCode(1);
    }

    public static AllowPincode getAllowPincodeSample2() {
        return new AllowPincode().id(2L).pinCode(2);
    }

    public static AllowPincode getAllowPincodeRandomSampleGenerator() {
        return new AllowPincode().id(longCount.incrementAndGet()).pinCode(intCount.incrementAndGet());
    }
}
