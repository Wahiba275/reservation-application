package ma.enset.reservationservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.*;
import ma.enset.reservationservice.model.Ressource;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reservation {
    @Id
    private String id;
    private String nom;

    private String contexte;
    private Date date;
    private int duree;
    private Long idPersonne;
    private Long resourceId;

    @Transient
    private Ressource ressource;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Personne personne;

}
