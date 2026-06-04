package com.ust.pos.shelfs.service;

import com.ust.pos.dto.ShelfsDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShelfsService {
    ShelfsDto save(ShelfsDto shelfsDto);

    WsDto<ShelfsDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    ShelfsDto findByIdentifier(String identifier);

    ShelfsDto update(ShelfsDto shelfsDto);

    ShelfsDto statusUpdate(String identifier, boolean status);

    List<ShelfsDto> findAllActive();

    List<ShelfsDto> findAllForHome();
}
