package com.ust.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Getter
@Setter
public class Customer extends CommonFields {

    private String name;
    private String username;
    private String phoneNo;
    private String partyType;

    private Double balance;
    private String balanceType;
    private Double creditLimit;
}