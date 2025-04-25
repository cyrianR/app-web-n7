package fr.n7.spring_boot_api.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.datafaker.Faker;
import fr.n7.spring_boot_api.model.ERole;
import fr.n7.spring_boot_api.model.Role;
import fr.n7.spring_boot_api.model.Tutorial;
import fr.n7.spring_boot_api.repository.RoleRepository;
import fr.n7.spring_boot_api.repository.TutorialRepository;
import fr.n7.spring_boot_api.repository.UserRepository;
import fr.n7.spring_boot_api.model.User;

@Component
@Profile("dev")
public class SeedDatasourceDev implements CommandLineRunner{

    Faker faker = new Faker();

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    TutorialRepository tutorialRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding database for development environment...");

        System.out.println("Loading Role data...");
        SeederUtils.loadRoles(roleRepo);

        System.out.println("Loading Tutorial data...");
        tutorialRepo.deleteAll();
        loadTutorialData(10);

        System.out.println("Loading User data...");
        userRepo.deleteAll();
        loadUserData(10);
        loadAdminUsersData();

        System.out.println("Seeding completed.");
    }

    private void loadTutorialData(int numTutorials) {
        for (int i = 0; i < numTutorials; i++) {
            tutorialRepo.save(new Tutorial(faker.zelda().game(), faker.lorem().paragraph(), faker.bool().bool()));
        }
    }

    private void loadUserData(int numUsers) {
        for (int i = 0; i < numUsers; i++) {
            String uniqueUsername = faker.name().username() + i;
            String uniqueEmail = faker.internet().emailAddress(uniqueUsername);
            Role userRole;
            User user = new User(uniqueUsername, uniqueEmail, encoder.encode(faker.internet().password()));
            // choose random role between MEMBER and EXTERN
            if (faker.bool().bool()) {
                userRole = roleRepo.findByName(ERole.EXTERN).orElseThrow(() -> new RuntimeException("Error: Role EXTERN is not found during seeding."));
            } else {
                userRole = roleRepo.findByName(ERole.MEMBER).orElseThrow(() -> new RuntimeException("Error: Role MEMBER is not found during seeding."));
            }
            user.addRole(userRole);
            userRepo.save(user);
        }
    }

    private void loadAdminUsersData() {
        // create global admin
        String uniqueUsername = "admin";
        String uniqueEmail = faker.internet().emailAddress(uniqueUsername);
        User adminUser = new User(uniqueUsername, uniqueEmail, encoder.encode("admin123"));
        Role adminRole = roleRepo.findByName(ERole.ADMIN).orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found during seeding."));
        adminUser.addRole(adminRole);
        userRepo.save(adminUser);

        // create karaoke admin
        uniqueUsername = "adminKaraoke";
        uniqueEmail = faker.internet().emailAddress(uniqueUsername);
        adminUser = new User(uniqueUsername, uniqueEmail, encoder.encode("admin123"));
        adminRole = roleRepo.findByName(ERole.KAROKE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role KAROKE_ADMIN is not found during seeding."));
        adminUser.addRole(adminRole);
        userRepo.save(adminUser);

        // create karaoke admin
        uniqueUsername = "adminLesson";
        uniqueEmail = faker.internet().emailAddress(uniqueUsername);
        adminUser = new User(uniqueUsername, uniqueEmail, encoder.encode("admin123"));
        adminRole = roleRepo.findByName(ERole.LESSON_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role LESSON_ADMIN is not found during seeding."));
        adminUser.addRole(adminRole);
        userRepo.save(adminUser);
    }

}
