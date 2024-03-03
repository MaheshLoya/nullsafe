package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FilesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Files getFilesSample1() {
        return new Files().id(1L).name("name1").fileUrl("fileUrl1").fileFor(1).fileForId(1);
    }

    public static Files getFilesSample2() {
        return new Files().id(2L).name("name2").fileUrl("fileUrl2").fileFor(2).fileForId(2);
    }

    public static Files getFilesRandomSampleGenerator() {
        return new Files()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .fileUrl(UUID.randomUUID().toString())
            .fileFor(intCount.incrementAndGet())
            .fileForId(intCount.incrementAndGet());
    }
}
