package com.example.ProjetSpringGestionDocuments.Web.model;

import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Theme;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Language;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.FileFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "L'ID de l'auteur est obligatoire.")
    private Long author_id;

    @NotNull(message = "L'ID de la catégorie est obligatoire.")
    private Long category_id;

    @NotNull(message = "L'ID de thème est obligatoire.")
    private Long theme_id;

    private Theme theme;
    private Language language;
    private FileFormat fileFormat;

    @NotBlank(message = "Le résumé est obligatoire.")
    private String summary;

    @NotBlank(message = "Les mots-clés sont obligatoires.")
    private String keywords;

    private LocalDate publishDate;

    @NotNull(message = "La langue est obligatoire.")
    private Long language_id;

    @NotNull(message = "Le format du fichier est obligatoire.")
    private Long fileformat_id;

    private MultipartFile documentFile;
}
