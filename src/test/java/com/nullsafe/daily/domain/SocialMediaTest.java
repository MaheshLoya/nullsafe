package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.SocialMediaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialMedia.class);
        SocialMedia socialMedia1 = getSocialMediaSample1();
        SocialMedia socialMedia2 = new SocialMedia();
        assertThat(socialMedia1).isNotEqualTo(socialMedia2);

        socialMedia2.setId(socialMedia1.getId());
        assertThat(socialMedia1).isEqualTo(socialMedia2);

        socialMedia2 = getSocialMediaSample2();
        assertThat(socialMedia1).isNotEqualTo(socialMedia2);
    }
}
