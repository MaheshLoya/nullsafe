package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.PersonalAccessTokensTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalAccessTokensTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalAccessTokens.class);
        PersonalAccessTokens personalAccessTokens1 = getPersonalAccessTokensSample1();
        PersonalAccessTokens personalAccessTokens2 = new PersonalAccessTokens();
        assertThat(personalAccessTokens1).isNotEqualTo(personalAccessTokens2);

        personalAccessTokens2.setId(personalAccessTokens1.getId());
        assertThat(personalAccessTokens1).isEqualTo(personalAccessTokens2);

        personalAccessTokens2 = getPersonalAccessTokensSample2();
        assertThat(personalAccessTokens1).isNotEqualTo(personalAccessTokens2);
    }
}
