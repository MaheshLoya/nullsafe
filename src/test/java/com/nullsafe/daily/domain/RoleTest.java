package com.nullsafe.daily.domain;

import static com.nullsafe.daily.domain.AssignRoleTestSamples.*;
import static com.nullsafe.daily.domain.RoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nullsafe.daily.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
        Role role1 = getRoleSample1();
        Role role2 = new Role();
        assertThat(role1).isNotEqualTo(role2);

        role2.setId(role1.getId());
        assertThat(role1).isEqualTo(role2);

        role2 = getRoleSample2();
        assertThat(role1).isNotEqualTo(role2);
    }

    @Test
    void assignRoleTest() throws Exception {
        Role role = getRoleRandomSampleGenerator();
        AssignRole assignRoleBack = getAssignRoleRandomSampleGenerator();

        role.addAssignRole(assignRoleBack);
        assertThat(role.getAssignRoles()).containsOnly(assignRoleBack);
        assertThat(assignRoleBack.getRole()).isEqualTo(role);

        role.removeAssignRole(assignRoleBack);
        assertThat(role.getAssignRoles()).doesNotContain(assignRoleBack);
        assertThat(assignRoleBack.getRole()).isNull();

        role.assignRoles(new HashSet<>(Set.of(assignRoleBack)));
        assertThat(role.getAssignRoles()).containsOnly(assignRoleBack);
        assertThat(assignRoleBack.getRole()).isEqualTo(role);

        role.setAssignRoles(new HashSet<>());
        assertThat(role.getAssignRoles()).doesNotContain(assignRoleBack);
        assertThat(assignRoleBack.getRole()).isNull();
    }
}
