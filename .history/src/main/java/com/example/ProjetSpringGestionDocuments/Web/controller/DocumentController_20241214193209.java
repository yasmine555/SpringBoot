package com.example.ProjetSpringGestionDocuments.Web.controller;

import java.io.File;
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
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Language;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Theme;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.AuthorRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.CategoryRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.ThemeRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.LanguageRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.DocumentRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.FileFormatRepository;
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
    private AuthorRepository AuthorRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository CategoryRepository;

    @Autowired
    private ThemeRepository ThemeRepository;

    @Autowired
    private LanguageRepository LanguageRepository;

    @Autowired
    private FileFormatRepository FileFormatRepository;

    @GetMapping("/documents/create")
    public String showAddDocumentPage(Model model) {
    model.addAttribute("documentForm", new DocumentForm());
    model.addAttribute("authors", AuthorRepository.findAll());
    model.addAttribute("categories", CategoryRepository.findAll());
    model.addAttribute("themes", ThemeRepository.findAll());
    model.addAttribute("languages", LanguageRepository.findAll());
    model.addAttribute("fileFormats", FileFormatRepository.findAll()); 

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
            Theme theme = themeRepository.findById(documentForm.getTheme_id())
                    .orElseThrow(() -> new RuntimeException("Theme introuvable : " + documentForm.getTheme_id()));
            Language language = languageRepository.findById(documentForm.getLanguage_id())
                    .orElseThrow(() -> new RuntimeException("Language introuvable : " + documentForm.getLanguage_id()));

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
    Model model) {
    
    // Use pagination directly
    Page<Document> documentPage = documentService.getAllDocumentPagination(PageRequest.of(page, pageSize));
    
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
    model.addAttribute("totalPages", documentPage.getTotalPages()); // Add this line
    
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
        documentForm.setAuthor_id(document.getAuthor().getId());

        model.addAttribute("authors", authors);
        model.addAttribute("documentForm", documentForm);

        return "editDocument";
    }

    @PostMapping("/edit/{id}")
    public String editDocument(@PathVariable Long id, @ModelAttribute DocumentForm documentForm, BindingResult result,
                                Model model) {
        try {
            Document document = documentRepository.findById(id).orElse(null);
            if (document == null) {
                model.addAttribute("error", "Document introuvable.");
                return "redirect:/documents";
            }

            document.setTitle(documentForm.getTitle());
            document.setTheme(documentForm.getTheme());
            document.setFileFormat(documentForm.getFileFormat());
            document.setLanguage(documentForm.getLanguage());
            document.setSummary(documentForm.getSummary());
            document.setKeywords(documentForm.getKeywords());
            document.setPublishDate(java.sql.Date.valueOf(documentForm.getPublishDate()));

            Author author = authorService.getAuthorById(documentForm.getAuthor_id());
            if (author == null) {
                model.addAttribute("error", "Auteur introuvable.");
                return "editDocument";
            }
            document.setAuthor(author);

            MultipartFile file = documentForm.getDocumentFile();
            documentService.updateDocument(id, document, file);

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