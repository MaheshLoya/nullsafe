package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MigrationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MigrationsDTO.class);
        MigrationsDTO migrationsDTO1 = new MigrationsDTO();
        migrationsDTO1.setId(1L);
        MigrationsDTO migrationsDTO2 = new MigrationsDTO();
        assertThat(migrationsDTO1).isNotEqualTo(migrationsDTO2);
        migrationsDTO2.setId(migrationsDTO1.getId());
        assertThat(migrationsDTO1).isEqualTo(migrationsDTO2);
        migrationsDTO2.setId(2L);
        assertThat(migrationsDTO1).isNotEqualTo(migrationsDTO2);
        migrationsDTO1.setId(null);
        assertThat(migrationsDTO1).isNotEqualTo(migrationsDTO2);
    }
}
