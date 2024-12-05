package com.example.ProjetSpringGestionDocuments.Web.model;

import java.time.LocalDate;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Theme;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Language;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.FileFormat;




import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AuthorForm {

    @NotBlank(message = "Le nom  de l'auteur est obligatoire.")
    private String name;

    @NotBlank(message = "L'email d'auteur est obligatoire.")
    private String email;
    
}
