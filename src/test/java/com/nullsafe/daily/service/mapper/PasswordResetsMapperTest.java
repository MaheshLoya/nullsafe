package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PasswordResetsMapperTest {

    private PasswordResetsMapper passwordResetsMapper;

    @BeforeEach
    public void setUp() {
        passwordResetsMapper = new PasswordResetsMapperImpl();
    }
}
