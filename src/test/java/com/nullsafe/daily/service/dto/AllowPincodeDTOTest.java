package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllowPincodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllowPincodeDTO.class);
        AllowPincodeDTO allowPincodeDTO1 = new AllowPincodeDTO();
        allowPincodeDTO1.setId(1L);
        AllowPincodeDTO allowPincodeDTO2 = new AllowPincodeDTO();
        assertThat(allowPincodeDTO1).isNotEqualTo(allowPincodeDTO2);
        allowPincodeDTO2.setId(allowPincodeDTO1.getId());
        assertThat(allowPincodeDTO1).isEqualTo(allowPincodeDTO2);
        allowPincodeDTO2.setId(2L);
        assertThat(allowPincodeDTO1).isNotEqualTo(allowPincodeDTO2);
        allowPincodeDTO1.setId(null);
        assertThat(allowPincodeDTO1).isNotEqualTo(allowPincodeDTO2);
    }
}
