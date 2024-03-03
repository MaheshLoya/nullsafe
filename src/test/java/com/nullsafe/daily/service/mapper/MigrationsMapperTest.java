package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class MigrationsMapperTest {

    private MigrationsMapper migrationsMapper;

    @BeforeEach
    public void setUp() {
        migrationsMapper = new MigrationsMapperImpl();
    }
}
