package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.InvoiceSettingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceSettingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceSetting.class);
        InvoiceSetting invoiceSetting1 = getInvoiceSettingSample1();
        InvoiceSetting invoiceSetting2 = new InvoiceSetting();
        assertThat(invoiceSetting1).isNotEqualTo(invoiceSetting2);

        invoiceSetting2.setId(invoiceSetting1.getId());
        assertThat(invoiceSetting1).isEqualTo(invoiceSetting2);

        invoiceSetting2 = getInvoiceSettingSample2();
        assertThat(invoiceSetting1).isNotEqualTo(invoiceSetting2);
    }
}
