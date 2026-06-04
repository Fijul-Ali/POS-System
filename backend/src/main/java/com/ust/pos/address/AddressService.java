package com.ust.pos.address;

import com.ust.pos.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto save(AddressDto addressDto);

    List<AddressDto> findbyPhoneNo(String phoneNumber);

    AddressDto update(AddressDto addressDto);
}
