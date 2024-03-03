package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceSettingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceSetting getInvoiceSettingSample1() {
        return new InvoiceSetting().id(1L).title("title1").value("value1");
    }

    public static InvoiceSetting getInvoiceSettingSample2() {
        return new InvoiceSetting().id(2L).title("title2").value("value2");
    }

    public static InvoiceSetting getInvoiceSettingRandomSampleGenerator() {
        return new InvoiceSetting().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
