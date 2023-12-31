package ma.enset.reservationservice.repositories;

import ma.enset.reservationservice.entities.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonneRepository extends JpaRepository<Personne , Long> {
}
