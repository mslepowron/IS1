package ar.uba.fi.ingsoft1.product.rules.model.scope;

import ar.uba.fi.ingsoft1.product.rules.model.OrderContext;

public class OrderScopeStrategy implements ScopeStrategy{
        @Override
        public boolean apply(OrderContext context) {
            return false;
        }
}
