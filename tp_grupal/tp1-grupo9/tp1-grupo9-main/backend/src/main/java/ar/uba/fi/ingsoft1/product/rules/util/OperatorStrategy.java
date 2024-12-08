package ar.uba.fi.ingsoft1.product.rules.util;

import ar.uba.fi.ingsoft1.product.rules.exceptions.RuleException;
import ar.uba.fi.ingsoft1.product.rules.model.Item;
import ar.uba.fi.ingsoft1.product.rules.model.Rule;

import java.util.Set;

public interface OperatorStrategy {
    public boolean compare(Rule rule, Set<Item> order) throws RuleException, IllegalArgumentException;
}
