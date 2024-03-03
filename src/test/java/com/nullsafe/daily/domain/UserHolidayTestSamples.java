package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserHolidayTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserHoliday getUserHolidaySample1() {
        return new UserHoliday().id(1L).userId(1L);
    }

    public static UserHoliday getUserHolidaySample2() {
        return new UserHoliday().id(2L).userId(2L);
    }

    public static UserHoliday getUserHolidayRandomSampleGenerator() {
        return new UserHoliday().id(longCount.incrementAndGet()).userId(longCount.incrementAndGet());
    }
}
