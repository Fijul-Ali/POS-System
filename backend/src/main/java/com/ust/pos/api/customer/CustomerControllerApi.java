package com.ust.pos.api.customer;

import com.ust.pos.address.AddressService;
import com.ust.pos.api.BaseController;
import com.ust.pos.customer.service.CustomerService;
import com.ust.pos.dto.AddressDto;
import com.ust.pos.dto.CustomerDto;
import com.ust.pos.dto.PaginationDto;
import com.ust.pos.dto.WsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerControllerApi extends BaseController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @PostMapping("/list")
    public WsDto<CustomerDto> home(@RequestBody PaginationDto paginationDto) {
        Pageable pageable = getPageable(paginationDto.getPage(), paginationDto.getSizePerPage(), paginationDto.getSortDirection(), paginationDto.getSortField());
        return customerService.findAll(pageable);
    }

    @PostMapping("/add")
    public CustomerDto addPost(@RequestBody CustomerDto customerDto) {
        return customerService.save(customerDto);
    }

    @GetMapping("/get")
    public CustomerDto update(@RequestParam String identifier) {
        return customerService.findByIdentifier(identifier);
    }

    @PostMapping("/update")
    public CustomerDto updatePost(@RequestBody CustomerDto customerDto) {
        return customerService.update(customerDto);
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam String identifier) {
        try {
            customerService.delete(identifier);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @GetMapping("/fetch")
    public CustomerDto getAddresses(@RequestParam String phoneNo) {
        return customerService.findByAddress(phoneNo);
    }

    @GetMapping("/findactive")
    public List<CustomerDto> findAllActive() {
        return customerService.findAllActive();
    }

    @PostMapping("/changestatus")
    public CustomerDto changeStatus(@RequestBody CustomerDto customerDto) {
        return customerService.changeStatus(customerDto.getPhoneNo(), customerDto.isStatus());
    }

    @PostMapping("/saveaddr")
    public AddressDto saveAddress(@RequestBody AddressDto addressDto) {
        return addressService.save(addressDto);
    }

    @PostMapping("/updateaddr")
    public AddressDto updateAddress(@RequestBody AddressDto addressDto) {
        return addressService.update(addressDto);
    }

    @GetMapping("/findaddr")
    public List<AddressDto> getAddress(@RequestParam String phoneNumber) {
        return addressService.findbyPhoneNo(phoneNumber);
    }
}
