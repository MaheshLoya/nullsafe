package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.PersonalAccessTokens;
import com.nullsafe.daily.service.dto.PersonalAccessTokensDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PersonalAccessTokens} and its DTO {@link PersonalAccessTokensDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalAccessTokensMapper extends EntityMapper<PersonalAccessTokensDTO, PersonalAccessTokens> {}
