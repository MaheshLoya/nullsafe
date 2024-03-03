package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Role;
import com.nullsafe.daily.service.dto.RoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {}
