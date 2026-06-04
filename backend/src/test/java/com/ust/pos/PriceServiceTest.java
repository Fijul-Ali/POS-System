package com.ust.pos;

import com.ust.pos.dto.PriceDto;
import com.ust.pos.model.Price;
import com.ust.pos.model.PriceRepository;
import com.ust.pos.price.service.impl.PriceServiceImpl;
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
class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PriceServiceImpl priceService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        PriceDto priceDto = new PriceDto();
        priceDto.setIdentifier("Admin");

        Price price = new Price();
        Mockito.when(modelMapper.map(priceDto, Price.class)).thenReturn(price);
        Mockito.when(priceRepository.save(price)).thenReturn(price);
        Mockito.when(priceRepository.existsByIdentifier("Admin")).thenReturn(false);

        PriceDto response = priceService.save(priceDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        PriceDto priceDto = new PriceDto();
        priceDto.setIdentifier("Admin");

        Mockito.when(priceRepository.existsByIdentifier("Admin")).thenReturn(true);

        PriceDto response = priceService.save(priceDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Price price = new Price();
        price.setIdentifier("Admin");

        PriceDto priceDto = new PriceDto();
        priceDto.setIdentifier("Admin");

        Mockito.when(priceRepository.findByIdentifier("Admin")).thenReturn(price);
        Mockito.when(modelMapper.map(price, PriceDto.class)).thenReturn(priceDto);

        PriceDto response = priceService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        PriceDto priceDto = new PriceDto();
        priceDto.setIdentifier("Admin");

        Price price = new Price();
        Mockito.when(priceRepository.findByIdentifier("Admin")).thenReturn(price);

        PriceDto response = priceService.update(priceDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = priceService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Price price = new Price();
        price.setIdentifier("Admin");

        PriceDto priceDto = new PriceDto();
        priceDto.setIdentifier("Admin");

        List<Price> prices = List.of(price);
        List<PriceDto> priceDtos = List.of(priceDto);

        Page<Price> pricePage = new PageImpl<>(prices, PageRequest.of(0, 2), prices.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(priceRepository.findAll(pageable)).thenReturn(pricePage);
        Mockito.when(modelMapper.map(Mockito.eq(prices), Mockito.any(java.lang.reflect.Type.class))).thenReturn(priceDtos);

        List<PriceDto> response = priceService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        PriceDto priceDto = new PriceDto();
        priceDto.setIdentifier("Admin");
        priceDto.setStatus(true);

        Price price = new Price();
        Mockito.when(priceRepository.findByIdentifier("Admin")).thenReturn(price);
        Mockito.when(modelMapper.map(price, PriceDto.class)).thenReturn(priceDto);
        PriceDto response = priceService.updateStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Price price = new Price();
        price.setIdentifier("Admin");

        PriceDto priceDto = new PriceDto();
        priceDto.setIdentifier("Admin");

        List<Price> prices = List.of(price);
        List<PriceDto> priceDtos = List.of(priceDto);

        Mockito.when(priceRepository.findByStatus(true)).thenReturn(prices);
        Mockito.when(modelMapper.map(Mockito.eq(prices), Mockito.any(java.lang.reflect.Type.class))).thenReturn(priceDtos);

        List<PriceDto> response = priceService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}