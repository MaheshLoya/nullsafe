package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Users} and its DTO {@link UsersDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsersMapper extends EntityMapper<UsersDTO, Users> {}
