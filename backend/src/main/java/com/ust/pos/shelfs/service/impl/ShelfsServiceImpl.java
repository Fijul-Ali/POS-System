package com.ust.pos.shelfs.service.impl;

import com.ust.pos.dto.ShelfsDto;
import com.ust.pos.dto.WsDto;
import com.ust.pos.model.Shelfs;
import com.ust.pos.model.ShelfsRepository;
import com.ust.pos.shelfs.service.ShelfsService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ShelfsServiceImpl implements ShelfsService {
    @Autowired
    ShelfsRepository shelfsRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ShelfsDto save(ShelfsDto shelfsDto) {
        shelfsDto.setIdentifier(shelfsDto.getIdentifier().trim());
        String identifier = shelfsDto.getIdentifier();
        if (shelfsRepository.existsByIdentifier(identifier)) {
            shelfsDto.setMessage("Already exists");
            shelfsDto.setSuccess(false);
            return shelfsDto;
        }
        Shelfs shelfs = modelMapper.map(shelfsDto, Shelfs.class);
        shelfsRepository.save(shelfs);
        return shelfsDto;
    }

    @Override
    public WsDto<ShelfsDto> findAll(Pageable pageable) {
        Type listType = new TypeToken<List<ShelfsDto>>() {
        }.getType();
        Page<Shelfs> shelfsPage = shelfsRepository.findAll(pageable);
        WsDto<ShelfsDto> shelfsWsDto = new WsDto<>();
        shelfsWsDto.setDtoList(modelMapper.map(shelfsPage.getContent(), listType));
        shelfsWsDto.setTotalRecords(shelfsPage.getTotalElements());
        shelfsWsDto.setTotalPages(shelfsPage.getTotalPages());
        shelfsWsDto.setSizePerPage(pageable.getPageSize());
        shelfsWsDto.setPage(pageable.getPageNumber());

        return shelfsWsDto;
    }

    @Override
    public boolean delete(String identifier) {
        shelfsRepository.deleteByIdentifier(identifier);
        return true;
    }

    @Override
    public ShelfsDto findByIdentifier(String identifier) {
        Shelfs shelfs = shelfsRepository.findByIdentifier(identifier);
        return modelMapper.map(shelfs, ShelfsDto.class);
    }

    @Override
    public ShelfsDto update(ShelfsDto shelfsDto) {
        Shelfs shelfs = shelfsRepository.findByIdentifier(shelfsDto.getIdentifier());
        modelMapper.map(shelfsDto, shelfs);
        shelfs.setStatus(shelfsDto.isStatus());
        shelfsRepository.save(shelfs);
        return shelfsDto;
    }

    @Override
    public ShelfsDto statusUpdate(String identifier, boolean status) {
        Shelfs shelfs = shelfsRepository.findByIdentifier(identifier);
        shelfs.setStatus(status);
        shelfsRepository.save(shelfs);
        return modelMapper.map(shelfs, ShelfsDto.class);
    }

    @Override
    public List<ShelfsDto> findAllActive() {
        Type listType = new TypeToken<List<ShelfsDto>>() {
        }.getType();
        return modelMapper.map(shelfsRepository.findByStatus(true), listType);
    }

    @Override
    public List<ShelfsDto> findAllForHome() {
        Type listType = new TypeToken<List<ShelfsDto>>() {
        }.getType();
        return modelMapper.map(shelfsRepository.findAll(), listType);
    }
}
