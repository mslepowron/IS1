package ar.uba.fi.ingsoft1.product.rules.util.operatoraction;

import java.util.Optional;
import java.util.Set;

import ar.uba.fi.ingsoft1.product.rules.exceptions.actionexceptions.ActionException;
import ar.uba.fi.ingsoft1.product.rules.exceptions.actionexceptions.GreaterThanActionException;
import ar.uba.fi.ingsoft1.product.rules.util.OperatorUtils;

public class GreaterThanAction implements OperatorAction {
    @Override
    public boolean execute(Set<Object> ruleAttributeValues, Object valueToCompare) throws ActionException, IllegalArgumentException {        
        try {
            Optional<Number> max = OperatorUtils.getMaxNumber(ruleAttributeValues);
            if (max.isPresent()) {
                if (OperatorUtils.compareNumbers(valueToCompare, max.get(), (a, b) -> a > b)) {
                    return true;
                } else {
                    double valueToPrint = OperatorUtils.convertToDouble(valueToCompare, "valueToCompare");
                    throw new GreaterThanActionException(String.format("The value %.2f is not greater (>) than %.2f", valueToPrint, max.get().doubleValue()));
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return false; // If there is any doubt, we'll not create the order.
    }
}
