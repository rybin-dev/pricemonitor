package com.senla.pricemonitor.repository.impl;

import com.senla.pricemonitor.dto.PriceAtDate;
import com.senla.pricemonitor.dto.ShopPriceDto;
import com.senla.pricemonitor.entity.ProductPrice;
import com.senla.pricemonitor.repository.ProductPriceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductPriceRepositoryImpl extends BaseRepository<ProductPrice, Long> implements ProductPriceRepository {

    public ProductPriceRepositoryImpl() {
        super(ProductPrice.class);
    }

    @Override
    public Optional<ProductPrice> findCurrentPrice(Long productId, Long shopId) {
        var jpql = """
                select p 
                from ProductPrice p 
                where p.product.id = :productId and p.shop.id = :shopId and p.endDate is null
                """;

        return em.createQuery(jpql, entityClass)
                .setParameter("productId", productId)
                .setParameter("shopId", shopId)
                .getResultList().stream()
                .findFirst();

    }

    @Override
    public List<ShopPriceDto> findCurrentPrice(Long productId, int page, int size) {
        var jpql = """
                select new com.senla.pricemonitor.dto.ShopPriceDto(
                s.id,
                s.name,
                p.price
                ) 
                from ProductPrice p 
                join  p.shop s
                where p.product.id = :productId and p.endDate is null
                order by p.price
                """;

        return em.createQuery(jpql, ShopPriceDto.class)
                .setParameter("productId", productId)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<PriceAtDate> calculatePriceStatsFor(Long productId, LocalDate startDate, LocalDate endDate) {
        var sql = """
                WITH date_series AS (
                  SELECT generate_series(
                      (:startDate)::date,
                      (:endDate)::date,
                      '1 day'::interval
                  ) AS day
                )
                SELECT
                    MIN(p.price) AS min,
                    MAX(p.price) AS max,
                    AVG(p.price) AS avg,
                    ds.day AS date
                FROM
                    date_series ds
                JOIN product_price p
                    ON ds.day BETWEEN p.start_date AND COALESCE(p.end_date, CURRENT_DATE)
                WHERE
                    p.product_id = :productId
                GROUP BY date
                ORDER BY date
                """;

        return em.unwrap(org.hibernate.Session.class)
                .createNativeQuery(sql, Object[].class)
                .setParameter("productId", productId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setTupleTransformer((tuple, aliases) ->
                        new PriceAtDate(
                                tuple[0] != null ? ((Number) tuple[0]).longValue() : null,
                                tuple[1] != null ? ((Number) tuple[1]).longValue() : null,
                                tuple[2] != null ? ((Number) tuple[2]).doubleValue() : null,
                                tuple[3] != null ? ((java.time.Instant) tuple[3]).atZone(ZoneId.systemDefault()).toLocalDate() : null
                        )
                ).getResultList();
    }
}
