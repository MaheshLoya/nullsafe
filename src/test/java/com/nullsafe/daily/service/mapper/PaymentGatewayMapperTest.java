package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PaymentGatewayMapperTest {

    private PaymentGatewayMapper paymentGatewayMapper;

    @BeforeEach
    public void setUp() {
        paymentGatewayMapper = new PaymentGatewayMapperImpl();
    }
}
