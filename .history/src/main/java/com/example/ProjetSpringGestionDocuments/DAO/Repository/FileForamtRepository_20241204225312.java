package com.example.ProjetSpringGestionDocuments.DAO.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Category;

public interface FileForamtRepository extends JpaRepository<FileFormat, Long> {
    Optional<Category> findByName(String name);
}