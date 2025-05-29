package fr.n7.spring_boot_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.security.access.prepost.PreAuthorize;

import fr.n7.spring_boot_api.model.Event;
import fr.n7.spring_boot_api.model.Post;
import fr.n7.spring_boot_api.payload.response.UserResponse;
import fr.n7.spring_boot_api.repository.EventRepository;
import fr.n7.spring_boot_api.repository.PostRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostRepository postRepository;

    // Get all posts
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        try {
            List<Post> posts = new ArrayList<Post>();

            postRepository.findAll().forEach(posts::add);

            if (posts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get post by ID
    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable("id") long id) {
        Optional<Post> postData = postRepository.findById(id);

        if (postData.isPresent()) {
            return new ResponseEntity<>(postData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Create a new post
    @PostMapping("/post")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        try {
            Post _post = postRepository.save(new Post(post.getDescription(), post.getDate(), post.getAuthor()));
            return new ResponseEntity<>(_post, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update post by ID
    @PutMapping("/post/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Post> updatePost(@PathVariable("id") long id, @RequestBody Post post) {
        Optional<Post> postData = postRepository.findById(id);

        if (postData.isPresent()) {
            Post _post = postData.get();
            _post.setDate(post.getDate());
            _post.setAuthor(post.getAuthor());
            _post.setEvents(post.getEvents());
            _post.setDescription(post.getDescription());
            return new ResponseEntity<>(postRepository.save(_post), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add event to post
    @PutMapping("/post/{id}/addEvent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Post> addEvent(@PathVariable("id") long id, @RequestBody Event event) {
        Optional<Post> postData = postRepository.findById(id);
        if (postData.isPresent()) {
            Post post = postData.get();
            post.addEvent(event);
            return new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Remove event from post
    @PutMapping("/post/{id}/removeEvent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Post> deleteEvent(@PathVariable("id") long id, @RequestBody Event event) {
        Optional<Post> postData = postRepository.findById(id);
        if (postData.isPresent()) {
            Post post = postData.get();
            post.deleteEvent(event);
            return new ResponseEntity<>(postRepository.save(post), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a post
    @DeleteMapping("/post/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") long id) {
        try {
            postRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
