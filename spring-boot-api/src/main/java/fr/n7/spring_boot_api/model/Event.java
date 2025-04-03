package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

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

    public Event(String nom, String date, EventType eventType, String description) {
        this.nom = nom;
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
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
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
                ", nom='" + nom + '\'' +
                ", date='" + date + '\'' +
                ", eventType=" + eventType +
                ", description='" + description + '\'' +
                ", note=" + note +
                ']';
    }
}
