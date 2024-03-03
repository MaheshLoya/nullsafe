package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MigrationsTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Migrations getMigrationsSample1() {
        return new Migrations().id(1).migration("migration1").batch(1);
    }

    public static Migrations getMigrationsSample2() {
        return new Migrations().id(2).migration("migration2").batch(2);
    }

    public static Migrations getMigrationsRandomSampleGenerator() {
        return new Migrations().id(intCount.incrementAndGet()).migration(UUID.randomUUID().toString()).batch(intCount.incrementAndGet());
    }
}
