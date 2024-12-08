package ar.uba.fi.ingsoft1.product.orders;

import java.util.Optional;

import ar.uba.fi.ingsoft1.product.orders.states.States;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateDTO {
    private Optional<States> state;

    public Order applyTo(Order order) {
        state.ifPresent(order::setState);
        return order;
    }
}