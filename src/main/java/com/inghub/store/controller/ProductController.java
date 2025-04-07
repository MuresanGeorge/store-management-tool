package com.inghub.store.controller;

import com.inghub.store.dto.ProductDto;
import com.inghub.store.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger LOGGER = LogManager.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(summary = "Adding a new product", description = "Returns 201 CREATED and the new product")
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid ProductDto productDto) {
        LOGGER.info("Adding the product ... Product = {}", productDto.toString());
        ProductDto product = productService.addProduct(productDto);
        ResponseEntity<ProductDto> response = new ResponseEntity<>(product, HttpStatusCode.valueOf(201));
        LOGGER.info("Status code: {} . Response: {}", response.getStatusCode(), response.getBody());
        return response;
    }

    @Operation(summary = "Getting products of a speciif category", description = "Returns 200 OK and the list of products")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long categoryId) {
        LOGGER.info("Getting products ... categoryId = {}", categoryId);
        List<ProductDto> products = productService.getProductsByCategory(categoryId);
        ResponseEntity<List<ProductDto>> response = new ResponseEntity<>(products, HttpStatusCode.valueOf(200));
        LOGGER.info("Status code: {} . Response: {}", response.getStatusCode(), response.getBody());
        return response;
    }

    @Operation(summary = "Getting a product ", description = "Returns 200 OK and the product")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
        LOGGER.info("Getting product ... productId = {}", productId);
        ProductDto product = productService.getProduct(productId);
        ResponseEntity<ProductDto> response = new ResponseEntity<>(product, HttpStatusCode.valueOf(200));
        LOGGER.info("Status code: {} . Response: {}", response.getStatusCode(), response.getBody());
        return response;
    }
}
