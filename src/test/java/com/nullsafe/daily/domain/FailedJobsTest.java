package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.FailedJobsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FailedJobsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FailedJobs.class);
        FailedJobs failedJobs1 = getFailedJobsSample1();
        FailedJobs failedJobs2 = new FailedJobs();
        assertThat(failedJobs1).isNotEqualTo(failedJobs2);

        failedJobs2.setId(failedJobs1.getId());
        assertThat(failedJobs1).isEqualTo(failedJobs2);

        failedJobs2 = getFailedJobsSample2();
        assertThat(failedJobs1).isNotEqualTo(failedJobs2);
    }
}
