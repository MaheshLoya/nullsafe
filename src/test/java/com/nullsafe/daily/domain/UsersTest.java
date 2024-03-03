package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.AssignRoleTestSamples.*;
import static com.nullsafe.daily.domain.CartTestSamples.*;
import static com.nullsafe.daily.domain.OrderUserAssignTestSamples.*;
import static com.nullsafe.daily.domain.OrdersTestSamples.*;
import static com.nullsafe.daily.domain.SpecificNotificationTestSamples.*;
import static com.nullsafe.daily.domain.SubscribedOrderDeliveryTestSamples.*;
import static com.nullsafe.daily.domain.SubscribedOrdersTestSamples.*;
import static com.nullsafe.daily.domain.TransactionsTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UsersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Users.class);
        Users users1 = getUsersSample1();
        Users users2 = new Users();
        assertThat(users1).isNotEqualTo(users2);

        users2.setId(users1.getId());
        assertThat(users1).isEqualTo(users2);

        users2 = getUsersSample2();
        assertThat(users1).isNotEqualTo(users2);
    }

    @Test
    void assignRoleTest() throws Exception {
        Users users = getUsersRandomSampleGenerator();
        AssignRole assignRoleBack = getAssignRoleRandomSampleGenerator();

        users.addAssignRole(assignRoleBack);
        assertThat(users.getAssignRoles()).containsOnly(assignRoleBack);
        assertThat(assignRoleBack.getUser()).isEqualTo(users);

        users.removeAssignRole(assignRoleBack);
        assertThat(users.getAssignRoles()).doesNotContain(assignRoleBack);
        assertThat(assignRoleBack.getUser()).isNull();

        users.assignRoles(new HashSet<>(Set.of(assignRoleBack)));
        assertThat(users.getAssignRoles()).containsOnly(assignRoleBack);
        assertThat(assignRoleBack.getUser()).isEqualTo(users);

        users.setAssignRoles(new HashSet<>());
        assertThat(users.getAssignRoles()).doesNotContain(assignRoleBack);
        assertThat(assignRoleBack.getUser()).isNull();
    }

    @Test
    void cartTest() throws Exception {
        Users users = getUsersRandomSampleGenerator();
        Cart cartBack = getCartRandomSampleGenerator();

        users.addCart(cartBack);
        assertThat(users.getCarts()).containsOnly(cartBack);
        assertThat(cartBack.getUser()).isEqualTo(users);

        users.removeCart(cartBack);
        assertThat(users.getCarts()).doesNotContain(cartBack);
        assertThat(cartBack.getUser()).isNull();

        users.carts(new HashSet<>(Set.of(cartBack)));
        assertThat(users.getCarts()).containsOnly(cartBack);
        assertThat(cartBack.getUser()).isEqualTo(users);

        users.setCarts(new HashSet<>());
        assertThat(users.getCarts()).doesNotContain(cartBack);
        assertThat(cartBack.getUser()).isNull();
    }

    @Test
    void orderUserAssignTest() throws Exception {
        Users users = getUsersRandomSampleGenerator();
        OrderUserAssign orderUserAssignBack = getOrderUserAssignRandomSampleGenerator();

        users.addOrderUserAssign(orderUserAssignBack);
        assertThat(users.getOrderUserAssigns()).containsOnly(orderUserAssignBack);
        assertThat(orderUserAssignBack.getUser()).isEqualTo(users);

        users.removeOrderUserAssign(orderUserAssignBack);
        assertThat(users.getOrderUserAssigns()).doesNotContain(orderUserAssignBack);
        assertThat(orderUserAssignBack.getUser()).isNull();

        users.orderUserAssigns(new HashSet<>(Set.of(orderUserAssignBack)));
        assertThat(users.getOrderUserAssigns()).containsOnly(orderUserAssignBack);
        assertThat(orderUserAssignBack.getUser()).isEqualTo(users);

        users.setOrderUserAssigns(new HashSet<>());
        assertThat(users.getOrderUserAssigns()).doesNotContain(orderUserAssignBack);
        assertThat(orderUserAssignBack.getUser()).isNull();
    }

    @Test
    void ordersTest() throws Exception {
        Users users = getUsersRandomSampleGenerator();
        Orders ordersBack = getOrdersRandomSampleGenerator();

        users.addOrders(ordersBack);
        assertThat(users.getOrders()).containsOnly(ordersBack);
        assertThat(ordersBack.getUser()).isEqualTo(users);

        users.removeOrders(ordersBack);
        assertThat(users.getOrders()).doesNotContain(ordersBack);
        assertThat(ordersBack.getUser()).isNull();

        users.orders(new HashSet<>(Set.of(ordersBack)));
        assertThat(users.getOrders()).containsOnly(ordersBack);
        assertThat(ordersBack.getUser()).isEqualTo(users);

        users.setOrders(new HashSet<>());
        assertThat(users.getOrders()).doesNotContain(ordersBack);
        assertThat(ordersBack.getUser()).isNull();
    }

    @Test
    void specificNotificationTest() throws Exception {
        Users users = getUsersRandomSampleGenerator();
        SpecificNotification specificNotificationBack = getSpecificNotificationRandomSampleGenerator();

        users.addSpecificNotification(specificNotificationBack);
        assertThat(users.getSpecificNotifications()).containsOnly(specificNotificationBack);
        assertThat(specificNotificationBack.getUser()).isEqualTo(users);

        users.removeSpecificNotification(specificNotificationBack);
        assertThat(users.getSpecificNotifications()).doesNotContain(specificNotificationBack);
        assertThat(specificNotificationBack.getUser()).isNull();

        users.specificNotifications(new HashSet<>(Set.of(specificNotificationBack)));
        assertThat(users.getSpecificNotifications()).containsOnly(specificNotificationBack);
        assertThat(specificNotificationBack.getUser()).isEqualTo(users);

        users.setSpecificNotifications(new HashSet<>());
        assertThat(users.getSpecificNotifications()).doesNotContain(specificNotificationBack);
        assertThat(specificNotificationBack.getUser()).isNull();
    }

    @Test
    void subscribedOrderDeliveryTest() throws Exception {
        Users users = getUsersRandomSampleGenerator();
        SubscribedOrderDelivery subscribedOrderDeliveryBack = getSubscribedOrderDeliveryRandomSampleGenerator();

        users.addSubscribedOrderDelivery(subscribedOrderDeliveryBack);
        assertThat(users.getSubscribedOrderDeliveries()).containsOnly(subscribedOrderDeliveryBack);
        assertThat(subscribedOrderDeliveryBack.getEntryUser()).isEqualTo(users);

        users.removeSubscribedOrderDelivery(subscribedOrderDeliveryBack);
        assertThat(users.getSubscribedOrderDeliveries()).doesNotContain(subscribedOrderDeliveryBack);
        assertThat(subscribedOrderDeliveryBack.getEntryUser()).isNull();

        users.subscribedOrderDeliveries(new HashSet<>(Set.of(subscribedOrderDeliveryBack)));
        assertThat(users.getSubscribedOrderDeliveries()).containsOnly(subscribedOrderDeliveryBack);
        assertThat(subscribedOrderDeliveryBack.getEntryUser()).isEqualTo(users);

        users.setSubscribedOrderDeliveries(new HashSet<>());
        assertThat(users.getSubscribedOrderDeliveries()).doesNotContain(subscribedOrderDeliveryBack);
        assertThat(subscribedOrderDeliveryBack.getEntryUser()).isNull();
    }

    @Test
    void subscribedOrdersTest() throws Exception {
        Users users = getUsersRandomSampleGenerator();
        SubscribedOrders subscribedOrdersBack = getSubscribedOrdersRandomSampleGenerator();

        users.addSubscribedOrders(subscribedOrdersBack);
        assertThat(users.getSubscribedOrders()).containsOnly(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getUser()).isEqualTo(users);

        users.removeSubscribedOrders(subscribedOrdersBack);
        assertThat(users.getSubscribedOrders()).doesNotContain(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getUser()).isNull();

        users.subscribedOrders(new HashSet<>(Set.of(subscribedOrdersBack)));
        assertThat(users.getSubscribedOrders()).containsOnly(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getUser()).isEqualTo(users);

        users.setSubscribedOrders(new HashSet<>());
        assertThat(users.getSubscribedOrders()).doesNotContain(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getUser()).isNull();
    }

    @Test
    void transactionsTest() throws Exception {
        Users users = getUsersRandomSampleGenerator();
        Transactions transactionsBack = getTransactionsRandomSampleGenerator();

        users.addTransactions(transactionsBack);
        assertThat(users.getTransactions()).containsOnly(transactionsBack);
        assertThat(transactionsBack.getUser()).isEqualTo(users);

        users.removeTransactions(transactionsBack);
        assertThat(users.getTransactions()).doesNotContain(transactionsBack);
        assertThat(transactionsBack.getUser()).isNull();

        users.transactions(new HashSet<>(Set.of(transactionsBack)));
        assertThat(users.getTransactions()).containsOnly(transactionsBack);
        assertThat(transactionsBack.getUser()).isEqualTo(users);

        users.setTransactions(new HashSet<>());
        assertThat(users.getTransactions()).doesNotContain(transactionsBack);
        assertThat(transactionsBack.getUser()).isNull();
    }
}
