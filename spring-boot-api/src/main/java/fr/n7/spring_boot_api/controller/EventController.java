package fr.n7.spring_boot_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.n7.spring_boot_api.model.Vote;
import fr.n7.spring_boot_api.model.Event;
import fr.n7.spring_boot_api.repository.VoteRepository;
import fr.n7.spring_boot_api.repository.EventRepository;

// filter authorized origin
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class EventController {
    
    @Autowired
    EventRepository eventRepository;
    @Autowired
    VoteRepository voteRepository;

    // Get all events with optional filter by name
    @GetMapping("/event")
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(required = false) String name) {
        try {
            List<Event> events = new ArrayList<Event>();

            if (name == null)
                eventRepository.findAll().forEach(events::add);
            else
                eventRepository.findByNameContaining(name).forEach(events::add);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get event by ID
    @GetMapping("/event/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") long id) {
        Optional<Event> eventData = eventRepository.findById(id);

        if (eventData.isPresent()) {
            return new ResponseEntity<>(eventData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new event
    @PostMapping("/event")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            Event _event = eventRepository.save(new Event(event.getName(), event.getDate(), event.getEventType(), event.getDescription()));
            return new ResponseEntity<>(_event, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an event
    @PutMapping("/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") long id, @RequestBody Event event) {
        Optional<Event> eventData = eventRepository.findById(id);

        if (eventData.isPresent()) {
            Event _event = eventData.get();
            _event.setName(event.getName());
            _event.setDate(event.getDate());
            _event.setEventType(event.getEventType());
            _event.setDescription(event.getDescription());
            return new ResponseEntity<>(eventRepository.save(_event), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete an event and all its votes
    @DeleteMapping("/event/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") long id) {
        try {
            // Check if event exists
            Optional<Event> eventData = eventRepository.findById(id);
            if (!eventData.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // Delete all votes associated with the event
            List<Vote> votes = voteRepository.findByEvent(eventData.get());
            for (Vote vote : votes) {
                voteRepository.deleteById(vote.getId());
            }
            eventRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
