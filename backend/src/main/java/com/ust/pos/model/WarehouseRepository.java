package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, String> {
    void deleteByIdentifier(String identifier);

    Warehouse findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Warehouse> findByStatus(boolean status);
}
