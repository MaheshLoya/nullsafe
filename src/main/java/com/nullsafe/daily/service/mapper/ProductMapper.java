package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.domain.SubCat;
import com.nullsafe.daily.service.dto.ProductDTO;
import com.nullsafe.daily.service.dto.SubCatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "subCat", source = "subCat", qualifiedByName = "subCatId")
    ProductDTO toDto(Product s);

    @Named("subCatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubCatDTO toDtoSubCatId(SubCat subCat);
}
