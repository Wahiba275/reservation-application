package ma.enset.ressourceservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import ma.enset.ressourceservice.enums.TypeResource;


@Entity
@Data @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class Ressource {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String nom ;
    private TypeResource typeResource;
}
