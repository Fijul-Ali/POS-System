package com.ust.pos.address.impl;

import com.ust.pos.address.AddressService;
import com.ust.pos.dto.AddressDto;
import com.ust.pos.model.Address;
import com.ust.pos.model.AddressRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public AddressDto save(AddressDto addressDto) {
        Address address = modelMapper.map(addressDto, Address.class);
        addressRepository.save(address);
        return addressDto;
    }

    @Override
    public List<AddressDto> findbyPhoneNo(String phoneNumber) {
        Type listType = new TypeToken<List<AddressDto>>() {
        }.getType();
        return modelMapper.map(addressRepository.findAllByPhoneNumber(phoneNumber), listType);
    }

    @Override
    public AddressDto update(AddressDto addressDto) {
        Address existing = addressRepository.findByPhoneNumberAndAddressType(addressDto.getPhoneNumber(), addressDto.getAddressType());
        modelMapper.map(addressDto, existing);
        addressRepository.save(existing);
        return addressDto;
    }
}
