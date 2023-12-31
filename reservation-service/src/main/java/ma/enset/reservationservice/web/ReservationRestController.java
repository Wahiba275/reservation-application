package ma.enset.reservationservice.web;

import ma.enset.reservationservice.entities.Personne;
import ma.enset.reservationservice.entities.Reservation;
import ma.enset.reservationservice.mappers.PersonneMapper;
import ma.enset.reservationservice.repositories.PersonneRepository;
import ma.enset.reservationservice.repositories.ReservationRepository;
import ma.enset.reservationservice.openfeign.PersonneRestClient;
import ma.enset.reservationservice.service.PersonneService;
import ma.enset.reservationservice.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationRestController {
    private PersonneService personneService;
    private ReservationService reservationService;
    private PersonneMapper personneMapper;
    private PersonneRestClient personneRestClient;
    private ReservationRepository reservationRepository;
    private PersonneRepository personneRepository;

    public ReservationRestController(PersonneService personneService, ReservationService reservationService, PersonneMapper personneMapper, PersonneRestClient personneRestClient, ReservationRepository reservationRepository, PersonneRepository personneRepository) {
        this.personneService = personneService;
        this.reservationService = reservationService;
        this.personneMapper = personneMapper;
        this.personneRestClient = personneRestClient;
        this.reservationRepository = reservationRepository;
        this.personneRepository = personneRepository;
    }

    @GetMapping("/Reservations/idPersonne/{id}")
    public List<Reservation> reservationsByIdPersonne(@PathVariable Long id) {
        List<Reservation> reservationList = reservationRepository.findByIdPersonne(id);
        return reservationList;
    }


    @GetMapping("/Reservations")
    public List<Reservation> reservationsList(){
        return reservationService.getReservations();
    }

    @GetMapping("/Reservations/{id}")
    public Reservation getReservation(@PathVariable String id){
        Reservation reservation  = reservationRepository.findById(id).orElse(null);
        if (reservation != null) {
            Personne personne = personneRestClient.personneById(reservation.getIdPersonne());
            reservation.setPersonne(personne);
        }
        return reservation;
    }

    @GetMapping("/Personnes")
    public List<Personne> getAllPersonnes(){
        List<Personne> personnes = personneRepository.findAll();
        personnes.forEach(personne -> {
            List<Reservation> reservations = reservationRepository.findByIdPersonne(personne.getId());
            personne.setReservations(reservations);
        });
        return personnes;
    }

    @GetMapping("/Personnes/{id}")
    public Personne getPersonneById(@PathVariable Long id){
        Personne personne = personneRepository.findById(id).get();
        List<Reservation> reservations = reservationRepository.findByIdPersonne(personne.getId());
        personne.setReservations(reservations);
        return personne;
    }


}
