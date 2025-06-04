package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "likedEvents")
    private Set<User> likedBy = new HashSet<>();

    public Event() {
    }

    public Event(String name, ZonedDateTime date, EventType eventType, String description) {
        this.name = name;
        this.date = date;
        this.eventType = eventType;
        this.description = description;
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
    public ZonedDateTime getDate() {
        return date;
    }
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    public EventType getEventType() {
        return eventType;
    }
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<User> getLikedBy() {
        return likedBy;
    }
    public void setLikedBy(Set<User> likedBy) {
        this.likedBy = likedBy;
    }

    @Override
    public String toString() {
        return "Event [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", eventType=" + eventType +
                ", description='" + description + '\'' +
                ", note=" + likedBy.size() +
                ']';
    }
}
