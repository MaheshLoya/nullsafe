package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecificNotificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecificNotificationDTO.class);
        SpecificNotificationDTO specificNotificationDTO1 = new SpecificNotificationDTO();
        specificNotificationDTO1.setId(1L);
        SpecificNotificationDTO specificNotificationDTO2 = new SpecificNotificationDTO();
        assertThat(specificNotificationDTO1).isNotEqualTo(specificNotificationDTO2);
        specificNotificationDTO2.setId(specificNotificationDTO1.getId());
        assertThat(specificNotificationDTO1).isEqualTo(specificNotificationDTO2);
        specificNotificationDTO2.setId(2L);
        assertThat(specificNotificationDTO1).isNotEqualTo(specificNotificationDTO2);
        specificNotificationDTO1.setId(null);
        assertThat(specificNotificationDTO1).isNotEqualTo(specificNotificationDTO2);
    }
}
