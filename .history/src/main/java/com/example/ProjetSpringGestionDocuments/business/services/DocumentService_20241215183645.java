package com.example.ProjetSpringGestionDocuments.business.services;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;

public interface DocumentService {
    List<Document> getAllDocuments();
    Document getDocumentById(Long id);
    void saveDocument(Document document, MultipartFile file) throws IOException;
    void updateDocument(Long id, Document updatedDocument, MultipartFile file) throws IOException;
    void deleteDocument(Long id);
    List<Document> searchDocumentsByTitleOrAuthor(String title, String author);
    int getTotalDocumentsCount();
    Page<Document> getAllDocumentPagination(Pageable pageable);
    Page<Document> getDocumentSortedByCategoryPagination(String order, Pageable pageable);
}
