package ar.uba.fi.ingsoft1.product.rules.model.scope;

public class ScopeStrategyFactory {

    public static ScopeStrategy getStrategy(String scope) {
        if ("PRODUCT".equalsIgnoreCase(scope)) {
            return new ItemScopeStrategy();
        } else if ("ORDER".equalsIgnoreCase(scope)) {
            return new OrderScopeStrategy();
        }
        throw new IllegalArgumentException("Unknown scope: " + scope);
    }
}