package com.example.ProjetSpringGestionDocuments.Web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Controller
@RequestMapping("/contact")
public class ContactController {

    // Ajout du logger
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @GetMapping
    public String showContactPage(Model model) {
        model.addAttribute("systemVersion", "1.0.0");
        model.addAttribute("systemName", "Plateforme de Gestion Documentaire");
        
        return "Contact"; // Correspond au nom du fichier HTML (contact.html)
    }

    // Méthode pour gérer la soumission du formulaire de contact avec validation
    @PostMapping("/send")
    public String sendContactMessage(
            @RequestParam("name") @NotBlank(message = "Le nom ne peut pas être vide") @Size(min = 2, max = 100, message = "Le nom doit avoir entre 2 et 100 caractères") String name,
            @RequestParam("email") @NotBlank(message = "L'email ne peut pas être vide") @Email(message = "Format d'email invalide") String email,
            @RequestParam("message") @NotBlank(message = "Le message ne peut pas être vide") @Size(min = 10, max = 1000, message = "Le message doit avoir entre 10 et 1000 caractères") String message,
            RedirectAttributes redirectAttributes) {

        // Validation manuelle (à remplacer par une validation automatique avec Bean Validation)
        if (name == null || name.trim().isEmpty() || name.length() < 2) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nom invalide");
            return "redirect:/contact";
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email invalide");
            return "redirect:/contact";
        }

        if (message == null || message.trim().isEmpty() || message.length() < 10) {
            redirectAttributes.addFlashAttribute("errorMessage", "Message invalide");
            return "redirect:/contact";
        }

        // Logique d'envoi de message
        try {
            // Journalisation du message
            logger.info("Message reçu de : {} ({})", name, email);
            logger.info("Contenu : {}", message);

            // Ici, vous pourriez ajouter l'envoi d'email ou l'enregistrement en base de données
            // Par exemple : emailService.sendContactEmail(name, email, message);

            redirectAttributes.addFlashAttribute("successMessage", 
                "Votre message a été envoyé avec succès !");
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du message", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Une erreur est survenue lors de l'envoi du message.");
        }

        return "redirect:/contact";
    }
}