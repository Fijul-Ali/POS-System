package com.ust.pos.racks.service;

import com.ust.pos.dto.RacksDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RacksService {
    RacksDto save(RacksDto racksDto);

    WsDto<RacksDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    RacksDto findByIdentifier(String identifier);

    RacksDto update(RacksDto racksDto);

    List<RacksDto> findAllActive();

    RacksDto changeStatus(String identifier, boolean status);
}
