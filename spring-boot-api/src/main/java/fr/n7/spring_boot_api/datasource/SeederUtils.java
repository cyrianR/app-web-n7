package fr.n7.spring_boot_api.datasource;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.n7.spring_boot_api.model.ERole;
import fr.n7.spring_boot_api.model.Role;
import fr.n7.spring_boot_api.repository.RoleRepository;

@Component
public class SeederUtils {
    
    public static void loadRoles(RoleRepository roleRepo) {
        List<ERole> roles = Arrays.asList(ERole.values());
        for (ERole role : roles) {
            if (!roleRepo.existsByName(role)) {
                roleRepo.save(new Role(role));
            }
        }
    }
    
}
