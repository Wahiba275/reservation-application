package ma.enset.reservationservice;

import ma.enset.reservationservice.entities.Personne;
import ma.enset.reservationservice.entities.Reservation;
import ma.enset.reservationservice.repositories.PersonneRepository;
import ma.enset.reservationservice.repositories.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(ReservationRepository reservationRepository, PersonneRepository personneRepository){
		return args -> {

				Personne p1=Personne.builder().nom("personne1").email("personne1@gmail.com").fonction("fonction1").build();
				Personne p2=Personne.builder().nom("personne2").email("personne2@gmail.com").fonction("fonction2").build();
				//personneRepository.saveAll(p1,p2);
				personneRepository.save(p1);
				personneRepository.save(p2);
				reservationRepository.saveAll(List.of(
					Reservation.builder().id(UUID.randomUUID().toString()).nom("reservation1").contexte("context1").date(new Date()).duree(3).idPersonne(p1.getId()).resourceId(1L).build(),
					Reservation.builder().id(UUID.randomUUID().toString()).nom("reservation2").contexte("context2").date(new Date()).duree(5).idPersonne(p2.getId()).resourceId(2L).build()
			));
		};
	}
}
