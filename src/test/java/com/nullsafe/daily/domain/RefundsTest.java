package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.RefundsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefundsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Refunds.class);
        Refunds refunds1 = getRefundsSample1();
        Refunds refunds2 = new Refunds();
        assertThat(refunds1).isNotEqualTo(refunds2);

        refunds2.setId(refunds1.getId());
        assertThat(refunds1).isEqualTo(refunds2);

        refunds2 = getRefundsSample2();
        assertThat(refunds1).isNotEqualTo(refunds2);
    }
}
