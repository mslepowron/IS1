package ar.uba.fi.ingsoft1.product.rules.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RuleDataDTO {

    private String attribute;
    private String operator;
    private Set<Object> values;
    private String scope;

    public RuleDataDTO(String attribute, String operator, Set<Object> values, String scope) {
        this.attribute = attribute;
        this.operator = operator;
        this.values = values;
        this.scope = scope;
    }
}