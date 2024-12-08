package ar.uba.fi.ingsoft1.product.rules.model.scope;

import ar.uba.fi.ingsoft1.product.rules.model.OrderContext;

// Interfaz común para estrategias de evaluación
public interface ScopeStrategy {
    boolean apply(OrderContext context);
}