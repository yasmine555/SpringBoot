package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ProjetSpringGestionDocuments.Web.model.user;

public interface userRepository extends JpaRepository<user, Long> {
    Optional<user> findByUsername(String username);
}
