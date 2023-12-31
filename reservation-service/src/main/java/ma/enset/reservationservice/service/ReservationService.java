package ma.enset.reservationservice.service;



import ma.enset.reservationservice.entities.Reservation;

import java.util.List;

public interface ReservationService {
    List<Reservation> getReservations();
    Reservation getReservationById(String id);
}
