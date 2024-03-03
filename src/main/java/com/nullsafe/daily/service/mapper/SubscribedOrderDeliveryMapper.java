package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.SubscribedOrderDelivery;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.service.dto.OrdersDTO;
import com.nullsafe.daily.service.dto.SubscribedOrderDeliveryDTO;
import com.nullsafe.daily.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscribedOrderDelivery} and its DTO {@link SubscribedOrderDeliveryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscribedOrderDeliveryMapper extends EntityMapper<SubscribedOrderDeliveryDTO, SubscribedOrderDelivery> {
    @Mapping(target = "order", source = "order", qualifiedByName = "ordersId")
    @Mapping(target = "entryUser", source = "entryUser", qualifiedByName = "usersEmail")
    SubscribedOrderDeliveryDTO toDto(SubscribedOrderDelivery s);

    @Named("ordersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrdersDTO toDtoOrdersId(Orders orders);

    @Named("usersEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    UsersDTO toDtoUsersEmail(Users users);
}
