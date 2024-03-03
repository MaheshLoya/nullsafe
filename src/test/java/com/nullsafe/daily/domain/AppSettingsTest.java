package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.AppSettingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppSettings.class);
        AppSettings appSettings1 = getAppSettingsSample1();
        AppSettings appSettings2 = new AppSettings();
        assertThat(appSettings1).isNotEqualTo(appSettings2);

        appSettings2.setId(appSettings1.getId());
        assertThat(appSettings1).isEqualTo(appSettings2);

        appSettings2 = getAppSettingsSample2();
        assertThat(appSettings1).isNotEqualTo(appSettings2);
    }
}
