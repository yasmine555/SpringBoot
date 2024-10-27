package com.example.ProjetSpringGestionDocuments.controller;

import com.example.ProjetSpringGestionDocuments.model.Document;
import com.example.ProjetSpringGestionDocuments.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DocumentController {

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

    @PostMapping("/add-document")
    public String addDocument(@RequestParam String title, @RequestParam String author, 
                              @RequestParam String genre, @RequestParam String type, 
                              @RequestParam String language, @RequestParam String summary,
                              @RequestParam Date publishDate, @RequestParam int pageCount, 
                              @RequestParam String fileFormat, Model model) {

        Document document = new Document();
        document.setTitle(title);
        document.setAuthor(author);
        document.setGenre(genre);
        document.setType(type);
        document.setLanguage(language);
        document.setSummary(summary);
        document.setPublishDate(publishDate);
        document.setPageCount(pageCount);
        document.setFileFormat(fileFormat);
        document.setCreationDate(new Date());
        documentRepository.save(document);
        return "redirect:/";
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
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
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
    @PostMapping("/add-document")
    public String addDocument(@RequestParam String title, @RequestParam String author, 
                            @RequestParam String genre, @RequestParam String type, 
                            @RequestParam String language, @RequestParam String summary,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDate, 
                            @RequestParam int pageCount, @RequestParam String fileFormat,
                            @RequestParam("documentFile") MultipartFile documentFile, 
                            Model model) {

        Document document = new Document();
        document.setTitle(title);
        document.setAuthor(author);
        document.setGenre(genre);
        document.setType(type);
        document.setLanguage(language);
        document.setSummary(summary);
        document.setPublishDate(publishDate);
        document.setPageCount(pageCount);
        document.setFileFormat(fileFormat);
        
        // Handle file upload
        if (!documentFile.isEmpty()) {
            String filePath = "/path/to/upload/directory/" + documentFile.getOriginalFilename();
            try {
                documentFile.transferTo(new File(filePath)); // Save the file
                document.setFilePath(filePath); // Store the file path in the document
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception
            }
        }

        document.setCreationDate(new Date());
        documentRepository.save(document);
        return "redirect:/";
    }


    // Remaining methods for filtering, updating, etc.
}
