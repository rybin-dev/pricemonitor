package com.senla.pricemonitor.repository.impl;

import com.senla.pricemonitor.entity.User;
import com.senla.pricemonitor.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, Long> implements UserRepository {
    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();
    }
}
