package com.example.ProjetSpringGestionDocuments.Web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Theme;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Language;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.FileFormat;



import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.AuthorRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.ThemeRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.LanguageRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.FileFormatRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.CategoryRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.DocumentRepository;
import com.example.ProjetSpringGestionDocuments.Web.model.DocumentForm;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AuthorRepository AuthorRepository;

    @Autowired
    private CategoryRepository CategoryRepository;

    @GetMapping("/")
    public String showIndexPage(Model model, HttpSession session) {
        model.addAttribute("showFilter", false);

        List<Document> documents = documentRepository.findByTitleContaining(uploadDir);
        model.addAttribute("documents", documents);

        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        model.addAttribute("isLoggedIn", isLoggedIn != null && isLoggedIn);

        return "index";
    }

    @PostMapping("/toggle-filter")
    public String toggleFilter(@RequestParam boolean showFilter, HttpSession session, Model model) {
        session.setAttribute("showFilter", !showFilter);
        model.addAttribute("showFilter", !showFilter);

        List<Document> documents = documentRepository.findByTitleContaining(uploadDir);
        model.addAttribute("documents", documents);

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
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/documents/create")
    public String showAddDocumentPage(Model model) {
        model.addAttribute("documentForm", new DocumentForm());
        model.addAttribute("authors", AuthorRepository.findAll());
        model.addAttribute("categories", CategoryRepository.findAll());
        return "AddDocument";
    }

    @PostMapping("/documents/create")
    public String addDocument(
            @Valid @ModelAttribute("documentForm") DocumentForm documentForm,
            BindingResult bindingResult,
            @RequestParam("documentFile") MultipartFile documentFile,
            RedirectAttributes redirectAttributes) {
    
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vérifier les champs obligatoires.");
            return "redirect:/documents/create";
        }
    
        try {
            // Trouver l'auteur à l'aide de l'ID
            Author author = AuthorRepository.findById(documentForm.getAuthor_id())
                    .orElseThrow(() -> new RuntimeException("Auteur introuvable : " + documentForm.getAuthor_id()));
    
            // Trouver la catégorie à l'aide de l'ID
            Category category = CategoryRepository.findById(documentForm.getCategory_id())
                    .orElseThrow(() -> new RuntimeException("Catégorie introuvable : " + documentForm.getCategory_id()));
                    
    
            Document document = new Document();
            Theme category = CategoryRepository.findById(documentForm.getCategory_id())
                    .orElseThrow(() -> new RuntimeException("Catégorie introuvable : " + documentForm.getCategory_id()));
            document.setTitle(documentForm.getTitle());
            document.setAuthor(author);
            document.setCategory(category);
            document.setTheme(theme);
            document.setLanguage(language);
            document.setFileFormat(fileFormat);
            document.setPublishDate(java.sql.Date.valueOf(documentForm.getPublishDate()));
            document.setFilePath(documentFile.getOriginalFilename());
    
            documentRepository.save(document);
    
            redirectAttributes.addFlashAttribute("successMessage", "Document ajouté avec succès !");
            return "redirect:/documents";
    
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du document", e);
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
            return "redirect:/documents/create";
        }
    }
    

    @GetMapping("/documents/view")
    public String viewDocument(@RequestParam("selectedDocument") Long documentId, Model model) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID: " + documentId));
        model.addAttribute("documents", List.of(document));
        return "documents";
    }

    @GetMapping("/documents")
    public String listDocuments(Model model) {
        List<Document> documents = documentRepository.findAll();
        model.addAttribute("documents", documents);

        List<String> fileNames = new ArrayList<>();
        File directory = new File(uploadDir);
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        model.addAttribute("fileNames", fileNames);

        return "documents";
    }

    @GetMapping("/documents/edit/{id}")
    public String showEditDocumentPage(@PathVariable Long id, Model model) {
        // Trouver le document à modifier
        Document document = documentRepository.findById(id).orElse(null);
        if (document == null) {
            model.addAttribute("errorMessage", "Document not found");
            return "redirect:/documents";
        }
    
        // Ajouter le document au modèle
        model.addAttribute("document", document);
    
        // Ajouter les listes des auteurs et des catégories au modèle
        model.addAttribute("authors", AuthorRepository.findAll()); 
        model.addAttribute("categories", CategoryRepository.findAll());
    
        // Retourner le nom de la vue (editDocument.html)
        return "editDocument";
    }
    

    @PostMapping("/documents/edit/{id}/with-file")
    public String updateDocumentWithFile(@PathVariable("id") Long documentId,
                                         @ModelAttribute("document") Document document,
                                         @RequestParam("documentFile") MultipartFile file) {
        Document existingDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID: " + documentId));

        existingDocument.setTitle(document.getTitle());
        existingDocument.setAuthor(document.getAuthor());
        existingDocument.setCategory(document.getCategory());
        existingDocument.setLanguage(document.getLanguage());
        existingDocument.setSummary(document.getSummary());
        existingDocument.setPublishDate(document.getPublishDate());
        existingDocument.setFileFormat(document.getFileFormat());

        if (!file.isEmpty()) {
            String filePath = saveFile(file);
            existingDocument.setFilePath(filePath);
        }

        documentRepository.save(existingDocument);

        return "documents";
    }

    private String saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String uploadDir = "/DocumentUploadsdirectory";

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store file " + fileName, e);
        }

        return uploadDir + fileName;
    }

    @PostMapping("/documents/edit/{id}")
    public String updateDocument(@PathVariable Long id, @ModelAttribute Document document) {
        documentRepository.save(document);
        return "redirect:/documents";
    }

    @PostMapping("/documents/delete/{id}")
    public String deleteDocumentPost(@PathVariable Long id) {
        documentRepository.deleteById(id);
        return "redirect:/documents";
    }

   
}
