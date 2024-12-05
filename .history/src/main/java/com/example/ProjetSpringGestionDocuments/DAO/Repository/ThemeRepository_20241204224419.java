package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Optional<Category> findByName(String name);
}
