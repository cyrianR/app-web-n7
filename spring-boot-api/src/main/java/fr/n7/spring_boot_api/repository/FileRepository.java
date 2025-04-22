package  fr.n7.spring_boot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.n7.spring_boot_api.model.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
    
}