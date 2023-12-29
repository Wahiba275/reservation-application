package ma.enset.ressourceservice.dtos;

import lombok.*;
import ma.enset.ressourceservice.enums.TypeResource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RessourceResponseDTO {
    private Long id ;
    private String nom ;
    private TypeResource typeResource;
}
