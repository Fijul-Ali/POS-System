package com.ust.pos;

import com.ust.pos.dto.RacksDto;
import com.ust.pos.model.Racks;
import com.ust.pos.model.RacksRepository;
import com.ust.pos.racks.service.impl.RacksServiceImpl;
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
class RacksServiceTest {

    @Mock
    private RacksRepository racksRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RacksServiceImpl racksService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        RacksDto racksDto = new RacksDto();
        racksDto.setIdentifier("Admin");

        Racks racks = new Racks();
        Mockito.when(modelMapper.map(racksDto, Racks.class)).thenReturn(racks);
        Mockito.when(racksRepository.save(racks)).thenReturn(racks);
        Mockito.when(racksRepository.existsByIdentifier("Admin")).thenReturn(false);

        RacksDto response = racksService.save(racksDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        RacksDto racksDto = new RacksDto();
        racksDto.setIdentifier("Admin");

        Mockito.when(racksRepository.existsByIdentifier("Admin")).thenReturn(true);

        RacksDto response = racksService.save(racksDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Racks racks = new Racks();
        racks.setIdentifier("Admin");

        RacksDto racksDto = new RacksDto();
        racksDto.setIdentifier("Admin");

        Mockito.when(racksRepository.findByIdentifier("Admin")).thenReturn(racks);
        Mockito.when(modelMapper.map(racks, RacksDto.class)).thenReturn(racksDto);

        RacksDto response = racksService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        RacksDto racksDto = new RacksDto();
        racksDto.setIdentifier("Admin");

        Racks racks = new Racks();
        Mockito.when(racksRepository.findByIdentifier("Admin")).thenReturn(racks);

        RacksDto response = racksService.update(racksDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = racksService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Racks racks = new Racks();
        racks.setIdentifier("Admin");

        RacksDto racksDto = new RacksDto();
        racksDto.setIdentifier("Admin");

        List<Racks> rackss = List.of(racks);
        List<RacksDto> racksDtos = List.of(racksDto);

        Page<Racks> racksPage = new PageImpl<>(rackss, PageRequest.of(0, 2), rackss.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(racksRepository.findAll(pageable)).thenReturn(racksPage);
        Mockito.when(modelMapper.map(Mockito.eq(rackss), Mockito.any(java.lang.reflect.Type.class))).thenReturn(racksDtos);

        List<RacksDto> response = racksService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        RacksDto racksDto = new RacksDto();
        racksDto.setIdentifier("Admin");
        racksDto.setStatus(true);

        Racks racks = new Racks();
        Mockito.when(racksRepository.findByIdentifier("Admin")).thenReturn(racks);
        Mockito.when(modelMapper.map(racks, RacksDto.class)).thenReturn(racksDto);
        RacksDto response = racksService.changeStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Racks racks = new Racks();
        racks.setIdentifier("Admin");

        RacksDto racksDto = new RacksDto();
        racksDto.setIdentifier("Admin");

        List<Racks> rackss = List.of(racks);
        List<RacksDto> racksDtos = List.of(racksDto);

        Mockito.when(racksRepository.findByStatus(true)).thenReturn(rackss);
        Mockito.when(modelMapper.map(Mockito.eq(rackss), Mockito.any(java.lang.reflect.Type.class))).thenReturn(racksDtos);

        List<RacksDto> response = racksService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}