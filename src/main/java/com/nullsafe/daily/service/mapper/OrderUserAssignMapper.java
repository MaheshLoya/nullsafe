package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.OrderUserAssign;
import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.service.dto.OrderUserAssignDTO;
import com.nullsafe.daily.service.dto.OrdersDTO;
import com.nullsafe.daily.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderUserAssign} and its DTO {@link OrderUserAssignDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderUserAssignMapper extends EntityMapper<OrderUserAssignDTO, OrderUserAssign> {
    @Mapping(target = "order", source = "order", qualifiedByName = "ordersId")
    @Mapping(target = "user", source = "user", qualifiedByName = "usersEmail")
    OrderUserAssignDTO toDto(OrderUserAssign s);

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
