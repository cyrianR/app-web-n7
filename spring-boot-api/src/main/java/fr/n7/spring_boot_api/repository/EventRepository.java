package fr.n7.spring_boot_api.repository;

import java.util.List;
import java.util.Optional;
import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.n7.spring_boot_api.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    List<Event> findByNameContaining(String name);
    
    Optional<Event> findById(long id);

    List<Event> findByDateGreaterThanEqualAndDateLessThan(ZonedDateTime start, ZonedDateTime end);
}
