package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HomePage {
    @GetMapping("/index")
public String homePage(Model model) {
    return "index";
}

    
}
