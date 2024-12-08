package ar.uba.fi.ingsoft1.product.rules.service;

import ar.uba.fi.ingsoft1.product.rules.model.*;
import ar.uba.fi.ingsoft1.product.users.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.rules.entity.RuleEntity;
import ar.uba.fi.ingsoft1.product.rules.exceptions.RuleException;
import ar.uba.fi.ingsoft1.product.rules.repository.RuleRepository;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;
import ar.uba.fi.ingsoft1.product.services.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to manage rules.
 */
@Service
public class RuleService {

    private static final Logger log = LoggerFactory.getLogger(RuleService.class);
    private static final Integer MAX_QUANTITY_PER_PRODUCT = 100000;
    private static final Integer MAX_QUANTITY_PER_ORDER = 1000000000;

    private final RuleRepository ruleRepository;
    private final ObjectMapper objectMapper;
    private final RuleQuantityService ruleQuantityService;
    private final JwtService jwtService;

    public RuleService(RuleRepository ruleRepository, ObjectMapper objectMapper, RuleQuantityService ruleQuantityService, JwtService jwtService) {
        this.ruleRepository = ruleRepository;
        this.objectMapper = objectMapper;
        this.ruleQuantityService = ruleQuantityService;
        this.jwtService = jwtService;
    }

    /**
     * Save a list of rules to the database.
     * @param ruleDTOs: List of rules to save.
     * @throws JsonProcessingException if there is an error serializing the rule to JSON.
     */
    @Transactional
    public void saveRules(@RequestHeader("Authorization") String authorizationHeader,
                          List<RuleDTO> ruleDTOs) throws JsonProcessingException, ResponseStatusException{
        // Only possible by Admin
        validateRole(authorizationHeader);

        for (RuleDTO ruleDTO : ruleDTOs) {
            try {
                if (ruleDTO.getValues().isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rule values list cannot be empty");
                }
                if (hasMoreThanOneTypeOfValue(ruleDTO)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All rule values must be of the same type");
                }
                saveRule(ruleDTO); // Reutiliza el método para guardar cada regla
                log.info("Regla guardada: {}", ruleDTO);
            } catch (JsonProcessingException e) {
                log.error("Error al guardar la regla: {}", ruleDTO, e);
                throw e; // Opcional: Puedes decidir si continúas o detienes el proceso
            }
        }
    }

    @Transactional
    public void saveRule(RuleDTO ruleDTO) throws JsonProcessingException {
        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setRuleData(objectMapper.writeValueAsString(ruleDTO));
        ruleEntity.setDeleted(false); // Marcamos como no eliminado
        ruleRepository.save(ruleEntity);
    }

    /**
     * Load rules from database and map them to RuleDTO objects.
     */
    public List<RuleDTO> getAllRules() {
        List<RuleEntity> ruleEntities = findAllActiveRules();
        return ruleEntities.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Validate an order based on the rules stored on the DB.
     */
    public void validateOrder(List<Product> products) throws ResponseStatusException, RuntimeException, RuleException, IllegalArgumentException {
        try {
            log.info("1111");
            List<RuleDTO> rulesDTO = getAllRules();
            log.info("2222");
            List<Rule> rules = getAllRulesAsModel(rulesDTO);
            log.info("3333");
            RuleQuantityDTO ruleQuantityDTO = getRuleQuantity();
            log.info("4444");
            if (ruleQuantityDTO == null) {
                ruleQuantityDTO = new RuleQuantityDTO(MAX_QUANTITY_PER_PRODUCT, MAX_QUANTITY_PER_ORDER); // Valores por defecto
            }
            log.info("Reglas quantity obtenidas: {}", ruleQuantityDTO);
            RuleValidator ruleValidator = new RuleValidator(products, ruleQuantityDTO.getMaxQuantityPerProduct(), ruleQuantityDTO.getMaxQuantityPerOrder()); // TODO: recordar obtener estos valores de DB
            ruleValidator.isValidOrder(rules);
        } catch (RuleException e) {
            throw new RuleException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void updateRules(@RequestHeader("Authorization") String authorizationHeader,
                            List<RuleDTO> ruleDTOs) {
        validateRole(authorizationHeader);
        for (RuleDTO dto : ruleDTOs) {
            // Validar si la regla existe
            RuleEntity ruleEntity = ruleRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rule not found with ID: " + dto.getId()));

            // Actualizar los campos relevantes (sin alterar el campo 'deleted')
            ruleEntity.setRuleData(convertDtoToJson(dto));

            // Guardar cambios
            ruleRepository.save(ruleEntity);
        }
    }

    // Metodo que construye un JSON para la ruleData sin el id del DTO
    private String convertDtoToJson(RuleDTO dto) {
        try {
            // Construir RuleDataDTO sin el campo id
            RuleDataDTO ruleDataDTO = new RuleDataDTO(
                    dto.getAttribute(),
                    dto.getOperator(),
                    dto.getValues(),
                    dto.getScope()
            );
            return objectMapper.writeValueAsString(ruleDataDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing RuleDataDTO to JSON", e);
        }
    }



    @Transactional
    public void deleteRuleById(@RequestHeader("Authorization") String authorizationHeader,
                               Long id) {
        validateRole(authorizationHeader);
        RuleEntity ruleEntity = ruleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rule not found"));

        if (ruleEntity.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rule is already deleted");
        }

        ruleEntity.setDeleted(true); // Marca como eliminado
        ruleRepository.save(ruleEntity); // Persistencia del cambio
    }


    private RuleDTO mapEntityToDTO(RuleEntity entity) {
        try {
            RuleDTO ruleDTO = objectMapper.readValue(entity.getRuleData(), RuleDTO.class);
            ruleDTO.setId(entity.getId());
            return ruleDTO;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error mapping JSON to RuleDTO", e);
        }
    }

    private List<RuleEntity> findAllActiveRules() {
        return ruleRepository.findByDeletedFalse();
    }

    private List<Rule> getAllRulesAsModel(List<RuleDTO> rulesDTO) {
        return rulesDTO.stream()
                .map(dto -> RuleFactory.getRule(dto.getAttribute(), dto.getOperator(), dto.getValues(), dto.getScope()))
                .collect(Collectors.toList());
    }

    private boolean hasMoreThanOneTypeOfValue(RuleDTO ruleDTO) {
        return ruleDTO.getValues().stream()
                .map(Object::getClass)
                .distinct()
                .count() > 1;
    }

//    private RuleQuantityDTO getRuleQuantity() {
//        List<RuleQuantityDTO> ruleQuantitiesDTO = ruleQuantityService.getAllQuantities();
//        return ruleQuantitiesDTO.getFirst();
//    }

    private RuleQuantityDTO getRuleQuantity() {
        List<RuleQuantityDTO> ruleQuantitiesDTO = ruleQuantityService.getAllQuantities();
        if (ruleQuantitiesDTO.isEmpty()) {
            RuleQuantityDTO defaultQuantityDTO = new RuleQuantityDTO(MAX_QUANTITY_PER_PRODUCT, MAX_QUANTITY_PER_ORDER);
            ruleQuantitiesDTO.add(defaultQuantityDTO);
        }

        RuleQuantityDTO maxQuantityDTO = ruleQuantitiesDTO.getFirst();
        for (RuleQuantityDTO dto : ruleQuantitiesDTO) {
            if (dto.getId() > maxQuantityDTO.getId()) {
                maxQuantityDTO = dto;
            }
        }
        return maxQuantityDTO;
    }

    private void validateRole(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));

        if(role.equals(Role.USER.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissions denied");
        }
    }

}