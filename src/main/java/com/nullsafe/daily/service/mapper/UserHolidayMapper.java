package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.UserHoliday;
import com.nullsafe.daily.service.dto.UserHolidayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserHoliday} and its DTO {@link UserHolidayDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserHolidayMapper extends EntityMapper<UserHolidayDTO, UserHoliday> {}
