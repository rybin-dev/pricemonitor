package com.senla.pricemonitor.service;

import com.senla.pricemonitor.dto.CategoryDto;
import com.senla.pricemonitor.dto.PageableResponse;
import com.senla.pricemonitor.entity.Category;
import com.senla.pricemonitor.exception.NotFoundException;
import com.senla.pricemonitor.mapper.CategoryMapper;
import com.senla.pricemonitor.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto create(CategoryDto dto) {
        var category = new Category();
        categoryMapper.map(category, dto);

        category = categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    public CategoryDto get(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Category not found for id " + categoryId));
    }

    public PageableResponse<CategoryDto> getAll(int page, int size) {
        var categories = categoryRepository.findAll(page, size);

        return PageableResponse.of(categoryMapper.toDto(categories));
    }

    @Transactional
    public CategoryDto update(CategoryDto dto) {
        return categoryRepository.findById(dto.getId())
                .map(c -> categoryMapper.map(c, dto))
                .map(categoryRepository::save)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Category not found for id " + dto.getId()));
    }

    @Transactional
    public void delete(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
