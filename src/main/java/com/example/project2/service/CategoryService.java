package com.example.project2.service;

import com.example.project2.model.CategoryModel;
import com.example.project2.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryModel> findAllSorted(String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "name");
        return categoryRepository.findAllByDeletedFalse(sort);
    }

    public List<CategoryModel> searchByName(String name, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "name");
        return categoryRepository.findByNameContainingAndDeletedFalse(name, sort);
    }

    public List<CategoryModel> findAllCategories() {
        return categoryRepository.findAll();
    }

    public List<CategoryModel> findActiveCategories() {
        return categoryRepository.findByDeletedFalse();
    }

    public List<CategoryModel> findAll() {
        return categoryRepository.findAll();
    }

    public CategoryModel findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public CategoryModel save(CategoryModel category) {
        return categoryRepository.save(category);
    }

    // Поиск по названию
    public List<CategoryModel> searchByName(String name) {
        return categoryRepository.findByNameContainingAndDeletedFalse(name);
    }

    // Логическое удаление
    public void softDeleteById(Long id) {
        CategoryModel category = findById(id);
        if (category != null) {
            category.setDeleted(true);  // Логическое удаление
            categoryRepository.save(category);  // Сохранение изменений
        }
    }

    // Восстановление логически удаленной категории
    public void restoreById(Long id) {
        CategoryModel category = findById(id);
        if (category != null && category.isDeleted()) {
            category.setDeleted(false);  // Восстановление
            categoryRepository.save(category);  // Сохранение изменений
        }
    }

    // Физическое удаление
    public void hardDeleteById(Long id) {
        categoryRepository.deleteById(id);  // Физическое удаление из БД
    }


    public List<CategoryModel> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<CategoryModel> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public CategoryModel createCategory(CategoryModel category) {
        return categoryRepository.save(category);
    }

    public CategoryModel updateCategory(Long id, CategoryModel categoryDetails) {
        Optional<CategoryModel> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            CategoryModel category = categoryOptional.get();
            category.setName(categoryDetails.getName());
            category.setDeleted(categoryDetails.isDeleted());
            return categoryRepository.save(category);
        }
        return null;
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
