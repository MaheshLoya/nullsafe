package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.AvailableDeliveryLocation;
import com.nullsafe.daily.service.dto.AvailableDeliveryLocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AvailableDeliveryLocation} and its DTO {@link AvailableDeliveryLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AvailableDeliveryLocationMapper extends EntityMapper<AvailableDeliveryLocationDTO, AvailableDeliveryLocation> {}
