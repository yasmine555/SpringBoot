package com.example.ProjetSpringGestionDocuments.business.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.Web.model.FileFormat;

public interface DocumentService {
    List<Document> getAllDocuments();
    Document getDocumentById(Long id);
    void saveDocument(Document document, MultipartFile file) throws IOException;
    void updateDocument(Long id, Document updatedDocument, MultipartFile file) throws IOException;
    void deleteDocument(Long id);
    List<Document> searchDocumentsByTitleOrAuthor(String title, String author);
    Page<Document> searchDocumentsByTitle(String searchQuery, PageRequest pageRequest);
    Page<Document> searchDocumentsWithFilters(String searchQuery, 
                                              String category, 
                                              String fileFormat, 
                                              LocalDate startDate, 
                                              LocalDate endDate, 
                                              Pageable pageable);    
    int getTotalDocumentsCount();
    Page<Document> getAllDocumentPagination(Pageable pegeable);
    Page<Document> getDocumentsSortedByCategory(String sortByCategory, PageRequest pageRequest);
    Page<Document> getDocumentsSortedByFileFormat(FileFormat sortByFileFormat, PageRequest pageRequest);
    Page<Document> getDocumentSortedByCategoryPagination(String order, Pageable pegeable);
    Page<Document> searchDocumentsWithCategoryOrFileFormat(
        String searchQuery, 
        String category, 
        FileFormat fileFormat,  
        Pageable pageable);
}
