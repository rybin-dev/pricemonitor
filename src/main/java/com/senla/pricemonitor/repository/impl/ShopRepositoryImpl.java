package com.senla.pricemonitor.repository.impl;

import com.senla.pricemonitor.entity.Shop;
import com.senla.pricemonitor.repository.CategoryRepository;
import com.senla.pricemonitor.repository.ShopRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ShopRepositoryImpl extends BaseRepository<Shop, Long> implements ShopRepository {

    public ShopRepositoryImpl() {
        super(Shop.class);
    }

}
