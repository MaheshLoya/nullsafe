package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.AssignRoleTestSamples.*;
import static com.nullsafe.daily.domain.RoleTestSamples.*;
import static com.nullsafe.daily.domain.UsersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssignRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignRole.class);
        AssignRole assignRole1 = getAssignRoleSample1();
        AssignRole assignRole2 = new AssignRole();
        assertThat(assignRole1).isNotEqualTo(assignRole2);

        assignRole2.setId(assignRole1.getId());
        assertThat(assignRole1).isEqualTo(assignRole2);

        assignRole2 = getAssignRoleSample2();
        assertThat(assignRole1).isNotEqualTo(assignRole2);
    }

    @Test
    void userTest() throws Exception {
        AssignRole assignRole = getAssignRoleRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        assignRole.setUser(usersBack);
        assertThat(assignRole.getUser()).isEqualTo(usersBack);

        assignRole.user(null);
        assertThat(assignRole.getUser()).isNull();
    }

    @Test
    void roleTest() throws Exception {
        AssignRole assignRole = getAssignRoleRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        assignRole.setRole(roleBack);
        assertThat(assignRole.getRole()).isEqualTo(roleBack);

        assignRole.role(null);
        assertThat(assignRole.getRole()).isNull();
    }
}
