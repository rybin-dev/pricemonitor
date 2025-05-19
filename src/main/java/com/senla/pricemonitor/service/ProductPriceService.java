package com.senla.pricemonitor.service;

import com.senla.pricemonitor.dto.ChangePriceDto;
import com.senla.pricemonitor.dto.PageableResponse;
import com.senla.pricemonitor.dto.PriceAtDate;
import com.senla.pricemonitor.dto.ShopPriceDto;
import com.senla.pricemonitor.entity.ProductPrice;
import com.senla.pricemonitor.repository.ProductPriceRepository;
import com.senla.pricemonitor.repository.ProductRepository;
import com.senla.pricemonitor.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductPriceService {

    private final ProductPriceRepository productPriceRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    public PageableResponse<ShopPriceDto> getShopPrices(Long productId, int page, int size) {
        var prices = productPriceRepository.findCurrentPrice(productId, page, size);
        return PageableResponse.of(prices);
    }

    public PageableResponse<PriceAtDate> getStats(Long productId, LocalDate startDate, LocalDate endDate) {
        var prices = productPriceRepository.calculatePriceStatsFor(productId, startDate, endDate);
        return PageableResponse.of(prices);
    }

    @Transactional
    public void changePrice(ChangePriceDto dto) {
        var product = productRepository.getReferenceById(dto.getProductId());
        var shop = shopRepository.getReferenceById(dto.getShopId());

        productPriceRepository.findCurrentPrice(dto.getProductId(), dto.getShopId())
                .ifPresent(oldPrice -> {
                    oldPrice.setEndDate(LocalDate.now());
                    productPriceRepository.save(oldPrice);
                });

        var newPrice = ProductPrice.builder()
                .product(product)
                .shop(shop)
                .price(dto.getNewPrice())
                .startDate(LocalDate.now())
                .build();

        productPriceRepository.save(newPrice);
    }
}
