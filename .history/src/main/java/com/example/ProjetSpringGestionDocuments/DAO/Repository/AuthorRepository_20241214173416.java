package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    
    
}