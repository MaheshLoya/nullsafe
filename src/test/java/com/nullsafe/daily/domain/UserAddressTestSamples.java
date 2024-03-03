package com.nullsafe.daily.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserAddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserAddress getUserAddressSample1() {
        return new UserAddress()
            .id(1L)
            .userId(1L)
            .name("name1")
            .sPhone("sPhone1")
            .flatNo("flatNo1")
            .apartmentName("apartmentName1")
            .area("area1")
            .landmark("landmark1")
            .city("city1")
            .pincode(1);
    }

    public static UserAddress getUserAddressSample2() {
        return new UserAddress()
            .id(2L)
            .userId(2L)
            .name("name2")
            .sPhone("sPhone2")
            .flatNo("flatNo2")
            .apartmentName("apartmentName2")
            .area("area2")
            .landmark("landmark2")
            .city("city2")
            .pincode(2);
    }

    public static UserAddress getUserAddressRandomSampleGenerator() {
        return new UserAddress()
            .id(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .sPhone(UUID.randomUUID().toString())
            .flatNo(UUID.randomUUID().toString())
            .apartmentName(UUID.randomUUID().toString())
            .area(UUID.randomUUID().toString())
            .landmark(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .pincode(intCount.incrementAndGet());
    }
}
