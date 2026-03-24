package com.eventhub.api.service;

import com.eventhub.api.dto.CategoryDTO;
import com.eventhub.api.exception.DuplicateResourceException;
import com.eventhub.api.exception.ResourceNotFoundException;
import com.eventhub.api.model.Category;
import com.eventhub.api.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Cacheable("categories")
    public List<CategoryDTO> getAllCategories() {
        log.info("Fetching all categories from DB");
        return categoryRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Cacheable(value = "category", key = "#id")
    public CategoryDTO getCategoryById(Long id) {
        log.info("Fetching category with id={}", id);
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return toDTO(cat);
    }

    @CacheEvict(value = { "categories", "category" }, allEntries = true)
    public CategoryDTO createCategory(CategoryDTO dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new DuplicateResourceException("Category already exists: " + dto.getName());
        }
        Category cat = new Category();
        cat.setName(dto.getName());
        cat.setDescription(dto.getDescription());
        Category saved = categoryRepository.save(cat);
        log.info("Created category id={}", saved.getId());
        return toDTO(saved);
    }

    @CacheEvict(value = { "categories", "category" }, allEntries = true)
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        cat.setName(dto.getName());
        cat.setDescription(dto.getDescription());
        return toDTO(categoryRepository.save(cat));
    }

    @CacheEvict(value = { "categories", "category" }, allEntries = true)
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
        log.info("Deleted category id={}", id);
    }

    public Category getCategoryEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private CategoryDTO toDTO(Category cat) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(cat.getId());
        dto.setName(cat.getName());
        dto.setDescription(cat.getDescription());
        return dto;
    }
}
