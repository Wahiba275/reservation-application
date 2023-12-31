package ma.enset.reservationservice.dtos;

import lombok.*;
import ma.enset.reservationservice.entities.Personne;
import ma.enset.reservationservice.model.Ressource;

import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ReservationResponseDto {
    private Long id;
    private String nom;
    private String contexte;
    private Date date;
    private Long duree;
    private Long idPersonne;
    private Long resourceId;
    private Ressource ressource;
    private Personne personne;
}
