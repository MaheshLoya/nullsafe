package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BannerImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BannerImageDTO.class);
        BannerImageDTO bannerImageDTO1 = new BannerImageDTO();
        bannerImageDTO1.setId(1L);
        BannerImageDTO bannerImageDTO2 = new BannerImageDTO();
        assertThat(bannerImageDTO1).isNotEqualTo(bannerImageDTO2);
        bannerImageDTO2.setId(bannerImageDTO1.getId());
        assertThat(bannerImageDTO1).isEqualTo(bannerImageDTO2);
        bannerImageDTO2.setId(2L);
        assertThat(bannerImageDTO1).isNotEqualTo(bannerImageDTO2);
        bannerImageDTO1.setId(null);
        assertThat(bannerImageDTO1).isNotEqualTo(bannerImageDTO2);
    }
}
