package ar.uba.fi.ingsoft1.product.rules.entity;

import ar.uba.fi.ingsoft1.product.rules.model.Rule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

@Entity
@Table(name = "rules")
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "json", nullable = false)
    private String ruleData; // Store JSON as a String

    @Column(nullable = false)
    private boolean deleted;

    public RuleEntity() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleData() {
        return ruleData;
    }

    public void setRuleData(String ruleData) {
        this.ruleData = ruleData;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Rule toRule(ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.readValue(ruleData, Rule.class);
    }

    public void fromRule(Rule rule, ObjectMapper objectMapper) throws JsonProcessingException {
        this.ruleData = objectMapper.writeValueAsString(rule);
    }
}