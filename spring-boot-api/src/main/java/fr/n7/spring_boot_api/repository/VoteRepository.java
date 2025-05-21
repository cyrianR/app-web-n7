package fr.n7.spring_boot_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.n7.spring_boot_api.model.Vote;
import fr.n7.spring_boot_api.model.VoteId;
import fr.n7.spring_boot_api.model.Event;
import fr.n7.spring_boot_api.model.User;


public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    List<Vote> findByEvent(Event event);
    List<Vote> findByUser(User user);

    Optional<Vote> findByUserAndEvent(User user, Event event);
}
