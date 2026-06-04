package com.ust.pos.customer.service;

import com.ust.pos.dto.CustomerDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    CustomerDto save(CustomerDto customerDto);

    CustomerDto update(CustomerDto customerDto);

    boolean delete(String phoneNo);

    WsDto<CustomerDto> findAll(Pageable pageable);

    CustomerDto findByIdentifier(String identifier);

    CustomerDto findByAddress(String phoneNo);

    List<CustomerDto> findAllActive();

    CustomerDto changeStatus(String phoneNo, boolean status);
}
