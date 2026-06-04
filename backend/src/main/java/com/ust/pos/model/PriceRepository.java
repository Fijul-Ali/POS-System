package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends MongoRepository<Price, String> {
    void deleteByIdentifier(String identifier);

    Price findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Price> findByStatus(boolean status);

    Price findByProduct(String product);
}
