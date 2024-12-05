package com.example.ProjetSpringGestionDocuments.DAO.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ProjetSpringGestionDocuments.DAO.Entity.FileFormat;

public interface FileFormtRepository extends JpaRepository<FileFormat, Long> {
    Optional<FileFormat> findByName(String name);
}
