package com.senla.pricemonitor.repository;

import com.senla.pricemonitor.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Iterable<Product> findAll(Long categoryId, String name, int page, int size);
}
