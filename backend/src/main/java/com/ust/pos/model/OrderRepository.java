package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByIdentifier(String identifier);

    void deleteByIdentifier(String identifier);
}