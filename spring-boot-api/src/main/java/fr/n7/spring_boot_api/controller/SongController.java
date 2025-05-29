package fr.n7.spring_boot_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.n7.spring_boot_api.model.Song;
import fr.n7.spring_boot_api.repository.SongRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SongController {
    
    @Autowired
    SongRepository songRepository;

    // Get song by ID
    @GetMapping("/song/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable("id") long id) {
        Optional<Song> songData = songRepository.findById(id);
        if (songData.isPresent()) {
            return new ResponseEntity<>(songData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get songs by title
    @GetMapping("/song/title/{title}")
    public ResponseEntity<List<Song>> getSongsByTitle(@PathVariable("title") String title) {
        List<Song> songs = songRepository.findByTitleContaining(title);
        if (songs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }
    }

    // Get all songs
    @GetMapping("/song")
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        if (songs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }
    }

    // Create a new song
    @PostMapping("/song")
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        try {
            Song _song = songRepository.save(song);
            return new ResponseEntity<>(_song, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a song by ID
    @DeleteMapping("/song/{id}")
    public ResponseEntity<HttpStatus> deleteSong(@PathVariable("id") long id) {
        try {
            songRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
