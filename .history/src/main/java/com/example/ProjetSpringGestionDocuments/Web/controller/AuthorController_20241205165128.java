package com.example.ProjetSpringGestionDocuments.Web.controller;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.AuthorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    // Display all authors
    @GetMapping
    public String listAuthors(Model model) {
        List<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "authors/list";
    }

    // Show form to create a new author
    @GetMapping("/create")
    public String showCreateAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "AddAuthor";
    }

    // Handle form submission for creating a new author
    @PostMapping("/authors/create")
    public String createAuthor(@Valid @ModelAttribute("author") Author author,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/authors/create";
        }
        authorRepository.save(author);
        redirectAttributes.addFlashAttribute("successMessage", "Author created successfully!");
        return "redirect:/authors";
    }

    // Show form to edit an existing author
    @GetMapping("/edit/{id}")
    public String showEditAuthorForm(@PathVariable Long id, Model model) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID: " + id));
        model.addAttribute("author", author);
        return "authors/edit";
    }

    // Handle form submission for editing an author
    @PostMapping("/edit/{id}")
    public String editAuthor(@PathVariable Long id,
                             @Valid @ModelAttribute("author") Author author,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "authors/edit";
        }
        author.setId(id);
        authorRepository.save(author);
        redirectAttributes.addFlashAttribute("successMessage", "Author updated successfully!");
        return "redirect:/authors";
    }

    // Delete an author
    @PostMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        authorRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Author deleted successfully!");
        return "redirect:/authors";
    }
}
