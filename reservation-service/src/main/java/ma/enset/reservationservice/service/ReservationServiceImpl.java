package ma.enset.reservationservice.service;

import ma.enset.reservationservice.dtos.PersonneResponseDto;
import ma.enset.reservationservice.entities.Reservation;
import ma.enset.reservationservice.mappers.PersonneMapper;
import ma.enset.reservationservice.mappers.ReservationMapper;
import ma.enset.reservationservice.model.Ressource;
import ma.enset.reservationservice.repositories.ReservationRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{
    private ReservationMapper reservationMapper;
    private ReservationRepository reservationRepository;
    private PersonneService personneService;
    private PersonneMapper personneMapper;

    public ReservationServiceImpl(ReservationMapper reservationMapper, ReservationRepository reservationRepository, PersonneService personneService, PersonneMapper personneMapper) {
        this.reservationMapper = reservationMapper;
        this.reservationRepository = reservationRepository;
        this.personneService = personneService;
        this.personneMapper = personneMapper;
    }

    @Override
    public List<Reservation> getReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
//        return reservations.stream().map(reservationMapper::from).collect(Collectors.toList());
        return reservations;
    }

    @Override
    public Reservation getReservationById(String id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation == null) throw new RuntimeException(String.format("reservation by Id : %d is not found", id));
        PersonneResponseDto personne = personneService.getPersonneById(reservation.getPersonne().getId());
        reservation.setPersonne(personneMapper.to(personne));
        RestClient restClient = RestClient.create("http://localhost:8081/resources-service");
        Ressource resource = restClient.get().uri("/Resources/" + reservation.getRessource().getId()).retrieve().body(new ParameterizedTypeReference<>() {});
        reservation.setRessource(resource);
        return reservation;
    }


}
