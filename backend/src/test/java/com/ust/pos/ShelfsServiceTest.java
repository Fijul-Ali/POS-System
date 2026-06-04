package com.ust.pos;

import com.ust.pos.dto.ShelfsDto;
import com.ust.pos.model.Shelfs;
import com.ust.pos.model.ShelfsRepository;
import com.ust.pos.shelfs.service.impl.ShelfsServiceImpl;
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
class ShelfsServiceTest {

    @Mock
    private ShelfsRepository shelfsRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ShelfsServiceImpl shelfsService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        ShelfsDto shelfsDto = new ShelfsDto();
        shelfsDto.setIdentifier("Admin");

        Shelfs shelfs = new Shelfs();
        Mockito.when(modelMapper.map(shelfsDto, Shelfs.class)).thenReturn(shelfs);
        Mockito.when(shelfsRepository.save(shelfs)).thenReturn(shelfs);
        Mockito.when(shelfsRepository.existsByIdentifier("Admin")).thenReturn(false);

        ShelfsDto response = shelfsService.save(shelfsDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        ShelfsDto shelfsDto = new ShelfsDto();
        shelfsDto.setIdentifier("Admin");

        Mockito.when(shelfsRepository.existsByIdentifier("Admin")).thenReturn(true);

        ShelfsDto response = shelfsService.save(shelfsDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Shelfs shelfs = new Shelfs();
        shelfs.setIdentifier("Admin");

        ShelfsDto shelfsDto = new ShelfsDto();
        shelfsDto.setIdentifier("Admin");

        Mockito.when(shelfsRepository.findByIdentifier("Admin")).thenReturn(shelfs);
        Mockito.when(modelMapper.map(shelfs, ShelfsDto.class)).thenReturn(shelfsDto);

        ShelfsDto response = shelfsService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        ShelfsDto shelfsDto = new ShelfsDto();
        shelfsDto.setIdentifier("Admin");

        Shelfs shelfs = new Shelfs();
        Mockito.when(shelfsRepository.findByIdentifier("Admin")).thenReturn(shelfs);

        ShelfsDto response = shelfsService.update(shelfsDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = shelfsService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Shelfs shelfs = new Shelfs();
        shelfs.setIdentifier("Admin");

        ShelfsDto shelfsDto = new ShelfsDto();
        shelfsDto.setIdentifier("Admin");

        List<Shelfs> shelfss = List.of(shelfs);
        List<ShelfsDto> shelfsDtos = List.of(shelfsDto);

        Page<Shelfs> shelfsPage = new PageImpl<>(shelfss, PageRequest.of(0, 2), shelfss.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(shelfsRepository.findAll(pageable)).thenReturn(shelfsPage);
        Mockito.when(modelMapper.map(Mockito.eq(shelfss), Mockito.any(java.lang.reflect.Type.class))).thenReturn(shelfsDtos);

        List<ShelfsDto> response = shelfsService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        ShelfsDto shelfsDto = new ShelfsDto();
        shelfsDto.setIdentifier("Admin");
        shelfsDto.setStatus(true);

        Shelfs shelfs = new Shelfs();
        Mockito.when(shelfsRepository.findByIdentifier("Admin")).thenReturn(shelfs);
        Mockito.when(modelMapper.map(shelfs, ShelfsDto.class)).thenReturn(shelfsDto);
        ShelfsDto response = shelfsService.statusUpdate("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Shelfs shelfs = new Shelfs();
        shelfs.setIdentifier("Admin");

        ShelfsDto shelfsDto = new ShelfsDto();
        shelfsDto.setIdentifier("Admin");

        List<Shelfs> shelfss = List.of(shelfs);
        List<ShelfsDto> shelfsDtos = List.of(shelfsDto);

        Mockito.when(shelfsRepository.findByStatus(true)).thenReturn(shelfss);
        Mockito.when(modelMapper.map(Mockito.eq(shelfss), Mockito.any(java.lang.reflect.Type.class))).thenReturn(shelfsDtos);

        List<ShelfsDto> response = shelfsService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}