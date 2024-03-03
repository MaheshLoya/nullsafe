package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Migrations;
import com.nullsafe.daily.service.dto.MigrationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Migrations} and its DTO {@link MigrationsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MigrationsMapper extends EntityMapper<MigrationsDTO, Migrations> {}
