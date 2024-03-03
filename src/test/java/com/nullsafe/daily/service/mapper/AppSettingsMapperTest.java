package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AppSettingsMapperTest {

    private AppSettingsMapper appSettingsMapper;

    @BeforeEach
    public void setUp() {
        appSettingsMapper = new AppSettingsMapperImpl();
    }
}
