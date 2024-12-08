package ar.uba.fi.ingsoft1.product.rules.util;

import ar.uba.fi.ingsoft1.product.rules.model.Rule;
import ar.uba.fi.ingsoft1.product.rules.exceptions.RuleException;
import ar.uba.fi.ingsoft1.product.rules.model.Item;
import ar.uba.fi.ingsoft1.product.rules.model.Operator;
import ar.uba.fi.ingsoft1.product.rules.util.operatoraction.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ComparisonOperatorsStrategy implements OperatorStrategy {

    private final Map<Operator, OperatorAction> actions = new HashMap<>();
    private static ComparisonOperatorsStrategy instance;

    private ComparisonOperatorsStrategy() {
        this.actions.put(Operator.GREATER_THAN, new GreaterThanAction());
        this.actions.put(Operator.LESS_THAN, new LessThanAction());
        this.actions.put(Operator.GREATER_THAN_OR_EQUAL, new GreaterThanOrEqualAction());
        this.actions.put(Operator.LESS_THAN_OR_EQUAL, new LessThanOrEqualAction());
        this.actions.put(Operator.EQUAL, new EqualAction());
        this.actions.put(Operator.NOT_EQUAL, new NotEqualAction());
    }

    public static ComparisonOperatorsStrategy getInstance() {
        if (instance == null) {
            instance = new ComparisonOperatorsStrategy();
        }
        return instance;
    }

    @Override
    public boolean compare(Rule rule, Set<Item> cart) throws RuleException, IllegalArgumentException {
        Operator operator = rule.getOperator();
        Set<Object> ruleValues = rule.getValues();
        String attribute = rule.getAttribute();

        OperatorAction action = this.actions.get(operator);

        if (action == null) {
            action = new DefaultAction();
        }
        
        // Quizas que podemos aplicar Strategy para el scope de Rule, pero estas estrategias estarian asociadas a ComparisonOperatorsComparator
        if (rule.getScope().equalsIgnoreCase("PRODUCT")) {
            return compareForItem(attribute, action, ruleValues, cart);
        } else {
            return compareForOrder(attribute, action, ruleValues, cart);
        }
    }
    
    private boolean compareForItem(String attribute, OperatorAction action, Set<Object> ruleValues, Set<Item> cart) throws RuleException, IllegalArgumentException {
        for (Item item : cart) {
            Object attributeValue = item.getProduct().getAttribute(attribute);
            if (attributeValue != null) { // If the attribute is not present, the rule does not apply.
                if (!action.execute(ruleValues, attributeValue)) {
                    // TODO: maybe we can return the rule that fails
                    return false;
                }
            }
        }
        return true;
    }

    private boolean compareForOrder(String attribute, OperatorAction action, Set<Object> ruleValues, Set<Item> cart) throws RuleException, IllegalArgumentException {
        double totalAttributeValue = cart.stream()
                .mapToDouble(item -> item.calculateTotal(attribute))
                .sum();

        return action.execute(ruleValues, totalAttributeValue);
    }
}
