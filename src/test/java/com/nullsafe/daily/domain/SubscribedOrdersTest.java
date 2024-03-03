package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.ProductTestSamples.*;
import static com.nullsafe.daily.domain.SubscribedOrdersTestSamples.*;
import static com.nullsafe.daily.domain.TransactionsTestSamples.*;
import static com.nullsafe.daily.domain.UserAddressTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscribedOrdersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscribedOrders.class);
        SubscribedOrders subscribedOrders1 = getSubscribedOrdersSample1();
        SubscribedOrders subscribedOrders2 = new SubscribedOrders();
        assertThat(subscribedOrders1).isNotEqualTo(subscribedOrders2);

        subscribedOrders2.setId(subscribedOrders1.getId());
        assertThat(subscribedOrders1).isEqualTo(subscribedOrders2);

        subscribedOrders2 = getSubscribedOrdersSample2();
        assertThat(subscribedOrders1).isNotEqualTo(subscribedOrders2);
    }

    @Test
    void userTest() throws Exception {
        SubscribedOrders subscribedOrders = getSubscribedOrdersRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        subscribedOrders.setUser(usersBack);
        assertThat(subscribedOrders.getUser()).isEqualTo(usersBack);

        subscribedOrders.user(null);
        assertThat(subscribedOrders.getUser()).isNull();
    }

    @Test
    void transactionTest() throws Exception {
        SubscribedOrders subscribedOrders = getSubscribedOrdersRandomSampleGenerator();
        Transactions transactionsBack = getTransactionsRandomSampleGenerator();

        subscribedOrders.setTransaction(transactionsBack);
        assertThat(subscribedOrders.getTransaction()).isEqualTo(transactionsBack);

        subscribedOrders.transaction(null);
        assertThat(subscribedOrders.getTransaction()).isNull();
    }

    @Test
    void productTest() throws Exception {
        SubscribedOrders subscribedOrders = getSubscribedOrdersRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        subscribedOrders.setProduct(productBack);
        assertThat(subscribedOrders.getProduct()).isEqualTo(productBack);

        subscribedOrders.product(null);
        assertThat(subscribedOrders.getProduct()).isNull();
    }

    @Test
    void addressTest() throws Exception {
        SubscribedOrders subscribedOrders = getSubscribedOrdersRandomSampleGenerator();
        UserAddress userAddressBack = getUserAddressRandomSampleGenerator();

        subscribedOrders.setAddress(userAddressBack);
        assertThat(subscribedOrders.getAddress()).isEqualTo(userAddressBack);

        subscribedOrders.address(null);
        assertThat(subscribedOrders.getAddress()).isNull();
    }
}
