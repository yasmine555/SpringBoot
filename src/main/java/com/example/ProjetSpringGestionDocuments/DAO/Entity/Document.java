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

    @Column(name = "Author", length = 250, nullable = false)
    private String author;
    
    /* how it should be later
        @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    */
    
    @Column(name = "Theme", length = 250, nullable = false)
    private String theme;

    @Column(name = "Type", length = 250, nullable = false)
    private String type;

    @Column(name = "Language", length = 250, nullable = true)
    private String language;

    @Column(name = "Summary")
    private String summary;

    @Column(name = "Creation_date")
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "Publishdate")
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

    @Column(name = "Keywords", length = 500)
    private String keywords;

}
