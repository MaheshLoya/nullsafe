package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.OrdersTestSamples.*;
import static com.nullsafe.daily.domain.SubscribedOrdersTestSamples.*;
import static com.nullsafe.daily.domain.TransactionsTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TransactionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transactions.class);
        Transactions transactions1 = getTransactionsSample1();
        Transactions transactions2 = new Transactions();
        assertThat(transactions1).isNotEqualTo(transactions2);

        transactions2.setId(transactions1.getId());
        assertThat(transactions1).isEqualTo(transactions2);

        transactions2 = getTransactionsSample2();
        assertThat(transactions1).isNotEqualTo(transactions2);
    }

    @Test
    void orderTest() throws Exception {
        Transactions transactions = getTransactionsRandomSampleGenerator();
        Orders ordersBack = getOrdersRandomSampleGenerator();

        transactions.setOrder(ordersBack);
        assertThat(transactions.getOrder()).isEqualTo(ordersBack);

        transactions.order(null);
        assertThat(transactions.getOrder()).isNull();
    }

    @Test
    void userTest() throws Exception {
        Transactions transactions = getTransactionsRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        transactions.setUser(usersBack);
        assertThat(transactions.getUser()).isEqualTo(usersBack);

        transactions.user(null);
        assertThat(transactions.getUser()).isNull();
    }

    @Test
    void ordersTest() throws Exception {
        Transactions transactions = getTransactionsRandomSampleGenerator();
        Orders ordersBack = getOrdersRandomSampleGenerator();

        transactions.addOrders(ordersBack);
        assertThat(transactions.getOrders()).containsOnly(ordersBack);
        assertThat(ordersBack.getTrasation()).isEqualTo(transactions);

        transactions.removeOrders(ordersBack);
        assertThat(transactions.getOrders()).doesNotContain(ordersBack);
        assertThat(ordersBack.getTrasation()).isNull();

        transactions.orders(new HashSet<>(Set.of(ordersBack)));
        assertThat(transactions.getOrders()).containsOnly(ordersBack);
        assertThat(ordersBack.getTrasation()).isEqualTo(transactions);

        transactions.setOrders(new HashSet<>());
        assertThat(transactions.getOrders()).doesNotContain(ordersBack);
        assertThat(ordersBack.getTrasation()).isNull();
    }

    @Test
    void subscribedOrdersTest() throws Exception {
        Transactions transactions = getTransactionsRandomSampleGenerator();
        SubscribedOrders subscribedOrdersBack = getSubscribedOrdersRandomSampleGenerator();

        transactions.addSubscribedOrders(subscribedOrdersBack);
        assertThat(transactions.getSubscribedOrders()).containsOnly(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getTransaction()).isEqualTo(transactions);

        transactions.removeSubscribedOrders(subscribedOrdersBack);
        assertThat(transactions.getSubscribedOrders()).doesNotContain(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getTransaction()).isNull();

        transactions.subscribedOrders(new HashSet<>(Set.of(subscribedOrdersBack)));
        assertThat(transactions.getSubscribedOrders()).containsOnly(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getTransaction()).isEqualTo(transactions);

        transactions.setSubscribedOrders(new HashSet<>());
        assertThat(transactions.getSubscribedOrders()).doesNotContain(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getTransaction()).isNull();
    }
}
