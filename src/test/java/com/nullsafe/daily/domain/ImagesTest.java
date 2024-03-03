package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.ImagesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImagesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Images.class);
        Images images1 = getImagesSample1();
        Images images2 = new Images();
        assertThat(images1).isNotEqualTo(images2);

        images2.setId(images1.getId());
        assertThat(images1).isEqualTo(images2);

        images2 = getImagesSample2();
        assertThat(images1).isNotEqualTo(images2);
    }
}
