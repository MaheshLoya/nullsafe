package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class WebAppSettingsTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WebAppSettings getWebAppSettingsSample1() {
        return new WebAppSettings().id(1).title("title1").value("value1");
    }

    public static WebAppSettings getWebAppSettingsSample2() {
        return new WebAppSettings().id(2).title("title2").value("value2");
    }

    public static WebAppSettings getWebAppSettingsRandomSampleGenerator() {
        return new WebAppSettings().id(intCount.incrementAndGet()).title(UUID.randomUUID().toString()).value(UUID.randomUUID().toString());
    }
}
