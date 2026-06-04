package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {
    void deleteByIdentifier(String identifier);

    Stock findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Stock> findByStatus(boolean status);
}
