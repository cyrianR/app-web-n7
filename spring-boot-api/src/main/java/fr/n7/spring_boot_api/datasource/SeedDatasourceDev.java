package fr.n7.spring_boot_api.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.datafaker.Faker;
import fr.n7.spring_boot_api.model.*;
import fr.n7.spring_boot_api.repository.*;

import java.util.List;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;
import java.time.Instant;

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
    EventRepository eventRepo;

    @Autowired
    VoteRepository voteRepo;
    
    @Autowired
    LessonRepository lessonRepo;

    @Autowired
    PostRepository postRepo;

    int numTutorials = 10;
    int numUsers = 1;
    int numEvents = 10;
    int numVotes = 10;
    int numLessons = 10;
    int numPosts = 10;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding database for development environment...");

        System.out.println("Loading Role data...");
        SeederUtils.loadRoles(roleRepo);

        System.out.println("Loading Tutorial data...");
        loadTutorialData(numTutorials);

        System.out.println("Loading User data...");
        loadUserData(numUsers);
        loadAdminUsersData();

        System.out.println("Loading Event data...");
        loadEventData(numEvents);

        System.out.println("Loading Vote data...");
        loadVoteData(numVotes);

        System.out.println("Loading Lesson data...");
        loadLessonData(numLessons);

        System.out.println("Loading Post data...");
        loadPostData(numPosts);

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
                userRole = roleRepo.findByName(ERole.ROLE_EXTERN).orElseThrow(() -> new RuntimeException("Error: Role EXTERN is not found during seeding."));
            } else {
                userRole = roleRepo.findByName(ERole.ROLE_MEMBER).orElseThrow(() -> new RuntimeException("Error: Role MEMBER is not found during seeding."));
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
        Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found during seeding."));
        adminUser.addRole(adminRole);
        userRepo.save(adminUser);

        // create karaoke admin
        uniqueUsername = "adminKaraoke";
        uniqueEmail = faker.internet().emailAddress(uniqueUsername);
        adminUser = new User(uniqueUsername, uniqueEmail, encoder.encode("admin123"));
        adminRole = roleRepo.findByName(ERole.ROLE_KAROKE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role KAROKE_ADMIN is not found during seeding."));
        adminUser.addRole(adminRole);
        userRepo.save(adminUser);

        // create lesson admin
        uniqueUsername = "adminLesson";
        uniqueEmail = faker.internet().emailAddress(uniqueUsername);
        adminUser = new User(uniqueUsername, uniqueEmail, encoder.encode("admin123"));
        adminRole = roleRepo.findByName(ERole.ROLE_LESSON_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role LESSON_ADMIN is not found during seeding."));
        adminUser.addRole(adminRole);
        userRepo.save(adminUser);
    }

    private void loadEventData(int numEvents) {

        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime max = now.plusDays(30);

        long startEpoch = now.toEpochSecond();
        long endEpoch = max.toEpochSecond();

        for (int i = 0; i < numEvents; i++) {
            long randomEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);
            ZonedDateTime randomDateTime = Instant.ofEpochSecond(randomEpoch).atZone(zone);

            eventRepo.save(new Event(faker.book().title(),
                randomDateTime,
                EventType.values()[faker.number().numberBetween(0, EventType.values().length)],
                faker.lorem().sentence()));
        }
    }

    private void loadVoteData(int numVotes) {
        List<Event> events = eventRepo.findAll();
        List<User> users = userRepo.findAll();
        for (int i = 0; i < numVotes; i++) {
            Event event = events.get(faker.number().numberBetween(0, numEvents - 1));
            voteRepo.save(new Vote(event,
                users.get(faker.number().numberBetween(0, numUsers -1))));
            event.setNote(event.getNote() + 1);
            eventRepo.save(event);
        }
    }

    private void loadLessonData(int numLessons) {
        for (int i = 0; i < numLessons; i++) {
            lessonRepo.save(new Lesson(faker.book().title(), faker.file().fileName(),
                faker.file().fileName(),
                faker.file().fileName(),
                faker.file().fileName()));
        }
    }

    private void loadPostData(int numPosts) {
        List<User> users = userRepo.findAll();
        for (int i = 0; i < numPosts; i++) {
            postRepo.save(new Post(faker.book().title(), faker.date().future(30, java.util.concurrent.TimeUnit.DAYS).toString(), users.get(faker.number().numberBetween(0, numUsers -1))));
        }
    }

}
