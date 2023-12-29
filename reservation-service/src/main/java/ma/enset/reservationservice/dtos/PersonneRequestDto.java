package ma.enset.reservationservice.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonneRequestDto {
    private String nom;
    private String email;
    private String fonction;

}
