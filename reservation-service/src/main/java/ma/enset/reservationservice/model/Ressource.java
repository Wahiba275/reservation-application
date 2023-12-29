package ma.enset.reservationservice.model;

import lombok.*;
import ma.enset.reservationservice.enums.TypeResource;

@Data
public class Ressource {
    private Long id ;
    private String nom ;
    private TypeResource typeResource;
}
