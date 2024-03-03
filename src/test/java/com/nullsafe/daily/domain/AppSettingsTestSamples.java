package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppSettingsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppSettings getAppSettingsSample1() {
        return new AppSettings().id(1L).settingId(1).title("title1").value("value1");
    }

    public static AppSettings getAppSettingsSample2() {
        return new AppSettings().id(2L).settingId(2).title("title2").value("value2");
    }

    public static AppSettings getAppSettingsRandomSampleGenerator() {
        return new AppSettings()
            .id(longCount.incrementAndGet())
            .settingId(intCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString());
    }
}
