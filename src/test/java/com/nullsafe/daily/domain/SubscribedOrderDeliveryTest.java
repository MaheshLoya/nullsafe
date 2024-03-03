package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.OrdersTestSamples.*;
import static com.nullsafe.daily.domain.SubscribedOrderDeliveryTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscribedOrderDeliveryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscribedOrderDelivery.class);
        SubscribedOrderDelivery subscribedOrderDelivery1 = getSubscribedOrderDeliverySample1();
        SubscribedOrderDelivery subscribedOrderDelivery2 = new SubscribedOrderDelivery();
        assertThat(subscribedOrderDelivery1).isNotEqualTo(subscribedOrderDelivery2);

        subscribedOrderDelivery2.setId(subscribedOrderDelivery1.getId());
        assertThat(subscribedOrderDelivery1).isEqualTo(subscribedOrderDelivery2);

        subscribedOrderDelivery2 = getSubscribedOrderDeliverySample2();
        assertThat(subscribedOrderDelivery1).isNotEqualTo(subscribedOrderDelivery2);
    }

    @Test
    void orderTest() throws Exception {
        SubscribedOrderDelivery subscribedOrderDelivery = getSubscribedOrderDeliveryRandomSampleGenerator();
        Orders ordersBack = getOrdersRandomSampleGenerator();

        subscribedOrderDelivery.setOrder(ordersBack);
        assertThat(subscribedOrderDelivery.getOrder()).isEqualTo(ordersBack);

        subscribedOrderDelivery.order(null);
        assertThat(subscribedOrderDelivery.getOrder()).isNull();
    }

    @Test
    void entryUserTest() throws Exception {
        SubscribedOrderDelivery subscribedOrderDelivery = getSubscribedOrderDeliveryRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        subscribedOrderDelivery.setEntryUser(usersBack);
        assertThat(subscribedOrderDelivery.getEntryUser()).isEqualTo(usersBack);

        subscribedOrderDelivery.entryUser(null);
        assertThat(subscribedOrderDelivery.getEntryUser()).isNull();
    }
}
