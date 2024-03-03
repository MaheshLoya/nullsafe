package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.SpecificNotification;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.service.dto.SpecificNotificationDTO;
import com.nullsafe.daily.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecificNotification} and its DTO {@link SpecificNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecificNotificationMapper extends EntityMapper<SpecificNotificationDTO, SpecificNotification> {
    @Mapping(target = "user", source = "user", qualifiedByName = "usersEmail")
    SpecificNotificationDTO toDto(SpecificNotification s);

    @Named("usersEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    UsersDTO toDtoUsersEmail(Users users);
}
