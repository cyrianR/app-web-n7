package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "file", nullable = false)
    private String file;

    @Column(name = "vocab_file")
    private String vocabFile;

    @Column(name = "ex_file")
    private String exFile;

    @Column(name = "cultural_file", nullable = false)
    private String culturalFile;

    public Lesson() {
    }

    public Lesson(String title, String file, String culturalFile) {
        this.title = title;
        this.file = file;
        this.culturalFile = culturalFile;
    }

    public Lesson(String title, String file, String vocabFile, String exFile, String culturalFile) {
        this.title = title;
        this.file = file;
        this.vocabFile = vocabFile;
        this.exFile = exFile;
        this.culturalFile = culturalFile;
    }

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
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getVocabFile() {
        return vocabFile;
    }
    public void setVocabFile(String vocabFile) {
        this.vocabFile = vocabFile;
    }
    public String getExFile() {
        return exFile;
    }
    public void setExFile(String exFile) {
        this.exFile = exFile;
    }
    public String getCulturalFile() {
        return culturalFile;
    }
    public void setCulturalFile(String culturalFile) {
        this.culturalFile = culturalFile;
    }
    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", file='" + file + '\'' +
                ", vocabFile='" + vocabFile + '\'' +
                ", exFile='" + exFile + '\'' +
                ", culturalFile='" + culturalFile + '\'' +
                '}';
    }

}
