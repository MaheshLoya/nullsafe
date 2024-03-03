package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatDTO.class);
        CatDTO catDTO1 = new CatDTO();
        catDTO1.setId(1L);
        CatDTO catDTO2 = new CatDTO();
        assertThat(catDTO1).isNotEqualTo(catDTO2);
        catDTO2.setId(catDTO1.getId());
        assertThat(catDTO1).isEqualTo(catDTO2);
        catDTO2.setId(2L);
        assertThat(catDTO1).isNotEqualTo(catDTO2);
        catDTO1.setId(null);
        assertThat(catDTO1).isNotEqualTo(catDTO2);
    }
}
