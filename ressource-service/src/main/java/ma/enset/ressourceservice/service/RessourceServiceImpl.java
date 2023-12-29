package ma.enset.ressourceservice.service;



import ma.enset.ressourceservice.dtos.RessourceResponseDTO;
import ma.enset.ressourceservice.entities.Ressource;
import ma.enset.ressourceservice.mappers.RessourceMapper;
import ma.enset.ressourceservice.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RessourceServiceImpl implements RessourceService {
    private RessourceMapper ressourceMapper ;
    private ResourceRepository resourceRepository ;

    public RessourceServiceImpl(RessourceMapper ressourceMapper, ResourceRepository resourceRepository) {
        this.ressourceMapper = ressourceMapper;
        this.resourceRepository = resourceRepository;
    }

    @Override
    public List<RessourceResponseDTO> getRessources() {
        List<Ressource> ressources = resourceRepository.findAll();
        return ressources.stream().map(ressourceMapper::from).collect(Collectors.toList());
    }

    @Override
    public RessourceResponseDTO getRessourceById(Long id) {
        Ressource ressource  = resourceRepository.findById(id).orElse(null);
        if (ressource == null) throw new RuntimeException(String.format("resource by Id : %d is not found", id));
        return ressourceMapper.from(ressource);
    }
}
