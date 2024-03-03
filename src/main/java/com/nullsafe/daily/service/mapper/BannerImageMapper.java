package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.BannerImage;
import com.nullsafe.daily.service.dto.BannerImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BannerImage} and its DTO {@link BannerImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface BannerImageMapper extends EntityMapper<BannerImageDTO, BannerImage> {}
