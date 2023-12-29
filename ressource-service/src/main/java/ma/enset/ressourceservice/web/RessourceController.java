package ma.enset.ressourceservice.web;


import ma.enset.ressourceservice.dtos.RessourceResponseDTO;
import ma.enset.ressourceservice.repository.ResourceRepository;
import ma.enset.ressourceservice.service.RessourceServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RessourceController {
    private ResourceRepository resourceRepository;
    private RessourceServiceImpl ressourceService;

    public RessourceController(ResourceRepository resourceRepository, RessourceServiceImpl ressourceService) {
        this.resourceRepository = resourceRepository;
        this.ressourceService = ressourceService;
    }
    @GetMapping("/ressources")
    public List<RessourceResponseDTO> getAllRessource() {
        return ressourceService.getRessources();
    }
    @GetMapping("/ressources/{id}")
    public RessourceResponseDTO getRessourceById(@PathVariable Long id){
        return ressourceService.getRessourceById(id);
    }

}
