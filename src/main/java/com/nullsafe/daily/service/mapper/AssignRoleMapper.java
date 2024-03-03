package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.AssignRole;
import com.nullsafe.daily.domain.Role;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.service.dto.AssignRoleDTO;
import com.nullsafe.daily.service.dto.RoleDTO;
import com.nullsafe.daily.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssignRole} and its DTO {@link AssignRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssignRoleMapper extends EntityMapper<AssignRoleDTO, AssignRole> {
    @Mapping(target = "user", source = "user", qualifiedByName = "usersEmail")
    @Mapping(target = "role", source = "role", qualifiedByName = "roleId")
    AssignRoleDTO toDto(AssignRole s);

    @Named("usersEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    UsersDTO toDtoUsersEmail(Users users);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);
}
