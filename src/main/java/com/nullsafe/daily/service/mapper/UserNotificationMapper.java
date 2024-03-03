package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.UserNotification;
import com.nullsafe.daily.service.dto.UserNotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserNotification} and its DTO {@link UserNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserNotificationMapper extends EntityMapper<UserNotificationDTO, UserNotification> {}
