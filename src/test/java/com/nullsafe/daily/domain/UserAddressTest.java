package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.OrdersTestSamples.*;
import static com.nullsafe.daily.domain.SubscribedOrdersTestSamples.*;
import static com.nullsafe.daily.domain.UserAddressTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAddress.class);
        UserAddress userAddress1 = getUserAddressSample1();
        UserAddress userAddress2 = new UserAddress();
        assertThat(userAddress1).isNotEqualTo(userAddress2);

        userAddress2.setId(userAddress1.getId());
        assertThat(userAddress1).isEqualTo(userAddress2);

        userAddress2 = getUserAddressSample2();
        assertThat(userAddress1).isNotEqualTo(userAddress2);
    }

    @Test
    void ordersTest() throws Exception {
        UserAddress userAddress = getUserAddressRandomSampleGenerator();
        Orders ordersBack = getOrdersRandomSampleGenerator();

        userAddress.addOrders(ordersBack);
        assertThat(userAddress.getOrders()).containsOnly(ordersBack);
        assertThat(ordersBack.getAddress()).isEqualTo(userAddress);

        userAddress.removeOrders(ordersBack);
        assertThat(userAddress.getOrders()).doesNotContain(ordersBack);
        assertThat(ordersBack.getAddress()).isNull();

        userAddress.orders(new HashSet<>(Set.of(ordersBack)));
        assertThat(userAddress.getOrders()).containsOnly(ordersBack);
        assertThat(ordersBack.getAddress()).isEqualTo(userAddress);

        userAddress.setOrders(new HashSet<>());
        assertThat(userAddress.getOrders()).doesNotContain(ordersBack);
        assertThat(ordersBack.getAddress()).isNull();
    }

    @Test
    void subscribedOrdersTest() throws Exception {
        UserAddress userAddress = getUserAddressRandomSampleGenerator();
        SubscribedOrders subscribedOrdersBack = getSubscribedOrdersRandomSampleGenerator();

        userAddress.addSubscribedOrders(subscribedOrdersBack);
        assertThat(userAddress.getSubscribedOrders()).containsOnly(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getAddress()).isEqualTo(userAddress);

        userAddress.removeSubscribedOrders(subscribedOrdersBack);
        assertThat(userAddress.getSubscribedOrders()).doesNotContain(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getAddress()).isNull();

        userAddress.subscribedOrders(new HashSet<>(Set.of(subscribedOrdersBack)));
        assertThat(userAddress.getSubscribedOrders()).containsOnly(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getAddress()).isEqualTo(userAddress);

        userAddress.setSubscribedOrders(new HashSet<>());
        assertThat(userAddress.getSubscribedOrders()).doesNotContain(subscribedOrdersBack);
        assertThat(subscribedOrdersBack.getAddress()).isNull();
    }
}
