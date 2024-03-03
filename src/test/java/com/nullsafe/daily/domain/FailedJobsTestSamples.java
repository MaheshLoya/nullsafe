package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FailedJobsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FailedJobs getFailedJobsSample1() {
        return new FailedJobs().id(1L).uuid("uuid1").connection("connection1").queue("queue1");
    }

    public static FailedJobs getFailedJobsSample2() {
        return new FailedJobs().id(2L).uuid("uuid2").connection("connection2").queue("queue2");
    }

    public static FailedJobs getFailedJobsRandomSampleGenerator() {
        return new FailedJobs()
            .id(longCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString())
            .connection(UUID.randomUUID().toString())
            .queue(UUID.randomUUID().toString());
    }
}
