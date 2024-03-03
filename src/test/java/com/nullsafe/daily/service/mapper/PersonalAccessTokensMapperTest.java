package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PersonalAccessTokensMapperTest {

    private PersonalAccessTokensMapper personalAccessTokensMapper;

    @BeforeEach
    public void setUp() {
        personalAccessTokensMapper = new PersonalAccessTokensMapperImpl();
    }
}
