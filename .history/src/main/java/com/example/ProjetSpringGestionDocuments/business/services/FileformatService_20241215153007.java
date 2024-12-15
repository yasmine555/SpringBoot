package com.example.ProjetSpringGestionDocuments.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ProjetSpringGestionDocuments.DAO.Repository.FileFormatRepository;
import com.example.ProjetSpringGestionDocuments.DAO.Entity.FileFormat;

import java.util.List;
import java.util.Optional;

@Service
public class FileformatService {

    @Autowired
    private FileFormatRepository fileformatRepository;

    public List<FileFormat> findAll() {
        return fileformatRepository.findAll();
    }

    public FileFormat getFileFormatById(Long id) {
        Optional<FileFormat> fileFormat = fileformatRepository.findById(id);
        return fileFormat.orElseThrow(() -> new RuntimeException("File format not found with ID: " + id));
    }
}
