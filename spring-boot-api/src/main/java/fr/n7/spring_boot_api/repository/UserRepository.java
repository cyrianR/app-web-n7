package fr.n7.spring_boot_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import fr.n7.spring_boot_api.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    List<User> findByUsernameContaining(String username);
    Boolean existsByUsername(String username);
    Optional<User> findById(long id);
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users", nativeQuery = true)
    void deleteUsers();
}
