package ma.enset.ressourceservice.mappers;



import ma.enset.ressourceservice.dtos.RessourceResponseDTO;
import ma.enset.ressourceservice.entities.Ressource;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RessourceMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public RessourceResponseDTO from(Ressource ressource ){
        return modelMapper.map(ressource, RessourceResponseDTO.class);
    }

    public Ressource to(RessourceResponseDTO ressourceResponseDTO){
        return modelMapper.map(ressourceResponseDTO, Ressource.class);
    }
}
