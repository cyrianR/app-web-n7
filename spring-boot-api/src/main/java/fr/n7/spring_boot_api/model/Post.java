package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description = "";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_events", joinColumns = @JoinColumn(name = "post_id"),  inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events = new HashSet<>();

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime date;

    @Column(name = "title", nullable = false)
    private String title = "";

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private User author;


    public Post() {
    }

    public Post(String description, ZonedDateTime date, String title, User author) {
        this.description = description;
        this.date = date;
        this.title = title;
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

    public void clearEvents(){
        this.events.clear();
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Post [id=" + id + ", description=" + description + ", events=" + events + ", date=" + date + ", author="
                + author + "]";
    }

    
}
