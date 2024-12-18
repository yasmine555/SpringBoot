package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;
import com.example.ProjetSpringGestionDocuments.business.services.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Liste des categories
    @GetMapping
    public String listCategories(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "4") int pageSize,
    @RequestParam(required = false) String searchName,
    Model model) {
    
    // Utiliser la pagination avec recherche optionnelle
    Page<Category> categoryPage;
    if (searchName != null && !searchName.isEmpty()) {
        categoryPage = categoryService.searchCategoriesByNamePaginated(searchName, PageRequest.of(page, pageSize));
    } else {
        categoryPage = categoryService.getAllCategoriesPaginated(PageRequest.of(page, pageSize));
    }
    
    model.addAttribute("categories", categoryPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("totalPages", categoryPage.getTotalPages());
    model.addAttribute("searchName", searchName); // Ajouter pour conserver le terme de recherche
    
    return "categories";
}

    // Ajouter une catégorie
    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "AddCategory";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("category", category);
            return "AddCategory";
        }
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "EditCategory";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("category") Category category,
        BindingResult bindingResult, 
        Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", category);
            return "EditCategory";
        }
        
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}