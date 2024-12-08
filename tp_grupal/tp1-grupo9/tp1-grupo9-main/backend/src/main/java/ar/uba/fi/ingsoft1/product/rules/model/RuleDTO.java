package ar.uba.fi.ingsoft1.product.rules.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RuleDTO {

    private Long id;

    @NotBlank
    private String attribute;

    @NotBlank
    private String operator;

    @NotNull
    private Set<Object> values;

    @NotBlank
    private String scope;

    public RuleDTO() {}

    public RuleDTO(Long id, String attribute, String operator, Set<Object> values, String scope) {
        this.id = id;
        this.attribute = attribute;
        this.operator = operator;
        this.values = values;
        this.scope = scope;
    }
}
