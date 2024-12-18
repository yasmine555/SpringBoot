package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.Web.model.FileFormat;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
    List<Document> findByTitleContaining(String title); 
    List<Document> findByAuthorId(int authorId);
    List<Document> findByKeywordsContaining(String keyword);
    List<Document> findByTitleContainingOrAuthor_NameContaining(String searchQuery, String searchQuery2);
    List<Document> findByTitleAndAuthor_Name(String title, String authorName);
    List<Document> findByTitleAndAuthor_Id(String title, Long authorId);
    Page<Document> findByCategory_NameOrderByCategory_NameAsc(String categoryName, Pageable pageable);
    Page<Document> findByFileFormatOrderByFileFormatAsc(FileFormat sortByFileFormat, Pageable pageable);
    Page<Document> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    

}
