package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalAccessTokensDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalAccessTokensDTO.class);
        PersonalAccessTokensDTO personalAccessTokensDTO1 = new PersonalAccessTokensDTO();
        personalAccessTokensDTO1.setId(1L);
        PersonalAccessTokensDTO personalAccessTokensDTO2 = new PersonalAccessTokensDTO();
        assertThat(personalAccessTokensDTO1).isNotEqualTo(personalAccessTokensDTO2);
        personalAccessTokensDTO2.setId(personalAccessTokensDTO1.getId());
        assertThat(personalAccessTokensDTO1).isEqualTo(personalAccessTokensDTO2);
        personalAccessTokensDTO2.setId(2L);
        assertThat(personalAccessTokensDTO1).isNotEqualTo(personalAccessTokensDTO2);
        personalAccessTokensDTO1.setId(null);
        assertThat(personalAccessTokensDTO1).isNotEqualTo(personalAccessTokensDTO2);
    }
}
