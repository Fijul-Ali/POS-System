package com.ust.pos;

import com.ust.pos.address.AddressService;
import com.ust.pos.customer.service.impl.CustomerServiceImpl;
import com.ust.pos.dto.AddressDto;
import com.ust.pos.dto.CustomerDto;
import com.ust.pos.model.Customer;
import com.ust.pos.model.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        AddressDto billingAddress = new AddressDto();
        AddressDto shippingAddress = new AddressDto();

        CustomerDto customerDto = new CustomerDto();
        customerDto.setPhoneNo("9876543210");
        customerDto.setName("TestUser");
        customerDto.setBillingAddress(billingAddress);
        customerDto.setShippingAddress(shippingAddress);

        Mockito.when(customerRepository.findByPhoneNo("9876543210")).thenReturn(null);

        Customer customer = new Customer();
        customer.setName("TestUser");
        Mockito.when(modelMapper.map(customerDto, Customer.class)).thenReturn(customer);

        AddressDto mappedBilling = new AddressDto();
        AddressDto mappedShipping = new AddressDto();
        Mockito.when(modelMapper.map(billingAddress, AddressDto.class)).thenReturn(mappedBilling);
        Mockito.when(modelMapper.map(shippingAddress, AddressDto.class)).thenReturn(mappedShipping);

        Mockito.when(addressService.save(Mockito.any(AddressDto.class))).thenReturn(billingAddress);

        CustomerDto response = customerService.save(customerDto);

        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setPhoneNo("9876543210");

        Customer existingCustomer = new Customer();
        Mockito.when(customerRepository.findByPhoneNo("9876543210")).thenReturn(existingCustomer);

        CustomerDto response = customerService.save(customerDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Customer customer = new Customer();
        customer.setPhoneNo("9876543210");

        CustomerDto customerDto = new CustomerDto();
        customerDto.setPhoneNo("9876543210");

        Mockito.when(customerRepository.findByIdentifier("CU-abc123")).thenReturn(customer);
        Mockito.when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);

        CustomerDto response = customerService.findByIdentifier("CU-abc123");

        Assertions.assertEquals("9876543210", response.getPhoneNo());
    }

    /* ===================== FIND BY ADDRESS ===================== */

    @Test
    void findByAddressTest() {
        Customer customer = new Customer();
        CustomerDto customerDto = new CustomerDto();

        AddressDto billing = new AddressDto();
        billing.setAddressType("Billing");
        AddressDto shipping = new AddressDto();
        shipping.setAddressType("Shipping");

        Mockito.when(customerRepository.findByPhoneNo("9876543210")).thenReturn(customer);
        Mockito.when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);
        Mockito.when(addressService.findbyPhoneNo("9876543210")).thenReturn(List.of(billing, shipping));
        Mockito.when(modelMapper.map(billing, AddressDto.class)).thenReturn(billing);
        Mockito.when(modelMapper.map(shipping, AddressDto.class)).thenReturn(shipping);

        CustomerDto response = customerService.findByAddress("9876543210");

        Assertions.assertNotNull(response);
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        AddressDto billingAddress = new AddressDto();
        AddressDto shippingAddress = new AddressDto();

        CustomerDto customerDto = new CustomerDto();
        customerDto.setPhoneNo("9876543210");
        customerDto.setBillingAddress(billingAddress);
        customerDto.setShippingAddress(shippingAddress);

        Customer existingCustomer = new Customer();
        Mockito.when(customerRepository.findByPhoneNo("9876543210")).thenReturn(existingCustomer);

        AddressDto billing = new AddressDto();
        billing.setAddressType("Billing");
        AddressDto shipping = new AddressDto();
        shipping.setAddressType("Shipping");
        Mockito.when(addressService.findbyPhoneNo("9876543210")).thenReturn(List.of(billing, shipping));

        CustomerDto response = customerService.update(customerDto);

        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void updateTestFailure() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setPhoneNo("9876543210");

        Mockito.when(customerRepository.findByPhoneNo("9876543210")).thenReturn(null);

        CustomerDto response = customerService.update(customerDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {
        boolean response = customerService.delete("9876543210");
        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Customer customer = new Customer();
        CustomerDto customerDto = new CustomerDto();

        List<Customer> customers = List.of(customer);
        List<CustomerDto> customerDtos = List.of(customerDto);

        Page<Customer> userPage = new PageImpl<>(customers, PageRequest.of(0, 2), customers.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));
        Mockito.when(customerRepository.findAll(pageable)).thenReturn(userPage);
        Mockito.when(modelMapper.map(Mockito.eq(customers), Mockito.any(java.lang.reflect.Type.class))).thenReturn(customerDtos);

        List<CustomerDto> response = customerService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    /* ===================== FIND ALL ACTIVE ===================== */

    @Test
    void findAllActiveTest() {
        Customer customer = new Customer();
        CustomerDto customerDto = new CustomerDto();

        List<Customer> customers = List.of(customer);
        List<CustomerDto> customerDtos = List.of(customerDto);

        Mockito.when(customerRepository.findByStatus(true)).thenReturn(customers);
        Mockito.when(modelMapper.map(Mockito.eq(customers), Mockito.any(java.lang.reflect.Type.class))).thenReturn(customerDtos);

        List<CustomerDto> response = customerService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }

    /* ===================== CHANGE STATUS ===================== */

    @Test
    void changeStatusTest() {
        Customer customer = new Customer();
        CustomerDto customerDto = new CustomerDto();

        Mockito.when(customerRepository.findByPhoneNo("9876543210")).thenReturn(customer);
        Mockito.when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);

        CustomerDto response = customerService.changeStatus("9876543210", true);

        Assertions.assertNotNull(response);
    }

    @Test
    void changeStatusTestNotFound() {
        Mockito.when(customerRepository.findByPhoneNo("9876543210")).thenReturn(null);
        Mockito.when(modelMapper.map(null, CustomerDto.class)).thenReturn(new CustomerDto());

        CustomerDto response = customerService.changeStatus("9876543210", true);

        Assertions.assertNotNull(response);
    }
}