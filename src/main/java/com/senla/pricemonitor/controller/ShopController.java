package com.senla.pricemonitor.controller;

import com.senla.pricemonitor.config.AdminOnly;
import com.senla.pricemonitor.dto.PageableResponse;
import com.senla.pricemonitor.dto.ShopDto;
import com.senla.pricemonitor.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/shops")
@Tag(name = "Shop Controller", description = "Управление магазинами")
public class ShopController {
    private final ShopService shopService;

    @AdminOnly
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать магазин", description = "Создаёт новый магазин. Доступно только администраторам.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Магазин успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public ShopDto createShop(@RequestBody
                              @Validated
                              ShopDto shopDto) {
        return shopService.create(shopDto);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить магазин по ID", description = "Возвращает магазин по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Магазин найден"),
            @ApiResponse(responseCode = "404", description = "Магазин не найден")
    })
    public ShopDto getShop(@PathVariable Long id) {
        return shopService.get(id);
    }

    @GetMapping
    @Operation(summary = "Получить список магазинов", description = "Возвращает постраничный список всех магазинов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список магазинов получен")
    })
    public PageableResponse<ShopDto> listShops(@RequestParam(required = false, defaultValue = "0")
                                               int page,
                                               @RequestParam(required = false, defaultValue = "50")
                                               int size) {
        page = Math.max(0, page);
        size = Math.min(100, Math.max(0, size));

        return shopService.getAll(page, size);
    }

    @AdminOnly
    @PutMapping("{id}")
    @Operation(summary = "Обновить магазин", description = "Обновляет информацию о магазине. Доступно только администраторам.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Магазин успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "404", description = "Магазин не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public ShopDto updateShop(@PathVariable
                              Long id,
                              @RequestBody
                              @Validated
                              ShopDto shopDto) {
        shopDto.setId(id);
        return shopService.update(shopDto);
    }

    @AdminOnly
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить магазин", description = "Удаляет магазин по указанному идентификатору. Только для администраторов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Магазин успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Магазин не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public void deleteShop(@PathVariable Long id) {
        shopService.delete(id);
    }
}
