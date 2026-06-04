package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartEntryRepository extends MongoRepository<CartEntry, String> {
    void deleteByIdentifier(String identifier);

    CartEntry findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<CartEntry> findByStatus(boolean status);

    List<CartEntry> findByCart(String cart);
}
