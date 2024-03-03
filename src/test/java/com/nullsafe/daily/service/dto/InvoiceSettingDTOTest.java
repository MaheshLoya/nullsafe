package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceSettingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceSettingDTO.class);
        InvoiceSettingDTO invoiceSettingDTO1 = new InvoiceSettingDTO();
        invoiceSettingDTO1.setId(1L);
        InvoiceSettingDTO invoiceSettingDTO2 = new InvoiceSettingDTO();
        assertThat(invoiceSettingDTO1).isNotEqualTo(invoiceSettingDTO2);
        invoiceSettingDTO2.setId(invoiceSettingDTO1.getId());
        assertThat(invoiceSettingDTO1).isEqualTo(invoiceSettingDTO2);
        invoiceSettingDTO2.setId(2L);
        assertThat(invoiceSettingDTO1).isNotEqualTo(invoiceSettingDTO2);
        invoiceSettingDTO1.setId(null);
        assertThat(invoiceSettingDTO1).isNotEqualTo(invoiceSettingDTO2);
    }
}
