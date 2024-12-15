package com.example.ProjetSpringGestionDocuments.DAO.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.FileFormat;

public interface FileForamtRepository extends JpaRepository<FileFormat, Long> {
    Optional<FileFomar> findByName(String name);
}