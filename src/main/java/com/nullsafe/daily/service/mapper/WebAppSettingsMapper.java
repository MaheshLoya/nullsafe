package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.WebAppSettings;
import com.nullsafe.daily.service.dto.WebAppSettingsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WebAppSettings} and its DTO {@link WebAppSettingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface WebAppSettingsMapper extends EntityMapper<WebAppSettingsDTO, WebAppSettings> {}
