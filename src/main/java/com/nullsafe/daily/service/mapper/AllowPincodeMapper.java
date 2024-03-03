package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.AllowPincode;
import com.nullsafe.daily.service.dto.AllowPincodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AllowPincode} and its DTO {@link AllowPincodeDTO}.
 */
@Mapper(componentModel = "spring")
public interface AllowPincodeMapper extends EntityMapper<AllowPincodeDTO, AllowPincode> {}
