package com.senla.pricemonitor.dto;

public record ShopPriceDto(
    Long shopId,
    String shopName,
    Long productPrice
) {
}