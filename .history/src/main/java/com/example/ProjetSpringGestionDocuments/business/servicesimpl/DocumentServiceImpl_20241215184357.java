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
public class DocumentServiceImpl implements DocumentService.update {

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
    public Page<Document> getAllDocumentPagination(Pageable pegeable) {
        if(pegeable ==null){
            return null;
        }
        return this.documentRepository.findAll(pegeable);

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
            Path filePath = Path.of(uploadDir, fileName);

            // Création des répertoires si nécessaire
            Files.createDirectories(filePath.getParent());

            // Sauvegarde du fichier
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Mise à jour du chemin dans l'objet Document
            document.setFilePath(filePath.toString());
        }

        documentRepository.save(document);
    }

    @Override
    public void updateDocument(Long id, Document updatedDocument, MultipartFile file) throws IOException {
        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        // Mettre à jour les champs du document
        existingDocument.setTitle(updatedDocument.getTitle());
        existingDocument.setTheme(updatedDocument.getTheme());
        existingDocument.setFileFormat(updatedDocument.getFileFormat());
        existingDocument.setLanguage(updatedDocument.getLanguage());
        existingDocument.setSummary(updatedDocument.getSummary());
        existingDocument.setKeywords(updatedDocument.getKeywords());
        existingDocument.setPublishDate(updatedDocument.getPublishDate());
        existingDocument.setAuthor(updatedDocument.getAuthor());

        // Gestion du fichier
        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file); // Implémentez cette méthode pour enregistrer le fichier
            existingDocument.setFilePath(filePath);
        }

        documentRepository.save(existingDocument);
    }

    // Exemple de méthode pour enregistrer un fichier
    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/documents/";
        String fileName = file.getOriginalFilename();
        Path uploadPath = Path.of(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        }
    }

    @Override
    public Document findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé pour l'ID : " + id));
    }

    public List<Document> getDocumentsByAuthorId(int authorId) {
        return documentRepository.findByAuthorId(authorId);
    }
    public Page<Document> getDocumentSortedByCategoryPagination(String order, Pageable pegeable) {
        if(pegeable ==null){
            return null;
        }  
        Sort.Direction direction= Sort.Direction.ASC;
        if("desc".equalsIgnoreCase(order)){
            direction= Sort.Direction.DESC;
        }
        Pageable sortedPageable=PageRequest.of(
            pegeable.getPageNumber(),
            pegeable.getPageSize(),
            Sort.by(direction,"category")
        );
        return this.documentRepository.findAll(sortedPageable);
    }
    public int getTotalDocumentsCount() {
        return (int) documentRepository.count();
    }


    @Override
    public List<Document> searchDocumentsByTitleOrAuthor(String title, String author) {
        if (title != null && !title.isEmpty()) {
            return documentRepository.findByTitleContaining(title);
        } else if (author != null && !author.isEmpty()) {
            // Convert author string to int (assuming author is provided as an ID)
            int authorId = Integer.parseInt(author);
            return documentRepository.findByAuthorId(authorId); // Create a new method to search by author ID
        } else {
            return documentRepository.findAll();
        }
    }

    @Override
    public void deleteDocument(Long id) {

        if (id == null) {
            return;
        }
        this.documentRepository.deleteById(id);
    }

}