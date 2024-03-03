package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.OrderUserAssignTestSamples.*;
import static com.nullsafe.daily.domain.OrdersTestSamples.*;
import static com.nullsafe.daily.domain.ProductTestSamples.*;
import static com.nullsafe.daily.domain.SubscribedOrderDeliveryTestSamples.*;
import static com.nullsafe.daily.domain.TransactionsTestSamples.*;
import static com.nullsafe.daily.domain.UserAddressTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrdersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orders.class);
        Orders orders1 = getOrdersSample1();
        Orders orders2 = new Orders();
        assertThat(orders1).isNotEqualTo(orders2);

        orders2.setId(orders1.getId());
        assertThat(orders1).isEqualTo(orders2);

        orders2 = getOrdersSample2();
        assertThat(orders1).isNotEqualTo(orders2);
    }

    @Test
    void userTest() throws Exception {
        Orders orders = getOrdersRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        orders.setUser(usersBack);
        assertThat(orders.getUser()).isEqualTo(usersBack);

        orders.user(null);
        assertThat(orders.getUser()).isNull();
    }

    @Test
    void trasationTest() throws Exception {
        Orders orders = getOrdersRandomSampleGenerator();
        Transactions transactionsBack = getTransactionsRandomSampleGenerator();

        orders.setTrasation(transactionsBack);
        assertThat(orders.getTrasation()).isEqualTo(transactionsBack);

        orders.trasation(null);
        assertThat(orders.getTrasation()).isNull();
    }

    @Test
    void productTest() throws Exception {
        Orders orders = getOrdersRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        orders.setProduct(productBack);
        assertThat(orders.getProduct()).isEqualTo(productBack);

        orders.product(null);
        assertThat(orders.getProduct()).isNull();
    }

    @Test
    void addressTest() throws Exception {
        Orders orders = getOrdersRandomSampleGenerator();
        UserAddress userAddressBack = getUserAddressRandomSampleGenerator();

        orders.setAddress(userAddressBack);
        assertThat(orders.getAddress()).isEqualTo(userAddressBack);

        orders.address(null);
        assertThat(orders.getAddress()).isNull();
    }

    @Test
    void orderUserAssignTest() throws Exception {
        Orders orders = getOrdersRandomSampleGenerator();
        OrderUserAssign orderUserAssignBack = getOrderUserAssignRandomSampleGenerator();

        orders.addOrderUserAssign(orderUserAssignBack);
        assertThat(orders.getOrderUserAssigns()).containsOnly(orderUserAssignBack);
        assertThat(orderUserAssignBack.getOrder()).isEqualTo(orders);

        orders.removeOrderUserAssign(orderUserAssignBack);
        assertThat(orders.getOrderUserAssigns()).doesNotContain(orderUserAssignBack);
        assertThat(orderUserAssignBack.getOrder()).isNull();

        orders.orderUserAssigns(new HashSet<>(Set.of(orderUserAssignBack)));
        assertThat(orders.getOrderUserAssigns()).containsOnly(orderUserAssignBack);
        assertThat(orderUserAssignBack.getOrder()).isEqualTo(orders);

        orders.setOrderUserAssigns(new HashSet<>());
        assertThat(orders.getOrderUserAssigns()).doesNotContain(orderUserAssignBack);
        assertThat(orderUserAssignBack.getOrder()).isNull();
    }

    @Test
    void subscribedOrderDeliveryTest() throws Exception {
        Orders orders = getOrdersRandomSampleGenerator();
        SubscribedOrderDelivery subscribedOrderDeliveryBack = getSubscribedOrderDeliveryRandomSampleGenerator();

        orders.addSubscribedOrderDelivery(subscribedOrderDeliveryBack);
        assertThat(orders.getSubscribedOrderDeliveries()).containsOnly(subscribedOrderDeliveryBack);
        assertThat(subscribedOrderDeliveryBack.getOrder()).isEqualTo(orders);

        orders.removeSubscribedOrderDelivery(subscribedOrderDeliveryBack);
        assertThat(orders.getSubscribedOrderDeliveries()).doesNotContain(subscribedOrderDeliveryBack);
        assertThat(subscribedOrderDeliveryBack.getOrder()).isNull();

        orders.subscribedOrderDeliveries(new HashSet<>(Set.of(subscribedOrderDeliveryBack)));
        assertThat(orders.getSubscribedOrderDeliveries()).containsOnly(subscribedOrderDeliveryBack);
        assertThat(subscribedOrderDeliveryBack.getOrder()).isEqualTo(orders);

        orders.setSubscribedOrderDeliveries(new HashSet<>());
        assertThat(orders.getSubscribedOrderDeliveries()).doesNotContain(subscribedOrderDeliveryBack);
        assertThat(subscribedOrderDeliveryBack.getOrder()).isNull();
    }

    @Test
    void transactionsTest() throws Exception {
        Orders orders = getOrdersRandomSampleGenerator();
        Transactions transactionsBack = getTransactionsRandomSampleGenerator();

        orders.addTransactions(transactionsBack);
        assertThat(orders.getTransactions()).containsOnly(transactionsBack);
        assertThat(transactionsBack.getOrder()).isEqualTo(orders);

        orders.removeTransactions(transactionsBack);
        assertThat(orders.getTransactions()).doesNotContain(transactionsBack);
        assertThat(transactionsBack.getOrder()).isNull();

        orders.transactions(new HashSet<>(Set.of(transactionsBack)));
        assertThat(orders.getTransactions()).containsOnly(transactionsBack);
        assertThat(transactionsBack.getOrder()).isEqualTo(orders);

        orders.setTransactions(new HashSet<>());
        assertThat(orders.getTransactions()).doesNotContain(transactionsBack);
        assertThat(transactionsBack.getOrder()).isNull();
    }
}
