package com.ust.pos.unit.service;

import com.ust.pos.dto.UnitDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UnitService {
    UnitDto save(UnitDto unitDto);

    WsDto<UnitDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    UnitDto findByIdentifier(String identifier);

    UnitDto update(UnitDto unitDto);

    UnitDto updateStatus(String identifier, boolean status);

    List<UnitDto> findAllActive();
}
