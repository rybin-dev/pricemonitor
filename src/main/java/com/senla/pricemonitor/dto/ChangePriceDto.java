package com.senla.pricemonitor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePriceDto {
    private Long productId;
    private Long shopId ;
    private Long newPrice;
}
