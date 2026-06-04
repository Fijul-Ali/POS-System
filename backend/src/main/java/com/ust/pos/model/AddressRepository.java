package com.ust.pos.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends MongoRepository<Address, String> {

    List<Address> findAllByPhoneNumber(String phoneNumber);

    Address findByPhoneNumber(String phoneNumber);

    Address findByPhoneNumberAndAddressType(String phoneNumber, String addressType);
}
