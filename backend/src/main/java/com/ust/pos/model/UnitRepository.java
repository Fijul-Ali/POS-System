package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends MongoRepository<Unit, String> {
    void deleteByIdentifier(String identifier);

    Unit findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Unit> findByStatus(boolean status);
}
