package com.example.ProjetSpringGestionDocuments.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProjetSpringGestionDocuments.DAO.Repository.CategoryRepository;

import java.util.List;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.Theme;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }


}