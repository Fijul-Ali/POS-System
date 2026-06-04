package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByStatus(boolean status);
}
