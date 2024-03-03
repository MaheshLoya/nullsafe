package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.AppSettings;
import com.nullsafe.daily.service.dto.AppSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppSettings} and its DTO {@link AppSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppSettingsMapper extends EntityMapper<AppSettingsDTO, AppSettings> {}
