package ar.uba.fi.ingsoft1.product.orders.states;

import org.springframework.stereotype.Component;

@Component
public class StateFactory {

    public State getInitialState(States state) {

        return switch (state) {
            case CONFIRMED -> new Confirmed();
            case PROCESSING -> new Processing();
            case CANCELED -> new Canceled();
            case SENT -> new Sent();
        };
    }
}
