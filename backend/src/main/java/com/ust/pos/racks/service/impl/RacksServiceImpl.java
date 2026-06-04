package com.ust.pos.racks.service.impl;

import com.ust.pos.dto.RacksDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Racks;
import com.ust.pos.model.RacksRepository;
import com.ust.pos.racks.service.RacksService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class RacksServiceImpl implements RacksService {
    @Autowired
    RacksRepository racksRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public RacksDto save(RacksDto racksDto) {
        racksDto.setIdentifier(racksDto.getIdentifier().trim());
        String identifier = racksDto.getIdentifier();
        if (racksRepository.existsByIdentifier(identifier)) {
            racksDto.setMessage("Already exists");
            racksDto.setSuccess(false);
            return racksDto;
        }
        Racks racks = modelMapper.map(racksDto, Racks.class);
        racksRepository.save(racks);
        return racksDto;
    }

    @Override
    public WsDto<RacksDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<RacksDto>>() {
        }.getType();
        Page<Racks> racksPage = racksRepository.findAll(pageable);
        WsDto<RacksDto> racksWsDto = new WsDto<>();
        racksWsDto.setDtoList(modelMapper.map(racksPage.getContent(), listType));
        racksWsDto.setTotalRecords(racksPage.getTotalElements());
        racksWsDto.setTotalPages(racksPage.getTotalPages());
        racksWsDto.setSizePerPage(pageable.getPageSize());
        racksWsDto.setPage(pageable.getPageNumber());

        return racksWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        racksRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public RacksDto findByIdentifier(String identifier) {
        Racks racks = racksRepository.findByIdentifier(identifier);
        return modelMapper.map(racks, RacksDto.class);
    }

    @Override
    public RacksDto update(RacksDto racksDto) {
        Racks racks = racksRepository.findByIdentifier(racksDto.getIdentifier());
        modelMapper.map(racksDto, racks);
        racksRepository.save(racks);
        return racksDto;
    }

    @Override
    public List<RacksDto> findAllActive() {
        Type listType = new TypeToken<List<RacksDto>>() {
        }.getType();
        return modelMapper.map(racksRepository.findByStatus(true), listType);
    }

    @Override
    public RacksDto changeStatus(String identifier, boolean status) {
        Racks racks = racksRepository.findByIdentifier(identifier);
        racks.setStatus(status);
        racksRepository.save(racks);
        return modelMapper.map(racks, RacksDto.class);
    }
}
