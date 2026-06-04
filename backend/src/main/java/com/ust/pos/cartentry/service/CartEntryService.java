package com.ust.pos.cartentry.service;

import com.ust.pos.dto.CartEntryDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartEntryService {
    CartEntryDto save(CartEntryDto cartEntryDto);

    WsDto<CartEntryDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    CartEntryDto findByIdentifier(String identifier);

    CartEntryDto update(CartEntryDto cartEntryDto);

    List<CartEntryDto> findAllActive();

    CartEntryDto changeCartEntryStatus(String identifier, boolean status);

    List<CartEntryDto> findAllCarts(String cart);

}
