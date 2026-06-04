package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    void deleteByIdentifier(String identifier);

    Cart findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Cart> findByStatus(boolean status);
}
