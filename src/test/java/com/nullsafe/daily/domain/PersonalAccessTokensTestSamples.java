package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonalAccessTokensTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PersonalAccessTokens getPersonalAccessTokensSample1() {
        return new PersonalAccessTokens()
            .id(1L)
            .tokenableType("tokenableType1")
            .tokenableId(1L)
            .name("name1")
            .token("token1")
            .abilities("abilities1");
    }

    public static PersonalAccessTokens getPersonalAccessTokensSample2() {
        return new PersonalAccessTokens()
            .id(2L)
            .tokenableType("tokenableType2")
            .tokenableId(2L)
            .name("name2")
            .token("token2")
            .abilities("abilities2");
    }

    public static PersonalAccessTokens getPersonalAccessTokensRandomSampleGenerator() {
        return new PersonalAccessTokens()
            .id(longCount.incrementAndGet())
            .tokenableType(UUID.randomUUID().toString())
            .tokenableId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .token(UUID.randomUUID().toString())
            .abilities(UUID.randomUUID().toString());
    }
}
