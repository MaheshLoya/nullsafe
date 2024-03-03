package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.OrderUserAssignTestSamples.*;
import static com.nullsafe.daily.domain.OrdersTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderUserAssignTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderUserAssign.class);
        OrderUserAssign orderUserAssign1 = getOrderUserAssignSample1();
        OrderUserAssign orderUserAssign2 = new OrderUserAssign();
        assertThat(orderUserAssign1).isNotEqualTo(orderUserAssign2);

        orderUserAssign2.setId(orderUserAssign1.getId());
        assertThat(orderUserAssign1).isEqualTo(orderUserAssign2);

        orderUserAssign2 = getOrderUserAssignSample2();
        assertThat(orderUserAssign1).isNotEqualTo(orderUserAssign2);
    }

    @Test
    void orderTest() throws Exception {
        OrderUserAssign orderUserAssign = getOrderUserAssignRandomSampleGenerator();
        Orders ordersBack = getOrdersRandomSampleGenerator();

        orderUserAssign.setOrder(ordersBack);
        assertThat(orderUserAssign.getOrder()).isEqualTo(ordersBack);

        orderUserAssign.order(null);
        assertThat(orderUserAssign.getOrder()).isNull();
    }

    @Test
    void userTest() throws Exception {
        OrderUserAssign orderUserAssign = getOrderUserAssignRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        orderUserAssign.setUser(usersBack);
        assertThat(orderUserAssign.getUser()).isEqualTo(usersBack);

        orderUserAssign.user(null);
        assertThat(orderUserAssign.getUser()).isNull();
    }
}
