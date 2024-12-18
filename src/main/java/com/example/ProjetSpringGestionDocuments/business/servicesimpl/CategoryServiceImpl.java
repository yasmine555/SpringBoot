package com.example.ProjetSpringGestionDocuments.business.servicesimpl;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.CategoryRepository;
import com.example.ProjetSpringGestionDocuments.business.services.CategoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    

    @Override
    public Page<Category> getAllCategoriesPaginated(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    public Page<Category> searchCategoriesByNamePaginated(String name, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}