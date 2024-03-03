package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilesDTO.class);
        FilesDTO filesDTO1 = new FilesDTO();
        filesDTO1.setId(1L);
        FilesDTO filesDTO2 = new FilesDTO();
        assertThat(filesDTO1).isNotEqualTo(filesDTO2);
        filesDTO2.setId(filesDTO1.getId());
        assertThat(filesDTO1).isEqualTo(filesDTO2);
        filesDTO2.setId(2L);
        assertThat(filesDTO1).isNotEqualTo(filesDTO2);
        filesDTO1.setId(null);
        assertThat(filesDTO1).isNotEqualTo(filesDTO2);
    }
}
