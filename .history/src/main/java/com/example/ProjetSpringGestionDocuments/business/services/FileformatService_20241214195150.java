package com.example.ProjetSpringGestionDocuments.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProjetSpringGestionDocuments.DAO.Repository.FileFormatRepository;

import java.util.List;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.FileFormat;

@Service
public class FileformatService {

    @Autowired
    private FileFormatRepository fileformatRepository;

    public List<FileFormat> findAll() {
        return fileformatRepository.findAll();
    }


}