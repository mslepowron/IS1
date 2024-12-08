package ar.uba.fi.ingsoft1.product.orders.states;

import java.util.Map;

public class OrderStates {
    public static final Map<String, States> STATES = Map.of(
            "PROCESSING", States.PROCESSING,
            "CONFIRMED", States.CONFIRMED,
            "CANCELED", States.CANCELED,
            "SENT", States.SENT
    );
}
