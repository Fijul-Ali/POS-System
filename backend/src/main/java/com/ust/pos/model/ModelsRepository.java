package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelsRepository extends MongoRepository<Models, String> {
    void deleteByIdentifier(String identifier);

    Models findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Models> findByStatus(boolean status);
}
