package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private String date;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "note", nullable = false)
    private Double note;

    public Event() {
    }

    public Event(String name, String date, EventType eventType, String description) {
        this.name = name;
        this.date = date;
        this.eventType = eventType;
        this.description = description;
        this.note = 0.0;
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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
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
    public Double getNote() {
        return note;
    }
    public void setNote(Double note) {
        this.note = note;
    }
    @Override
    public String toString() {
        return "Event [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", eventType=" + eventType +
                ", description='" + description + '\'' +
                ", note=" + note +
                ']';
    }
}
