package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.business.services.AuthorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    // Liste des auteurs
    @GetMapping
    public String listAuthors(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int pageSize,
    Model model) {
    
    // Use pagination
    Page<Author> authorPage = authorService.getAllAuthorsPaginated(PageRequest.of(page, pageSize));
    
    model.addAttribute("authors", authorPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("totalPages", authorPage.getTotalPages());
    
    return "auteurs";
}

    // Ajouter un auteur
    @GetMapping("/add")
    public String showAddAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "AddAuthor";
    }

    @PostMapping("/save")
    public String saveAuthor(@ModelAttribute Author author) {
        authorService.saveAuthor(author);
        return "redirect:/authors";
    }

    // Modifier un auteur
    @GetMapping("/edit/{id}")
    public String showEditAuthorForm(@PathVariable Long id, Model model) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "EditAuthor";
    }

    @PostMapping("/edit/{id}")
    public String updateAuthor(@PathVariable Long id,
            @Valid @ModelAttribute Author author,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("author", author);
            return "EditAuthor";
        }
        authorService.saveAuthor(author);
        return "redirect:/authors";
    }

    // Supprimer un auteur
    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }
}