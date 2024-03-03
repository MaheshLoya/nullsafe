package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.PasswordResetsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PasswordResetsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PasswordResets.class);
        PasswordResets passwordResets1 = getPasswordResetsSample1();
        PasswordResets passwordResets2 = new PasswordResets();
        assertThat(passwordResets1).isNotEqualTo(passwordResets2);

        passwordResets2.setId(passwordResets1.getId());
        assertThat(passwordResets1).isEqualTo(passwordResets2);

        passwordResets2 = getPasswordResetsSample2();
        assertThat(passwordResets1).isNotEqualTo(passwordResets2);
    }
}
