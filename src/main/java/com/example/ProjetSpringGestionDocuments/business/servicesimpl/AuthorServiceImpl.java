package com.example.ProjetSpringGestionDocuments.business.servicesimpl;
import com.example.ProjetSpringGestionDocuments.business.services.AuthorService;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Author;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Document;
import com.example.ProjetSpringGestionDocuments.DAO.Repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author getAuthorById(Long id) {
        Optional<Author> result = authorRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public void deleteAuthor(Long id) {
        if (id == null) {
            return;
        }
        this.authorRepository.deleteById(id);
    }
    
    public Page<Author> getAllAuthorsPaginated(Pageable pegeable) {
        if(pegeable ==null){
            return null;
        }
        return this.authorRepository.findAll(pegeable);

    }

    
}
