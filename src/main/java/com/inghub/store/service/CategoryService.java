package com.inghub.store.service;

import com.inghub.store.dto.CategoryDto;
import com.inghub.store.exception.DuplicateNameException;
import com.inghub.store.model.Category;
import com.inghub.store.repository.CategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private static final Logger LOGGER = LogManager.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.name()).isPresent()) {
            LOGGER.info("log for signaling the issue a...");
            throw new DuplicateNameException("Category with name " + categoryDto.name() + " exist");
        }
        Category categoryToBeAdded = new Category();
        categoryToBeAdded.setDescription(categoryDto.description());
        categoryToBeAdded.setName(categoryDto.name());
        Category categoryToBeReturned = categoryRepository.save(categoryToBeAdded);

        return new CategoryDto(categoryToBeReturned.getName(), categoryToBeReturned.getDescription());
    }
    //TODO: continue here
}
