package com.ust.pos.customer.service.impl;

import com.ust.pos.address.AddressService;
import com.ust.pos.customer.service.CustomerService;
import com.ust.pos.dto.AddressDto;
import com.ust.pos.dto.CustomerDto;
import com.ust.pos.dto.UserDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Customer;
import com.ust.pos.model.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressService addressService;

    @Override
    public CustomerDto findByIdentifier(String identifier) {
        return modelMapper.map(customerRepository.findByIdentifier(identifier), CustomerDto.class);
    }

    @Override
    public CustomerDto findByAddress(String phoneNo) {
        Customer customer = customerRepository.findByPhoneNo(phoneNo);
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        List<AddressDto> addressDtos = addressService.findbyPhoneNo(phoneNo);
        AddressDto billing = addressDtos.get(0);
        AddressDto shipping = addressDtos.get(1);
        customerDto.setBillingAddress(modelMapper.map(billing, AddressDto.class));
        customerDto.setShippingAddress(modelMapper.map(shipping, AddressDto.class));
        return customerDto;
    }

    @Override
    public CustomerDto changeStatus(String phoneNo, boolean status) {
        Customer customer = customerRepository.findByPhoneNo(phoneNo);
        customer.setStatus(status);
        customerRepository.save(customer);
        return modelMapper.map(customer, CustomerDto.class);
    }

    @Override
    public CustomerDto save(CustomerDto customerDto) {
        String phoneNo = customerDto.getPhoneNo();
        Customer existingCustomer = customerRepository.findByPhoneNo(phoneNo);
        if (existingCustomer != null) {
            customerDto.setMessage("User with username/email - " + phoneNo + " already exists");
            customerDto.setSuccess(false);
            return customerDto;
        }
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customerRepository.save(customer);

        AddressDto addressBilling = modelMapper.map(customerDto.getBillingAddress(), AddressDto.class);
        addressBilling.setPhoneNumber(phoneNo);
        addressBilling.setAddressType("Billing");
        addressService.save(addressBilling);

        AddressDto addressShipping = modelMapper.map(customerDto.getShippingAddress(), AddressDto.class);
        addressShipping.setPhoneNumber(phoneNo);
        addressShipping.setAddressType("Shipping");
        addressService.save(addressShipping);

        return customerDto;
    }

    @Override
    public CustomerDto update(CustomerDto customerDto) {
        String phoneNo = customerDto.getPhoneNo();
        Customer existingCustomer = customerRepository.findByPhoneNo(phoneNo);
        if (existingCustomer == null) {
            customerDto.setMessage("User with username/email - " + phoneNo + " already exists");
            customerDto.setSuccess(false);
            return customerDto;
        }
        modelMapper.map(customerDto, existingCustomer);
        customerRepository.save(existingCustomer);

        List<AddressDto> existingAddresses = addressService.findbyPhoneNo(phoneNo);
        AddressDto billing = existingAddresses.get(0);
        billing.setPhoneNumber(phoneNo);
        billing.setAddressType("Billing");
        modelMapper.map(customerDto.getBillingAddress(), billing);
        addressService.update(billing);

        AddressDto shipping = existingAddresses.get(1);
        shipping.setPhoneNumber(phoneNo);
        shipping.setAddressType("Shipping");
        modelMapper.map(customerDto.getShippingAddress(), shipping);
        addressService.update(shipping);

        return customerDto;
    }

    @Override
    public boolean delete(String phoneNo) {
        customerRepository.deleteByPhoneNo(phoneNo);
        return true;
    }

    @Override
    public WsDto<CustomerDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<CustomerDto>>() {
        }.getType();
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        WsDto<CustomerDto> customerWsDto = new WsDto<>();
        customerWsDto.setDtoList(modelMapper.map(customerPage.getContent(), listType));
        customerWsDto.setTotalRecords(customerPage.getTotalElements());
        customerWsDto.setTotalPages(customerPage.getTotalPages());
        customerWsDto.setSizePerPage(pageable.getPageSize());
        customerWsDto.setPage(pageable.getPageNumber());

        return customerWsDto;
    }

    @Override
    public List<CustomerDto> findAllActive() {
        Type listType = new TypeToken<List<CustomerDto>>() {
        }.getType();
        return modelMapper.map(customerRepository.findByStatus(true), listType);
    }
}
