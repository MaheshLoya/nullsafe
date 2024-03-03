package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.BannerImageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BannerImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BannerImage.class);
        BannerImage bannerImage1 = getBannerImageSample1();
        BannerImage bannerImage2 = new BannerImage();
        assertThat(bannerImage1).isNotEqualTo(bannerImage2);

        bannerImage2.setId(bannerImage1.getId());
        assertThat(bannerImage1).isEqualTo(bannerImage2);

        bannerImage2 = getBannerImageSample2();
        assertThat(bannerImage1).isNotEqualTo(bannerImage2);
    }
}
