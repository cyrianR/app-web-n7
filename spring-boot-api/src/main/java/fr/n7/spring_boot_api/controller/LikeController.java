package fr.n7.spring_boot_api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RestController;

import fr.n7.spring_boot_api.model.Event;
import fr.n7.spring_boot_api.model.User;
import fr.n7.spring_boot_api.repository.EventRepository;
import fr.n7.spring_boot_api.repository.UserRepository;
import fr.n7.spring_boot_api.payload.EventDTO;
import fr.n7.spring_boot_api.payload.EventMapper;

// filter authorized origin
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    // Like an event
    @PostMapping("/like/user/{userId}/event/{eventId}")
    public ResponseEntity<?> likeEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (userOpt.isPresent() && eventOpt.isPresent()) {
            User user = userOpt.get();
            Event event = eventOpt.get();

            if (user.getLikedEvents().contains(event)) {
                return new ResponseEntity<>("Already liked", HttpStatus.CONFLICT);
            }

            user.getLikedEvents().add(event);
            userRepository.save(user);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Unlike an event
    @DeleteMapping("/like/user/{userId}/event/{eventId}")
    public ResponseEntity<?> unlikeEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (userOpt.isPresent() && eventOpt.isPresent()) {
            User user = userOpt.get();
            Event event = eventOpt.get();

            if (!user.getLikedEvents().contains(event)) {
                return new ResponseEntity<>("Not liked", HttpStatus.NOT_FOUND);
            }

            user.getLikedEvents().remove(event);
            userRepository.save(user);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get all events liked by a user
    @GetMapping("/like/user/{userId}")
    public ResponseEntity<?> getLikedEventsByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            List<EventDTO> eventResponses = userOpt.get().getLikedEvents().stream()
            .map(EventMapper::toResponse)
            .collect(Collectors.toList());
        return new ResponseEntity<>(eventResponses, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get all users who liked an event
    @GetMapping("/like/event/{eventId}")
    public ResponseEntity<?> getUsersWhoLikedEvent(@PathVariable Long eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            return new ResponseEntity<>(eventOpt.get().getLikedBy(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // If a user liked a specific event
    @GetMapping("/like/user/{userId}/event/{eventId}")
    public ResponseEntity<?> hasUserLikedEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (userOpt.isPresent() && eventOpt.isPresent()) {
            boolean liked = userOpt.get().getLikedEvents().contains(eventOpt.get());
            return new ResponseEntity<>(liked, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
