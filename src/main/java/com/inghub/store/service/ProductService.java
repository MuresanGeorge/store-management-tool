package com.inghub.store.service;

import com.inghub.store.converter.ProductConverter;
import com.inghub.store.dto.ProductDto;
import com.inghub.store.exception.DuplicateNameException;
import com.inghub.store.model.Category;
import com.inghub.store.model.Inventory;
import com.inghub.store.model.Product;
import com.inghub.store.repository.CategoryRepository;
import com.inghub.store.repository.InventoryRepository;
import com.inghub.store.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    public ProductService(ProductRepository productRepository, ProductConverter productConverter, CategoryRepository categoryRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public ProductDto add(ProductDto productDto) {

        if (productRepository.findByName(productDto.getName()).isPresent()) {
            throw new DuplicateNameException("There is another product with the same name");
        }

        Product productToBeAdded = productConverter.convertFromDto(productDto);
        Category categoryOfProduct = categoryRepository.findByName(productDto.getCategoryName()).orElseThrow(() -> new EntityNotFoundException("Category with name " + productDto.getCategoryName() + " not found"));
        productToBeAdded.setCategory(categoryOfProduct);
        Product productToBeReturned = productRepository.save(productToBeAdded);

        Inventory productInventory = new Inventory();
        productInventory.setStock(productDto.getStock());
        productInventory.setProduct(productToBeReturned);
        inventoryRepository.save(productInventory);

        return productConverter.convertToDto(productToBeReturned);
    }

}
