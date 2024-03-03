package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserHolidayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserHolidayDTO.class);
        UserHolidayDTO userHolidayDTO1 = new UserHolidayDTO();
        userHolidayDTO1.setId(1L);
        UserHolidayDTO userHolidayDTO2 = new UserHolidayDTO();
        assertThat(userHolidayDTO1).isNotEqualTo(userHolidayDTO2);
        userHolidayDTO2.setId(userHolidayDTO1.getId());
        assertThat(userHolidayDTO1).isEqualTo(userHolidayDTO2);
        userHolidayDTO2.setId(2L);
        assertThat(userHolidayDTO1).isNotEqualTo(userHolidayDTO2);
        userHolidayDTO1.setId(null);
        assertThat(userHolidayDTO1).isNotEqualTo(userHolidayDTO2);
    }
}
