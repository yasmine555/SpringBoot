package com.example.ProjetSpringGestionDocuments.DAO.Entity;

import java.util.Date;
import jakarta.persistence.*;
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
    @JoinColumn(name = "language_id") // Fix for 'language'
    private Language language;

    @Column(name = "Summary")
    private String summary;

    @Column(name = "Publishdate")
    @Temporal(TemporalType.DATE)
    private Date publishDate;


    @ManyToOne
    @JoinColumn(name = "fileformat_id", nullable = false)
    private FileFormat fileFormat;

    @Column(name = "file_path", length = 250, nullable = false)
    private String filePath;

    @Column(name = "Keywords", length = 500)
    private String keywords;

}
