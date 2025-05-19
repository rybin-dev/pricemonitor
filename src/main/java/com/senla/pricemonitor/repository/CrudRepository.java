package com.senla.pricemonitor.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {
    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    Optional<T> findById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAll(int page, int size);

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAllById(Iterable<? extends ID> ids);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll();

    T getReferenceById(ID id);
}