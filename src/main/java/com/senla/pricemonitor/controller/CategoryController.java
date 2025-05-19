package com.senla.pricemonitor.controller;

import com.senla.pricemonitor.config.AdminOnly;
import com.senla.pricemonitor.dto.CategoryDto;
import com.senla.pricemonitor.dto.PageableResponse;
import com.senla.pricemonitor.service.CategoryService;
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
@RequestMapping("api/v1/categories")
@Tag(name = "Category Controller", description = "Управление категориями продуктов")
public class CategoryController {
    private final CategoryService categoryService;

    @AdminOnly
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать категорию", description = "Создаёт новую категорию. Только для администраторов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Категория успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверный формат запроса"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public CategoryDto createCategory(@RequestBody
                                      @Validated
                                      CategoryDto categoryDto) {
        return categoryService.create(categoryDto);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить категорию по ID", description = "Возвращает категорию по её идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.get(id);
    }

    @GetMapping
    @Operation(summary = "Получить список категорий", description = "Возвращает постраничный список всех категорий.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список категорий получен")
    })
    public PageableResponse<CategoryDto> listCategories(@RequestParam(required = false, defaultValue = "0")
                                                        int page,
                                                        @RequestParam(required = false, defaultValue = "50")
                                                        int size) {
        page = Math.max(0, page);
        size = Math.min(100, Math.max(0, size));

        return categoryService.getAll(page, size);
    }

    @AdminOnly
    @PutMapping("{id}")
    @Operation(summary = "Обновить категорию", description = "Обновляет существующую категорию. Только для администраторов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Категория успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверный формат запроса"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public CategoryDto updateCategory(@PathVariable
                                      Long id,
                                      @RequestBody
                                      @Validated
                                      CategoryDto categoryDto) {
        categoryDto.setId(id);
        return categoryService.update(categoryDto);
    }

    @AdminOnly
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить категорию", description = "Удаляет категорию по идентификатору. Только для администраторов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Категория успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
