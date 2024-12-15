package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        boolean isLoggedIn = /* logic to check if user is logged in */;
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "index";
    }
}
