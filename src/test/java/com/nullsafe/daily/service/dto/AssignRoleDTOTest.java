package com.nullsafe.daily.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssignRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignRoleDTO.class);
        AssignRoleDTO assignRoleDTO1 = new AssignRoleDTO();
        assignRoleDTO1.setId(1L);
        AssignRoleDTO assignRoleDTO2 = new AssignRoleDTO();
        assertThat(assignRoleDTO1).isNotEqualTo(assignRoleDTO2);
        assignRoleDTO2.setId(assignRoleDTO1.getId());
        assertThat(assignRoleDTO1).isEqualTo(assignRoleDTO2);
        assignRoleDTO2.setId(2L);
        assertThat(assignRoleDTO1).isNotEqualTo(assignRoleDTO2);
        assignRoleDTO1.setId(null);
        assertThat(assignRoleDTO1).isNotEqualTo(assignRoleDTO2);
    }
}
