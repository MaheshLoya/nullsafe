package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.InvoiceSetting;
import com.nullsafe.daily.service.dto.InvoiceSettingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InvoiceSetting} and its DTO {@link InvoiceSettingDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceSettingMapper extends EntityMapper<InvoiceSettingDTO, InvoiceSetting> {}
