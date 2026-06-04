package com.ust.pos;

import com.ust.pos.dto.UnitDto;
import com.ust.pos.model.Unit;
import com.ust.pos.model.UnitRepository;
import com.ust.pos.unit.service.impl.UnitServiceImpl;
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
class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UnitServiceImpl unitService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        UnitDto unitDto = new UnitDto();
        unitDto.setIdentifier("Admin");

        Unit unit = new Unit();
        Mockito.when(modelMapper.map(unitDto, Unit.class)).thenReturn(unit);
        Mockito.when(unitRepository.save(unit)).thenReturn(unit);
        Mockito.when(unitRepository.existsByIdentifier("Admin")).thenReturn(false);

        UnitDto response = unitService.save(unitDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        UnitDto unitDto = new UnitDto();
        unitDto.setIdentifier("Admin");

        Mockito.when(unitRepository.existsByIdentifier("Admin")).thenReturn(true);

        UnitDto response = unitService.save(unitDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Unit unit = new Unit();
        unit.setIdentifier("Admin");

        UnitDto unitDto = new UnitDto();
        unitDto.setIdentifier("Admin");

        Mockito.when(unitRepository.findByIdentifier("Admin")).thenReturn(unit);
        Mockito.when(modelMapper.map(unit, UnitDto.class)).thenReturn(unitDto);

        UnitDto response = unitService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        UnitDto unitDto = new UnitDto();
        unitDto.setIdentifier("Admin");

        Unit unit = new Unit();
        Mockito.when(unitRepository.findByIdentifier("Admin")).thenReturn(unit);

        UnitDto response = unitService.update(unitDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = unitService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Unit unit = new Unit();
        unit.setIdentifier("Admin");

        UnitDto unitDto = new UnitDto();
        unitDto.setIdentifier("Admin");

        List<Unit> units = List.of(unit);
        List<UnitDto> unitDtos = List.of(unitDto);

        Page<Unit> unitPage = new PageImpl<>(units, PageRequest.of(0, 2), units.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(unitRepository.findAll(pageable)).thenReturn(unitPage);
        Mockito.when(modelMapper.map(Mockito.eq(units), Mockito.any(java.lang.reflect.Type.class))).thenReturn(unitDtos);

        List<UnitDto> response = unitService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        UnitDto unitDto = new UnitDto();
        unitDto.setIdentifier("Admin");
        unitDto.setStatus(true);

        Unit unit = new Unit();
        Mockito.when(unitRepository.findByIdentifier("Admin")).thenReturn(unit);
        Mockito.when(modelMapper.map(unit, UnitDto.class)).thenReturn(unitDto);
        UnitDto response = unitService.updateStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Unit unit = new Unit();
        unit.setIdentifier("Admin");

        UnitDto unitDto = new UnitDto();
        unitDto.setIdentifier("Admin");

        List<Unit> units = List.of(unit);
        List<UnitDto> unitDtos = List.of(unitDto);

        Mockito.when(unitRepository.findByStatus(true)).thenReturn(units);
        Mockito.when(modelMapper.map(Mockito.eq(units), Mockito.any(java.lang.reflect.Type.class))).thenReturn(unitDtos);

        List<UnitDto> response = unitService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}