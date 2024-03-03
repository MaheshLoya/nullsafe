package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UsersMapperTest {

    private UsersMapper usersMapper;

    @BeforeEach
    public void setUp() {
        usersMapper = new UsersMapperImpl();
    }
}
