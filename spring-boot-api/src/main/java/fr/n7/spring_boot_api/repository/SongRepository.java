package fr.n7.spring_boot_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.n7.spring_boot_api.model.Song;

public interface SongRepository extends JpaRepository<Song, Long> { 

    Song findByTitle(String title);
    Song findByUrl(String url);
    Song findBySongwriter(String songwriter);
    List<Song> findByTitleContaining(String title);
}
