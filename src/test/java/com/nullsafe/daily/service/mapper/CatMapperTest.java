package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CatMapperTest {

    private CatMapper catMapper;

    @BeforeEach
    public void setUp() {
        catMapper = new CatMapperImpl();
    }
}
