package fr.n7.spring_boot_api.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.datafaker.Faker;
import fr.n7.spring_boot_api.model.*;
import fr.n7.spring_boot_api.repository.*;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
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
    LessonRepository lessonRepo;

    @Autowired
    PostRepository postRepo;

    @Autowired
    AnimeRepository animeRepo;

    int numTutorials = 10;
    int numUsers = 1;
    int numEvents = 10;
    int numLikes = 6;
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

        System.out.println("Loading Event data...");
        loadEventData(numEvents);

        System.out.println("Loading User data...");
        loadUserData(numUsers);
        loadAdminUsersData();

        System.out.println("Loading Lesson data...");
        loadLessonData(numLessons);

        System.out.println("Loading Post data...");
        loadPostData(numPosts);

        System.out.println("Loading Anime data...");
        loadAnimeData();

        System.out.println("Seeding completed.");
    }

    private void loadTutorialData(int numTutorials) {
        for (int i = 0; i < numTutorials; i++) {
            tutorialRepo.save(new Tutorial(faker.zelda().game(), faker.lorem().paragraph(), faker.bool().bool()));
        }
    }

    private void loadUserData(int numUsers) {
        for (int i = 0; i < numUsers; i++) {
            List<Event> allEvents = eventRepo.findAll();  // Get seeded events

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

            // Add random likes (between 0 and numLikes)
            if (!allEvents.isEmpty()) {
                int likesCount = faker.number().numberBetween(0, numLikes);
                Set<Event> likedEvents = new HashSet<>();
                
                while (likedEvents.size() < likesCount) {
                    Event randomEvent = allEvents.get(
                        faker.number().numberBetween(0, allEvents.size())
                    );
                    likedEvents.add(randomEvent);
                }
                
                user.getLikedEvents().addAll(likedEvents);
            }

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

    private void loadLessonData(int numLessons) {
        for (int i = 0; i < numLessons; i++) {
            lessonRepo.save(new Lesson((i+1) + " - " + faker.book().title(), faker.file().fileName(),
                faker.file().fileName(),
                faker.file().fileName(),
                faker.file().fileName(),i+1));
        }
    }

    private void loadPostData(int numPosts) {
        List<User> users = userRepo.findAll();
        for (int i = 0; i < numPosts; i++) {
            postRepo.save(new Post(faker.book().title(), faker.date().future(30, java.util.concurrent.TimeUnit.DAYS).toString(), users.get(faker.number().numberBetween(0, numUsers -1))));
        }
    }

    private void loadAnimeData() {
        animeRepo.save(new Anime("Attack on Titan", 88, 88, "https://myanimelist.net/anime/16498/Shingeki_no_Kyojin"));
        animeRepo.save(new Anime("Death Note", 37, 37, "https://myanimelist.net/anime/1535/Death_Note"));
        animeRepo.save(new Anime("Naruto", 220, 219, "https://myanimelist.net/anime/20/Naruto"));
        animeRepo.save(new Anime("One Piece", 1000, 1000, "https://myanimelist.net/anime/21/One_Piece"));
        animeRepo.save(new Anime("Demon Slayer", 26, 3, "https://myanimelist.net/anime/38000/Kimetsu_no_Yaiba"));
    }

}
