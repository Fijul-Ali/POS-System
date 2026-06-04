package com.ust.pos;

import com.ust.pos.dto.ProductDto;
import com.ust.pos.model.Product;
import com.ust.pos.model.ProductRepository;
import com.ust.pos.product.service.impl.ProductServiceImpl;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    /* ===================== SAVE ===================== */

    @Test
    void saveTest() {
        ProductDto productDto = new ProductDto();
        productDto.setIdentifier("Admin");

        Product product = new Product();
        Mockito.when(modelMapper.map(productDto, Product.class)).thenReturn(product);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(productRepository.existsByIdentifier("Admin")).thenReturn(false);

        ProductDto response = productService.save(productDto);

        Assertions.assertEquals("Admin", response.getIdentifier());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void saveTestFailure() {
        ProductDto productDto = new ProductDto();
        productDto.setIdentifier("Admin");

        Mockito.when(productRepository.existsByIdentifier("Admin")).thenReturn(true);

        ProductDto response = productService.save(productDto);

        Assertions.assertFalse(response.isSuccess());
    }

    /* ===================== FIND BY IDENTIFIER ===================== */

    @Test
    void findByIdentifierTest() {
        Product product = new Product();
        product.setIdentifier("Admin");

        ProductDto productDto = new ProductDto();
        productDto.setIdentifier("Admin");

        Mockito.when(productRepository.findByIdentifier("Admin")).thenReturn(product);
        Mockito.when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);

        ProductDto response = productService.findByIdentifier("Admin");

        Assertions.assertEquals("Admin", response.getIdentifier());
    }

    /* ===================== UPDATE ===================== */

    @Test
    void updateTest() {
        ProductDto productDto = new ProductDto();
        productDto.setIdentifier("Admin");

        Product product = new Product();
        Mockito.when(productRepository.findByIdentifier("Admin")).thenReturn(product);

        ProductDto response = productService.update(productDto);
        Assertions.assertTrue(response.isSuccess());
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteTest() {

        boolean response = productService.delete("Admin");

        Assertions.assertTrue(response);
    }

    /* ===================== FIND ALL ===================== */

    @Test
    void findAllTest() {
        Product product = new Product();
        product.setIdentifier("Admin");

        ProductDto productDto = new ProductDto();
        productDto.setIdentifier("Admin");

        List<Product> products = List.of(product);
        List<ProductDto> productDtos = List.of(productDto);

        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 2), products.size());

        Pageable pageable = PageRequest.of(0, 50, Sort.by(new ArrayList<>()));

        Mockito.when(productRepository.findAll(pageable)).thenReturn(productPage);
        Mockito.when(modelMapper.map(Mockito.eq(products), Mockito.any(java.lang.reflect.Type.class))).thenReturn(productDtos);

        List<ProductDto> response = productService.findAll(pageable);

        Assertions.assertEquals(1, response.size());
    }

    @Test
    void updateStatusTest() {
        ProductDto productDto = new ProductDto();
        productDto.setIdentifier("Admin");
        productDto.setStatus(true);

        Product product = new Product();
        Mockito.when(productRepository.findByIdentifier("Admin")).thenReturn(product);
        Mockito.when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);
        ProductDto response = productService.updateStatus("Admin", true);

        Assertions.assertTrue(response.isStatus());
    }

    @Test
    void findAllActiveTest() {
        Product product = new Product();
        product.setIdentifier("Admin");

        ProductDto productDto = new ProductDto();
        productDto.setIdentifier("Admin");

        List<Product> products = List.of(product);
        List<ProductDto> productDtos = List.of(productDto);

        Mockito.when(productRepository.findByStatus(true)).thenReturn(products);
        Mockito.when(modelMapper.map(Mockito.eq(products), Mockito.any(java.lang.reflect.Type.class))).thenReturn(productDtos);

        List<ProductDto> response = productService.findAllActive();

        Assertions.assertEquals(1, response.size());
    }
}