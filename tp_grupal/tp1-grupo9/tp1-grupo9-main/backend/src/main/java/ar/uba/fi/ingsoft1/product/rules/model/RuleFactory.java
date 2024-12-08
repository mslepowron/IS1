package ar.uba.fi.ingsoft1.product.rules.model;

import ar.uba.fi.ingsoft1.product.rules.util.ComparisonOperatorsStrategy;
import ar.uba.fi.ingsoft1.product.rules.util.MutualExclusionStrategy;

import java.util.Set;

public class RuleFactory {

    public static Rule getRule(String attribute, String operatorSymbol, Set<Object> values, String scope) {
        return switch (Operator.fromString(operatorSymbol)) {
            case GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL, EQUAL, NOT_EQUAL ->
                    new Rule(attribute, operatorSymbol, values, scope, ComparisonOperatorsStrategy.getInstance());
            case MUTUAL_EXCLUSION ->
                    new Rule(attribute, operatorSymbol, values, scope, MutualExclusionStrategy.getInstance());
        };
    }

}
