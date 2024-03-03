package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebAppSettingsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebAppSettingsDTO.class);
        WebAppSettingsDTO webAppSettingsDTO1 = new WebAppSettingsDTO();
        webAppSettingsDTO1.setId(1L);
        WebAppSettingsDTO webAppSettingsDTO2 = new WebAppSettingsDTO();
        assertThat(webAppSettingsDTO1).isNotEqualTo(webAppSettingsDTO2);
        webAppSettingsDTO2.setId(webAppSettingsDTO1.getId());
        assertThat(webAppSettingsDTO1).isEqualTo(webAppSettingsDTO2);
        webAppSettingsDTO2.setId(2L);
        assertThat(webAppSettingsDTO1).isNotEqualTo(webAppSettingsDTO2);
        webAppSettingsDTO1.setId(null);
        assertThat(webAppSettingsDTO1).isNotEqualTo(webAppSettingsDTO2);
    }
}
