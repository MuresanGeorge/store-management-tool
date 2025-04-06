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

import java.util.List;
import java.util.Optional;

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
    public ProductDto addProduct(ProductDto productDto) {

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

    public ProductDto getProduct(Long id) {
        Product productToBeReturned = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " not found"));
        return productConverter.convertToDto(productToBeReturned);
    }

    public List<ProductDto> getProductsByCategory(Long categoryId) {
        List<Product> productsToBeReturned = productRepository.findByCategoryId(categoryId);
        return productsToBeReturned.stream().map(productConverter::convertToDto).toList();
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        Product productToBeUpdated = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
        Category category = categoryRepository.findByName(productDto.getCategoryName()).orElseThrow(() -> new EntityNotFoundException("Category with name " + productDto.getCategoryName() + " not found"));
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(() -> new EntityNotFoundException("Inventory for product " + productToBeUpdated.getName() + " not found"));

        productToBeUpdated.setCategory(category);
        productToBeUpdated.setPrice(productDto.getPrice());
        productToBeUpdated.setName(productDto.getName());
        productToBeUpdated.setDescription(productDto.getDescription());
        Optional.ofNullable(productDto.getImageUrl()).ifPresent(productToBeUpdated::setImageUrl);
        inventory.setStock(productDto.getStock());

        inventoryRepository.save(inventory);
        Product productToBeReturned = productRepository.save(productToBeUpdated);
        return productConverter.convertToDto(productToBeReturned);
    }

}
