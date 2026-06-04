package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "adresses")
@Getter
@Setter
public class Address {
    @Id
    private String id;
    private String line1;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String addressType;
    private String phoneNumber;
}
