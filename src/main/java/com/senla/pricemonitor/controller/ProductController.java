package com.senla.pricemonitor.controller;

import com.senla.pricemonitor.config.AdminOnly;
import com.senla.pricemonitor.dto.PageableResponse;
import com.senla.pricemonitor.dto.ProductDto;
import com.senla.pricemonitor.service.ProductService;
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
@RequestMapping("api/v1/products")
@Tag(name = "Product Controller", description = "Управление продуктами")
public class ProductController {
    private final ProductService productService;

    @AdminOnly
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать продукт", description = "Создаёт новый продукт. Только для администраторов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Продукт успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public ProductDto createProduct(@RequestBody
                                    @Validated
                                    ProductDto categoryDto) {
        return productService.create(categoryDto);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить продукт", description = "Возвращает информацию о продукте по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт найден"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    public ProductDto getProduct(@PathVariable Long id) {
        return productService.get(id);
    }

    @GetMapping
    @Operation(summary = "Список продуктов", description = "Возвращает список продуктов с возможностью фильтрации по категории и имени.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список продуктов успешно получен")
    })
    public PageableResponse<ProductDto> listProducts(@RequestParam(required = false) Long categoryId,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false, defaultValue = "0")
                                                     int page,
                                                     @RequestParam(required = false, defaultValue = "50")
                                                     int size) {
        page = Math.max(0, page);
        size = Math.min(100, Math.max(0, size));

        return productService.getAll(categoryId,name,page, size);
    }

    @AdminOnly
    @PutMapping("{id}")
    @Operation(summary = "Обновить продукт", description = "Обновляет данные продукта по его ID. Только для администраторов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Продукт успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    public ProductDto updateProduct(@PathVariable
                                    Long id,
                                    @RequestBody
                                    @Validated
                                    ProductDto categoryDto) {
        categoryDto.setId(id);
        return productService.update(categoryDto);
    }

    @AdminOnly
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить продукт", description = "Удаляет продукт по его ID. Только для администраторов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Продукт успешно удалён"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден")
    })
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }
}
