package com.ust.pos.product.service;

import com.ust.pos.dto.ProductDto;
import com.ust.pos.dto.WsDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductDto save(ProductDto productDto);

    WsDto<ProductDto> findAll(Pageable pageable);

    boolean delete(String identifier);

    ProductDto findByIdentifier(String identifier);

    ProductDto update(ProductDto productDto);

    List<ProductDto> findAllActive();

    ProductDto updateStatus(String identifier, boolean status);
}
