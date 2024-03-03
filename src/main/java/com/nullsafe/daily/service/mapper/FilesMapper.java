package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Files;
import com.nullsafe.daily.service.dto.FilesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Files} and its DTO {@link FilesDTO}.
 */
@Mapper(componentModel = "spring")
public interface FilesMapper extends EntityMapper<FilesDTO, Files> {}
