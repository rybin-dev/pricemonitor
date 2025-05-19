package com.senla.pricemonitor.mapper;

import com.senla.pricemonitor.dto.CategoryDto;
import com.senla.pricemonitor.entity.Category;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CategoryDto toDto(Category category);

    default Category map(Category category, CategoryDto dto) {
        category.setName(dto.getName());
        return category;
    }

    @InheritConfiguration
    Iterable<CategoryDto> toDto(Iterable<Category> categories);
}