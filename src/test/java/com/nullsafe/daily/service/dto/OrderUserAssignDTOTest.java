package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderUserAssignDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderUserAssignDTO.class);
        OrderUserAssignDTO orderUserAssignDTO1 = new OrderUserAssignDTO();
        orderUserAssignDTO1.setId(1L);
        OrderUserAssignDTO orderUserAssignDTO2 = new OrderUserAssignDTO();
        assertThat(orderUserAssignDTO1).isNotEqualTo(orderUserAssignDTO2);
        orderUserAssignDTO2.setId(orderUserAssignDTO1.getId());
        assertThat(orderUserAssignDTO1).isEqualTo(orderUserAssignDTO2);
        orderUserAssignDTO2.setId(2L);
        assertThat(orderUserAssignDTO1).isNotEqualTo(orderUserAssignDTO2);
        orderUserAssignDTO1.setId(null);
        assertThat(orderUserAssignDTO1).isNotEqualTo(orderUserAssignDTO2);
    }
}
