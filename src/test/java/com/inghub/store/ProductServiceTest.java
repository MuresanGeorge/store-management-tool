package com.inghub.store;

import com.inghub.store.converter.ProductConverter;
import com.inghub.store.dto.ProductDto;
import com.inghub.store.exception.DuplicateNameException;
import com.inghub.store.model.Category;
import com.inghub.store.model.Inventory;
import com.inghub.store.model.Product;
import com.inghub.store.repository.CategoryRepository;
import com.inghub.store.repository.InventoryRepository;
import com.inghub.store.repository.ProductRepository;
import com.inghub.store.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductConverter productConverter;
    private CategoryRepository categoryRepository;
    private InventoryRepository inventoryRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productConverter = mock(ProductConverter.class);
        categoryRepository = mock(CategoryRepository.class);
        inventoryRepository = mock(InventoryRepository.class);
        productService = new ProductService(productRepository, productConverter, categoryRepository, inventoryRepository);
    }

    @Test
    void shouldAddProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TestName");
        productDto.setCategoryName("TestCategory");
        productDto.setPrice(BigDecimal.valueOf(100.0));
        productDto.setStock(10);

        Product productToBeAdded = new Product();
        productToBeAdded.setName("TestName");

        Category category = new Category();
        category.setName("TestCategory");

        Inventory inventoryToBeAdded = new Inventory();
        inventoryToBeAdded.setStock(10);

        Product productToBeReturned = new Product();
        productToBeReturned.setId(1L);
        productToBeReturned.setName("TestName");
        productToBeReturned.setCategory(category);

        Inventory inventoryReturned = new Inventory();
        inventoryReturned.setId(1L);
        inventoryReturned.setStock(10);

        when(productRepository.findByName("TestName")).thenReturn(Optional.empty());
        when(productConverter.convertFromDto(productDto)).thenReturn(productToBeAdded);
        when(categoryRepository.findByName("TestCategory")).thenReturn(Optional.of(category));
        when(productRepository.save(productToBeAdded)).thenReturn(productToBeReturned);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventoryReturned);
        when(productConverter.convertToDto(productToBeReturned)).thenReturn(productDto);

        ProductDto toBeReturned = productService.addProduct(productDto);

        assertNotNull(toBeReturned);
        assertEquals("TestName", toBeReturned.getName());
        assertEquals("TestCategory", toBeReturned.getCategoryName());
        assertEquals(10, toBeReturned.getStock());
        verify(productRepository, times(1)).save(productToBeAdded);

    }

    @Test
    void shouldThrowDuplicateNameException_when_addProductTwice() {
        ProductDto productDto = new ProductDto();
        productDto.setName("existingName");

        when(productRepository.findByName("existingName")).thenReturn(Optional.of(new Product()));

        assertThrows(DuplicateNameException.class, () -> productService.addProduct(productDto));
        verify(productRepository, times(1)).findByName("existingName");
    }

    @Test
    void shouldThrowCategoryNotFound_when_addProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("test");
        productDto.setCategoryName("category");
        Product productToBeAdded = new Product();
        productToBeAdded.setName("test");

        when(productRepository.findByName("test")).thenReturn(Optional.empty());
        when(productConverter.convertFromDto(productDto)).thenReturn(productToBeAdded);
        when(categoryRepository.findByName("category")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.addProduct(productDto);
        });
        assertEquals("Category with name " + productDto.getCategoryName() + " not found", exception.getMessage());
        verify(categoryRepository, times(1)).findByName("category");
    }

    @Test
    void shouldGetProduct() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("TestName");
        Category category = new Category();
        category.setName("TestCategory");
        product.setCategory(category);
        ProductDto productDto = new ProductDto();
        productDto.setName("TestName");
        productDto.setCategoryName("TestCategory");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productConverter.convertToDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProduct(productId);

        assertNotNull(result);
        assertEquals("TestName", result.getName());
        assertEquals("TestCategory", result.getCategoryName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void shouldThrowEntityNotFound_when_getProductById() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getProduct(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void shouldGetProductsByCategory() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setName("TestCategory");
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setCategory(category);
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product2");
        product2.setCategory(category);
        List<Product> products = Arrays.asList(product1, product2);
        ProductDto productDto1 = new ProductDto();
        productDto1.setName("Product1");
        productDto1.setCategoryName("TestCategory");
        ProductDto productDto2 = new ProductDto();
        productDto2.setName("Product2");
        productDto2.setCategoryName("TestCategory");
        List<ProductDto> listToBeReturned = Arrays.asList(productDto1, productDto2);

        when(productRepository.findByCategoryId(categoryId)).thenReturn(products);
        when(productConverter.convertToDto(product1)).thenReturn(productDto1);
        when(productConverter.convertToDto(product2)).thenReturn(productDto2);

        List<ProductDto> result = productService.getProductsByCategory(categoryId);

        assertNotNull(result);
        assertEquals(listToBeReturned.size(), result.size());
        assertEquals(listToBeReturned.get(0).getName(), result.get(0).getName());
        assertEquals(listToBeReturned.get(0).getCategoryName(), result.get(0).getCategoryName());
        verify(productRepository, times(1)).findByCategoryId(categoryId);
        verify(productConverter, times(2)).convertToDto(any(Product.class));
    }

    @Test
    void shouldGetEmptyList_when_getProductByCategoryNotFound() {
        Long categoryId = 1L;
        List<Product> products = List.of();
        when(productRepository.findByCategoryId(categoryId)).thenReturn(products);

        List<ProductDto> result = productService.getProductsByCategory(categoryId);

        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByCategoryId(categoryId);
    }
}
