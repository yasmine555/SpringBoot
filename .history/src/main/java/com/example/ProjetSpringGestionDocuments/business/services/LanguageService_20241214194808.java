package com.example.ProjetSpringGestionDocuments.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProjetSpringGestionDocuments.DAO.Repository.LanguageRepository;

import java.util.List;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Language;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }


}