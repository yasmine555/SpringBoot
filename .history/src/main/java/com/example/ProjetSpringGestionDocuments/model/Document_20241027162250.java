package com.example.ProjetSpringGestionDocuments.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "infodoc")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Document_id")
    private Long id;

    @Column(name = "title", length = 550, nullable = false)
    private String title;

    @Column(name = "Author", length = 250, nullable = false)
    private String author;

    @Column(name = "Genre", length = 250, nullable = false)
    private String genre;

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
    private String file_path;

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Date getPublishDate() {
        return publishDate;
    }
    
    public void setPublishDate(Date publishDate2) {
        this.publishDate = publishDate2;
    }
    
    public Date getModificationDate() {
        return modificationDate;
    }
    
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
    
    public int getPageCount() {
        return pageCount;
    }
    
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    
    public String getFileFormat() {
        return fileFormat;
    }
    
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
    private String file_path; // This will store the path or filename of the uploaded document

    // Don't forget to add getter and setter methods
    public String getFilePath() {
        return file_path;
    }

    public void setFilePath(String file_path) {
        this.file_path = file_path;
    }

    
}
