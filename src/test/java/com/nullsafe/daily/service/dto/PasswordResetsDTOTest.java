package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasswordResetsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasswordResetsDTO.class);
        PasswordResetsDTO passwordResetsDTO1 = new PasswordResetsDTO();
        passwordResetsDTO1.setId(1L);
        PasswordResetsDTO passwordResetsDTO2 = new PasswordResetsDTO();
        assertThat(passwordResetsDTO1).isNotEqualTo(passwordResetsDTO2);
        passwordResetsDTO2.setId(passwordResetsDTO1.getId());
        assertThat(passwordResetsDTO1).isEqualTo(passwordResetsDTO2);
        passwordResetsDTO2.setId(2L);
        assertThat(passwordResetsDTO1).isNotEqualTo(passwordResetsDTO2);
        passwordResetsDTO1.setId(null);
        assertThat(passwordResetsDTO1).isNotEqualTo(passwordResetsDTO2);
    }
}
