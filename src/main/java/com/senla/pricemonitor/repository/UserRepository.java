package com.senla.pricemonitor.repository;

import com.senla.pricemonitor.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}