package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.SocialMedia;
import com.nullsafe.daily.service.dto.SocialMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialMedia} and its DTO {@link SocialMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialMediaMapper extends EntityMapper<SocialMediaDTO, SocialMedia> {}
