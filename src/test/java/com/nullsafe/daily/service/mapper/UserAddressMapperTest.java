package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserAddressMapperTest {

    private UserAddressMapper userAddressMapper;

    @BeforeEach
    public void setUp() {
        userAddressMapper = new UserAddressMapperImpl();
    }
}
