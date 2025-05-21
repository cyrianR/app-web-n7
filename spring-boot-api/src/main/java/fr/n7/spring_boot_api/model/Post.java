package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description = "";

    @ElementCollection(targetClass = Event.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "post_events", joinColumns = @JoinColumn(name = "post_id"))
    private Set<Event> events = new HashSet<>();

    @Column(name = "date", nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;


    public Post() {
    }

    public Post(String description, String date, User author) {
        this.description = description;
        this.date = date;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event){
        this.events.add(event);
    }

    public void deleteEvent(Event event){
        this.events.remove(event);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Post [id=" + id + ", description=" + description + ", events=" + events + ", date=" + date + ", author="
                + author + "]";
    }

    
}
