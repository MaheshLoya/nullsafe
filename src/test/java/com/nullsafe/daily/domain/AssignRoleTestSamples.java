package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AssignRoleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssignRole getAssignRoleSample1() {
        return new AssignRole().id(1L);
    }

    public static AssignRole getAssignRoleSample2() {
        return new AssignRole().id(2L);
    }

    public static AssignRole getAssignRoleRandomSampleGenerator() {
        return new AssignRole().id(longCount.incrementAndGet());
    }
}
