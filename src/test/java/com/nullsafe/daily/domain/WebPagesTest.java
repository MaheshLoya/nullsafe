package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.WebPagesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WebPagesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebPages.class);
        WebPages webPages1 = getWebPagesSample1();
        WebPages webPages2 = new WebPages();
        assertThat(webPages1).isNotEqualTo(webPages2);

        webPages2.setId(webPages1.getId());
        assertThat(webPages1).isEqualTo(webPages2);

        webPages2 = getWebPagesSample2();
        assertThat(webPages1).isNotEqualTo(webPages2);
    }
}
