package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Refunds;
import com.nullsafe.daily.service.dto.RefundsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Refunds} and its DTO {@link RefundsDTO}.
 */
@Mapper(componentModel = "spring")
public interface RefundsMapper extends EntityMapper<RefundsDTO, Refunds> {}
