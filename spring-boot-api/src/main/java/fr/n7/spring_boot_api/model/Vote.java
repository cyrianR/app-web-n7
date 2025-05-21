package fr.n7.spring_boot_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "votes")
@IdClass(VoteId.class)
public class Vote {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public Vote() {
    }

    public Vote(Event event, User user) {
        this.event = event;
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
