package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class OrdersMapperTest {

    private OrdersMapper ordersMapper;

    @BeforeEach
    public void setUp() {
        ordersMapper = new OrdersMapperImpl();
    }
}
