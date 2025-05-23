package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "animes")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "nb_episodes", nullable = false)
    private int nbEpisodes;

    @Column(name = "current_episode", nullable = false)
    private int currentEpisode;

    @Column(name = "mal_link", nullable = false)
    private String malLink;

    public Anime() {
    }

    public Anime(String name, int nbEpisodes, int currentEpisode, String malLink) {
        this.name = name;
        this.nbEpisodes = nbEpisodes;
        this.currentEpisode = currentEpisode;
        this.malLink = malLink;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getNbEpisodes() {
        return nbEpisodes;
    }
    public void setNbEpisodes(int nbEpisodes) {
        this.nbEpisodes = nbEpisodes;
    }
    public int getCurrentEpisode() {
        return currentEpisode;
    }
    public void setCurrentEpisode(int currentEpisode) {
        this.currentEpisode = currentEpisode;
    }
    public String getMalLink() {
        return malLink;
    }
    public void setMalLink(String malLink) {
        this.malLink = malLink;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nbEpisodes=" + nbEpisodes +
                ", currentEpisode=" + currentEpisode +
                ", malLink='" + malLink + '\'' +
                '}';
    }
}