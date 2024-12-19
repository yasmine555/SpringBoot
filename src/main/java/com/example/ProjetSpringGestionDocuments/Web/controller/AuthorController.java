package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.AuthorRepository;
import com.example.ProjetSpringGestionDocuments.business.services.AuthorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    
    @GetMapping
    public String listAuthors(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "4") int pageSize,
        @RequestParam(required = false) String searchQuery,
        Model model) {

        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Author> authorPage;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            authorPage = authorService.searchAuthorsByName(searchQuery, pageRequest);
        } else {
            authorPage = authorService.getAllAuthorsPaginated(pageRequest);
        }

        model.addAttribute("authors", authorPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", authorPage.getTotalPages());
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("documentCount", authorRepository.count());

        return "auteurs";
    }

    // Ajouter un auteur
    @GetMapping("/add")
    public String showAddAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "AddAuthor";
    }

    @PostMapping("/save")
    public String saveAuthor(@Valid @ModelAttribute Author author, BindingResult result, Model model) {
    if (result.hasErrors()) {
        System.out.println("Validation errors: " + result.getAllErrors());
        model.addAttribute("author", author);
        return "AddAuthor";
    }
    authorService.saveAuthor(author);
    return "redirect:/authors";
}


    
    @GetMapping("/edit/{id}")
    public String showEditAuthorForm(@PathVariable Long id, Model model) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "EditAuthor";
    }

    @PostMapping("/edit/{id}")
public String updateAuthor(@PathVariable("id") Long id, 
                           @Valid @ModelAttribute("author") Author author,
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
