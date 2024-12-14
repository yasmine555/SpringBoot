package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTitleContaining(String title); 
    List<Document> findByAuthorId(int authorId);
    List<Document> findByKeywordsContaining(String keyword);
    List<Document> findByTitleContainingOrAuthor_NameContaining(String searchQuery, String searchQuery2);
    List<Document> findByTitleAndAuthor_Name(String title, String authorName);
    List<Document> findByTitleAndAuthor_Id(String title, Long authorId);
    

}