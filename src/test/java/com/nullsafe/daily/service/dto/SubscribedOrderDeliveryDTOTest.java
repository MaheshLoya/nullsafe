package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscribedOrderDeliveryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscribedOrderDeliveryDTO.class);
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO1 = new SubscribedOrderDeliveryDTO();
        subscribedOrderDeliveryDTO1.setId(1L);
        SubscribedOrderDeliveryDTO subscribedOrderDeliveryDTO2 = new SubscribedOrderDeliveryDTO();
        assertThat(subscribedOrderDeliveryDTO1).isNotEqualTo(subscribedOrderDeliveryDTO2);
        subscribedOrderDeliveryDTO2.setId(subscribedOrderDeliveryDTO1.getId());
        assertThat(subscribedOrderDeliveryDTO1).isEqualTo(subscribedOrderDeliveryDTO2);
        subscribedOrderDeliveryDTO2.setId(2L);
        assertThat(subscribedOrderDeliveryDTO1).isNotEqualTo(subscribedOrderDeliveryDTO2);
        subscribedOrderDeliveryDTO1.setId(null);
        assertThat(subscribedOrderDeliveryDTO1).isNotEqualTo(subscribedOrderDeliveryDTO2);
    }
}
