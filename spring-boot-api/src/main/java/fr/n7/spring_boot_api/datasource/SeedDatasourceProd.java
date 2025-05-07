package fr.n7.spring_boot_api.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import fr.n7.spring_boot_api.repository.RoleRepository;

@Component
@Profile("prod")
public class SeedDatasourceProd implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding database for production environment...");

        System.out.println("Loading Role data...");
        SeederUtils.loadRoles(roleRepo);
    }
    
}
