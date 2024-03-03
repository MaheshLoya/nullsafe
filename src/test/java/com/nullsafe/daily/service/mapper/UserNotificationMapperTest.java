package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserNotificationMapperTest {

    private UserNotificationMapper userNotificationMapper;

    @BeforeEach
    public void setUp() {
        userNotificationMapper = new UserNotificationMapperImpl();
    }
}
