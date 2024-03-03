package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebPagesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebPagesDTO.class);
        WebPagesDTO webPagesDTO1 = new WebPagesDTO();
        webPagesDTO1.setId(1L);
        WebPagesDTO webPagesDTO2 = new WebPagesDTO();
        assertThat(webPagesDTO1).isNotEqualTo(webPagesDTO2);
        webPagesDTO2.setId(webPagesDTO1.getId());
        assertThat(webPagesDTO1).isEqualTo(webPagesDTO2);
        webPagesDTO2.setId(2L);
        assertThat(webPagesDTO1).isNotEqualTo(webPagesDTO2);
        webPagesDTO1.setId(null);
        assertThat(webPagesDTO1).isNotEqualTo(webPagesDTO2);
    }
}
