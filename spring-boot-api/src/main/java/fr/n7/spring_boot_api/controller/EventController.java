package fr.n7.spring_boot_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

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

import fr.n7.spring_boot_api.model.Event;
import fr.n7.spring_boot_api.model.EventType;
import fr.n7.spring_boot_api.repository.EventRepository;
import fr.n7.spring_boot_api.payload.EventDTO;
import fr.n7.spring_boot_api.payload.EventMapper;

// filter authorized origin
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class EventController {
    
    @Autowired
    EventRepository eventRepository;

    // Get all events with optional filter by name
    @GetMapping("/event")
    public ResponseEntity<List<EventDTO>> getAllEvents(@RequestParam(required = false) String name) {
        try {
            List<Event> events = new ArrayList<Event>();

            if (name == null)
                eventRepository.findAll().forEach(events::add);
            else
                eventRepository.findByNameContaining(name).forEach(events::add);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<EventDTO> eventResponses = events.stream()
            .map(EventMapper::toResponse)
            .collect(Collectors.toList());

            return new ResponseEntity<>(eventResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all events between two dates
    @GetMapping("/event/between")
    public ResponseEntity<List<EventDTO>> getEventsBetweenDates(
            @RequestParam String start,
            @RequestParam String end) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            ZonedDateTime startDate = ZonedDateTime.parse(start, formatter);
            ZonedDateTime endDate = ZonedDateTime.parse(end, formatter);

            List<Event> events = eventRepository.findByDateGreaterThanEqualAndDateLessThan(startDate, endDate);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<EventDTO> eventResponses = events.stream()
            .map(EventMapper::toResponse)
            .collect(Collectors.toList());
            
            return new ResponseEntity<>(eventResponses, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all future events of a specific type
    @GetMapping("/event/future")
    public ResponseEntity<List<EventDTO>> getFutureEventsByType(@RequestParam String eventType) {
        try {
            ZoneId zone = ZoneId.systemDefault();
            ZonedDateTime now = ZonedDateTime.now(zone);

            fr.n7.spring_boot_api.model.EventType type = fr.n7.spring_boot_api.model.EventType.valueOf(eventType);
            List<Event> events = eventRepository.findByEventTypeAndDateAfter(type, now);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<EventDTO> eventResponses = events.stream()
            .map(EventMapper::toResponse)
            .collect(Collectors.toList());

            // Take the first 5 events
            if (eventResponses.size() > 5) {
                eventResponses = eventResponses.subList(0, 5);
            }

            return new ResponseEntity<>(eventResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get event by ID
    @GetMapping("/event/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("id") long id) {
        Optional<Event> eventData = eventRepository.findById(id);

        if (eventData.isPresent()) {
            EventDTO dto = EventMapper.toResponse(eventData.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new event
    @PostMapping("/event")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO event) {
        try {
            Event _event = eventRepository.save(new Event(event.getName(), event.getDate(), event.getEventType(), event.getDescription()));
            return new ResponseEntity<>(EventMapper.toResponse(_event), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an event
    @PutMapping("/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") long id, @RequestBody EventDTO event) {
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

    // Delete an event
    @DeleteMapping("/event/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") long id) {
        try {
            // Check if event exists
            Optional<Event> eventData = eventRepository.findById(id);
            if (!eventData.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            eventRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
