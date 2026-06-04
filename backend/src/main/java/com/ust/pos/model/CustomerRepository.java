package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    Customer findByIdentifier(String identifier);

    void deleteByIdentifier(String identifier);

    Customer findByPhoneNo(String phoneNo);

    void deleteByPhoneNo(String phoneNo);

    Customer findByUsername(String username);

    List<Customer> findByStatus(boolean status);
}
