package fr.n7.spring_boot_api.model;

import java.io.Serializable;
import java.util.Objects;

public class VoteId implements Serializable {
    private User user;
    private Event event;

    public VoteId() {}

    public VoteId(User user, Event event) {
        this.user = user;
        this.event = event;
    }

    // Getters and setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteId voteId = (VoteId) o;
        return Objects.equals(user.getId(), voteId.user.getId()) &&
               Objects.equals(event.getId(), voteId.event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), event.getId());
    }
}