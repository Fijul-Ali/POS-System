package com.ust.pos.order.service.impl;

import com.ust.pos.dto.OrderDto;
import com.ust.pos.dto.UserDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.*;
import com.ust.pos.order.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public OrderDto findByIdentifier(String identifier) {
        return modelMapper.map(orderRepository.findByIdentifier(identifier), OrderDto.class);
    }

    @Override
    public WsDto<OrderDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<OrderDto>>() {}.getType();
        Page<Order> orderPage = orderRepository.findAll(pageable);
        WsDto<OrderDto> wsDto = new WsDto<>();
        wsDto.setDtoList(modelMapper.map(orderPage.getContent(), listType));
        wsDto.setTotalRecords(orderPage.getTotalElements());
        wsDto.setTotalPages(orderPage.getTotalPages());
        wsDto.setSizePerPage(pageable.getPageSize());
        wsDto.setPage(pageable.getPageNumber());
        return wsDto;
    }

    @Override
    public boolean delete(String identifier) {
        orderRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public OrderDto placeOrder(String cartIdentifier) {
        Cart cart = cartRepository.findByIdentifier(cartIdentifier);
        Customer customer = customerRepository.findByIdentifier(cartIdentifier);
        if (cart == null) {
            OrderDto dto = new OrderDto();
            dto.setSuccess(false);
            dto.setMessage("Cart not found: " + cartIdentifier);
            return dto;
        }
        Order order = new Order();
        order.setIdentifier(cart.getIdentifier());
        order.setCart(customer.getIdentifier());
        order.setCustomer(customer.getName());
        order.setTotalAmount(cart.getTotalPrice());
        order.setDiscount(cart.getTotalDiscount());
        order.setOriginalAmount(cart.getOriginalPrice());
        order.setOrderStatus("COMPLETED");
        order.setPlacedAt(LocalDateTime.now());
        order.setStatus(true);
        orderRepository.save(order);
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto cancel(String identifier) {
        Order order = orderRepository.findByIdentifier(identifier);
        if (order == null) {
            OrderDto dto = new OrderDto();
            dto.setSuccess(false);
            dto.setMessage("Order not found: " + identifier);
            return dto;
        }
        order.setOrderStatus("CANCELLED");
        orderRepository.save(order);
        return modelMapper.map(order, OrderDto.class);
    }
}