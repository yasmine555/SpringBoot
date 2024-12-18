package com.example.ProjetSpringGestionDocuments.business.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;

public interface CategoryService {
    Page<Category> getAllCategoriesPaginated(Pageable pageable);
    Category saveCategory(Category category);
    Category getCategoryById(Long id);
    void deleteCategory(Long id);
    List<Category> getAllCategories();
    Page<Category> searchCategoriesByNamePaginated(String name, Pageable pageable);
    
}