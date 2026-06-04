package com.ust.pos.price.service.impl;

import com.ust.pos.dto.PriceDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Price;
import com.ust.pos.model.PriceRepository;
import com.ust.pos.price.service.PriceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    PriceRepository priceRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public PriceDto save(PriceDto priceDto) {
        String identifier = priceDto.getIdentifier();
        if (priceRepository.existsByIdentifier(identifier)) {
            priceDto.setMessage("Already exists");
            priceDto.setSuccess(false);
            return priceDto;
        }
        Price price = modelMapper.map(priceDto, Price.class);
        priceRepository.save(price);
        return priceDto;
    }

    @Override
    public WsDto<PriceDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<PriceDto>>() {
        }.getType();
        Page<Price> pricePage = priceRepository.findAll(pageable);
        WsDto<PriceDto> priceWsDto = new WsDto<>();
        priceWsDto.setDtoList(modelMapper.map(pricePage.getContent(), listType));
        priceWsDto.setTotalRecords(pricePage.getTotalElements());
        priceWsDto.setTotalPages(pricePage.getTotalPages());
        priceWsDto.setSizePerPage(pageable.getPageSize());
        priceWsDto.setPage(pageable.getPageNumber());

        return priceWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        priceRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public PriceDto findByIdentifier(String identifier) {
        Price price = priceRepository.findByIdentifier(identifier);
        return modelMapper.map(price, PriceDto.class);
    }

    @Override
    public PriceDto update(PriceDto priceDto) {
        Price price = priceRepository.findByIdentifier(priceDto.getIdentifier());
        modelMapper.map(priceDto, price);
        priceRepository.save(price);
        return priceDto;
    }

    @Override
    public List<PriceDto> findAllActive() {
        Type listType = new TypeToken<List<PriceDto>>() {
        }.getType();
        return modelMapper.map(priceRepository.findByStatus(true), listType);
    }

    @Override
    public PriceDto updateStatus(String identifier, boolean status) {
        Price price = priceRepository.findByIdentifier(identifier);
        price.setStatus(status);
        priceRepository.save(price);
        return modelMapper.map(price, PriceDto.class);
    }
}
