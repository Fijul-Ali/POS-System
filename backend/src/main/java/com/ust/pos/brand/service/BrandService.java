package com.ust.pos.brand.service;

import com.ust.pos.dto.BrandDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    BrandDto save(BrandDto brandDto);

    WsDto<BrandDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    BrandDto findByIdentifier(String identifier);

    BrandDto update(BrandDto brandDto);

    List<BrandDto> findAllActive();

    BrandDto changeBrandStatus(String identifier, boolean status);

    List<BrandDto> findAllForHome();
}
