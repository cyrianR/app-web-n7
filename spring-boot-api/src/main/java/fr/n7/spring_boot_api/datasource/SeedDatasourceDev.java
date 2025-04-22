package fr.n7.spring_boot_api.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.datafaker.Faker;

import fr.n7.spring_boot_api.model.Tutorial;
import fr.n7.spring_boot_api.repository.*;
import fr.n7.spring_boot_api.model.*;

@Component
@Profile("dev")
public class SeedDatasourceDev implements CommandLineRunner{

    Faker faker = new Faker();

    @Autowired
    TutorialRepository tutorialRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    EventRepository eventRepo;
    @Autowired
    VoteRepository voteRepo;

    int numTutorials = 10;
    int numUsers = 1;
    int numEvents = 10;
    int numVotes = 10;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding database for development environment...");

        System.out.println("Loading Tutorial data...");
        loadTutorialData(numTutorials);

        System.out.println("Loading User data...");
        loadUserData(numUsers);

        System.out.println("Loading Event data...");
        loadEventData(numEvents);

        System.out.println("Loading Vote data...");
        loadVoteData(numVotes);

        System.out.println("Seeding completed.");
    }

    private void loadTutorialData(int numTutorials) {
        for (int i = 0; i < numTutorials; i++) {
            tutorialRepo.save(new Tutorial(faker.zelda().game(), faker.lorem().paragraph(), faker.bool().bool()));
        }
    }

    private void loadUserData(int numUsers) {
        for (int i = 0; i < numUsers; i++) {
            userRepo.save(new User(faker.name().username(), faker.internet().emailAddress(), faker.internet().password()));
        }
    }

    private void loadEventData(int numEvents) {
        for (int i = 0; i < numEvents; i++) {
            eventRepo.save(new Event(faker.book().title(), faker.date().future(30, java.util.concurrent.TimeUnit.DAYS).toString(), EventType.values()[faker.number().numberBetween(0, EventType.values().length)], faker.lorem().paragraph()));
        }
    }

    private void loadVoteData(int numVotes) {
        for (int i = 0; i < numVotes; i++) {
            voteRepo.save(new Vote(((double)faker.number().numberBetween(0, 5)), eventRepo.findById(faker.number().numberBetween(1, numEvents)).orElse(null), userRepo.findById(faker.number().numberBetween(1, numUsers)).orElse(null)));
        }
    }

}
