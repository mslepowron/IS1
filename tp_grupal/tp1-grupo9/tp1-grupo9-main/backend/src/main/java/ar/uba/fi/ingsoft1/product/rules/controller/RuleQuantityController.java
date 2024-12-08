package ar.uba.fi.ingsoft1.product.rules.controller;

import ar.uba.fi.ingsoft1.product.rules.model.RuleQuantityDTO;
import ar.uba.fi.ingsoft1.product.rules.service.RuleQuantityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/rules/quantity")
public class RuleQuantityController {

    private static final Logger log = LoggerFactory.getLogger(RuleQuantityController.class);

    private final RuleQuantityService ruleQuantityService;

    public RuleQuantityController(@Autowired RuleQuantityService ruleQuantityService) {
        this.ruleQuantityService = ruleQuantityService;
    }

    @PostMapping
    public ResponseEntity<String> saveQuantities(@Valid @RequestBody RuleQuantityDTO ruleQuantities) {
        try {
            RuleQuantityDTO savedQuantity = ruleQuantityService.saveQuantity(ruleQuantities);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Regla de cantidad guardada. Max por producto: %d, Max por orden: %d", savedQuantity.getMaxQuantityPerProduct(), savedQuantity.getMaxQuantityPerOrder()));
        } catch (ResponseStatusException e) {
            log.error("Error al guardar cantidades: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            log.error("Error inesperado al guardar cantidades: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<RuleQuantityDTO>> getAllQuantities() {
        try {
            List<RuleQuantityDTO> quantities = ruleQuantityService.getAllQuantities();
            log.info("Cantidades obtenidas: {}", quantities);
            return ResponseEntity.status(HttpStatus.OK).body(quantities);
        } catch (Exception e) {
            log.error("Error inesperado al obtener cantidades: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
