package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SubscribedOrdersMapperTest {

    private SubscribedOrdersMapper subscribedOrdersMapper;

    @BeforeEach
    public void setUp() {
        subscribedOrdersMapper = new SubscribedOrdersMapperImpl();
    }
}
