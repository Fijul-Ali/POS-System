package com.ust.pos;

import com.ust.pos.dto.StockDto;
import com.ust.pos.model.Stock;
import com.ust.pos.model.StockRepository;
import com.ust.pos.stock.service.impl.StockServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StockServiceImpl stockService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        StockDto stockDto = new StockDto();
        stockDto.setIdentifier("Admin");

        Stock stock = new Stock();
        Mockito.when(modelMapper.map(stockDto, Stock.class)).thenReturn(stock);
        Mockito.when(stockRepository.save(stock)).thenReturn(stock);
        Mockito.when(stockRepository.existsByIdentifier("Admin")).thenReturn(false);

        StockDto response = stockService.save(stockDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        StockDto stockDto = new StockDto();
        stockDto.setIdentifier("Admin");

        Mockito.when(stockRepository.existsByIdentifier("Admin")).thenReturn(true);

        StockDto response = stockService.save(stockDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Stock stock = new Stock();
        stock.setIdentifier("Admin");

        StockDto stockDto = new StockDto();
        stockDto.setIdentifier("Admin");

        Mockito.when(stockRepository.findByIdentifier("Admin")).thenReturn(stock);
        Mockito.when(modelMapper.map(stock, StockDto.class)).thenReturn(stockDto);

        StockDto response = stockService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        StockDto stockDto = new StockDto();
        stockDto.setIdentifier("Admin");

        Stock stock = new Stock();
        Mockito.when(stockRepository.findByIdentifier("Admin")).thenReturn(stock);

        StockDto response = stockService.update(stockDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = stockService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Stock stock = new Stock();
        stock.setIdentifier("Admin");

        StockDto stockDto = new StockDto();
        stockDto.setIdentifier("Admin");

        List<Stock> stocks = List.of(stock);
        List<StockDto> stockDtos = List.of(stockDto);

        Page<Stock> stockPage = new PageImpl<>(stocks, PageRequest.of(0, 2), stocks.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(stockRepository.findAll(pageable)).thenReturn(stockPage);
        Mockito.when(modelMapper.map(Mockito.eq(stocks), Mockito.any(java.lang.reflect.Type.class))).thenReturn(stockDtos);

        List<StockDto> response = stockService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        StockDto stockDto = new StockDto();
        stockDto.setIdentifier("Admin");
        stockDto.setStatus(true);

        Stock stock = new Stock();
        Mockito.when(stockRepository.findByIdentifier("Admin")).thenReturn(stock);
        Mockito.when(modelMapper.map(stock, StockDto.class)).thenReturn(stockDto);
        StockDto response = stockService.changeStockStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Stock stock = new Stock();
        stock.setIdentifier("Admin");

        StockDto stockDto = new StockDto();
        stockDto.setIdentifier("Admin");

        List<Stock> stocks = List.of(stock);
        List<StockDto> stockDtos = List.of(stockDto);

        Mockito.when(stockRepository.findByStatus(true)).thenReturn(stocks);
        Mockito.when(modelMapper.map(Mockito.eq(stocks), Mockito.any(java.lang.reflect.Type.class))).thenReturn(stockDtos);

        List<StockDto> response = stockService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}