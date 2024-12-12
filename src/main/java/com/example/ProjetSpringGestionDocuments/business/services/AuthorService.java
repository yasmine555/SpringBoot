package com.example.ProjetSpringGestionDocuments.business.services;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author saveAuthor(Author author);

    Author getAuthorById(Long id);

    void deleteAuthor(Long id);

    Page<Author> getAllAuthorsPaginated(Pageable pegeable);
}
