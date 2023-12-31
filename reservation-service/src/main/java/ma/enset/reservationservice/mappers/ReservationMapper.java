package ma.enset.reservationservice.mappers;


import ma.enset.reservationservice.dtos.ReservationRequestDto;
import ma.enset.reservationservice.dtos.ReservationResponseDto;
import ma.enset.reservationservice.entities.Reservation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public ReservationResponseDto from(Reservation reservation){
        return modelMapper.map(reservation, ReservationResponseDto.class);
    }

    public Reservation to(ReservationRequestDto reservationRequestDto){
        return modelMapper.map(reservationRequestDto, Reservation.class);
    }
}
