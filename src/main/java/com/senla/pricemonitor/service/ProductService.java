package com.senla.pricemonitor.service;

import com.senla.pricemonitor.dto.PageableResponse;
import com.senla.pricemonitor.dto.ProductDto;
import com.senla.pricemonitor.entity.Product;
import com.senla.pricemonitor.exception.NotFoundException;
import com.senla.pricemonitor.mapper.ProductMapper;
import com.senla.pricemonitor.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDto create(ProductDto dto) {
        var product = new Product();
        productMapper.map(product, dto);

        product = productRepository.save(product);

        return productMapper.toDto(product);
    }

    public ProductDto get(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found for id " + productId));
    }

    public PageableResponse<ProductDto> getAll(Long categoryId, String name, int page, int size) {
        var products = productRepository.findAll(categoryId, name, page, size);

        return PageableResponse.of(productMapper.toDto(products));
    }

    @Transactional
    public ProductDto update(ProductDto dto) {
        return productRepository.findById(dto.getId())
                .map(p -> productMapper.map(p, dto))
                .map(productRepository::save)
                .map(productMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found for id " + dto.getId()));
    }

    @Transactional
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }
}
