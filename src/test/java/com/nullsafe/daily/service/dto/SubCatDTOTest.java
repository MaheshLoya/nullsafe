package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubCatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCatDTO.class);
        SubCatDTO subCatDTO1 = new SubCatDTO();
        subCatDTO1.setId(1L);
        SubCatDTO subCatDTO2 = new SubCatDTO();
        assertThat(subCatDTO1).isNotEqualTo(subCatDTO2);
        subCatDTO2.setId(subCatDTO1.getId());
        assertThat(subCatDTO1).isEqualTo(subCatDTO2);
        subCatDTO2.setId(2L);
        assertThat(subCatDTO1).isNotEqualTo(subCatDTO2);
        subCatDTO1.setId(null);
        assertThat(subCatDTO1).isNotEqualTo(subCatDTO2);
    }
}
