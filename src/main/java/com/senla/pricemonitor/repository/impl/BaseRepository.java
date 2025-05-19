package com.senla.pricemonitor.repository.impl;

import com.senla.pricemonitor.repository.CrudRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class BaseRepository<T, ID> implements CrudRepository<T, ID> {

    protected final Class<T> entityClass;

    @Setter
    @PersistenceContext
    protected EntityManager em;

    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        return em.merge(entity);
    }

    @Transactional
    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        for (S entity : entities) {
            save(entity);
        }
        return entities;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public Iterable<T> findAll() {
        var query = em.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));

        return em.createQuery(query).getResultList();
    }

    @Override
    public Iterable<T> findAll(int page, int size) {
        var query = em.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));

        return em.createQuery(query)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        var query = em.getCriteriaBuilder().createQuery(entityClass);
        var root = query.from(entityClass);

        query.select(root)
                .where(root.get("id").in(ids));

        return em.createQuery(query).getResultList();
    }


    @Override
    public long count() {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(Long.class);
        var root = query.from(entityClass);

        query.select(cb.count(root));

        return em.createQuery(query).getSingleResult();
    }

    @Transactional
    @Override
    public void deleteById(ID id) {
        em.remove(em.find(entityClass, id));
    }

    @Transactional
    @Override
    public void delete(T entity) {
        em.remove(entity);
    }

    @Transactional
    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {
        for (ID id : ids) {
            em.remove(em.find(entityClass, id));
        }

    }

    @Transactional
    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        for (T entity : entities) {
            em.remove(entity);
        }
    }

    @Transactional
    @Override
    public void deleteAll() {
        var query = em.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
    }

    public T getReferenceById(ID id) {
        return em.getReference(entityClass, id);
    }
}
