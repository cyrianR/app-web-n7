package fr.n7.spring_boot_api.repository;

import java.util.List;
import java.util.Optional;
import java.beans.Transient;
import java.time.ZonedDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fr.n7.spring_boot_api.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    List<Event> findByNameContaining(String name);
    
    Optional<Event> findById(long id);

    @Transactional
    List<Event> findByDateGreaterThanEqualAndDateLessThan(ZonedDateTime start, ZonedDateTime end);

    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType AND e.date > :date ORDER BY e.date")
    @Transactional
    List<Event> findByEventTypeAndDateAfter(@Param("eventType") fr.n7.spring_boot_api.model.EventType eventType, @Param("date") ZonedDateTime date);
}
