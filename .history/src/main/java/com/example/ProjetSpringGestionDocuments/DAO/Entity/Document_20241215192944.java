package com.example.ProjetSpringGestionDocuments.DAO.Entity;

import java.time.LocalDate;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "infodocument")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Document_id")
    private Long id;

    @Column(name = "title", length = 550, nullable = false)
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    
    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "Summary" , nullable = true)
    private String summary;

    @Column(name = "Publishdate")
    private LocalDate publishDate;
    @Temporal(TemporalType.DATE)


    @ManyToOne
    @JoinColumn(name = "fileFormat_id", nullable = false)
    private FileFormat fileFormat;

    @Column(name = "file_path", length = 250, nullable = false)
    private String filePath;

    @Column(name = "Keywords", length = 500 , nullable = true )
    private String keywords;

}