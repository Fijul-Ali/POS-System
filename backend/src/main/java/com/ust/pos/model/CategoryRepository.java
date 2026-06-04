package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    void deleteByIdentifier(String identifier);

    Category findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Category> findBySuperCategoryIsNot(String empty);

    List<Category> findByStatus(boolean status);

    List<Category> findByStatusTrueAndSuperCategoryIsNot(String empty);
}
