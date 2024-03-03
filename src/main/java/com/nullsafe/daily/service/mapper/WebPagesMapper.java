package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.WebPages;
import com.nullsafe.daily.service.dto.WebPagesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WebPages} and its DTO {@link WebPagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface WebPagesMapper extends EntityMapper<WebPagesDTO, WebPages> {}
