package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialMediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialMediaDTO.class);
        SocialMediaDTO socialMediaDTO1 = new SocialMediaDTO();
        socialMediaDTO1.setId(1L);
        SocialMediaDTO socialMediaDTO2 = new SocialMediaDTO();
        assertThat(socialMediaDTO1).isNotEqualTo(socialMediaDTO2);
        socialMediaDTO2.setId(socialMediaDTO1.getId());
        assertThat(socialMediaDTO1).isEqualTo(socialMediaDTO2);
        socialMediaDTO2.setId(2L);
        assertThat(socialMediaDTO1).isNotEqualTo(socialMediaDTO2);
        socialMediaDTO1.setId(null);
        assertThat(socialMediaDTO1).isNotEqualTo(socialMediaDTO2);
    }
}
