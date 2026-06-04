package com.ust.pos.stock.service.impl;

import com.ust.pos.dto.StockDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Stock;
import com.ust.pos.model.StockRepository;
import com.ust.pos.stock.service.StockService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    StockRepository stockRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public StockDto save(StockDto stockDto) {
        String identifier = stockDto.getIdentifier();
        if (stockRepository.existsByIdentifier(identifier)) {
            stockDto.setMessage("Already exists");
            stockDto.setSuccess(false);
            return stockDto;
        }
        Stock stock = modelMapper.map(stockDto, Stock.class);
        stockRepository.save(stock);
        return stockDto;
    }

    @Override
    public WsDto<StockDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<StockDto>>() {
        }.getType();
        Page<Stock> stockPage = stockRepository.findAll(pageable);
        WsDto<StockDto> stockWsDto = new WsDto<>();
        stockWsDto.setDtoList(modelMapper.map(stockPage.getContent(), listType));
        stockWsDto.setTotalRecords(stockPage.getTotalElements());
        stockWsDto.setTotalPages(stockPage.getTotalPages());
        stockWsDto.setSizePerPage(pageable.getPageSize());
        stockWsDto.setPage(pageable.getPageNumber());

        return stockWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        stockRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public StockDto findByIdentifier(String identifier) {
        Stock stock = stockRepository.findByIdentifier(identifier);
        return modelMapper.map(stock, StockDto.class);
    }

    @Override
    public StockDto update(StockDto stockDto) {
        Stock stock = stockRepository.findByIdentifier(stockDto.getIdentifier());
        modelMapper.map(stockDto, stock);
        stockRepository.save(stock);
        return stockDto;
    }

    @Override
    public List<StockDto> findAllActive() {
        Type listType = new TypeToken<List<StockDto>>() {
        }.getType();
        return modelMapper.map(stockRepository.findByStatus(true), listType);
    }

    @Override
    public StockDto changeStockStatus(String identifier, boolean status) {
        Stock stock = stockRepository.findByIdentifier(identifier);

        if (stock != null) {
            stock.setStatus(status);
            stockRepository.save(stock);
        }
        return modelMapper.map(stock, StockDto.class);
    }
}