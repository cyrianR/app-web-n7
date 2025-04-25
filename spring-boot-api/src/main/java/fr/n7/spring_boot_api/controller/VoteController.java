package fr.n7.spring_boot_api.controller;

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
import org.springframework.web.bind.annotation.RestController;

import fr.n7.spring_boot_api.model.Vote;
import fr.n7.spring_boot_api.model.Event;
import fr.n7.spring_boot_api.model.User;
import fr.n7.spring_boot_api.repository.VoteRepository;
import fr.n7.spring_boot_api.repository.EventRepository;
import fr.n7.spring_boot_api.repository.UserRepository;

// filter authorized origin
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class VoteController {

    @Autowired
    VoteRepository voteRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    UserRepository userRepository;

    // Get vote by ID
    @GetMapping("/vote/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable("id") long id) {
        Optional<Vote> voteData = voteRepository.findById(id);

        if (voteData.isPresent()) {
            return new ResponseEntity<>(voteData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Get votes by event
    @GetMapping("/vote/event/{eventId}")
    public ResponseEntity<List<Vote>> getVotesByEvent(@PathVariable("eventId") long eventId) {
        Optional<Event> eventData = eventRepository.findById(eventId);

        if (eventData.isPresent()) {
            List<Vote> votes = voteRepository.findByEvent(eventData.get());
            return new ResponseEntity<>(votes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get votes by user
    @GetMapping("/vote/user/{userId}")
    public ResponseEntity<List<Vote>> getVotesByUser(@PathVariable("userId") long userId) {
        Optional<User> userData = userRepository.findById(userId);

        if (userData.isPresent()) {
            List<Vote> votes = voteRepository.findByUser(userData.get());
            return new ResponseEntity<>(votes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new vote and update event note
    @PostMapping("/vote")
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
        try {
            Optional<Event> eventData = eventRepository.findById(vote.getEvent().getId());
            Optional<User> userData = userRepository.findById(vote.getUser().getId());

            if (eventData.isPresent() && userData.isPresent()) {
                // new vote
                vote.setEvent(eventData.get());
                vote.setUser(userData.get());
                Vote _vote = voteRepository.save(vote);

                // update event note
                Event _event = eventData.get();
                List<Vote> votes = voteRepository.findByEvent(_event);
                double totalNote = 0;
                for (Vote v : votes) {
                    totalNote += v.getNote();
                }
                double averageNote = totalNote / votes.size();
                _event.setNote(averageNote);
                eventRepository.save(_event);

                return new ResponseEntity<>(_vote, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a vote and update event note
    @PutMapping("/vote/{id}")
    public ResponseEntity<Vote> updateVote(@PathVariable("id") long id, @RequestBody Vote vote) {
        Optional<Vote> voteData = voteRepository.findById(id);

        if (voteData.isPresent()) {
            Vote _vote = voteData.get();
            _vote.setNote(vote.getNote());
            _vote.setEvent(vote.getEvent());
            _vote.setUser(vote.getUser());
            Vote updatedVote = voteRepository.save(_vote);

            // update event note
            Event _event = _vote.getEvent();
            List<Vote> votes = voteRepository.findByEvent(_event);
            double totalNote = 0;
            for (Vote v : votes) {
                totalNote += v.getNote();
            }
            double averageNote = totalNote / votes.size();
            _event.setNote(averageNote);
            eventRepository.save(_event);

            return new ResponseEntity<>(updatedVote, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a vote
    @DeleteMapping("/vote/{id}")
    public ResponseEntity<HttpStatus> deleteVote(@PathVariable("id") long id) {
        try {
            voteRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
