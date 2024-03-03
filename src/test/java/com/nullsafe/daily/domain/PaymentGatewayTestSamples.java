package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentGatewayTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentGateway getPaymentGatewaySample1() {
        return new PaymentGateway().id(1L).title("title1").keyId("keyId1").secretId("secretId1");
    }

    public static PaymentGateway getPaymentGatewaySample2() {
        return new PaymentGateway().id(2L).title("title2").keyId("keyId2").secretId("secretId2");
    }

    public static PaymentGateway getPaymentGatewayRandomSampleGenerator() {
        return new PaymentGateway()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .keyId(UUID.randomUUID().toString())
            .secretId(UUID.randomUUID().toString());
    }
}
