package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.SubscriptionRenewalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionRenewalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionRenewal.class);
        SubscriptionRenewal subscriptionRenewal1 = getSubscriptionRenewalSample1();
        SubscriptionRenewal subscriptionRenewal2 = new SubscriptionRenewal();
        assertThat(subscriptionRenewal1).isNotEqualTo(subscriptionRenewal2);

        subscriptionRenewal2.setId(subscriptionRenewal1.getId());
        assertThat(subscriptionRenewal1).isEqualTo(subscriptionRenewal2);

        subscriptionRenewal2 = getSubscriptionRenewalSample2();
        assertThat(subscriptionRenewal1).isNotEqualTo(subscriptionRenewal2);
    }
}
