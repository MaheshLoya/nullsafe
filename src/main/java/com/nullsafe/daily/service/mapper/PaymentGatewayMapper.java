package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.PaymentGateway;
import com.nullsafe.daily.service.dto.PaymentGatewayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentGateway} and its DTO {@link PaymentGatewayDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentGatewayMapper extends EntityMapper<PaymentGatewayDTO, PaymentGateway> {}
