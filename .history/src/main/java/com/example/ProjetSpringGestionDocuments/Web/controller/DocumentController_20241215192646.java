package com.example.ProjetSpringGestionDocuments.Web.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
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
import com.example.ProjetSpringGestionDocuments.DAO.Entity.FileFormat;
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
import com.example.ProjetSpringGestionDocuments.business.services.ThemeService;
import com.example.ProjetSpringGestionDocuments.business.services.DocumentService;
import com.example.ProjetSpringGestionDocuments.business.services.FileformatService;
import com.example.ProjetSpringGestionDocuments.business.services.LanguageService;

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
    private CategoryRepository CategoryRepository;

    @Autowired
    private ThemeRepository ThemeRepository;

    @Autowired
    private LanguageRepository LanguageRepository;

    @Autowired
    private FileFormatRepository FileFormatRepository;
    @Autowired
    private DocumentService documentService; 

    @Autowired
    private AuthorService authorService;
    @Autowired
    private LanguageService languageService; // Injecting DocumentService

    @Autowired
    private FileformatService fileformatService; // Injecting AuthorService

    @GetMapping("/create")
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
    @Valid @ModelAttribute("documentForm") DocumentForm documentForm,
    BindingResult bindingResult,
    @RequestParam("documentFile") MultipartFile documentFile,
    RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
        redirectAttributes.addFlashAttribute("error", "Veuillez vérifier les champs obligatoires.");
        return "redirect:/documents/create";
    }

    try {
        String summary = documentForm.getSummary();
        String keywords = documentForm.getKeywords();

        // Trouver l'auteur à l'aide de l'ID
        Author author = AuthorRepository.findById(documentForm.getAuthor_id())
                .orElseThrow(() -> new RuntimeException("Auteur introuvable : " + documentForm.getAuthor_id()));

        // Trouver la catégorie à l'aide de l'ID
        Category category = CategoryRepository.findById(documentForm.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable : " + documentForm.getCategory_id()));

        // Trouver le thème à l'aide de l'ID
        Theme theme = ThemeRepository.findById(documentForm.getTheme_id())
                .orElseThrow(() -> new RuntimeException("Thème introuvable : " + documentForm.getTheme_id()));

        // Trouver la langue à l'aide de l'ID
        Language language = LanguageRepository.findById(documentForm.getLanguage_id())
                .orElseThrow(() -> new RuntimeException("Langue introuvable : " + documentForm.getLanguage_id()));

        // Trouver le format de fichier à l'aide de l'ID
        FileFormat fileFormat = FileFormatRepository.findById(documentForm.getFileformat_id())
                .orElseThrow(() -> new RuntimeException("Format de fichier introuvable : " + documentForm.getFileformat_id()));

        // Créer le document
        Document document = new Document();
        document.setTitle(documentForm.getTitle());
        document.setAuthor(author);
        document.setCategory(category);
        document.setTheme(theme);
        document.setSummary(summary);
        document.setKeywords(keywords);
        document.setPublishDate(java.sql.Date.valueOf(documentForm.getPublishDate()));
        document.setLanguage(language);
        document.setFileFormat(fileFormat);

        // Ensure the upload directory exists
        String uploadDir = System.getProperty("user.dir") + "/DocumentUploadsdirectory"; // Get the project directory
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs(); // Create the directory if it doesn't exist
        }

        // Handle the uploaded file
        if (!documentFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + documentFile.getOriginalFilename();
            File fileToSave = new File(uploadDirectory, fileName);

            // Save file to the directory
            documentFile.transferTo(fileToSave);

            // Save the relative path to the document
            String relativePath = "DocumentUploadsdirectory/" + fileName; 
            document.setFilePath(relativePath); // Set the file path for the document
        }

        // Save the document to the repository
        documentRepository.save(document);

        redirectAttributes.addFlashAttribute("successMessage", "Document ajouté avec succès !");
        return "redirect:/documents";

    } catch (Exception e) {
        // Handle errors
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
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private FileFormatRepository fileFormatRepository;


    @GetMapping("/edit/{id}")
public String showEditDocumentForm(@PathVariable Long id, Model model) {
    try {
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            model.addAttribute("error", "Document introuvable.");
            return "redirect:/documents";
        }

        // Add document to the model
        model.addAttribute("document", document);

        DocumentForm documentForm = new DocumentForm();
        documentForm.setTitle(document.getTitle());
        documentForm.setSummary(document.getSummary());
        documentForm.setKeywords(document.getKeywords());
        documentForm.setPublishDate(document.getPublishDate(;
        documentForm.setAuthor_id(document.getAuthor().getId());
        documentForm.setCategory_id(document.getCategory().getId());
        documentForm.setTheme_id(document.getTheme().getId());
        documentForm.setLanguage_id(document.getLanguage().getId());
        documentForm.setFileformat_id(document.getFileFormat().getId());

        model.addAttribute("documentForm", documentForm);
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("themes", themeRepository.findAll());
        model.addAttribute("languages", languageRepository.findAll());
        model.addAttribute("fileFormats", fileFormatRepository.findAll());

        return "editDocument";
    } catch (Exception e) {
        logger.error("Error loading document for editing", e);
        model.addAttribute("error", "Erreur interne : " + e.getMessage());
        return "redirect:/documents";
    }
}
    @PostMapping("/edit/{id}")
    public String editDocument(
        @PathVariable Long id,
        @Valid @ModelAttribute("documentForm") DocumentForm documentForm,
        BindingResult result,
        @RequestParam("documentFile") MultipartFile documentFile,
        Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("authors", authorRepository.findAll());
                model.addAttribute("categories", categoryRepository.findAll());
                model.addAttribute("themes", themeRepository.findAll());
                model.addAttribute("languages", languageRepository.findAll());
                model.addAttribute("fileFormats", fileFormatRepository.findAll());
                return "editDocument";
            }

            Document document = documentService.getDocumentById(id);
            if (document == null) {
                model.addAttribute("error", "Document introuvable.");
                return "redirect:/documents";
            }

            document.setTitle(documentForm.getTitle());
            document.setSummary(documentForm.getSummary());
            document.setKeywords(documentForm.getKeywords());
            document.setPublishDate(java.sql.Date.valueOf(documentForm.getPublishDate()));
            document.setAuthor(authorService.getAuthorById(documentForm.getAuthor_id()));
            document.setCategory(categoryRepository.findById(documentForm.getCategory_id()).orElse(null));
            document.setTheme(themeRepository.findById(documentForm.getTheme_id()).orElse(null));
            document.setLanguage(languageService.getLanguageById(documentForm.getLanguage_id()));
            document.setFileFormat(fileformatService.getFileFormatById(documentForm.getFileformat_id()));

            if (documentFile != null && !documentFile.isEmpty()) {
                documentService.updateDocumentFile(id, document, documentFile);
            }

            documentRepository.save(document);

            return "redirect:/documents";
        } catch (Exception e) {
            logger.error("Error while updating document", e);
            model.addAttribute("error", "Erreur lors de la mise à jour du document : " + e.getMessage());
            return "editDocument";
        }
    }
}