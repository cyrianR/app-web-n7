package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {

    @EmbeddedId
    private VoteId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    public Vote() {
    }

    public Vote(User user, Event event) {
        this.id = new VoteId(user.getId(), event.getId());
        this.user = user;
        this.event = event;
    }

    public VoteId getId() { return id; }
    public void setId(VoteId id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
