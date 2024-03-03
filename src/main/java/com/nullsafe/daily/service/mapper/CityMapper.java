package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.City;
import com.nullsafe.daily.service.dto.CityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {}
