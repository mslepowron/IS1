package ar.uba.fi.ingsoft1.product.rules.controller;

import ar.uba.fi.ingsoft1.product.rules.model.RuleDTO;
import ar.uba.fi.ingsoft1.product.rules.service.RuleService;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    private static final Logger log = LoggerFactory.getLogger(RuleController.class);

    private final RuleService ruleService;

    public RuleController(@Autowired RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<String> saveRules(@RequestHeader("Authorization") String authorizationHeader,
                                            @Valid @RequestBody List<RuleDTO> ruleDTOs) {
        try {
            ruleService.saveRules(authorizationHeader, ruleDTOs);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            log.error("Error parcial al guardar reglas: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            log.error("Error inesperado al guardar reglas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Void> updateRules(@RequestHeader("Authorization") String authorizationHeader,
                                            @RequestBody List<RuleDTO> ruleDTOs) {
        try {
            ruleService.updateRules(authorizationHeader, ruleDTOs);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResponseStatusException e) {
            log.error("Error al actualizar reglas: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("Error inesperado al actualizar reglas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable @NonNull Long id) {
        try {
            ruleService.deleteRuleById(authorizationHeader, id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResponseStatusException e) {
            log.error("Error al eliminar regla: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("Error inesperado al eliminar regla: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<RuleDTO>> getAllRules() {
        try {
            List<RuleDTO> rules = ruleService.getAllRules();
            log.info("Reglas obtenidas: {}", rules);
            return ResponseEntity.ok(rules);
        } catch (Exception e) {
            log.error("Error inesperado al obtener reglas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}