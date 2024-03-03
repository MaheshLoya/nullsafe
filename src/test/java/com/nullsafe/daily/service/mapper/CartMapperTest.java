package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CartMapperTest {

    private CartMapper cartMapper;

    @BeforeEach
    public void setUp() {
        cartMapper = new CartMapperImpl();
    }
}
