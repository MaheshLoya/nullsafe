package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.WebAppSettingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebAppSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebAppSettings.class);
        WebAppSettings webAppSettings1 = getWebAppSettingsSample1();
        WebAppSettings webAppSettings2 = new WebAppSettings();
        assertThat(webAppSettings1).isNotEqualTo(webAppSettings2);

        webAppSettings2.setId(webAppSettings1.getId());
        assertThat(webAppSettings1).isEqualTo(webAppSettings2);

        webAppSettings2 = getWebAppSettingsSample2();
        assertThat(webAppSettings1).isNotEqualTo(webAppSettings2);
    }
}
