package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePage {

    @GetMapping("/index")
    public String homePage(Model model) {
        // You can add attributes to the model if needed
        // model.addAttribute("title", "Home Page");
        return "index";  // This will render the index template
    }

    @GetMapping("/")  // This will map the root URL ("/") as well
    public String homePageRoot(Model model) {
        // You can add attributes to the model if needed
        // model.addAttribute("title", "Home Page");
        return "index";  // This will also render the same index template
    }
}
