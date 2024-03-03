package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.Cat;
import com.nullsafe.daily.domain.SubCat;
import com.nullsafe.daily.service.dto.CatDTO;
import com.nullsafe.daily.service.dto.SubCatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubCat} and its DTO {@link SubCatDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubCatMapper extends EntityMapper<SubCatDTO, SubCat> {
    @Mapping(target = "cat", source = "cat", qualifiedByName = "catId")
    SubCatDTO toDto(SubCat s);

    @Named("catId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CatDTO toDtoCatId(Cat cat);
}
