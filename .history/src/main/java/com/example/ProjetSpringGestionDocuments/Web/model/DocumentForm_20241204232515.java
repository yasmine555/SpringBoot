package com.example.ProjetSpringGestionDocuments.Web.model;

import java.time.LocalDate;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Theme;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DocumentForm {
    @NotBlank(message = "Le titre est obligatoire.")
    private String title;

    @NotBlank(message = "L'ID de l'auteur est obligatoire.")
    private Long author_id;

    @NotBlank(message = "L'ID de la catégorie est obligatoire.")
    private Long category_id;

    @NotBlank
    private String theme;

    @NotBlank
    private String fileFormat;

    private LocalDate publishDate;

    private String language;

    private String summary;

    private String keywords;

}
