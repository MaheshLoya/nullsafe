package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UsersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Users getUsersSample1() {
        return new Users()
            .id(1L)
            .email("email1")
            .phone("phone1")
            .password("password1")
            .rememberToken("rememberToken1")
            .name("name1")
            .fcm("fcm1")
            .subscriptionAmount(1);
    }

    public static Users getUsersSample2() {
        return new Users()
            .id(2L)
            .email("email2")
            .phone("phone2")
            .password("password2")
            .rememberToken("rememberToken2")
            .name("name2")
            .fcm("fcm2")
            .subscriptionAmount(2);
    }

    public static Users getUsersRandomSampleGenerator() {
        return new Users()
            .id(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .rememberToken(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .fcm(UUID.randomUUID().toString())
            .subscriptionAmount(intCount.incrementAndGet());
    }
}
