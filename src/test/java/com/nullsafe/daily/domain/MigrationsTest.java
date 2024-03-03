package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.MigrationsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MigrationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Migrations.class);
        Migrations migrations1 = getMigrationsSample1();
        Migrations migrations2 = new Migrations();
        assertThat(migrations1).isNotEqualTo(migrations2);

        migrations2.setId(migrations1.getId());
        assertThat(migrations1).isEqualTo(migrations2);

        migrations2 = getMigrationsSample2();
        assertThat(migrations1).isNotEqualTo(migrations2);
    }
}
