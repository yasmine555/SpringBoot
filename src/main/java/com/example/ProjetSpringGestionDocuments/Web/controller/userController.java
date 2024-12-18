package com.example.ProjetSpringGestionDocuments.Web.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.business.services.CategoryService;
import com.example.ProjetSpringGestionDocuments.business.services.DocumentService;

@Controller
@RequestMapping("/ListeDocumentUser")
public class userController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listDocumentsUser(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int pageSize,
    @RequestParam(name = "searchQuery", required = false) String searchQuery,
    @RequestParam(name = "sortByCategory", required = false) String sortByCategory,
    @RequestParam(name = "fileFormat", required = false) String fileFormat,
    @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
    @RequestParam(required = false) String sortBy,
    Model model) {

    // Défaut pour le tri si non fourni
    String defaultSortBy = "publishDate";
    if (sortBy == null || sortBy.isEmpty()) {
        sortBy = defaultSortBy;
    }

    // Appel au service pour appliquer les filtres et la pagination
    Page<Document> documentPage = documentService.searchDocumentsWithFilters(
        searchQuery, 
        sortByCategory, 
        fileFormat, 
        startDate, 
        endDate, 
        PageRequest.of(page, pageSize, Sort.by(sortBy))
    );

    List<Category> categories = categoryService.getAllCategories();
    model.addAttribute("categories", categories);
    model.addAttribute("documents", documentPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("totalPages", documentPage.getTotalPages());
    model.addAttribute("hasPreviousPage", documentPage.hasPrevious());
    model.addAttribute("hasNextPage", documentPage.hasNext());

    // Ajouter les filtres et critères au modèle pour les préserver dans la vue
    model.addAttribute("searchQuery", searchQuery);
    model.addAttribute("sortByCategory", sortByCategory);
    model.addAttribute("fileFormat", fileFormat);
    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("sortBy", sortBy);

    return "ListeDocumentUser";
}


    @GetMapping("/view/{id}")
    public String viewDocument(@PathVariable Long id, Model model) {
        Document document = documentService.getDocumentById(id);
        model.addAttribute("document", document);

        return "DetailDocumentUser";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        Path filePath = Paths.get(document.getFilePath());

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Fichier introuvable : " + document.getFilePath());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du téléchargement : " + e.getMessage());
        }
    }
    
    @GetMapping("/AboutUs")
    public String aboutPage(Model model) {
    return "AboutUs"; 
}
}
 

