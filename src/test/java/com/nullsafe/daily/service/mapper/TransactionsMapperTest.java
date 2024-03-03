package com.nullsafe.daily.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TransactionsMapperTest {

    private TransactionsMapper transactionsMapper;

    @BeforeEach
    public void setUp() {
        transactionsMapper = new TransactionsMapperImpl();
    }
}
