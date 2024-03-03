package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvailableDeliveryLocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvailableDeliveryLocationDTO.class);
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO1 = new AvailableDeliveryLocationDTO();
        availableDeliveryLocationDTO1.setId(1L);
        AvailableDeliveryLocationDTO availableDeliveryLocationDTO2 = new AvailableDeliveryLocationDTO();
        assertThat(availableDeliveryLocationDTO1).isNotEqualTo(availableDeliveryLocationDTO2);
        availableDeliveryLocationDTO2.setId(availableDeliveryLocationDTO1.getId());
        assertThat(availableDeliveryLocationDTO1).isEqualTo(availableDeliveryLocationDTO2);
        availableDeliveryLocationDTO2.setId(2L);
        assertThat(availableDeliveryLocationDTO1).isNotEqualTo(availableDeliveryLocationDTO2);
        availableDeliveryLocationDTO1.setId(null);
        assertThat(availableDeliveryLocationDTO1).isNotEqualTo(availableDeliveryLocationDTO2);
    }
}
