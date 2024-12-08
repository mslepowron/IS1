package ar.uba.fi.ingsoft1.product.rules.util.operatoraction;

import java.util.Set;

import ar.uba.fi.ingsoft1.product.rules.exceptions.actionexceptions.ActionException;

public interface OperatorAction {
    // "valueToCompare" is the value of the attribute in product or order scope
    
    // Comparing one value with a set of values
    default boolean execute(Set<Object> ruleAttributeValues, Object valueToCompare) throws ActionException, IllegalArgumentException {   
        throw new UnsupportedOperationException("This method is not implemented for this action.");
    }

    // Comparing one value with another value
    default boolean execute(Object ruleAttributeValue, Object valueToCompare) {
        throw new UnsupportedOperationException("This method is not implemented for this action.");
    }

    // Comparing two sets of values (maybe it's not necessary)
    default boolean execute(Set<Object> ruleAttributeValues, Set<Object> valuesToCompare) {
        throw new UnsupportedOperationException("This method is not implemented for this action.");
    }

}
