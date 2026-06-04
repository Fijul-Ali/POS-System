package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends MongoRepository<Node, String> {
    Node findByIdentifier(String identifier);

    List<Node> findByRoles(List<String> roles);

    void deleteByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);

    List<Node> findByStatus(boolean status);
}
