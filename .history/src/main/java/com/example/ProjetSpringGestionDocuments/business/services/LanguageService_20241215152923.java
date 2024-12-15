package com.example.ProjetSpringGestionDocuments.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProjetSpringGestionDocuments.DAO.Repository.LanguageRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.Language;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<Language> findAll() {
        return languageRepository.findAll();
    }

    public Language getLanguageById(Long id) {
        Optional<Language> language = languageRepository.findById(id);
        return language.orElseThrow(() -> new RuntimeException("Language not found with ID: " + id));
    }
}
