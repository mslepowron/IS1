package ar.uba.fi.ingsoft1.product.rules.util.operatoraction;

import java.util.Set;
import java.util.function.BiPredicate;

import ar.uba.fi.ingsoft1.product.rules.exceptions.actionexceptions.ActionException;
import ar.uba.fi.ingsoft1.product.rules.exceptions.actionexceptions.NotEqualActionException;
import ar.uba.fi.ingsoft1.product.rules.util.OperatorUtils;

public class NotEqualAction implements OperatorAction {    
    @Override
    public boolean execute(Set<Object> ruleAttributeValues, Object valueToCompare) throws ActionException, IllegalArgumentException {
        try {
            Object firstValue = ruleAttributeValues.iterator().next();
            boolean result;
            String message;
            if (firstValue instanceof Number) {
                result = execute(ruleAttributeValues, valueToCompare, 
                    (a, b) -> OperatorUtils.compareNumbers(a, b, (x, y) -> x != y));
                message = String.format("The value %.2f is equal to one of the forbidden rule values", OperatorUtils.convertToDouble(valueToCompare, "valueToCompare"));
            } else {
                result = execute(ruleAttributeValues, valueToCompare, 
                    (a, b) -> !OperatorUtils.compareStrings(a, b, String::equals));
                message = String.format("The value %s is equal to one of the forbidden rule values", (String)valueToCompare);
            }

            if (result) {
                return true;
            } else {
                throw new NotEqualActionException(message);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Executes the comparison logic based on the provided predicate.
     *
     * @param ruleAttributeValues The set of attribute values to compare against.
     * @param valueToCompare      The value to compare.
     * @param comparator          The comparison function.
     * @return {@code true} if no elements in ruleAttributeValues match valueToCompare based on the comparator, {@code false} otherwise.
     */
    private boolean execute(Set<Object> ruleAttributeValues, Object valueToCompare, BiPredicate<Object, Object> comparator) {
        for (Object ruleValue : ruleAttributeValues) {
            if (!comparator.test(valueToCompare, ruleValue)) {
                return false;
            }
        }
        return true;
    }
}
