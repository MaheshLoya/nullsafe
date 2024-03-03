package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefundsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RefundsDTO.class);
        RefundsDTO refundsDTO1 = new RefundsDTO();
        refundsDTO1.setId(1L);
        RefundsDTO refundsDTO2 = new RefundsDTO();
        assertThat(refundsDTO1).isNotEqualTo(refundsDTO2);
        refundsDTO2.setId(refundsDTO1.getId());
        assertThat(refundsDTO1).isEqualTo(refundsDTO2);
        refundsDTO2.setId(2L);
        assertThat(refundsDTO1).isNotEqualTo(refundsDTO2);
        refundsDTO1.setId(null);
        assertThat(refundsDTO1).isNotEqualTo(refundsDTO2);
    }
}
