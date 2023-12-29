package ma.enset.ressourceservice;

import ma.enset.ressourceservice.entities.Ressource;
import ma.enset.ressourceservice.enums.TypeResource;
import ma.enset.ressourceservice.repository.ResourceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class RessourceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RessourceServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(ResourceRepository resourceRepository ){
		return args -> {
			resourceRepository.saveAll(List.of(

					Ressource.builder().nom("Resource1")
							.typeResource(Math.random() > 0.5 ? TypeResource.MATERIEL_INF0 : TypeResource.MATERIEL_AUDIO_VUSUEL)
							.build(),
					Ressource.builder().nom("Resource2")
							.typeResource(Math.random() > 0.3 ? TypeResource.MATERIEL_INF0 : TypeResource.MATERIEL_AUDIO_VUSUEL)
							.build()
			));
			resourceRepository.findAll().forEach(System.out::println);
		};
	}
}
