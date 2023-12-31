package ma.enset.ressourceservice.service;





import ma.enset.ressourceservice.dtos.RessourceResponseDTO;

import java.util.List;

public interface RessourceService {
    List<RessourceResponseDTO> getRessources();
    RessourceResponseDTO getRessourceById(Long id);
}
