package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class FailedJobsMapperTest {

    private FailedJobsMapper failedJobsMapper;

    @BeforeEach
    public void setUp() {
        failedJobsMapper = new FailedJobsMapperImpl();
    }
}
