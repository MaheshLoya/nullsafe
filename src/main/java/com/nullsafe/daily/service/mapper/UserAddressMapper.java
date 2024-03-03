package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.UserAddress;
import com.nullsafe.daily.service.dto.UserAddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAddress} and its DTO {@link UserAddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAddressMapper extends EntityMapper<UserAddressDTO, UserAddress> {}
