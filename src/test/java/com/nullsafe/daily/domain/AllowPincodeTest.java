package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.AllowPincodeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllowPincodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllowPincode.class);
        AllowPincode allowPincode1 = getAllowPincodeSample1();
        AllowPincode allowPincode2 = new AllowPincode();
        assertThat(allowPincode1).isNotEqualTo(allowPincode2);

        allowPincode2.setId(allowPincode1.getId());
        assertThat(allowPincode1).isEqualTo(allowPincode2);

        allowPincode2 = getAllowPincodeSample2();
        assertThat(allowPincode1).isNotEqualTo(allowPincode2);
    }
}
