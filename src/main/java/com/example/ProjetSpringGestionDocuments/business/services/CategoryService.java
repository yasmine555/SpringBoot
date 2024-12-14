package com.example.ProjetSpringGestionDocuments.business.services;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<Category> getAllCategoriesPaginated(Pageable pageable);
    Category saveCategory(Category category);
    Category getCategoryById(Long id);
    void deleteCategory(Long id);
}