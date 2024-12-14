package com.example.ProjetSpringGestionDocuments.Web.controller;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.AuthorRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.CategoryRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.DocumentRepository;
import com.example.ProjetSpringGestionDocuments.Web.model.DocumentForm;
import com.example.ProjetSpringGestionDocuments.business.services.AuthorService;
import com.example.ProjetSpringGestionDocuments.business.services.CategoryService;
import com.example.ProjetSpringGestionDocuments.business.services.DocumentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/documents") 
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private AuthorService authorService;

    @GetMapping("/create")
    public String showAddDocumentPage(Model model) {
        model.addAttribute("documentForm", new DocumentForm());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "AddDocument";
    }

    @PostMapping("/create")
    public String addDocument(
            @Valid @ModelAttribute DocumentForm documentForm,
            BindingResult bindingResult,
            @RequestParam MultipartFile documentFile,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vérifier les champs obligatoires.");
            return "redirect:/documents/create";
        }

        try {
            Author author = authorRepository.findById(documentForm.getAuthor_id())
                    .orElseThrow(() -> new RuntimeException("Auteur introuvable : " + documentForm.getAuthor_id()));
            Category category = categoryRepository.findById(documentForm.getCategory_id())
                    .orElseThrow(() -> new RuntimeException("Catégorie introuvable : " + documentForm.getCategory_id()));

            Document document = new Document();
            document.setTitle(documentForm.getTitle());
            document.setAuthor(author);
            document.setCategory(category);
            document.setTheme(documentForm.getTheme());
            document.setLanguage(documentForm.getLanguage());
            document.setFileFormat(documentForm.getFileFormat());
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

    @GetMapping
    public String listDocuments(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int pageSize,
    @RequestParam(required = false) String sortByCategory,
    @RequestParam(required = false) String sortByFileFormat,
    Model model) {

    // Applique les filtres si nécessaires
    PageRequest pageRequest = PageRequest.of(page, pageSize);

    // Si tu veux trier par catégorie ou par format de fichier
    Page<Document> documentPage;
    if (sortByCategory != null && !sortByCategory.isEmpty()) {
        documentPage = documentService.getDocumentsSortedByCategory(sortByCategory, pageRequest);
    } else if (sortByFileFormat != null && !sortByFileFormat.isEmpty()) {
        documentPage = documentService.getDocumentsSortedByFileFormat(sortByFileFormat, pageRequest);
    } else {
        documentPage = documentService.getAllDocumentPagination(pageRequest);
    }

    List<String> fileNames = new ArrayList<>();
    File directory = new File(uploadDir);
    if (directory.exists() && directory.isDirectory()) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }
    }

    model.addAttribute("documents", documentPage.getContent());
    model.addAttribute("fileNames", fileNames);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", documentPage.getTotalPages());

    return "documents";
}


@GetMapping("/edit/{id}")
public String showEditDocumentForm(@PathVariable Long id, Model model) {
    Document document = documentService.getDocumentById(id);
    if (document == null) {
        model.addAttribute("error", "Document introuvable.");
        return "redirect:/documents";
    }

    List<Author> authors = authorService.getAllAuthors();
    DocumentForm documentForm = new DocumentForm();

    // Remplir les données du formulaire avec les données du document
    if (document.getAuthor() != null) {
        documentForm.setAuthor_id(document.getAuthor().getId());
    }
    documentForm.setTitle(document.getTitle());
    documentForm.setTheme(document.getTheme());
    documentForm.setFileFormat(document.getFileFormat());
    documentForm.setLanguage(document.getLanguage());
    documentForm.setSummary(document.getSummary());
    documentForm.setKeywords(document.getKeywords());
    if (document.getPublishDate() != null) {
        LocalDate localDate = document.getPublishDate().toInstant()
                                       .atZone(ZoneId.systemDefault())
                                       .toLocalDate();
        documentForm.setPublishDate(localDate);
    }
    

    model.addAttribute("authors", authors);
    model.addAttribute("documentForm", documentForm);

    return "editDocument";
}

@PostMapping("/edit/{id}")
public String editDocument(@PathVariable Long id, @ModelAttribute DocumentForm documentForm, BindingResult result,
                            Model model) {
    try {
        // Vérifier si le document existe
        Document document = documentRepository.findById(id).orElse(null);
        if (document == null) {
            model.addAttribute("error", "Document introuvable.");
            return "redirect:/documents";
        }

        // Mettre à jour les propriétés du document
        document.setTitle(documentForm.getTitle());
        document.setTheme(documentForm.getTheme());
        document.setFileFormat(documentForm.getFileFormat());
        document.setLanguage(documentForm.getLanguage());
        document.setSummary(documentForm.getSummary());
        document.setKeywords(documentForm.getKeywords());

        // Validation de la date
        try {
            document.setPublishDate(java.sql.Date.valueOf(documentForm.getPublishDate()));
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Date invalide.");
            return "editDocument";
        }

        // Assigner l'auteur
        Author author = authorService.getAuthorById(documentForm.getAuthor_id());
        if (author == null) {
            model.addAttribute("error", "Auteur introuvable.");
            return "editDocument";
        }
        document.setAuthor(author);

        // Gérer le fichier
        MultipartFile file = documentForm.getDocumentFile();
        if (file != null && !file.isEmpty()) {
            documentService.updateDocument(id, document, file);
        } else {
            documentService.updateDocument(id, document, null); // ou ignorer le fichier
        }

        return "redirect:/documents";
    } catch (Exception e) {
        model.addAttribute("error", "Erreur lors de la mise à jour du document.");
        return "editDocument";
    }
}


    @PostMapping("/delete/{id}")
    public String deleteDocumentPost(@PathVariable Long id) {
        documentRepository.deleteById(id);
        return "redirect:/documents";
    }
}
