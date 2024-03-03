package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscribedOrdersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscribedOrdersDTO.class);
        SubscribedOrdersDTO subscribedOrdersDTO1 = new SubscribedOrdersDTO();
        subscribedOrdersDTO1.setId(1L);
        SubscribedOrdersDTO subscribedOrdersDTO2 = new SubscribedOrdersDTO();
        assertThat(subscribedOrdersDTO1).isNotEqualTo(subscribedOrdersDTO2);
        subscribedOrdersDTO2.setId(subscribedOrdersDTO1.getId());
        assertThat(subscribedOrdersDTO1).isEqualTo(subscribedOrdersDTO2);
        subscribedOrdersDTO2.setId(2L);
        assertThat(subscribedOrdersDTO1).isNotEqualTo(subscribedOrdersDTO2);
        subscribedOrdersDTO1.setId(null);
        assertThat(subscribedOrdersDTO1).isNotEqualTo(subscribedOrdersDTO2);
    }
}
