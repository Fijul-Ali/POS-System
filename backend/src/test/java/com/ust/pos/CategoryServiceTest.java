package com.ust.pos;

import com.ust.pos.category.service.impl.CategoryServiceImpl;
import com.ust.pos.dto.CategoryDto;
import com.ust.pos.model.Category;
import com.ust.pos.model.CategoryRepository;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setIdentifier("Admin");

        Category category = new Category();
        Mockito.when(modelMapper.map(categoryDto, Category.class)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryRepository.existsByIdentifier("Admin")).thenReturn(false);

        CategoryDto response = categoryService.save(categoryDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setIdentifier("Admin");

        Mockito.when(categoryRepository.existsByIdentifier("Admin")).thenReturn(true);

        CategoryDto response = categoryService.save(categoryDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Category category = new Category();
        category.setIdentifier("Admin");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setIdentifier("Admin");

        Mockito.when(categoryRepository.findByIdentifier("Admin")).thenReturn(category);
        Mockito.when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);

        CategoryDto response = categoryService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setIdentifier("Admin");

        Category category = new Category();
        Mockito.when(categoryRepository.findByIdentifier("Admin")).thenReturn(category);

        CategoryDto response = categoryService.update(categoryDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = categoryService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Category category = new Category();
        category.setIdentifier("Admin");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setIdentifier("Admin");

        List<Category> categorys = List.of(category);
        List<CategoryDto> categoryDtos = List.of(categoryDto);

        Page<Category> userPage = new PageImpl<>(categorys, PageRequest.of(0, 2), categorys.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));
        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(userPage);
        Mockito.when(modelMapper.map(Mockito.eq(categorys), Mockito.any(java.lang.reflect.Type.class))).thenReturn(categoryDtos);

        List<CategoryDto> response = categoryService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setIdentifier("Admin");
        categoryDto.setStatus(true);

        Category category = new Category();
        Mockito.when(categoryRepository.findByIdentifier("Admin")).thenReturn(category);
        Mockito.when(modelMapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        CategoryDto response = categoryService.updateStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Category category = new Category();
        category.setIdentifier("Admin");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setIdentifier("Admin");

        List<Category> categorys = List.of(category);
        List<CategoryDto> categoryDtos = List.of(categoryDto);

        Mockito.when(categoryRepository.findByStatus(true)).thenReturn(categorys);
        Mockito.when(modelMapper.map(Mockito.eq(categorys), Mockito.any(java.lang.reflect.Type.class))).thenReturn(categoryDtos);

        List<CategoryDto> response = categoryService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void findAllActiveChildTest() {
        Category category = new Category();
        category.setIdentifier("Admin");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setIdentifier("Admin");

        List<Category> categorys = List.of(category);
        List<CategoryDto> categoryDtos = List.of(categoryDto);

        Mockito.when(categoryRepository.findByStatusTrueAndSuperCategoryIsNot("")).thenReturn(categorys);
        Mockito.when(modelMapper.map(Mockito.eq(categorys), Mockito.any(java.lang.reflect.Type.class))).thenReturn(categoryDtos);

        List<CategoryDto> response = categoryService.findAllActiveChilds();

        Assertions.assertEquals(1, response.size());
    }
}