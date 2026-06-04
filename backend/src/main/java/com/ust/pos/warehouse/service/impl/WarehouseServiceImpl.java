package com.ust.pos.warehouse.service.impl;

import com.ust.pos.dto.WarehouseDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Warehouse;
import com.ust.pos.model.WarehouseRepository;
import com.ust.pos.warehouse.service.WarehouseService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WarehouseDto save(WarehouseDto warehouseDto) {
        String identifier = warehouseDto.getIdentifier();
        if (warehouseRepository.existsByIdentifier(identifier)) {
            warehouseDto.setMessage("Already exists");
            warehouseDto.setSuccess(false);
            return warehouseDto;
        }
        Warehouse warehouse = modelMapper.map(warehouseDto, Warehouse.class);
        warehouseRepository.save(warehouse);
        return warehouseDto;
    }

    @Override
    public WsDto<WarehouseDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<WarehouseDto>>() {
        }.getType();
        Page<Warehouse> warehousePage = warehouseRepository.findAll(pageable);
        WsDto<WarehouseDto> warehouseWsDto = new WsDto<>();
        warehouseWsDto.setDtoList(modelMapper.map(warehousePage.getContent(), listType));
        warehouseWsDto.setTotalRecords(warehousePage.getTotalElements());
        warehouseWsDto.setTotalPages(warehousePage.getTotalPages());
        warehouseWsDto.setSizePerPage(pageable.getPageSize());
        warehouseWsDto.setPage(pageable.getPageNumber());

        return warehouseWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        warehouseRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public WarehouseDto findByIdentifier(String identifier) {
        Warehouse warehouse = warehouseRepository.findByIdentifier(identifier);
        return modelMapper.map(warehouse, WarehouseDto.class);
    }

    @Override
    public WarehouseDto update(WarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseRepository.findByIdentifier(warehouseDto.getIdentifier());
        modelMapper.map(warehouseDto, warehouse);
        warehouse.setStatus(warehouseDto.isStatus());
        warehouseRepository.save(warehouse);
        return warehouseDto;
    }

    @Override
    public List<WarehouseDto> findAllActive() {
        Type listType = new TypeToken<List<WarehouseDto>>() {
        }.getType();
        return modelMapper.map(warehouseRepository.findByStatus(true), listType);
    }

    @Override
    public WarehouseDto updateStatus(String identifier, boolean status) {
        Warehouse warehouse = warehouseRepository.findByIdentifier(identifier);
        warehouse.setStatus(status);
        warehouseRepository.save(warehouse);
        return modelMapper.map(warehouse, WarehouseDto.class);
    }
}
