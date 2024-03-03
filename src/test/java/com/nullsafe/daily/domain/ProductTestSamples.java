package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .title("title1")
            .qtyText("qtyText1")
            .stockQty(1L)
            .offerText("offerText1")
            .description("description1")
            .disclaimer("disclaimer1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .title("title2")
            .qtyText("qtyText2")
            .stockQty(2L)
            .offerText("offerText2")
            .description("description2")
            .disclaimer("disclaimer2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .qtyText(UUID.randomUUID().toString())
            .stockQty(longCount.incrementAndGet())
            .offerText(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .disclaimer(UUID.randomUUID().toString());
    }
}
