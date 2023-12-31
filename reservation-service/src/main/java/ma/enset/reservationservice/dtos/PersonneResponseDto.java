package ma.enset.reservationservice.dtos;

import lombok.*;
import ma.enset.reservationservice.entities.Reservation;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonneResponseDto {
    private Long id;
    private String nom;
    private String email;
    private String fonction;
    private List<Reservation> reservations;
}
