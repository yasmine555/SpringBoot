package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Theme> findByName(String name);
}
