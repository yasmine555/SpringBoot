package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ProjetSpringGestionDocuments.DAO.models.user;

public interface UserRepository extends JpaRepository<user, Long> {
    Optional<user> findByUsername(String username);
}
