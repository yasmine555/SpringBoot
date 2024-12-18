package com.example.ProjetSpringGestionDocuments.Web.model;

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
public class CategoryForm {
    @NotBlank(message = "Le nom de la catégorie est obligatoire.")
    @Size(max = 100, message = "Le nom de la catégorie ne doit pas dépasser 100 caractères.")
    private String name;
}