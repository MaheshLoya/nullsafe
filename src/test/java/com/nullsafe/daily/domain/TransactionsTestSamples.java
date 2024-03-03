package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Transactions getTransactionsSample1() {
        return new Transactions().id(1L).paymentId("paymentId1").description("description1").type(1).paymentMode(1);
    }

    public static Transactions getTransactionsSample2() {
        return new Transactions().id(2L).paymentId("paymentId2").description("description2").type(2).paymentMode(2);
    }

    public static Transactions getTransactionsRandomSampleGenerator() {
        return new Transactions()
            .id(longCount.incrementAndGet())
            .paymentId(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .type(intCount.incrementAndGet())
            .paymentMode(intCount.incrementAndGet());
    }
}
