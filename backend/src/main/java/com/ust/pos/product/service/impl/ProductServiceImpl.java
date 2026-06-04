package com.ust.pos.product.service.impl;

import com.ust.pos.dto.ProductDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Product;
import com.ust.pos.model.ProductRepository;
import com.ust.pos.product.service.ProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ProductDto save(ProductDto productDto) {
        String identifier = productDto.getIdentifier();
        if (productRepository.existsByIdentifier(identifier)) {
            productDto.setMessage("Already exists");
            productDto.setSuccess(false);
            return productDto;
        }
        Product product = modelMapper.map(productDto, Product.class);
        productRepository.save(product);
        return productDto;
    }

    @Override
    public WsDto<ProductDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<ProductDto>>() {
        }.getType();
        Page<Product> productPage = productRepository.findAll(pageable);
        WsDto<ProductDto> productWsDto = new WsDto<>();
        productWsDto.setDtoList(modelMapper.map(productPage.getContent(), listType));
        productWsDto.setTotalRecords(productPage.getTotalElements());
        productWsDto.setTotalPages(productPage.getTotalPages());
        productWsDto.setSizePerPage(pageable.getPageSize());
        productWsDto.setPage(pageable.getPageNumber());

        return productWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        productRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public ProductDto findByIdentifier(String identifier) {
        Product product = productRepository.findByIdentifier(identifier);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        Product product = productRepository.findByIdentifier(productDto.getIdentifier().trim());
        modelMapper.map(productDto, product);
        productRepository.save(product);
        return productDto;
    }

    @Override
    public List<ProductDto> findAllActive() {
        Type listType = new TypeToken<List<ProductDto>>() {
        }.getType();
        return modelMapper.map(productRepository.findByStatus(true), listType);
    }

    @Override
    public ProductDto updateStatus(String identifier, boolean status) {
        Product product = productRepository.findByIdentifier(identifier);
        product.setStatus(status);
        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }
}
