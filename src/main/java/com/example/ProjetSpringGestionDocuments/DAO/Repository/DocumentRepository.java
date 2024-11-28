package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTitleContaining(String title); 
    List<Document> findByAuthor_NameContaining(String name);

    List<Document> findByKeywordsContaining(String keyword);


}
