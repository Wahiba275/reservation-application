package ma.enset.reservationservice.mappers;


import ma.enset.reservationservice.dtos.PersonneResponseDto;
import ma.enset.reservationservice.entities.Personne;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonneMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public PersonneResponseDto from(Personne personne){
        return modelMapper.map(personne, PersonneResponseDto.class);
    }

    public Personne to(PersonneResponseDto personneResponseDto){
        return modelMapper.map(personneResponseDto, Personne.class);
    }
}
