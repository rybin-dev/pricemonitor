package com.senla.pricemonitor.repository.impl;

import com.senla.pricemonitor.entity.Category;
import com.senla.pricemonitor.entity.Product;
import com.senla.pricemonitor.entity.ProductCategory;
import com.senla.pricemonitor.repository.ProductRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl extends BaseRepository<Product, Long> implements ProductRepository {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    public Iterable<Product> findAll(int page, int size){
        return findAll(null, null, page, size);
    }

    @Override
    public Iterable<Product> findAll(Long categoryId, String name, int page, int size) {
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(Long.class);
        var product = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (categoryId != null) {
            Join<Product, ProductCategory> productCategoryJoin = product.join("productCategories");
            Join<ProductCategory, Category> categoryJoin = productCategoryJoin.join("category");

            predicates.add(cb.equal(categoryJoin.get("id"), categoryId));
        }

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(product.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.select(product.get("id")).distinct(true);

        List<Long> ids = em.createQuery(cq)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        var cq2 = cb.createQuery(Product.class);
        var product2 = cq2.from(Product.class);

        product2.fetch("productCategories", JoinType.LEFT);

        cq2.select(product2).distinct(true).where(product2.get("id").in(ids));

        return em.createQuery(cq2).getResultList();
    }
}
