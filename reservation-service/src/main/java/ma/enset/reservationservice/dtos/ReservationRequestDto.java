package ma.enset.reservationservice.dtos;

import lombok.*;

import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ReservationRequestDto {
    private String nom;
    private String contexte;
    private Date date;
    private Long duree;
}
