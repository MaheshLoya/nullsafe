package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserHolidayMapperTest {

    private UserHolidayMapper userHolidayMapper;

    @BeforeEach
    public void setUp() {
        userHolidayMapper = new UserHolidayMapperImpl();
    }
}
