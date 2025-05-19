package com.senla.pricemonitor.repository.impl;

import com.senla.pricemonitor.entity.ProductCategory;
import com.senla.pricemonitor.repository.ProductCategoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCategoryRepositoryImpl extends BaseRepository<ProductCategory, Long> implements ProductCategoryRepository {

    public ProductCategoryRepositoryImpl() {
        super(ProductCategory.class);
    }

}
