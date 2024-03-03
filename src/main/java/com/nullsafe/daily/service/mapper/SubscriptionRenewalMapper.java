package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.SubscriptionRenewal;
import com.nullsafe.daily.service.dto.SubscriptionRenewalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscriptionRenewal} and its DTO {@link SubscriptionRenewalDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionRenewalMapper extends EntityMapper<SubscriptionRenewalDTO, SubscriptionRenewal> {}
