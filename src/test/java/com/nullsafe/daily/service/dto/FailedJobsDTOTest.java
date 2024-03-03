package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FailedJobsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FailedJobsDTO.class);
        FailedJobsDTO failedJobsDTO1 = new FailedJobsDTO();
        failedJobsDTO1.setId(1L);
        FailedJobsDTO failedJobsDTO2 = new FailedJobsDTO();
        assertThat(failedJobsDTO1).isNotEqualTo(failedJobsDTO2);
        failedJobsDTO2.setId(failedJobsDTO1.getId());
        assertThat(failedJobsDTO1).isEqualTo(failedJobsDTO2);
        failedJobsDTO2.setId(2L);
        assertThat(failedJobsDTO1).isNotEqualTo(failedJobsDTO2);
        failedJobsDTO1.setId(null);
        assertThat(failedJobsDTO1).isNotEqualTo(failedJobsDTO2);
    }
}
