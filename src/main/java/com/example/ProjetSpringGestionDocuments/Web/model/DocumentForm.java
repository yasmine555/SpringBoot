package com.example.ProjetSpringGestionDocuments.Web.model;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères.")
    private String title;

    @NotNull(message = "L'ID de l'auteur est obligatoire.")
    private Long author_id;

    @NotNull(message = "L'ID de la catégorie est obligatoire.")
    private Long category_id;

    @NotBlank(message = "Le thème est obligatoire.")
    @Size(max = 100, message = "Le thème ne doit pas dépasser 100 caractères.")
    private String theme;

    @NotBlank(message = "Le format de fichier est obligatoire.")
    private String fileFormat;

    private MultipartFile documentFile;

    @NotNull(message = "La date de publication est obligatoire.")
    private LocalDate publishDate;

    @NotBlank(message = "La langue est obligatoire.")
    private String language;

    @NotBlank(message = "Le résumé est obligatoire.")
    @Size(max = 2000, message = "Le résumé ne doit pas dépasser 2000 caractères.")
    private String summary;

    @NotBlank(message = "Les mots-clés sont obligatoires.")
    @Size(max = 255, message = "Les mots-clés ne doivent pas dépasser 255 caractères.")
    private String keywords;

    private Long id; 
}