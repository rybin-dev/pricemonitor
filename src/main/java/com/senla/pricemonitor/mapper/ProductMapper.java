package com.senla.pricemonitor.mapper;

import com.senla.pricemonitor.dto.ProductDto;
import com.senla.pricemonitor.entity.Product;
import com.senla.pricemonitor.repository.CategoryRepository;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    @Autowired
    protected  CategoryRepository repository;

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "info", source = "info")
    @Mapping(target = "categoryIds", expression = "java(this.toCategoryIds(product))")
    public abstract ProductDto toDto(Product product);

    public Product map(Product product, ProductDto dto) {
        product.setName(dto.getName());
        product.setInfo(dto.getInfo());

        product.getProductCategories().clear();
        dto.getCategoryIds().stream()
                .map(repository::getReferenceById)
                .forEach(product::addCategory);

        return product;
    }

    @InheritConfiguration
    public abstract Iterable<ProductDto> toDto(Iterable<Product> products);

    protected List<Long> toCategoryIds(Product product) {
        return product.getProductCategories().stream()
                .map(pc -> pc.getCategory().getId())
                .toList();
    }
}