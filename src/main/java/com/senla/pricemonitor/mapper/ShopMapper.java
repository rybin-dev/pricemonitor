package com.senla.pricemonitor.mapper;

import com.senla.pricemonitor.dto.ShopDto;
import com.senla.pricemonitor.entity.Shop;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "info", source = "info")
    ShopDto toDto(Shop shop);

    default Shop map(Shop shop, ShopDto dto){
        shop.setName(dto.getName());
        shop.setInfo(dto.getInfo());
        return shop;
    }

    @InheritConfiguration
    Iterable<ShopDto> toDto(Iterable<Shop> shops);
}