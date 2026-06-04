package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RacksRepository extends MongoRepository<Racks, Long> {
    void deleteByIdentifier(String identifier);

    Racks findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Racks> findByStatus(boolean status);
}
