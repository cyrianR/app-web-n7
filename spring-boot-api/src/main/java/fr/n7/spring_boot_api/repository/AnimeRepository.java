package fr.n7.spring_boot_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.n7.spring_boot_api.model.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findAllByOrderByNameAsc();
    Optional<Anime> findById(long id);
}