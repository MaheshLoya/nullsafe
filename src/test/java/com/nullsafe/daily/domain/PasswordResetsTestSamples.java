package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PasswordResetsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PasswordResets getPasswordResetsSample1() {
        return new PasswordResets().id(1L).email("email1").token("token1");
    }

    public static PasswordResets getPasswordResetsSample2() {
        return new PasswordResets().id(2L).email("email2").token("token2");
    }

    public static PasswordResets getPasswordResetsRandomSampleGenerator() {
        return new PasswordResets().id(longCount.incrementAndGet()).email(UUID.randomUUID().toString()).token(UUID.randomUUID().toString());
    }
}
