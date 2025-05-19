package com.senla.pricemonitor.repository;

import com.senla.pricemonitor.dto.PriceAtDate;
import com.senla.pricemonitor.dto.ShopPriceDto;
import com.senla.pricemonitor.entity.ProductPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductPriceRepository extends CrudRepository<ProductPrice, Long> {

    Optional<ProductPrice> findCurrentPrice(Long productId, Long shopId);

    List<ShopPriceDto> findCurrentPrice(Long productId, int page, int size);

    List<PriceAtDate> calculatePriceStatsFor(Long productId, LocalDate startDate, LocalDate endDate);

}
