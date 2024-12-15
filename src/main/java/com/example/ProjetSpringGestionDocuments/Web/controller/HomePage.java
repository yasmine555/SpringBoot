package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePage {
    @GetMapping("/index")
    public String homePage(Model model) {
        return "index"; // ensure 'index.html' exists in 'src/main/resources/templates'
    }

    @GetMapping("/")
    public String homePageRoot(Model model) {
        return "index"; // same template for root '/'
    }
}

