package com.ust.pos.category.service;

import com.ust.pos.dto.CategoryDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryDto save(CategoryDto categoryDto);

    WsDto<CategoryDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    CategoryDto findByIdentifier(String identifier);

    CategoryDto update(CategoryDto categoryDto);

    CategoryDto updateStatus(String identifier, boolean status);

    List<CategoryDto> findAllActive();

    List<CategoryDto> findAllActiveChilds();
}
