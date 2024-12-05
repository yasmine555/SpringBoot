package com.example.ProjetSpringGestionDocuments.DAO.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "fileformats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileformat_id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "fileformat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;
}