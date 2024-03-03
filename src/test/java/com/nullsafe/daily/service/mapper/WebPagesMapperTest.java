package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class WebPagesMapperTest {

    private WebPagesMapper webPagesMapper;

    @BeforeEach
    public void setUp() {
        webPagesMapper = new WebPagesMapperImpl();
    }
}
