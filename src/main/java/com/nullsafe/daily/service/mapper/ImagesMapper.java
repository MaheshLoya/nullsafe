package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Images;
import com.nullsafe.daily.service.dto.ImagesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Images} and its DTO {@link ImagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImagesMapper extends EntityMapper<ImagesDTO, Images> {}
