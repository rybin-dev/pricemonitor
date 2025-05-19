package com.senla.pricemonitor.controller;

import com.senla.pricemonitor.config.AdminOnly;
import com.senla.pricemonitor.dto.ChangePriceDto;
import com.senla.pricemonitor.dto.PageableResponse;
import com.senla.pricemonitor.dto.PriceAtDate;
import com.senla.pricemonitor.dto.ShopPriceDto;
import com.senla.pricemonitor.service.ProductPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "Product Price Controller", description = "Управление ценами продуктов и статистикой")
public class ProductPriceController {
    private final ProductPriceService productPriceService;

    @GetMapping("products/{id}/shop-prices")
    @Operation(summary = "Список цен по магазинам", description = "Возвращает текущие цены на продукт в разных магазинах.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Цены успешно получены"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    public PageableResponse<ShopPriceDto> listShopPrices(@PathVariable Long id,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "50") int size) {
        page = Math.max(0, page);
        size = Math.min(100, Math.max(0, size));

        return productPriceService.getShopPrices(id, page, size);
    }

    @GetMapping("products/{id}/stats")
    @Operation(summary = "Статистика цен", description = "Возвращает историю изменения цены на продукт за указанный период.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    public PageableResponse<PriceAtDate> getStats(@PathVariable Long id,
                                                  @RequestParam() LocalDate startDate,
                                                  @RequestParam() LocalDate endDate) {
        return productPriceService.getStats(id, startDate, endDate);
    }

    @AdminOnly
    @PostMapping("products/{id}/change-price")
    @Operation(summary = "Изменить цену", description = "Изменяет цену на продукт. Только для администраторов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Цена успешно изменена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Продукт или магазин не найдены")
    })
    public void changePrice(@PathVariable Long id,
                            @RequestBody ChangePriceDto changePriceDto) {
        changePriceDto.setProductId(id);

        productPriceService.changePrice(changePriceDto);
    }
}
