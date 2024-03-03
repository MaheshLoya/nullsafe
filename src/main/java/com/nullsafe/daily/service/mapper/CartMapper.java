package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Cart;
import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.service.dto.CartDTO;
import com.nullsafe.daily.service.dto.ProductDTO;
import com.nullsafe.daily.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "user", source = "user", qualifiedByName = "usersEmail")
    CartDTO toDto(Cart s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("usersEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    UsersDTO toDtoUsersEmail(Users users);
}
