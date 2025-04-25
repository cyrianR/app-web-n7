package fr.n7.spring_boot_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import fr.n7.spring_boot_api.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContaining(String title);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM tutorials", nativeQuery = true)
    void deleteTutorials();
}

