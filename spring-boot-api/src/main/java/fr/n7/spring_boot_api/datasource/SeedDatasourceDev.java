package fr.n7.spring_boot_api.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.datafaker.Faker;

import fr.n7.spring_boot_api.model.Tutorial;
import fr.n7.spring_boot_api.repository.TutorialRepository;

@Component
@Profile("dev")
public class SeedDatasourceDev implements CommandLineRunner{

    Faker faker = new Faker();

    @Autowired
    TutorialRepository tutorialRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding database for development environment...");

        System.out.println("Loading Tutorial data...");
        loadTutorialData(10);
    }

    private void loadTutorialData(int numTutorials) {
        for (int i = 0; i < numTutorials; i++) {
            tutorialRepo.save(new Tutorial(faker.zelda().game(), faker.lorem().paragraph(), faker.bool().bool()));
        }
    }

}
