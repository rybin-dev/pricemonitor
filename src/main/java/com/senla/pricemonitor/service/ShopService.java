package com.senla.pricemonitor.service;

import com.senla.pricemonitor.dto.PageableResponse;
import com.senla.pricemonitor.dto.ShopDto;
import com.senla.pricemonitor.entity.Shop;
import com.senla.pricemonitor.exception.NotFoundException;
import com.senla.pricemonitor.mapper.ShopMapper;
import com.senla.pricemonitor.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {
    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;

    @Transactional
    public ShopDto create(ShopDto dto) {
        var shop = new Shop();
        shopMapper.map(shop, dto);

        shop = shopRepository.save(shop);

        return shopMapper.toDto(shop);
    }

    public ShopDto get(Long shopId) {
        return shopRepository.findById(shopId)
                .map(shopMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Shop not found for shopId " + shopId));

    }

    public PageableResponse<ShopDto> getAll(int page, int size) {
        var shops = shopRepository.findAll(page, size);

        return PageableResponse.of(shopMapper.toDto(shops));
    }

    @Transactional
    public ShopDto update(ShopDto dto) {
        return shopRepository.findById(dto.getId())
                .map(s -> shopMapper.map(s, dto))
                .map(shopRepository::save)
                .map(shopMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Shop not found for shopId " + dto.getId()));
    }

    @Transactional
    public void delete(Long shopId) {
        shopRepository.deleteById(shopId);
    }
}
