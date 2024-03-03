package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.UserHolidayTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserHolidayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserHoliday.class);
        UserHoliday userHoliday1 = getUserHolidaySample1();
        UserHoliday userHoliday2 = new UserHoliday();
        assertThat(userHoliday1).isNotEqualTo(userHoliday2);

        userHoliday2.setId(userHoliday1.getId());
        assertThat(userHoliday1).isEqualTo(userHoliday2);

        userHoliday2 = getUserHolidaySample2();
        assertThat(userHoliday1).isNotEqualTo(userHoliday2);
    }
}
