package com.ust.pos;

import com.ust.pos.brand.service.impl.BrandServiceImpl;
import com.ust.pos.dto.BrandDto;
import com.ust.pos.model.Brand;
import com.ust.pos.model.BrandRepository;
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
class CartServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        BrandDto brandDto = new BrandDto();
        brandDto.setIdentifier("Admin");

        Brand brand = new Brand();
        Mockito.when(modelMapper.map(brandDto, Brand.class)).thenReturn(brand);
        Mockito.when(brandRepository.save(brand)).thenReturn(brand);
        Mockito.when(brandRepository.existsByIdentifier("Admin")).thenReturn(false);

        BrandDto response = brandService.save(brandDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        BrandDto brandDto = new BrandDto();
        brandDto.setIdentifier("Admin");

        Mockito.when(brandRepository.existsByIdentifier("Admin")).thenReturn(true);

        BrandDto response = brandService.save(brandDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Brand brand = new Brand();
        brand.setIdentifier("Admin");

        BrandDto brandDto = new BrandDto();
        brandDto.setIdentifier("Admin");

        Mockito.when(brandRepository.findByIdentifier("Admin")).thenReturn(brand);
        Mockito.when(modelMapper.map(brand, BrandDto.class)).thenReturn(brandDto);

        BrandDto response = brandService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        BrandDto brandDto = new BrandDto();
        brandDto.setIdentifier("Admin");

        Brand brand = new Brand();
        Mockito.when(brandRepository.findByIdentifier("Admin")).thenReturn(brand);

        BrandDto response = brandService.update(brandDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = brandService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Brand brand = new Brand();
        brand.setIdentifier("Admin");

        BrandDto brandDto = new BrandDto();
        brandDto.setIdentifier("Admin");

        List<Brand> brands = List.of(brand);
        List<BrandDto> brandDtos = List.of(brandDto);

        Page<Brand> brandPage = new PageImpl<>(brands, PageRequest.of(0, 2), brands.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(brandRepository.findAll(pageable)).thenReturn(brandPage);
        Mockito.when(modelMapper.map(Mockito.eq(brands), Mockito.any(java.lang.reflect.Type.class))).thenReturn(brandDtos);

        List<BrandDto> response = brandService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        BrandDto brandDto = new BrandDto();
        brandDto.setIdentifier("Admin");
        brandDto.setStatus(true);

        Brand brand = new Brand();
        Mockito.when(brandRepository.findByIdentifier("Admin")).thenReturn(brand);
        Mockito.when(modelMapper.map(brand, BrandDto.class)).thenReturn(brandDto);
        BrandDto response = brandService.changeBrandStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Brand brand = new Brand();
        brand.setIdentifier("Admin");

        BrandDto brandDto = new BrandDto();
        brandDto.setIdentifier("Admin");

        List<Brand> brands = List.of(brand);
        List<BrandDto> brandDtos = List.of(brandDto);

        Mockito.when(brandRepository.findByStatus(true)).thenReturn(brands);
        Mockito.when(modelMapper.map(Mockito.eq(brands), Mockito.any(java.lang.reflect.Type.class))).thenReturn(brandDtos);

        List<BrandDto> response = brandService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}