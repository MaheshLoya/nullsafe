package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.UserNotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserNotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserNotification.class);
        UserNotification userNotification1 = getUserNotificationSample1();
        UserNotification userNotification2 = new UserNotification();
        assertThat(userNotification1).isNotEqualTo(userNotification2);

        userNotification2.setId(userNotification1.getId());
        assertThat(userNotification1).isEqualTo(userNotification2);

        userNotification2 = getUserNotificationSample2();
        assertThat(userNotification1).isNotEqualTo(userNotification2);
    }
}
