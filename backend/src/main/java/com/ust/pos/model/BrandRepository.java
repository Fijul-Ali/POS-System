package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends MongoRepository<Brand, String> {
    void deleteByIdentifier(String identifier);

    Brand findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Brand> findByStatus(boolean status);
}
