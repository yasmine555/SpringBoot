package com.example.ProjetSpringGestionDocuments.DAO.models;

import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "infodoc")
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Document_id")
    private Long id;

    @Column(name = "title", length = 550, nullable = false)
    private String title;

    @Column(name = "Author", length = 250, nullable = false)
    private String author;

    @Column(name = "Theme", length = 250, nullable = false)
    private String Theme;

    @Column(name = "Type", length = 250, nullable = false)
    private String type;

    @Column(name = "Language", length = 250, nullable = false)
    private String language;

    @Column(name = "Summary")
    private String summary;

    @Column(name = "Creation_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "Publish_date")
    @Temporal(TemporalType.DATE)
    private Date publishDate;

    @Column(name = "Modification_Date")
    @Temporal(TemporalType.DATE)
    private Date modificationDate;

    @Column(name = "Page_Count")
    private int pageCount;

    @Column(name = "File_Format", length = 250, nullable = false)
    private String fileFormat;

    @Column(name = "file_path", length = 250, nullable = false)
    private String filePath;
}
