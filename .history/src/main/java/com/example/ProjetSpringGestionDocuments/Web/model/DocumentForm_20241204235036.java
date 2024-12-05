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

public class DocumentForm {
    @NotBlank(message = "Le titre est obligatoire.")
    private String title;

    @NotBlank(message = "L'ID de l'auteur est obligatoire.")
    private Long author_id;

    @NotBlank(message = "L'ID de la catégorie est obligatoire.")
    private Long category_id;

    @NotBlank(message = "L'id de thème est obligatoire.")
    private Long theme_id;

    @NotBlank(message = "La language est obligatoire.")
    private String language_id;

    @NotBlank(message = "La format du fichier est obligatoire")
    private String fileformat_id;

    private LocalDate publishDate;

    

    private String summary;

    private String keywords;

}
