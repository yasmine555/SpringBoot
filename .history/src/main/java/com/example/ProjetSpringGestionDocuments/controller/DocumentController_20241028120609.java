package com.example.ProjetSpringGestionDocuments.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ProjetSpringGestionDocuments.Repository.DocumentRepository;
import com.example.ProjetSpringGestionDocuments.model.Document;

import jakarta.servlet.http.HttpSession;

@Controller
public class DocumentController {
    @Value("${upload.dir}")
    private String uploadDir; // Path to upload directory

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/")
    public String showIndexPage(Model model, HttpSession session) {
        model.addAttribute("showFilter", false);

        List<Document> documents = documentRepository.findTop10ByOrderByCreationDateDesc();
        model.addAttribute("documents", documents);

        // Check if the user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        model.addAttribute("isLoggedIn", isLoggedIn != null && isLoggedIn);

        return "index";
    }

    @PostMapping("/toggle-filter")
    public String toggleFilter(@RequestParam boolean showFilter, HttpSession session, Model model) {
        // Toggle the showFilter state in session
        session.setAttribute("showFilter", !showFilter);
        model.addAttribute("showFilter", !showFilter);

        List<Document> documents = documentRepository.findTop10ByOrderByCreationDateDesc();
        model.addAttribute("documents", documents);

        // Check if the user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        model.addAttribute("isLoggedIn", isLoggedIn != null && isLoggedIn);

        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
            Model model) {
        if ("admin".equals(username) && "adminpassword".equals(password)) {
            session.setAttribute("isLoggedIn", true);
        }
        return "redirect:/"; // Redirect to the index page after login
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/"; // Redirect to the index page after logout
    }

    @GetMapping("/add-document") // method to show the add document form
    public String showAddDocumentPage(Model model, HttpSession session) {
        // Check if the user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        model.addAttribute("isLoggedIn", isLoggedIn != null && isLoggedIn);

        return "AddDocument"; // Return the name of the HTML template (without .html)
    }

    @PostMapping("/add-document")
    public String addDocument(@RequestParam String title, @RequestParam String author,
            @RequestParam String genre, @RequestParam String type,
            @RequestParam String language, @RequestParam String summary,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publish_date, // consider using
                                                                                                 // LocalDate
            @RequestParam int page_count, @RequestParam String file_format,
            @RequestParam("documentFile") MultipartFile documentFile,
            Model model) {
        try {
            Document document = new Document();
            document.setTitle(title);
            document.setAuthor(author);
            document.setGenre(genre);
            document.setType(type);
            document.setLanguage(language);
            document.setSummary(summary);
            document.setPublishDate(java.sql.Date.valueOf(publish_date)); // convert LocalDate to java.sql.Date
            document.setPageCount(page_count);
            document.setFileFormat(file_format);

            // Handle file upload
            if (!documentFile.isEmpty()) {
                String file_path = uploadDir + File.separator + documentFile.getOriginalFilename();
                documentFile.transferTo(new File(file_path));
                document.setFilePath(file_path);
            }

            document.setCreationDate(new Date());
            documentRepository.save(document);

            return "redirect:/"; // Redirect after successful upload
        } catch (Exception e) {
            // Log the error with more context if possible
            model.addAttribute("errorMessage", "Error uploading document: " + e.getMessage());
            return "AddDocument"; // Return to the add document form with error
        }
    }

    @GetMapping("/view-document")
    public String viewDocument(@RequestParam("selectedDocument") Long documentId, Model model) {
        // Fetch the document by ID
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID: " + documentId));
        // Pass the document details to the view
        model.addAttribute("documents", List.of(document)); 
        return "documents"; // The Thymeleaf template that displays document details
    }
    


    @GetMapping("/edit-document/{id}")
    public String showEditDocumentPage(@PathVariable Long id, Model model) {
        Document document = documentRepository.findById(id).orElse(null);
        if (document == null) {
            model.addAttribute("errorMessage", "Document not found");
            return "redirect:/documents";
        }
        model.addAttribute("document", document);
        return "editDocument";
    }
    @PostMapping("/edit-document/{id}")
    @PostMapping("/edit-document/{id}")
    public String updateDocument(@PathVariable("id") Long documentId, 
                                @ModelAttribute("document") Document document,
                                @RequestParam("documentFile") MultipartFile file) {
        Document existingDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID: " + documentId));

        existingDocument.setTitle(document.getTitle());
        existingDocument.setAuthor(document.getAuthor());
        existingDocument.setGenre(document.getGenre());
        existingDocument.setType(document.getType());
        existingDocument.setLanguage(document.getLanguage());
        existingDocument.setSummary(document.getSummary());
        existingDocument.setPublishDate(document.getPublishDate());
        existingDocument.setPageCount(document.getPageCount());
        existingDocument.setFileFormat(document.getFileFormat());
        existingDocument.setModificationDate(new Date());

        if (!file.isEmpty()) {
            try {
                String filePath = uploadDir + File.separator + file.getOriginalFilename();
                file.transferTo(new File(filePath));
                existingDocument.setFilePath(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file", e);
            }
        }

        documentRepository.save(existingDocument);

        return "redirect:/";
    }




    @PostMapping("/edit-document/{id}")
    public String updateDocument(@PathVariable Long id, @ModelAttribute Document document) {
        documentRepository.save(document);
        return "redirect:/documents";
    }

    @GetMapping("/delete-document/{id}")
    public String deleteDocument(@PathVariable Long id) {
        documentRepository.deleteById(id);
        return "redirect:/documents";
    }

    // Remaining methods for filtering, updating, etc.
}
