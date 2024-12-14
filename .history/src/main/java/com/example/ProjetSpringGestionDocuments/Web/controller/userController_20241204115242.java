package com.example.ProjetSpringGestionDocuments.Web.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.*;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.*;

public class userController {
    

 /* @GetMapping("/search")
    public String searchDocumentsByTitle(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String author,
                                         @RequestParam(required = false) String genre,
                                         Model model) {
        List<Document> documents;

        if (title != null && !title.isEmpty()) {
            documents = DocumentRepository.findByTitleContaining(title);
        } else {
            documents = DocumentRepository.findAll();
        }

        model.addAttribute("documents", documents);
        return "documents";
    } */
}
