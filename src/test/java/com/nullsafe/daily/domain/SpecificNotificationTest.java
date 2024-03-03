package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.SpecificNotificationTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecificNotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecificNotification.class);
        SpecificNotification specificNotification1 = getSpecificNotificationSample1();
        SpecificNotification specificNotification2 = new SpecificNotification();
        assertThat(specificNotification1).isNotEqualTo(specificNotification2);

        specificNotification2.setId(specificNotification1.getId());
        assertThat(specificNotification1).isEqualTo(specificNotification2);

        specificNotification2 = getSpecificNotificationSample2();
        assertThat(specificNotification1).isNotEqualTo(specificNotification2);
    }

    @Test
    void userTest() throws Exception {
        SpecificNotification specificNotification = getSpecificNotificationRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        specificNotification.setUser(usersBack);
        assertThat(specificNotification.getUser()).isEqualTo(usersBack);

        specificNotification.user(null);
        assertThat(specificNotification.getUser()).isNull();
    }
}
