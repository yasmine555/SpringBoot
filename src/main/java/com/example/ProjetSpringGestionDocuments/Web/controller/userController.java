package com.example.ProjetSpringGestionDocuments.Web.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.business.services.DocumentService;

@Controller
@RequestMapping("/ListeDocumentUser")
public class userController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public String listDocumentsUser(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int pageSize,
    @RequestParam(required = false) String sortBy, // Optional sorting parameter
    Model model) {

    // Validate page and pageSize to prevent potential issues
    page = Math.max(0, page); // Ensure page is not negative
    pageSize = Math.min(Math.max(pageSize, 1), 100); // Limit pageSize between 1 and 100

    // Create a sort specification if sortBy is provided
    Pageable pageable = sortBy != null 
        ? PageRequest.of(page, pageSize, Sort.by(sortBy)) 
        : PageRequest.of(page, pageSize);

    // Retrieve documents with pagination
    Page<Document> documentPage = documentService.getAllDocumentPagination(pageable);

    // Add attributes to the model
    model.addAttribute("documents", documentPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("totalPages", documentPage.getTotalPages());
    model.addAttribute("hasPreviousPage", documentPage.hasPrevious());
    model.addAttribute("hasNextPage", documentPage.hasNext());

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
    return "AboutUs"; // correspond au nom du template
}
}
 

