package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.PasswordResets;
import com.nullsafe.daily.service.dto.PasswordResetsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PasswordResets} and its DTO {@link PasswordResetsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PasswordResetsMapper extends EntityMapper<PasswordResetsDTO, PasswordResets> {}
