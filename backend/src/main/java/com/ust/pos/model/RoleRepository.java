package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByIdentifier(String identifier);

    void deleteByIdentifier(String identifier);

    List<Role> findByStatus(boolean status);
}
