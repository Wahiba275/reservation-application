package ma.enset.reservationservice.repositories;

import ma.enset.reservationservice.dtos.PersonneResponseDto;
import ma.enset.reservationservice.entities.Personne;
import ma.enset.reservationservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface ReservationRepository  extends JpaRepository<Reservation,String> {
       List<Reservation> findByIdPersonne(Long id);
}
