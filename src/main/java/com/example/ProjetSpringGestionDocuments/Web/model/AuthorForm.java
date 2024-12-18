package com.example.ProjetSpringGestionDocuments.Web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorForm {

    

    @NotBlank(message = "Le nom de l'auteur est obligatoire.")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères.")
    private String name;

    @Email(message = "L'email doit être valide.")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères.")
    private String email;
}