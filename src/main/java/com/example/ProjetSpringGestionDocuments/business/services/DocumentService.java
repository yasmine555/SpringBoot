package com.example.ProjetSpringGestionDocuments.business.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.example.ProjetSpringGestionDocuments.DAO.Repository.DocumentRepository;
import com.example.ProjetSpringGestionDocuments.DAO.models.Document;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document non trouvé pour l'ID : " + id));
    }

    public void saveDocument(Document document, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // Crée le répertoire s'il n'existe pas
            Files.createDirectories(filePath.getParent());

            // Sauvegarde le fichier
            Files.copy(file.getInputStream(), filePath);
            document.setFilePath(filePath.toString());
        }

        // Ajoute une date de création
        document.setCreationDate(new java.util.Date());

        documentRepository.save(document);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}
