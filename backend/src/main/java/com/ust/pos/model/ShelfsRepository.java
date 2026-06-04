package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelfsRepository extends MongoRepository<Shelfs, String> {
    void deleteByIdentifier(String identifier);

    Shelfs findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Shelfs> findByStatus(boolean status);
}
