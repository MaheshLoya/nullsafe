package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Orders;
import com.nullsafe.daily.domain.Transactions;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.service.dto.OrdersDTO;
import com.nullsafe.daily.service.dto.TransactionsDTO;
import com.nullsafe.daily.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transactions} and its DTO {@link TransactionsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionsMapper extends EntityMapper<TransactionsDTO, Transactions> {
    @Mapping(target = "order", source = "order", qualifiedByName = "ordersId")
    @Mapping(target = "user", source = "user", qualifiedByName = "usersEmail")
    TransactionsDTO toDto(Transactions s);

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
