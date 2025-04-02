package fr.n7.spring_boot_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.n7.spring_boot_api.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    List<User> findByUsernameContaining(String username);
    
    User findById(long id);
}
