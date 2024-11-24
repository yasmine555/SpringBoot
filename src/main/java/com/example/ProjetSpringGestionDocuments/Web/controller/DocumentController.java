package com.example.ProjetSpringGestionDocuments.Web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.DocumentRepository;
import com.example.ProjetSpringGestionDocuments.Web.model.DocumentForm;

import jakarta.servlet.http.HttpSession;

@Controller
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Value("${upload.dir}")
    private String uploadDir;
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
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/add-document")
    public String showAddDocumentPage(Model model) {
        // Prépare un objet vide pour lier les champs du formulaire
        model.addAttribute("documentForm", new DocumentForm());
        return "AddDocument"; // Retourne la vue correspondante
    }

    @PostMapping("/add-document")
public String addDocument(
        @Valid
        @ModelAttribute("documentForm") DocumentForm documentForm,
        BindingResult bindingResult,
        @RequestParam("documentFile") MultipartFile documentFile,
        RedirectAttributes redirectAttributes) {

    try {
        logger.info("Début de l'ajout d'un document.");
        
        if (documentFile.isEmpty()) {
            logger.error("Le fichier est vide.");
            throw new IllegalArgumentException("Veuillez sélectionner un fichier à télécharger.");
        }

        // Sauvegarde du fichier
        String filePath = uploadDir + File.separator + documentFile.getOriginalFilename();
        documentFile.transferTo(new File(filePath));
        logger.info("Fichier sauvegardé à l'emplacement : " + filePath);

        // Créer l'objet Document
        Document document = new Document();
        document.setTitle(documentForm.getTitle());
        document.setAuthor(documentForm.getAuthor());
        document.setTheme(documentForm.getTheme());
        document.setLanguage(documentForm.getLanguage()); 
        document.setFileFormat(documentForm.getFileFormat());
        document.setPublishDate(java.sql.Date.valueOf(documentForm.getPublishDate()));
        document.setFilePath(filePath);
        document.setCreationDate(new Date());

        // Sauvegarde dans la base de données
        documentRepository.save(document);
        logger.info("Document sauvegardé dans la base de données.");
        redirectAttributes.addFlashAttribute("successMessage", "Document ajouté avec succès !");
        return "redirect:/documents";

    } catch (Exception e) {
        logger.error("Erreur lors de l'ajout du document : ", e);
        redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        return "redirect:/add-document";
    }
}


    @GetMapping("/view-document")
    public String viewDocument(@RequestParam("selectedDocument") Long documentId, Model model) {
        // Fetch the document by ID
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID: " + documentId));
        // Pass the document details to the view
        model.addAttribute("documents", List.of(document));
        return "documents";
    }

    @GetMapping("/documents")
    public String listDocuments(Model model) {
        // Récupérer les documents depuis la base de données
        List<Document> documents = documentRepository.findAll();
        model.addAttribute("documents", documents);

        // Lire les fichiers depuis le répertoire "upload.dir"
        List<String> fileNames = new ArrayList<>();
        File directory = new File(uploadDir); // Utiliser le chemin défini dans @Value("${upload.dir}")
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        model.addAttribute("fileNames", fileNames); // Ajouter la liste des fichiers au modèle

        return "ListeDocument"; // Retourner la vue
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

    @PostMapping("/edit-document/{id}/with-file")
    public String updateDocumentWithFile(@PathVariable("id") Long documentId,
            @ModelAttribute("document") Document document,
            @RequestParam("documentFile") MultipartFile file) {
        // Retrieve the existing document from the database
        Document existingDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid document ID: " + documentId));

        // Update fields from the form
        existingDocument.setTitle(document.getTitle());
        existingDocument.setAuthor(document.getAuthor());
        existingDocument.setType(document.getType());
        existingDocument.setLanguage(document.getLanguage());
        existingDocument.setSummary(document.getSummary());
        existingDocument.setPublishDate(document.getPublishDate());
        existingDocument.setPageCount(document.getPageCount());
        existingDocument.setFileFormat(document.getFileFormat());
        existingDocument.setModificationDate(new Date()); // Set modification date to now

        // Handle file upload if a new file is provided
        if (!file.isEmpty()) {
            String filePath = saveFile(file); // Implement saveFile to handle file saving
            existingDocument.setFilePath(filePath); // Update file path
        }

        // Save updated document
        documentRepository.save(existingDocument);

        return "ListeDocument"; // Redirect after save
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

    @PostMapping("/edit-document/{id}")
    public String updateDocument(@PathVariable Long id, @ModelAttribute Document document) {
        documentRepository.save(document);
        return "redirect:/documents";
    }

    @GetMapping("/delete-document/{id}")
    public String deleteDocument(@PathVariable Long id) {
        documentRepository.deleteById(id);
        return "ListeDocument";
    }

    // Recherche par titre
    @GetMapping("/search")
    public String searchDocumentsByTitle(@RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            Model model) {
        List<Document> documents;

        // recherche par: Titre, Auteur
        if (title != null && !title.isEmpty()) {
            documents = documentRepository.findByTitleContaining(title);
        } else if (author != null && !author.isEmpty()) {
            documents = documentRepository.findByAuthorContaining(author);
        } else {
            documents = documentRepository.findAll();
        }

        model.addAttribute("documents", documents);
        return "ListeDocument";
    }
    // Remaining methods for filtering, updating, etc.
}