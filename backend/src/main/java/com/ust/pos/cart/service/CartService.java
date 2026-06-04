package com.ust.pos.cart.service;

import com.ust.pos.dto.CartDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartService {
    CartDto save(CartDto cartDto);

    WsDto<CartDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    CartDto findByIdentifier(String identifier);

    CartDto update(CartDto cartDto);

    List<CartDto> findAllActive();

    CartDto changeCartStatus(String identifier, boolean status);

    CartDto recalculate(String cart);


}
