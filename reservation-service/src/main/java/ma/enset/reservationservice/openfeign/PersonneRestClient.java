package ma.enset.reservationservice.openfeign;

import ma.enset.reservationservice.entities.Personne;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservations-service")
public interface PersonneRestClient {
    @GetMapping("/Personnes/{id}")
    Personne personneById(@PathVariable Long id);
}
