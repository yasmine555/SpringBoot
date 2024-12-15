package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.stereotype.Controller;  // Add this import
import org.springframework.ui.Model;  // Add this import
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // Add this annotation
public class HomePage {

    @GetMapping("/index")
    public String homePage(Model model) {
        // You can add attributes to the model if needed
        // model.addAttribute("title", "Home Page");
        return "index";  // This corresponds to the "index.html" view
    }
}
