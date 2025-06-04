package fr.n7.spring_boot_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.n7.spring_boot_api.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {  
    Optional<Post> findById(long id);
    Optional<List<Post>> findTop10ByOrderByDateDesc();
    Optional<List<Post>> findByOrderByDateDesc();
}
