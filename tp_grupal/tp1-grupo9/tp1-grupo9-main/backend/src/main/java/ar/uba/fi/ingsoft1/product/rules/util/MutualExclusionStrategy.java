package ar.uba.fi.ingsoft1.product.rules.util;

import java.util.*;
import java.util.stream.Collectors;

import ar.uba.fi.ingsoft1.product.rules.exceptions.MutualExclusionException;
import ar.uba.fi.ingsoft1.product.rules.exceptions.RuleException;
import ar.uba.fi.ingsoft1.product.rules.model.Item;
import ar.uba.fi.ingsoft1.product.rules.model.Rule;

public class MutualExclusionStrategy implements OperatorStrategy {

    private static MutualExclusionStrategy instance;

    public static MutualExclusionStrategy getInstance() {
        if (instance == null) {
            instance = new MutualExclusionStrategy();
        }
        return instance;
    }

    @Override
    public boolean compare(Rule rule, Set<Item> cart) throws RuleException {
        // Get only the items that are related to products with "type" attribute
        Set<Object> types = cart.stream()
                .map(item -> item.getAttribute(rule.getAttribute())) //
                .filter(Objects::nonNull) // Nulls filter (items without "type")
                .collect(Collectors.toSet()); // Get a set of unique types

        // Verify if the set of types contain the conflicted types simultaneously.
        if (verifySingleOccurrence(rule.getValues(), types)) {
            return true;
        } else {
            // extract the values that are un rule.getValues() and convert them into strings
            List<String> values = rule.getValues().stream().map(Object::toString).toList();
            throw new MutualExclusionException(String.format("Mutual exclusion rule not complied. Attribute being evaluated is %s",  rule.getAttribute()));
        }
    }

    private boolean verifySingleOccurrence(Collection<?> collection, Set<Object> set) {
        int count = 0;

        for (Object element : collection) {
            if (set.contains(element)) {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
