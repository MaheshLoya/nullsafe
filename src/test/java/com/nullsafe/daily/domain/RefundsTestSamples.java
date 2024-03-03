package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class RefundsTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Refunds getRefundsSample1() {
        return new Refunds()
            .id(1)
            .orderId(1)
            .transactionId("transactionId1")
            .razorpayRefundId("razorpayRefundId1")
            .razorpayPaymentId("razorpayPaymentId1")
            .currency("currency1")
            .status("status1")
            .createdBy("createdBy1");
    }

    public static Refunds getRefundsSample2() {
        return new Refunds()
            .id(2)
            .orderId(2)
            .transactionId("transactionId2")
            .razorpayRefundId("razorpayRefundId2")
            .razorpayPaymentId("razorpayPaymentId2")
            .currency("currency2")
            .status("status2")
            .createdBy("createdBy2");
    }

    public static Refunds getRefundsRandomSampleGenerator() {
        return new Refunds()
            .id(intCount.incrementAndGet())
            .orderId(intCount.incrementAndGet())
            .transactionId(UUID.randomUUID().toString())
            .razorpayRefundId(UUID.randomUUID().toString())
            .razorpayPaymentId(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
