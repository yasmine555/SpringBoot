package com.example.ProjetSpringGestionDocuments.business.servicesimpl;

import com.example.ProjetSpringGestionDocuments.DAO.models.Document;
import com.example.ProjetSpringGestionDocuments.business.services.DocumentService;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

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
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé pour l'ID : " + id));
    }

    @Override
    public void saveDocument(Document document, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // Création des répertoires si nécessaire
            Files.createDirectories(filePath.getParent());

            // Sauvegarde du fichier
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Mise à jour du chemin dans l'objet Document
            document.setFilePath(filePath.toString());
        }

        document.setCreationDate(new Date());
        documentRepository.save(document);
    }

    @Override
    public void updateDocument(Long id, Document updatedDocument, MultipartFile file) throws IOException {
        Document existingDocument = getDocumentById(id);

        // Mise à jour des informations principales
        existingDocument.setTitle(updatedDocument.getTitle());
        existingDocument.setAuthor(updatedDocument.getAuthor());
        existingDocument.setType(updatedDocument.getType());
        existingDocument.setLanguage(updatedDocument.getLanguage());
        existingDocument.setSummary(updatedDocument.getSummary());
        existingDocument.setPublishDate(updatedDocument.getPublishDate());
        existingDocument.setPageCount(updatedDocument.getPageCount());
        existingDocument.setFileFormat(updatedDocument.getFileFormat());
        existingDocument.setModificationDate(new Date());

        // Mise à jour du fichier si présent
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            existingDocument.setFilePath(filePath.toString());
        }

        documentRepository.save(existingDocument);
    }

    @Override
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Le document avec l'ID " + id + " n'existe pas.");
        }
        documentRepository.deleteById(id);
    }

    @Override
    public List<Document> searchDocumentsByTitleOrAuthor(String title, String author) {
        if (title != null && !title.isEmpty()) {
            return documentRepository.findByTitleContaining(title);
        } else if (author != null && !author.isEmpty()) {
            return documentRepository.findByAuthorContaining(author);
        } else {
            return documentRepository.findAll();
        }
    }
}
