package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SubscriptionRenewalMapperTest {

    private SubscriptionRenewalMapper subscriptionRenewalMapper;

    @BeforeEach
    public void setUp() {
        subscriptionRenewalMapper = new SubscriptionRenewalMapperImpl();
    }
}
