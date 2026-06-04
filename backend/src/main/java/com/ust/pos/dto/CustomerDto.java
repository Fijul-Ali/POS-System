package com.ust.pos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto extends CommonDto {
    private String name;
    private String username;
    private String phoneNo;
    private String partyType;

    private Double balance;
    private String balanceType;
    private Double creditLimit;

    private AddressDto billingAddress;
    private AddressDto shippingAddress;
}