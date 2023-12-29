package ma.enset.reservationservice.service;



import ma.enset.reservationservice.dtos.PersonneResponseDto;

import java.util.List;

public interface PersonneService {
    List<PersonneResponseDto> getPersonnes();
    PersonneResponseDto getPersonneById(Long id);
}
