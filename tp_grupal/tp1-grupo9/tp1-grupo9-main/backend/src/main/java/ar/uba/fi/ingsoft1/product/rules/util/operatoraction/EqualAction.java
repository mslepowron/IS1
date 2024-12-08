package ar.uba.fi.ingsoft1.product.rules.util.operatoraction;

import java.util.Set;
import java.util.function.BiPredicate;

import ar.uba.fi.ingsoft1.product.rules.exceptions.actionexceptions.ActionException;
import ar.uba.fi.ingsoft1.product.rules.util.OperatorUtils;

public class EqualAction implements OperatorAction {
    @Override
    public boolean execute(Set<Object> ruleAttributeValues, Object valueToCompare) throws ActionException, IllegalArgumentException {
        try {
            Object firstValue = ruleAttributeValues.iterator().next();
            if (firstValue instanceof Number) {
                return execute(ruleAttributeValues, valueToCompare,
                    (a, b) -> OperatorUtils.compareNumbers(a, b, (x, y) -> x == y));
            } else {
                return execute(ruleAttributeValues, valueToCompare,
                    (a, b) -> OperatorUtils.compareStrings(a, b, String::equals));
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
            if (comparator.test(valueToCompare, ruleValue)) {
                return true;
            }
        }
        return false;
    }
}
