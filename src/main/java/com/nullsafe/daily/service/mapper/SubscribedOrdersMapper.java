package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.domain.SubscribedOrders;
import com.nullsafe.daily.domain.Transactions;
import com.nullsafe.daily.domain.UserAddress;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.service.dto.ProductDTO;
import com.nullsafe.daily.service.dto.SubscribedOrdersDTO;
import com.nullsafe.daily.service.dto.TransactionsDTO;
import com.nullsafe.daily.service.dto.UserAddressDTO;
import com.nullsafe.daily.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubscribedOrders} and its DTO {@link SubscribedOrdersDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscribedOrdersMapper extends EntityMapper<SubscribedOrdersDTO, SubscribedOrders> {
    @Mapping(target = "user", source = "user", qualifiedByName = "usersEmail")
    @Mapping(target = "transaction", source = "transaction", qualifiedByName = "transactionsId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "address", source = "address", qualifiedByName = "userAddressId")
    SubscribedOrdersDTO toDto(SubscribedOrders s);

    @Named("usersEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    UsersDTO toDtoUsersEmail(Users users);

    @Named("transactionsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransactionsDTO toDtoTransactionsId(Transactions transactions);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("userAddressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserAddressDTO toDtoUserAddressId(UserAddress userAddress);
}
