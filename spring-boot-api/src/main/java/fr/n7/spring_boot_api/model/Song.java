package fr.n7.spring_boot_api.model;
// A song class representing a song entity
// is identified by a unique ID and has a title, a songwriter, and a url path.
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Songwriter is mandatory")
    private String songwriter; 

    @NotBlank(message = "Url is mandatory")
    private String url;

    public Song() {
    }
    public Song(String title, String songwriter, String url) {
        this.title = title;
        this.songwriter = songwriter;
        this.url = url;
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
    public String getSongwriter() {
        return songwriter;
    }
    public void setSongwriter(String songwriter) {
        this.songwriter = songwriter;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}