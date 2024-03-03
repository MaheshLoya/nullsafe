package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Cat;
import com.nullsafe.daily.service.dto.CatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cat} and its DTO {@link CatDTO}.
 */
@Mapper(componentModel = "spring")
public interface CatMapper extends EntityMapper<CatDTO, Cat> {}
