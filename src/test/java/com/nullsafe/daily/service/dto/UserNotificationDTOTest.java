package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserNotificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserNotificationDTO.class);
        UserNotificationDTO userNotificationDTO1 = new UserNotificationDTO();
        userNotificationDTO1.setId(1L);
        UserNotificationDTO userNotificationDTO2 = new UserNotificationDTO();
        assertThat(userNotificationDTO1).isNotEqualTo(userNotificationDTO2);
        userNotificationDTO2.setId(userNotificationDTO1.getId());
        assertThat(userNotificationDTO1).isEqualTo(userNotificationDTO2);
        userNotificationDTO2.setId(2L);
        assertThat(userNotificationDTO1).isNotEqualTo(userNotificationDTO2);
        userNotificationDTO1.setId(null);
        assertThat(userNotificationDTO1).isNotEqualTo(userNotificationDTO2);
    }
}
