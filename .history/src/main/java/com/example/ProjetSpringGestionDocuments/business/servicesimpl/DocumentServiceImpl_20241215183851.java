package com.example.ProjetSpringGestionDocuments.business.servicesimpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.DocumentRepository;
import com.example.ProjetSpringGestionDocuments.business.services.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Value("${upload.dir:uploads/}")
    private String uploadDir;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public List<Document> getAllDocuments() {
        return this.documentRepository.findAll();
    }

    @Override
    public Page<Document> getAllDocumentPagination(Pageable pageable) {
        return pageable != null ? this.documentRepository.findAll(pageable) : Page.empty();
    }

    @Override
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé pour l'ID : " + id));
    }

    @Override
    public void saveDocument(Document document, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file);
            document.setFilePath(filePath);
        }
        documentRepository.save(document);
    }

    @Override
    public void updateDocument(Long id, Document updatedDocument, MultipartFile file) throws IOException {
        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        // Update document fields
        existingDocument.setTitle(updatedDocument.getTitle());
        existingDocument.setTheme(updatedDocument.getTheme());
        existingDocument.setFileFormat(updatedDocument.getFileFormat());
        existingDocument.setLanguage(updatedDocument.getLanguage());
        existingDocument.setSummary(updatedDocument.getSummary());
        existingDocument.setKeywords(updatedDocument.getKeywords());
        existingDocument.setPublishDate(updatedDocument.getPublishDate());
        existingDocument.setAuthor(updatedDocument.getAuthor());

        // Update file if provided
        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file);
            existingDocument.setFilePath(filePath);
        }

        documentRepository.save(existingDocument);
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path uploadPath = Path.of(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return filePath.toString();
    }

    @Override
    public Document findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé pour l'ID : " + id));
    }

    public List<Document> getDocumentsByAuthorId(int authorId) {
        return documentRepository.findByAuthorId(authorId);
    }

    public Page<Document> getDocumentSortedByCategoryPagination(String order, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(direction, "category")
        );
        return this.documentRepository.findAll(sortedPageable);
    }

    @Override
    public List<Document> searchDocumentsByTitleOrAuthor(String title, String author) {
        if (title != null && !title.isEmpty()) {
            return documentRepository.findByTitleContaining(title);
        } else if (author != null && !author.isEmpty()) {
            try {
                int authorId = Integer.parseInt(author);
                return documentRepository.findByAuthorId(authorId);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Author ID invalide: " + author);
            }
        }
        return documentRepository.findAll();
    }

    @Override
    public void deleteDocument(Long id) {
        if (id != null) {
            documentRepository.deleteById(id);
        }
    }

    public int getTotalDocumentsCount() {
        return (int) documentRepository.count();
    }
}
