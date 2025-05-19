package com.senla.pricemonitor.repository.impl;

import com.senla.pricemonitor.entity.Category;
import com.senla.pricemonitor.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryImpl extends BaseRepository<Category, Long> implements CategoryRepository {

    public CategoryRepositoryImpl() {
        super(Category.class);
    }

}
