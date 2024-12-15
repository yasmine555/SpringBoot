package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.persistence.metamodel.Metamodel;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Metamodel model) {
        boolean isLoggedIn = /* logic to check if user is logged in */;
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "index";
    }
}
