package ar.uba.fi.ingsoft1.product.rules.model;

import java.util.Set;

import ar.uba.fi.ingsoft1.product.rules.exceptions.RuleException;
import ar.uba.fi.ingsoft1.product.rules.model.scope.ScopeStrategy;
import ar.uba.fi.ingsoft1.product.rules.model.scope.ScopeStrategyFactory;
import ar.uba.fi.ingsoft1.product.rules.util.OperatorStrategy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rule {

    private String attribute;
    private Operator operator;
    private Set<Object> values;
    private String scope;
    private ScopeStrategy scopeStrategy;
    private OperatorStrategy comparator;

    public Rule(String attribute, String operatorSymbol, Set<Object> values, String scope, OperatorStrategy comparator) {
        this.attribute = attribute;
        this.operator = Operator.fromString(operatorSymbol);
        this.values = values;
        this.scope = scope;
        this.scopeStrategy = ScopeStrategyFactory.getStrategy(scope);
        this.comparator = comparator;
    }

    public boolean isSatisfiedBy(Set<Item> cart) throws RuleException, IllegalArgumentException {
        return comparator.compare(this, cart);
    }
}