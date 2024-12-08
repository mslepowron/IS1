package ar.uba.fi.ingsoft1.product.rules.service;

import ar.uba.fi.ingsoft1.product.rules.entity.RuleQuantityEntity;
import ar.uba.fi.ingsoft1.product.rules.model.RuleQuantityDTO;
import ar.uba.fi.ingsoft1.product.rules.repository.RuleQuantityRepository;
import ar.uba.fi.ingsoft1.product.rules.repository.RuleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleQuantityService {

    private static final Logger log = LoggerFactory.getLogger(RuleQuantityService.class);

    private final RuleQuantityRepository ruleQuantityRepository;
    private final ObjectMapper objectMapper;

    public RuleQuantityService(RuleQuantityRepository ruleQuantityRepository, ObjectMapper objectMapper) {
        this.ruleQuantityRepository = ruleQuantityRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Save a rule quantity to the database.
     * @param ruleQuantity: Rule quantity to save.
     * @return The saved RuleQuantityDTO.
     */
    @Transactional
    public RuleQuantityDTO saveQuantity(RuleQuantityDTO ruleQuantity) {
        RuleQuantityEntity entity = mapDtoToEntity(ruleQuantity);
        RuleQuantityEntity savedEntity = ruleQuantityRepository.save(entity);
        log.info("Regla de cantidad guardada: {}", ruleQuantity);
        return mapEntityToDto(savedEntity); // Devuelve el DTO guardado
    }

    /**
     * Get all rule quantities from the database.
     * @return List of RuleQuantityDTO.
     */
    public List<RuleQuantityDTO> getAllQuantities() {
        List<RuleQuantityEntity> entities = ruleQuantityRepository.findAll();
        return entities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private RuleQuantityEntity mapDtoToEntity(RuleQuantityDTO dto) {
        RuleQuantityEntity entity = new RuleQuantityEntity();
        entity.setId(dto.getId());
        entity.setMaxQuantityPerProduct(dto.getMaxQuantityPerProduct());
        entity.setMaxQuantityPerOrder(dto.getMaxQuantityPerOrder());
        return entity;
    }

    private RuleQuantityDTO mapEntityToDto(RuleQuantityEntity entity) {
        RuleQuantityDTO dto = new RuleQuantityDTO();
        dto.setId(entity.getId());
        dto.setMaxQuantityPerProduct(entity.getMaxQuantityPerProduct());
        dto.setMaxQuantityPerOrder(entity.getMaxQuantityPerOrder());
        return dto;
    }
}
