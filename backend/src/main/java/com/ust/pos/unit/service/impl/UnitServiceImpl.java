package com.ust.pos.unit.service.impl;

import com.ust.pos.dto.UnitDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Unit;
import com.ust.pos.model.UnitRepository;
import com.ust.pos.unit.service.UnitService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService {
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public UnitDto save(UnitDto unitDto) {
        String identifier = unitDto.getIdentifier();
        if (unitRepository.existsByIdentifier(identifier)) {
            unitDto.setMessage("Already exists");
            unitDto.setSuccess(false);
            return unitDto;
        }
        Unit unit = modelMapper.map(unitDto, Unit.class);
        unitRepository.save(unit);
        return unitDto;
    }

    @Override
    public WsDto<UnitDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<UnitDto>>() {
        }.getType();
        Page<Unit> unitPage = unitRepository.findAll(pageable);
        WsDto<UnitDto> unitWsDto = new WsDto<>();
        unitWsDto.setDtoList(modelMapper.map(unitPage.getContent(), listType));
        unitWsDto.setTotalRecords(unitPage.getTotalElements());
        unitWsDto.setTotalPages(unitPage.getTotalPages());
        unitWsDto.setSizePerPage(pageable.getPageSize());
        unitWsDto.setPage(pageable.getPageNumber());

        return unitWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        unitRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public UnitDto findByIdentifier(String identifier) {
        Unit unit = unitRepository.findByIdentifier(identifier);
        return modelMapper.map(unit, UnitDto.class);
    }

    @Override
    public UnitDto update(UnitDto unitDto) {
        Unit unit = unitRepository.findByIdentifier(unitDto.getIdentifier());
        modelMapper.map(unitDto, unit);
        unitRepository.save(unit);
        return unitDto;
    }

    @Override
    public UnitDto updateStatus(String identifier, boolean status) {
        Unit unit = unitRepository.findByIdentifier(identifier);
        unit.setStatus(status);
        unitRepository.save(unit);
        return modelMapper.map(unit, UnitDto.class);
    }

    @Override
    public List<UnitDto> findAllActive() {
        Type listType = new TypeToken<List<UnitDto>>() {
        }.getType();
        return modelMapper.map(unitRepository.findByStatus(true), listType);
    }
}
