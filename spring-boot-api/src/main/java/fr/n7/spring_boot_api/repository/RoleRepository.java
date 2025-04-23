package fr.n7.spring_boot_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.n7.spring_boot_api.model.ERole;
import fr.n7.spring_boot_api.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
