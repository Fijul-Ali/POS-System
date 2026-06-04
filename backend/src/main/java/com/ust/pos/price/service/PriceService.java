package com.ust.pos.price.service;

import com.ust.pos.dto.PriceDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PriceService {
    PriceDto save(PriceDto priceDto);

    WsDto<PriceDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    PriceDto findByIdentifier(String identifier);

    PriceDto update(PriceDto priceDto);

    List<PriceDto> findAllActive();

    PriceDto updateStatus(String identifier, boolean status);
}
