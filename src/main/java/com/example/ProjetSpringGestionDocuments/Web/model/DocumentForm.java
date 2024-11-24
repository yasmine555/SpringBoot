package com.example.ProjetSpringGestionDocuments.Web.model;

import java.time.LocalDate;

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

    @NotBlank(message = "L'auteur est obligatoire.")
    private String author;

    private String theme;
    private String fileFormat;
    @NotBlank(message = "La date de publication est obligatoire.")
    private LocalDate publishDate;
    private String language;
}
