package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionRenewalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionRenewalDTO.class);
        SubscriptionRenewalDTO subscriptionRenewalDTO1 = new SubscriptionRenewalDTO();
        subscriptionRenewalDTO1.setId(1L);
        SubscriptionRenewalDTO subscriptionRenewalDTO2 = new SubscriptionRenewalDTO();
        assertThat(subscriptionRenewalDTO1).isNotEqualTo(subscriptionRenewalDTO2);
        subscriptionRenewalDTO2.setId(subscriptionRenewalDTO1.getId());
        assertThat(subscriptionRenewalDTO1).isEqualTo(subscriptionRenewalDTO2);
        subscriptionRenewalDTO2.setId(2L);
        assertThat(subscriptionRenewalDTO1).isNotEqualTo(subscriptionRenewalDTO2);
        subscriptionRenewalDTO1.setId(null);
        assertThat(subscriptionRenewalDTO1).isNotEqualTo(subscriptionRenewalDTO2);
    }
}
