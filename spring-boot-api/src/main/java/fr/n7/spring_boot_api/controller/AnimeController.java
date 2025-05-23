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

import fr.n7.spring_boot_api.model.Anime;
import fr.n7.spring_boot_api.repository.AnimeRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AnimeController {

    @Autowired
    AnimeRepository animeRepository;

    // Get anime by ID
    @GetMapping("/anime/{id}")
    public ResponseEntity<Anime> getAnimeById(@PathVariable("id") long id) {
        Optional<Anime> animeData = animeRepository.findById(id);
        return animeData.map(anime -> new ResponseEntity<>(anime, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all animes that are finished ordered by name
    @GetMapping("/anime/finished")
    public ResponseEntity<List<Anime>> getAllFinishedAnimes() {
        List<Anime> animes = animeRepository.findAllByOrderByNameAsc();
        // filter animes that are not finished
        animes.removeIf(anime -> anime.getCurrentEpisode() != anime.getNbEpisodes());
        if (animes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(animes, HttpStatus.OK);
        }
    }

    // Get all animes that are not finished ordered by name
    @GetMapping("/anime/not-finished")
    public ResponseEntity<List<Anime>> getAllNotFinishedAnimes() {
        List<Anime> animes = animeRepository.findAllByOrderByNameAsc();
        // filter animes that are finished
        animes.removeIf(anime -> anime.getCurrentEpisode() == anime.getNbEpisodes());
        if (animes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(animes, HttpStatus.OK);
        }
    }

    // Create a new anime
    @PostMapping("/anime")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> createAnime(@RequestBody Anime anime) {
        try {
            Anime newAnime = animeRepository.save(new Anime(anime.getName(), anime.getNbEpisodes(), anime.getCurrentEpisode(), anime.getMalLink()));
            return new ResponseEntity<>(newAnime, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an anime
    @PutMapping("/anime/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> updateAnime(@PathVariable("id") long id, @RequestBody Anime anime) {
        Optional<Anime> animeData = animeRepository.findById(id);
        if (animeData.isPresent()) {
            Anime _anime = animeData.get();
            _anime.setName(anime.getName());
            _anime.setNbEpisodes(anime.getNbEpisodes());
            _anime.setCurrentEpisode(anime.getCurrentEpisode());
            _anime.setMalLink(anime.getMalLink());
            return new ResponseEntity<>(animeRepository.save(_anime), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete an anime
    @DeleteMapping("/anime/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAnime(@PathVariable("id") long id) {
        try {
            animeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}