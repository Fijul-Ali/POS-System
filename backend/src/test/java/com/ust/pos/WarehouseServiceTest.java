package com.ust.pos;

import com.ust.pos.dto.WarehouseDto;
import com.ust.pos.model.Warehouse;
import com.ust.pos.model.WarehouseRepository;
import com.ust.pos.warehouse.service.impl.WarehouseServiceImpl;
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
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setIdentifier("Admin");

        Warehouse warehouse = new Warehouse();
        Mockito.when(modelMapper.map(warehouseDto, Warehouse.class)).thenReturn(warehouse);
        Mockito.when(warehouseRepository.save(warehouse)).thenReturn(warehouse);
        Mockito.when(warehouseRepository.existsByIdentifier("Admin")).thenReturn(false);

        WarehouseDto response = warehouseService.save(warehouseDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setIdentifier("Admin");

        Mockito.when(warehouseRepository.existsByIdentifier("Admin")).thenReturn(true);

        WarehouseDto response = warehouseService.save(warehouseDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifier("Admin");

        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setIdentifier("Admin");

        Mockito.when(warehouseRepository.findByIdentifier("Admin")).thenReturn(warehouse);
        Mockito.when(modelMapper.map(warehouse, WarehouseDto.class)).thenReturn(warehouseDto);

        WarehouseDto response = warehouseService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setIdentifier("Admin");

        Warehouse warehouse = new Warehouse();
        Mockito.when(warehouseRepository.findByIdentifier("Admin")).thenReturn(warehouse);

        WarehouseDto response = warehouseService.update(warehouseDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = warehouseService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifier("Admin");

        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setIdentifier("Admin");

        List<Warehouse> warehouses = List.of(warehouse);
        List<WarehouseDto> warehouseDtos = List.of(warehouseDto);

        Page<Warehouse> warehousePage = new PageImpl<>(warehouses, PageRequest.of(0, 2), warehouses.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(warehouseRepository.findAll(pageable)).thenReturn(warehousePage);
        Mockito.when(modelMapper.map(Mockito.eq(warehouses), Mockito.any(java.lang.reflect.Type.class))).thenReturn(warehouseDtos);

        List<WarehouseDto> response = warehouseService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setIdentifier("Admin");
        warehouseDto.setStatus(true);

        Warehouse warehouse = new Warehouse();
        Mockito.when(warehouseRepository.findByIdentifier("Admin")).thenReturn(warehouse);
        Mockito.when(modelMapper.map(warehouse, WarehouseDto.class)).thenReturn(warehouseDto);
        WarehouseDto response = warehouseService.updateStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifier("Admin");

        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setIdentifier("Admin");

        List<Warehouse> warehouses = List.of(warehouse);
        List<WarehouseDto> warehouseDtos = List.of(warehouseDto);

        Mockito.when(warehouseRepository.findByStatus(true)).thenReturn(warehouses);
        Mockito.when(modelMapper.map(Mockito.eq(warehouses), Mockito.any(java.lang.reflect.Type.class))).thenReturn(warehouseDtos);

        List<WarehouseDto> response = warehouseService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}