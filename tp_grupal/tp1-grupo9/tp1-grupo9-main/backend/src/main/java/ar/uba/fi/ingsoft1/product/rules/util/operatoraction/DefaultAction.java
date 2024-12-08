package ar.uba.fi.ingsoft1.product.rules.util.operatoraction;

import java.util.Set;

public class DefaultAction implements OperatorAction{
    @Override
    public boolean execute(Set<Object> ruleAttributeValues, Object valueToCompare) {
        return false;
    }
}
