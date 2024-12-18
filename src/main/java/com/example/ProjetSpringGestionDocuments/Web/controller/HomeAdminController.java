package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeAdminController {

    @GetMapping("/admin/home")
    public String showAdminHomePage() {
        return "homeAdmin";  
    }
}
