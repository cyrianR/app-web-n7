package fr.n7.spring_boot_api.payload;

import fr.n7.spring_boot_api.model.EventType;
import java.time.ZonedDateTime;

public class EventDTO {
    private Long id;
    private String name;
    private ZonedDateTime date;
    private EventType eventType;
    private String description;
    private int likes;

    public EventDTO(Long id, String name, ZonedDateTime date, EventType eventType, String description, int likes) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.eventType = eventType;
        this.description = description;
        this.likes = likes;
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
    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
}
