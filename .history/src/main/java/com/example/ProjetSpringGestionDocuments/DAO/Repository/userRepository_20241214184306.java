package com.example.ProjetSpringGestionDocuments.DAO.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface userRepository extends JpaRepository<user, Long> {
    Optional<user> findByUsername(String username);
}