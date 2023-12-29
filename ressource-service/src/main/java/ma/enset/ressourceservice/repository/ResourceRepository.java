package ma.enset.ressourceservice.repository;


import ma.enset.ressourceservice.entities.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Ressource, Long> {
}
