package fr.n7.spring_boot_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.n7.spring_boot_api.model.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
    List<Lesson> findByTitle(String file);
    List<Lesson> findByTitleContaining(String file);
    List<Lesson> findAllByOrderByOrderNumAsc();
    Optional<Lesson> findById(long id);
}
